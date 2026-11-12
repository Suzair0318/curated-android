package com.curated.app.core.ui.components.liquid

import android.graphics.RenderEffect
import android.graphics.Shader
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.hypot
import kotlin.math.max

/**
 * How one piece of liquid glass looks.
 *
 * @param tint colour laid over the refracted backdrop; its alpha is the strength.
 * @param blur backdrop blur, for glass that carries text over busy content.
 * @param lensBand width of the bending band inside the rim. Unspecified scales with size.
 * @param lensDepth how far the rim pulls the backdrop inwards. Unspecified scales with size.
 * @param dispersion red/blue split at the rim, as a fraction of the bend. 0 turns it off.
 * @param rim strength of the glossy rim highlight.
 * @param glow strength of the light inside the body and along the lower edge.
 * @param shadowColor drop shadow; transparent for none. Drawn by the shader so merged
 * glasses share one shadow.
 * @param mergeSoftness how eagerly this glass flows into group neighbours, as a fraction
 * of half its shorter side.
 */
@Immutable
data class LiquidGlassStyle(
    val tint: Color = Color.White.copy(alpha = 0.12f),
    val blur: Dp = 0.dp,
    val lensBand: Dp = Dp.Unspecified,
    val lensDepth: Dp = Dp.Unspecified,
    val dispersion: Float = 0.08f,
    val rim: Float = 1f,
    val glow: Float = 1f,
    val shadowColor: Color = Color.Transparent,
    val shadowBlur: Dp = 0.dp,
    val shadowOffsetY: Dp = 0.dp,
    val mergeSoftness: Float = 0.5f
)

/**
 * Draws [backdrop] behind this node, refracted through a glass of the given [shape].
 * Does nothing without a backdrop, below Android 13, or for shapes that are not
 * rectangles or rounded rectangles.
 *
 * The glass may draw outside its bounds (shadow, merge bridges), so apply any clip
 * after this modifier, not before, when those are in use.
 */
@Composable
fun Modifier.liquidGlass(
    backdrop: LiquidBackdrop?,
    shape: Shape,
    style: LiquidGlassStyle = LiquidGlassStyle(),
    mergeGroup: LiquidMergeGroup? = null
): Modifier {
    if (backdrop == null || !LiquidGlassSupported) return this
    return liquidGlassApi33(backdrop, shape, style, mergeGroup)
}

