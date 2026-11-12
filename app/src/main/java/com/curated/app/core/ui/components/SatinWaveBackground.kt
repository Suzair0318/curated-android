package com.curated.app.core.ui.components

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.imageResource
import com.curated.app.R
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.tanh

/**
 * Pink satin photo that billows like cloth: the image is warped on a mesh by slow
 * travelling waves, and a light map adds the sheen sliding over the folds.
 */
@Composable
fun SatinWaveBackground(modifier: Modifier = Modifier) {
    val satin = ImageBitmap.imageResource(R.drawable.splash_bg_satin)
    val bitmap = remember(satin) { satin.asAndroidBitmap() }

    // Read only while drawing, so each frame redraws without recomposing.
    val seconds = remember { mutableFloatStateOf(0f) }
    LaunchedEffect(Unit) {
        val start = withFrameNanos { it }
        while (true) {
            withFrameNanos { seconds.floatValue = (it - start) / 1_000_000_000f }
        }
    }

    Spacer(
        modifier.drawWithCache {
            val wave = SatinWave(bitmap, size.width, size.height)
            onDrawBehind {
                wave.update(seconds.floatValue)
                clipRect { drawIntoCanvas { wave.draw(it.nativeCanvas) } }
            }
        }
    )
}

private const val COLS = 28
private const val ROWS = 50

/** Overscan so warped edges never pull empty space into view. */
private const val ZOOM = 1.06f

/** Largest shift of the fabric, as a fraction of the view height. */
private const val DISPLACE = 0.020f
private const val SLOPE_NORM = 12f
private const val LIGHT_GAIN = 1.15f
private const val HIGHLIGHT_RGB = 0xFFEEF3
private const val HIGHLIGHT_ALPHA = 0.42f
private const val SHADOW_RGB = 0x7A4050
private const val SHADOW_ALPHA = 0.22f

/** Height of the cloth surface at a point, in view-height units, at [t] seconds. */
private fun clothHeight(x: Float, y: Float, t: Float): Float {
    // Bend the coordinates first so the folds curve instead of running as straight bands.
    val wx = x + 0.060f * sin(4.2f * y + 0.55f * t)
    val wy = y + 0.050f * cos(3.6f * x - 0.45f * t)
    return sin(3.1f * wx + 5.2f * wy + 1.25f * t) +
        0.60f * sin(-6.4f * wx + 4.3f * wy - 1.00f * t + 1.7f) +
        0.34f * sin(9.5f * wx + 10.5f * wy + 1.70f * t + 4.1f) +
        0.16f * sin(-15.0f * wx + 6.0f * wy + 2.30f * t + 2.6f)
}

private class SatinWave(
    private val bitmap: Bitmap,
    width: Float,
    private val height: Float
) {
    private val gridW = COLS + 1
    private val gridH = ROWS + 1

    // One extra ring of samples so every vertex has neighbours for its slope.
    private val ringW = gridW + 2
    private val heights = FloatArray(ringW * (gridH + 2))

    private val restVerts = FloatArray(gridW * gridH * 2)
    private val verts = FloatArray(gridW * gridH * 2)
    private val lightPixels = IntArray(gridW * gridH)
    private val lightMap = Bitmap.createBitmap(gridW, gridH, Bitmap.Config.ARGB_8888)
    private val lightRect: RectF
    private val paint = Paint(Paint.FILTER_BITMAP_FLAG)

    private val left: Float
    private val top: Float
    private val cellW: Float
    private val cellH: Float

    init {
        // Centre-crop the photo over the view, like ContentScale.Crop.
        val scale = max(width / bitmap.width, height / bitmap.height) * ZOOM
        val imageW = bitmap.width * scale
        val imageH = bitmap.height * scale
        left = (width - imageW) / 2f
        top = (height - imageH) / 2f
        cellW = imageW / COLS
        cellH = imageH / ROWS

        var i = 0
        for (row in 0 until gridH) {
            for (col in 0 until gridW) {
                restVerts[i++] = left + col * cellW
                restVerts[i++] = top + row * cellH
            }
        }
        // Half a cell wider on each side puts light-map texel centres on the vertices.
        lightRect = RectF(
            left - cellW / 2f,
            top - cellH / 2f,
            left + imageW + cellW / 2f,
            top + imageH + cellH / 2f
        )
    }

    fun update(t: Float) {
        if (height <= 0f) return
        for (row in -1..gridH) {
            val y = (top + row * cellH) / height
            for (col in -1..gridW) {
                val x = (left + col * cellW) / height
                heights[(row + 1) * ringW + col + 1] = clothHeight(x, y, t)
            }
        }

        val slopeX = 1f / (2f * cellW / height * SLOPE_NORM)
        val slopeY = 1f / (2f * cellH / height * SLOPE_NORM)
        for (row in 0 until gridH) {
            for (col in 0 until gridW) {
                val centre = (row + 1) * ringW + col + 1
                val sx = (heights[centre + 1] - heights[centre - 1]) * slopeX
                val sy = (heights[centre + ringW] - heights[centre - ringW]) * slopeY

                val vertex = (row * gridW + col) * 2
                verts[vertex] = restVerts[vertex] + tanh(sx) * DISPLACE * height
                verts[vertex + 1] = restVerts[vertex + 1] + tanh(sy) * DISPLACE * height

                // Slopes facing the light (top-left) catch a highlight, the others fall into shade.
                val light = ((-0.62f * sx - 0.78f * sy) * LIGHT_GAIN).coerceIn(-1f, 1f)
                lightPixels[row * gridW + col] = if (light >= 0f) {
                    val gloss = (0.35f * light + 0.9f * light.pow(2.2f)).coerceAtMost(1f)
                    argb(gloss * HIGHLIGHT_ALPHA, HIGHLIGHT_RGB)
                } else {
                    argb((-light).pow(1.3f) * SHADOW_ALPHA, SHADOW_RGB)
                }
            }
        }
        lightMap.setPixels(lightPixels, 0, gridW, 0, 0, gridW, gridH)
    }

    fun draw(canvas: Canvas) {
        canvas.drawBitmapMesh(bitmap, COLS, ROWS, verts, 0, null, 0, paint)
        canvas.drawBitmap(lightMap, null, lightRect, paint)
    }

    private fun argb(alpha: Float, rgb: Int): Int = ((alpha * 255f).roundToInt() shl 24) or rgb
}
