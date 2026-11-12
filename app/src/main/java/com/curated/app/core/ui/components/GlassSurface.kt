package com.curated.app.core.ui.components

import android.os.Build
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.curated.app.core.ui.components.liquid.LiquidBackdrop
import com.curated.app.core.ui.components.liquid.LiquidGlassStyle
import com.curated.app.core.ui.components.liquid.LocalLiquidBackdrop
import com.curated.app.core.ui.components.liquid.liquidGlass
import com.curated.app.core.ui.theme.CuratedShapes
import com.curated.app.core.ui.theme.GlassFill
import com.curated.app.core.ui.theme.GlassShadow
import com.curated.app.core.ui.theme.RoseBg
import com.curated.app.core.ui.theme.RoseBgEnd
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect

private val GlassRim = Brush.linearGradient(
    0f to Color.White.copy(alpha = 0.95f),
    0.45f to Color.White.copy(alpha = 0.35f),
    1f to Color.White.copy(alpha = 0.8f)
)

private val GlassHazeStyle = HazeStyle(
    backgroundColor = RoseBgEnd,
    tints = listOf(HazeTint(Color.White.copy(alpha = 0.3f))),
    blurRadius = 22.dp,
    noiseFactor = 0.02f
)

/** Refraction only: the white body, rim and shadow of a surface are drawn by Compose. */
private val SurfaceLiquidStyle = LiquidGlassStyle(
    tint = Color.Transparent,
    dispersion = 0.06f,
    rim = 0f,
    glow = 0f
)

/**
 * Liquid-glass surface: a translucent white body lit from the top-left, a bright rim
 * and a soft rose shadow that stays outside the shape so the glass itself stays clear.
 *
 * On Android 13+ the body sits on a refracted copy of [backdrop], which defaults to the
 * nearest `LiquidBackdropBox`. Surfaces that float above other content (bottom bar)
 * also pass [backdropBlur] so their labels stay readable.
 *
 * [hazeState] is the fallback for those floating surfaces on older Android: a plain
 * backdrop blur of the matching `hazeSource`, with no refraction.
 */
@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    shape: Shape = CuratedShapes.large,
    strong: Boolean = false,
    elevation: Dp = 10.dp,
    hazeState: HazeState? = null,
    backdrop: LiquidBackdrop? = LocalLiquidBackdrop.current,
    backdropBlur: Dp = 0.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val floating = hazeState != null || backdropBlur > 0.dp
    val topAlpha = when {
        floating -> 0.46f
        strong -> 0.74f
        else -> 0.58f
    }
    val bottomAlpha = when {
        floating -> 0.24f
        strong -> 0.5f
        else -> 0.3f
    }
    val blur = when {
        backdrop != null -> Modifier.liquidGlass(
            backdrop = backdrop,
            shape = shape,
            style = if (backdropBlur > 0.dp) SurfaceLiquidStyle.copy(blur = backdropBlur) else SurfaceLiquidStyle
        )
        hazeState != null -> Modifier.hazeEffect(state = hazeState, style = GlassHazeStyle)
        else -> Modifier
    }

    Box(
        modifier = modifier
            .softShadow(shape = shape, color = GlassShadow, blur = elevation, offsetY = elevation / 3)
            .clip(shape)
            .then(blur)
            .drawBehind {
                // Glass body
                drawRect(
                    Brush.verticalGradient(
                        listOf(
                            Color.White.copy(alpha = topAlpha),
                            Color.White.copy(alpha = bottomAlpha)
                        )
                    )
                )
                // Specular light pooling in the top-left corner
                drawRect(
                    Brush.radialGradient(
                        colors = listOf(Color.White.copy(alpha = 0.38f), Color.Transparent),
                        center = Offset(size.width * 0.18f, 0f),
                        radius = maxOf(size.width, size.height) * 0.85f
                    )
                )
                // Light caught by the lower edge
                drawRect(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.White.copy(alpha = 0.2f)),
                        startY = size.height * 0.68f,
                        endY = size.height
                    )
                )
            }
            .border(width = 1.dp, brush = GlassRim, shape = shape),
        content = content
    )
}

/**
 * Coloured blur shadow drawn only outside [shape]. `Modifier.shadow` paints under the
 * whole shape, which shows through translucent glass as a grey smudge.
 */
fun Modifier.softShadow(
    shape: Shape,
    color: Color,
    blur: Dp,
    offsetY: Dp = 0.dp
): Modifier {
    if (blur <= 0.dp) return this
    // Shadow layers on arbitrary paths need hardware support added in API 28.
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
        return shadow(blur / 2, shape, clip = false, ambientColor = color, spotColor = color)
    }
    return drawWithCache {
        val path = Path().apply { addOutline(shape.createOutline(size, layoutDirection, this@drawWithCache)) }
        val paint = android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG).apply {
            this.color = android.graphics.Color.TRANSPARENT
            setShadowLayer(blur.toPx(), 0f, offsetY.toPx(), color.toArgb())
        }
        onDrawBehind {
            clipPath(path, clipOp = ClipOp.Difference) {
                drawIntoCanvas { it.nativeCanvas.drawPath(path.asAndroidPath(), paint) }
            }
        }
    }
}

/**
 * Pink satin backdrop from the mockups: a diagonal rose gradient with two soft folds
 * of light. [intense] deepens it for hero cards.
 */
fun Modifier.satinBackground(intense: Boolean = false): Modifier = drawBehind {
    val base = if (intense) {
        listOf(Color(0xFFE9B0BD), Color(0xFFF4CDD6), Color(0xFFE4A7B5), Color(0xFFF0C3CD))
    } else {
        listOf(RoseBg, Color(0xFFF3CED6), RoseBg, RoseBgEnd)
    }
    drawRect(Brush.linearGradient(base, start = Offset.Zero, end = Offset(size.width, size.height)))

    val foldAlpha = if (intense) 0.42f else 0.26f
    fun fold(from: Offset, to: Offset) = drawRect(
        Brush.linearGradient(
            0f to Color.Transparent,
            0.5f to Color.White.copy(alpha = foldAlpha),
            1f to Color.Transparent,
            start = from,
            end = to
        )
    )
    fold(Offset(size.width * 0.05f, 0f), Offset(size.width * 0.5f, size.height * 0.32f))
    fold(Offset(size.width * 0.45f, size.height * 0.5f), Offset(size.width * 1.05f, size.height * 0.9f))
}

fun softGlassColor(apiBlurPreferred: Boolean = true): Color {
    return if (apiBlurPreferred && Build.VERSION.SDK_INT >= 31) {
        GlassFill
    } else {
        Color(0xCCFFF5F7)
    }
}
