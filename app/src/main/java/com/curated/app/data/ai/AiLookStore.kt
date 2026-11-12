package com.curated.app.data.ai

import android.content.Context
import com.curated.app.R
import com.curated.app.data.mock.MockCatalog
import com.curated.app.data.model.Outfit
import com.curated.app.data.model.Product
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/** Who wears the look in the generated image. */
enum class LookSubject(val key: String, val label: String) {
    Mannequin("mannequin", "Mannequin"),
    Model("model", "Model")
}

/** A look the AI Stylist put together for this user. */
data class AiLook(
    val id: String,
    val title: String,
    val tags: List<String>,
    val productIds: List<String>,
    /** The stylist's reason for the look. */
    val note: String,
    /** What the user asked for. */
    val request: String,
    val subject: LookSubject,
    val createdAt: Long,
    /** The generated look image, once there is one. */
    val imagePath: String? = null
)

/**
 * The user's AI looks, newest first, kept in the app's files as one small JSON document
 * with the generated images beside it. Nothing is stored on a server.
 */
@Singleton
class AiLookStore @Inject constructor(
    @ApplicationContext context: Context
) {
    /** Folder for the document and the look images. */
    val directory: File = File(context.filesDir, "ai_looks")
    private val document = File(directory, "looks.json")
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val writing = Mutex()
    private val productsById: Map<String, Product> by lazy { MockCatalog.products.associateBy { it.id } }

    // Read once at start-up: the document is a few kilobytes and the catalogue needs it
    // to resolve look ids straight away.
    private val _looks = MutableStateFlow(read())
    val looks: StateFlow<List<AiLook>> = _looks.asStateFlow()

    fun look(id: String): AiLook? = _looks.value.firstOrNull { it.id == id }

    /** The look in the shape the rest of the app shows; its cover is the main piece until an image exists. */
    fun outfit(id: String): Outfit? = look(id)?.let { look ->
        val products = look.productIds.mapNotNull { productsById[it] }
        Outfit(
            id = look.id,
            title = look.title,
            tags = look.tags,
            heroImageRes = products.firstOrNull()?.imageRes ?: R.drawable.img_look_dinner,
            productIds = look.productIds,
            brandChips = products.map { MockCatalog.brandName(it.brandId) }.distinct(),
            isCreation = true,
            vibe = "ai",
            heroImagePath = look.imagePath?.takeIf { File(it).exists() },
            note = look.note,
            isAiLook = true
        )
    }

    fun add(look: AiLook) {
        _looks.update { listOf(look) + it }
        save()
    }

    fun setImage(id: String, path: String) {
        _looks.update { all -> all.map { if (it.id == id) it.copy(imagePath = path) else it } }
        save()
    }

    private fun save() {
        scope.launch {
            writing.withLock {
                val snapshot = _looks.value
                runCatching {
                    directory.mkdirs()
                    val rows = JSONArray()
                    snapshot.forEach { look ->
                        rows.put(
                            JSONObject()
                                .put("id", look.id)
                                .put("title", look.title)
                                .put("tags", JSONArray(look.tags))
                                .put("products", JSONArray(look.productIds))
                                .put("note", look.note)
                                .put("request", look.request)
                                .put("subject", look.subject.key)
                                .put("createdAt", look.createdAt)
                                .put("image", look.imagePath ?: JSONObject.NULL)
                        )
                    }
                    // Written to a side file first so a crash never leaves half a document.
                    val draft = File(directory, document.name + ".tmp")
                    draft.writeText(rows.toString())
                    if (!draft.renameTo(document)) {
                        document.writeText(rows.toString())
                        draft.delete()
                    }
                }
            }
        }
    }

    private fun read(): List<AiLook> = runCatching {
        if (!document.exists()) return emptyList()
        val rows = JSONArray(document.readText())
        (0 until rows.length()).map { index ->
            val row = rows.getJSONObject(index)
            AiLook(
                id = row.getString("id"),
                title = row.getString("title"),
                tags = row.getJSONArray("tags").strings(),
                productIds = row.getJSONArray("products").strings(),
                note = row.optString("note"),
                request = row.optString("request"),
                subject = LookSubject.entries.firstOrNull { it.key == row.optString("subject") }
                    ?: LookSubject.Mannequin,
                createdAt = row.optLong("createdAt"),
                imagePath = if (row.isNull("image")) null else row.optString("image")
            )
        }
    }.getOrDefault(emptyList())

    private fun JSONArray.strings(): List<String> = (0 until length()).map { getString(it) }
}
