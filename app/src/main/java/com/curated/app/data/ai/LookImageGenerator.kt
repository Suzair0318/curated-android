package com.curated.app.data.ai

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import androidx.annotation.DrawableRes
import com.curated.app.data.mock.MockCatalog
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
import java.io.ByteArrayOutputStream
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.max
import kotlin.math.min

/** Where the image of an AI look stands while it is not there yet. */
sealed interface LookImageStatus {
    data object Generating : LookImageStatus
    data class Failed(val message: String) : LookImageStatus
}

/**
 * Makes the picture of an AI look: the photos of its pieces go to the image model, which
 * draws a model or mannequin wearing them. The picture is an approximation: cut and
 * colour come out close, but embroidery, prints and small accessories are not exact.
 */
@Singleton
class LookImageGenerator @Inject constructor(
    @ApplicationContext private val context: Context,
    private val ai: CloudflareAi,
    private val looks: AiLookStore,
    private val tags: StylistTags
) {
    // Outlives the screens, so the picture still arrives when the user has moved on.
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val productsById: Map<String, Product> by lazy { MockCatalog.products.associateBy { it.id } }

    private val _status = MutableStateFlow<Map<String, LookImageStatus>>(emptyMap())
    /** Looks whose image is being made or could not be made, by look id. */
    val status: StateFlow<Map<String, LookImageStatus>> = _status.asStateFlow()

    /** Starts making the image for [lookId] unless it is already on its way. */
    fun start(lookId: String) {
        val look = looks.look(lookId) ?: return
        var alreadyRunning = false
        _status.update { current ->
            alreadyRunning = current[lookId] == LookImageStatus.Generating
            if (alreadyRunning) current else current + (lookId to LookImageStatus.Generating)
        }
        if (alreadyRunning) return

        scope.launch {
            val failure = try {
                val path = reusableImage(look) ?: generate(look)
                looks.setImage(lookId, path)
                null
            } catch (error: AiException) {
                error.userMessage
            } catch (error: Exception) {
                "The look image could not be made. Please try again."
            }
            _status.update { current ->
                if (failure == null) current - lookId else current + (lookId to LookImageStatus.Failed(failure))
            }
        }
    }

    /** The same pieces on the same subject look the same, so an earlier image is used again. */
    private fun reusableImage(look: AiLook): String? = looks.looks.value.firstOrNull { other ->
        other.id != look.id &&
            other.subject == look.subject &&
            other.productIds.toSet() == look.productIds.toSet() &&
            other.imagePath?.let { File(it).exists() } == true
    }?.imagePath

    private suspend fun generate(look: AiLook): String {
        val pieces = look.productIds
            .mapNotNull { id -> productsById[id]?.let { product -> tags.of(id)?.let { product to it } } }
            .filter { (_, tags) -> tags.type in REFERENCE_ORDER }
            .sortedBy { (_, tags) -> REFERENCE_ORDER.indexOf(tags.type) }
            .take(MAX_REFERENCES)
        if (pieces.isEmpty()) throw AiException("This look has no pieces the image model can draw.")

        val prompt = lookPrompt(pieces.map { it.second }, look.subject)
        val references = pieces.map { (product, _) -> referenceJpeg(product.imageRes) }
        val image = ai.generateImage(prompt, IMAGE_WIDTH, IMAGE_HEIGHT, references)

        looks.directory.mkdirs()
        val file = File(looks.directory, "${look.id}.jpg")
        file.writeBytes(image)
        return file.path
    }

    /** The product photo fitted into a white square, which is what the image model was tested with. */
    private fun referenceJpeg(@DrawableRes resId: Int): ByteArray {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeResource(context.resources, resId, bounds)
        val longest = max(bounds.outWidth, bounds.outHeight)
        val options = BitmapFactory.Options().apply {
            inScaled = false
            inSampleSize = max(1, Integer.highestOneBit(max(1, longest / REFERENCE_SIZE)))
        }
        val photo = BitmapFactory.decodeResource(context.resources, resId, options)
            ?: throw AiException("A product photo could not be read.")

        val scale = min(1f, REFERENCE_SIZE.toFloat() / max(photo.width, photo.height))
        val width = photo.width * scale
        val height = photo.height * scale
        val left = (REFERENCE_SIZE - width) / 2f
        val top = (REFERENCE_SIZE - height) / 2f
        val square = Bitmap.createBitmap(REFERENCE_SIZE, REFERENCE_SIZE, Bitmap.Config.ARGB_8888)
        Canvas(square).apply {
            drawColor(Color.WHITE)
            drawBitmap(photo, null, RectF(left, top, left + width, top + height), Paint(Paint.FILTER_BITMAP_FLAG))
        }
        photo.recycle()
        return ByteArrayOutputStream().use { out ->
            square.compress(Bitmap.CompressFormat.JPEG, 90, out)
            square.recycle()
            out.toByteArray()
        }
    }

    private companion object {
        const val MAX_REFERENCES = 4
        const val REFERENCE_SIZE = 512
        const val IMAGE_WIDTH = 768
        const val IMAGE_HEIGHT = 1024

        /** Most important first: when a look has more than four pieces the last ones are left out. */
        val REFERENCE_ORDER = listOf(
            PieceType.Outfit,
            PieceType.Top,
            PieceType.Bottom,
            PieceType.Outerwear,
            PieceType.Shoes,
            PieceType.Bag,
            PieceType.Scarf,
            PieceType.Jewellery
        )
    }
}

/**
 * The wording that tested best: say who wears what by reference image and colour only.
 * Product names are left out on purpose; a name like "3 Piece Suit" made the model draw
 * a tailored jacket instead of the garment in the photo.
 */
internal fun lookPrompt(pieces: List<ProductTags>, subject: LookSubject): String {
    val audience = pieces.first().audience
    val (person, pronoun) = when (audience) {
        "men" -> "man" to "He"
        "kids" -> "child" to "The child"
        else -> "woman" to "She"
    }
    val who = when (subject) {
        LookSubject.Model ->
            if (audience == "kids") "one South Asian child with a new, original face"
            else "one young South Asian $person, a fashion model with a new, original face"
        LookSubject.Mannequin -> when (audience) {
            "men" -> "one white faceless male shop mannequin"
            "kids" -> "one white faceless child-size shop mannequin"
            else -> "one white faceless female shop mannequin"
        }
    }
    val wearing = pieces.mapIndexed { index, piece ->
        val image = "image ${index + 1}"
        val colour = if (piece.colour.isBlank()) "" else " (${piece.colour})"
        when (piece.type) {
            PieceType.Outfit -> "wears the complete outfit from $image$colour"
            PieceType.Top -> "wears the top from $image$colour"
            PieceType.Bottom -> "wears the bottoms from $image$colour"
            PieceType.Outerwear -> "wears, open over it, the outer layer from $image$colour"
            PieceType.Shoes -> "wears the shoes from $image$colour"
            PieceType.Bag -> "carries the one bag from $image$colour"
            PieceType.Scarf -> "wears, draped over one shoulder, the scarf from $image$colour"
            PieceType.Jewellery -> "wears the jewellery from $image$colour"
            PieceType.Other -> "has the item from $image$colour"
        }
    }
    return "Full-length fashion catalogue photo of $who, standing, facing the camera, " +
        "whole body visible from head to shoes, plain warm cream studio background, soft daylight. " +
        "$pronoun ${wearing.joinToString("; ")}. " +
        "Reproduce every garment and accessory exactly as in its reference image: same colour, cut, " +
        "print and embroidery. Do not copy the faces, poses or backgrounds of the reference images."
}
