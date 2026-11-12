package com.curated.app.data.ai

import android.util.Base64
import com.curated.app.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

/** A failure of the AI Stylist with a message that can be shown to the user as it is. */
class AiException(val userMessage: String, cause: Throwable? = null) : Exception(userMessage, cause)

/**
 * Calls Cloudflare Workers AI straight from the app. There is no server in between yet,
 * so the account's key ships inside the APK and the free daily allowance is shared by
 * every install; both have to move behind a server before a public release.
 */
@Singleton
class CloudflareAi @Inject constructor() {

    private val accountId = BuildConfig.CF_ACCOUNT_ID
    private val token = BuildConfig.CF_API_TOKEN

    /**
     * Asks the text model for an answer shaped like [schema] and returns it parsed.
     * The model can still stray from the schema, so callers must treat every field as optional.
     */
    suspend fun chatJson(system: String, user: String, schema: JSONObject, maxTokens: Int): JSONObject {
        val request = JSONObject()
            .put(
                "messages",
                JSONArray()
                    .put(JSONObject().put("role", "system").put("content", system))
                    .put(JSONObject().put("role", "user").put("content", user))
            )
            .put("response_format", JSONObject().put("type", "json_schema").put("json_schema", schema))
            .put("max_tokens", maxTokens)
            .put("temperature", 0.6)
        val body = post(TEXT_MODEL, "application/json", request.toString().toByteArray())
        return readable {
            val result = body.getJSONObject("result")
            val answer = result.opt("response")
                ?: result.optJSONArray("choices")?.optJSONObject(0)?.optJSONObject("message")?.opt("content")
            when (answer) {
                is JSONObject -> answer
                is String -> JSONObject(answer.substring(answer.indexOf('{').coerceAtLeast(0)))
                else -> throw JSONException("no answer in the response")
            }
        }
    }

    private inline fun <T> readable(read: () -> T): T = try {
        read()
    } catch (error: JSONException) {
        throw AiException("The AI service sent an answer the app could not read. Please try again.", error)
    } catch (error: IllegalArgumentException) {
        throw AiException("The AI service sent an answer the app could not read. Please try again.", error)
    }

    /**
     * Makes one image from [prompt]; [references] are JPEG photos the prompt refers to as
     * "image 1", "image 2" and so on (the model takes up to four). Returns the image file's bytes.
     */
    suspend fun generateImage(prompt: String, width: Int, height: Int, references: List<ByteArray>): ByteArray {
        val boundary = "curated-" + System.nanoTime()
        val form = ByteArrayOutputStream()
        fun field(name: String, value: String) {
            form.write("--$boundary\r\nContent-Disposition: form-data; name=\"$name\"\r\n\r\n$value\r\n".toByteArray())
        }
        field("prompt", prompt)
        field("width", width.toString())
        field("height", height.toString())
        references.forEachIndexed { index, jpeg ->
            form.write(
                ("--$boundary\r\nContent-Disposition: form-data; name=\"input_image_$index\"; " +
                    "filename=\"input_image_$index.jpg\"\r\nContent-Type: image/jpeg\r\n\r\n").toByteArray()
            )
            form.write(jpeg)
            form.write("\r\n".toByteArray())
        }
        form.write("--$boundary--\r\n".toByteArray())
        val body = post(IMAGE_MODEL, "multipart/form-data; boundary=$boundary", form.toByteArray())
        return readable { Base64.decode(body.getJSONObject("result").getString("image"), Base64.DEFAULT) }
    }

    private suspend fun post(model: String, contentType: String, payload: ByteArray): JSONObject =
        withContext(Dispatchers.IO) {
            if (accountId.isBlank() || token.isBlank()) {
                throw AiException("The AI Stylist is not set up in this build of the app.")
            }
            val connection = URL("$BASE_URL/$accountId/ai/run/$model").openConnection() as HttpURLConnection
            try {
                connection.requestMethod = "POST"
                connection.connectTimeout = CONNECT_TIMEOUT_MS
                connection.readTimeout = READ_TIMEOUT_MS
                connection.doOutput = true
                connection.setRequestProperty("Authorization", "Bearer $token")
                connection.setRequestProperty("Content-Type", contentType)
                connection.setFixedLengthStreamingMode(payload.size)
                connection.outputStream.use { it.write(payload) }

                val status = connection.responseCode
                val stream = if (status in 200..299) connection.inputStream else connection.errorStream
                val text = stream?.bufferedReader()?.use { it.readText() }.orEmpty()
                val body = runCatching { JSONObject(text) }.getOrNull()
                if (status !in 200..299 || body == null) throw AiException(messageFor(status, text))
                body
            } catch (error: IOException) {
                throw AiException("Could not reach the AI service. Check your internet connection.", error)
            } finally {
                connection.disconnect()
            }
        }

    private fun messageFor(status: Int, body: String): String = when {
        status == 429 || "neurons" in body.lowercase() || "allocation" in body.lowercase() ->
            "Today's free AI allowance is used up. It starts again at 5 in the morning."
        status == 401 || status == 403 -> "The AI service did not accept the app's key."
        else -> "The AI service had a problem (code $status). Please try again."
    }

    private companion object {
        const val BASE_URL = "https://api.cloudflare.com/client/v4/accounts"
        const val TEXT_MODEL = "@cf/meta/llama-3.3-70b-instruct-fp8-fast"
        const val IMAGE_MODEL = "@cf/black-forest-labs/flux-2-klein-4b"
        const val CONNECT_TIMEOUT_MS = 15_000
        const val READ_TIMEOUT_MS = 120_000
    }
}