@RequiresApi(33)
@Composable
private fun Modifier.liquidGlassApi33(
    backdrop: LiquidBackdrop,
    shape: Shape,
    style: LiquidGlassStyle,
    mergeGroup: LiquidMergeGroup?
): Modifier {
    // The effect layer, and inside it a plain layer that holds the backdrop. Moving the
    // glass over its backdrop only changes the inner layer's translation, a property
    // update, so scrolling never re-records this node or the list it lives in.
    val layer = rememberGraphicsLayer()
    val backdropLayer = rememberGraphicsLayer()
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val memberKey = remember { Any() }
    val placement = remember { GlassPlacement() }
    // Ratio of on-screen pixels to local pixels; not 1 while a press scale is running.
    var rootScale by remember { mutableFloatStateOf(1f) }

    val syncWithBackdrop = remember(backdrop, backdropLayer, mergeGroup) {
        {
            val origin = backdrop.positionInRoot
            backdropLayer.translationX = (origin.x - placement.topLeft.x) / placement.scale
            backdropLayer.translationY = (origin.y - placement.topLeft.y) / placement.scale
            if (mergeGroup != null && placement.placed) {
                // Relative to the backdrop, so a group that scrolls with its backdrop
                // keeps the same bounds and is not rebuilt on every frame.
                val member = LiquidMergeMember(
                    bounds = Rect(placement.topLeft - origin, placement.bottomRight - origin),
                    cornerRadius = placement.cornerRadius
                )
                if (mergeGroup.members[memberKey] != member) {
                    mergeGroup.members[memberKey] = member
                }
            }
        }
    }
    DisposableEffect(backdrop, syncWithBackdrop) {
        backdrop.addPositionListener(syncWithBackdrop)
        onDispose { backdrop.removePositionListener(syncWithBackdrop) }
    }
    DisposableEffect(mergeGroup) {
        onDispose { mergeGroup?.members?.remove(memberKey) }
    }

    return this
        .onGloballyPositioned { coordinates ->
            val width = coordinates.size.width.toFloat()
            val height = coordinates.size.height.toFloat()
            if (width <= 0f || height <= 0f) return@onGloballyPositioned
            val topLeft = coordinates.localToRoot(Offset.Zero)
            val bottomRight = coordinates.localToRoot(Offset(width, height))
            val scale = ((bottomRight.x - topLeft.x) / width).takeIf { it > 0f } ?: 1f
            placement.topLeft = topLeft
            placement.bottomRight = bottomRight
            placement.scale = scale
            placement.placed = true
            rootScale = scale
            if (mergeGroup != null) {
                val outline = shape.createOutline(Size(width, height), layoutDirection, density)
                placement.cornerRadius = (outline.cornerRadii()?.max() ?: 0f) * scale
            }
            syncWithBackdrop()
        }
        .drawWithCache {
            val radii = shape.createOutline(size, layoutDirection, this).cornerRadii()
            if (radii == null || size.minDimension <= 0f) {
                return@drawWithCache onDrawBehind { }
            }
            val minHalf = size.minDimension / 2f
            for (i in radii.indices) radii[i] = radii[i].coerceIn(0f, minHalf)

            val lensBand = if (style.lensBand.isSpecified) {
                style.lensBand.toPx()
            } else {
                (minHalf * 0.4f).coerceIn(5.dp.toPx(), 16.dp.toPx())
            }.coerceIn(1f, minHalf)
            val lensDepth = if (style.lensDepth.isSpecified) {
                style.lensDepth.toPx()
            } else {
                (minHalf * 0.9f).coerceIn(6.dp.toPx(), 40.dp.toPx())
            }

            // Neighbours, in this glass's local pixels and relative to its centre.
            val mergeK = max(minHalf * style.mergeSoftness, 1f)
            val reach = mergeK * 4f
            val neighbourRects = FloatArray(MAX_MERGE_NEIGHBOURS * 4)
            val neighbourRadii = FloatArray(MAX_MERGE_NEIGHBOURS)
            var count = 0
            val self = mergeGroup?.members?.get(memberKey)
            if (mergeGroup != null && self != null) {
                // Members of a group must share one backdrop: their bounds are stored
                // relative to it.
                val scale = rootScale
                mergeGroup.members.entries
                    .filter { it.key !== memberKey && gapBetween(self.bounds, it.value.bounds) < reach * scale }
                    .map { it.value }
                    .sortedBy { (it.bounds.center - self.bounds.center).getDistanceSquared() }
                    .take(MAX_MERGE_NEIGHBOURS)
                    .forEach { other ->
                        val centre = (other.bounds.center - self.bounds.center) / scale
                        neighbourRects[count * 4] = centre.x
                        neighbourRects[count * 4 + 1] = centre.y
                        neighbourRects[count * 4 + 2] = other.bounds.width / 2f / scale
                        neighbourRects[count * 4 + 3] = other.bounds.height / 2f / scale
                        neighbourRadii[count] = other.cornerRadius / scale
                        count++
                    }
            }

            val hasShadow = style.shadowColor.alpha > 0f
            val shadowBlur = style.shadowBlur.toPx()
            val shadowDy = style.shadowOffsetY.toPx()
            val shadowExtent = if (hasShadow) shadowBlur + abs(shadowDy) else 0f
            // A member only draws the half of a bridge on its own side.
            val mergeExtent = if (count > 0) reach / 2f + 2f else 0f
            val overflow = ceil(max(shadowExtent, mergeExtent)).toInt()
            val layerSize = IntSize(
                ceil(size.width).toInt() + overflow * 2,
                ceil(size.height).toInt() + overflow * 2
            )

            val shader = LiquidGlassShader.instance
            shader.setFloatUniform("origin", overflow.toFloat(), overflow.toFloat())
            shader.setFloatUniform("glassSize", size.width, size.height)
            shader.setFloatUniform("radii", radii)
            shader.setFloatUniform("px", this.density)
            shader.setFloatUniform("lensBand", lensBand)
            shader.setFloatUniform("lensDepth", lensDepth)
            shader.setFloatUniform("dispersion", style.dispersion)
            shader.setFloatUniform("tint", style.tint.red, style.tint.green, style.tint.blue, style.tint.alpha)
            shader.setFloatUniform("rim", style.rim)
            shader.setFloatUniform("glow", style.glow)
            shader.setFloatUniform(
                "shadowColor",
                style.shadowColor.red, style.shadowColor.green, style.shadowColor.blue, style.shadowColor.alpha
            )
            shader.setFloatUniform("shadowGeom", shadowBlur, shadowDy)
            shader.setFloatUniform("mergeK", mergeK)
            shader.setFloatUniform("count", count.toFloat())
            shader.setFloatUniform("nRect", neighbourRects)
            shader.setFloatUniform("nRadius", neighbourRadii)

            var effect = RenderEffect.createRuntimeShaderEffect(shader, "content")
            val blurPx = style.blur.toPx()
            if (blurPx > 0.5f) {
                effect = RenderEffect.createChainEffect(
                    effect,
                    RenderEffect.createBlurEffect(blurPx, blurPx, Shader.TileMode.CLAMP)
                )
            }
            layer.renderEffect = effect.asComposeRenderEffect()
            layer.topLeft = IntOffset(-overflow, -overflow)

            // Give the inner layer the backdrop's own bounds so nothing is culled when it
            // is translated far outside the glass.
            val backdropSize = backdrop.size.takeIf { it != IntSize.Zero } ?: layerSize
            backdropLayer.record(this, layoutDirection, backdropSize) {
                drawLayer(backdrop.layer)
            }
            layer.record(this, layoutDirection, layerSize) {
                translate(overflow.toFloat(), overflow.toFloat()) { drawLayer(backdropLayer) }
            }

            onDrawBehind { drawLayer(layer) }
        }
}

/** Where a glass currently is on screen. Plain fields: nothing here invalidates drawing. */
private class GlassPlacement {
    var topLeft = Offset.Zero
    var bottomRight = Offset.Zero
    var scale = 1f
    var cornerRadius = 0f
    var placed = false
}

/** Radii in the order the shader expects, or null when the shape is not a round rect. */
private fun Outline.cornerRadii(): FloatArray? = when (this) {
    is Outline.Rectangle -> FloatArray(4)
    is Outline.Rounded -> floatArrayOf(
        roundRect.bottomRightCornerRadius.x,
        roundRect.topRightCornerRadius.x,
        roundRect.bottomLeftCornerRadius.x,
        roundRect.topLeftCornerRadius.x
    )
    is Outline.Generic -> null
}

private fun gapBetween(a: Rect, b: Rect): Float {
    val dx = max(0f, max(a.left - b.right, b.left - a.right))
    val dy = max(0f, max(a.top - b.bottom, b.top - a.bottom))
    return hypot(dx, dy)
}
