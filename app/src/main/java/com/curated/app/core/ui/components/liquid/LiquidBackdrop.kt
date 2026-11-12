package com.curated.app.core.ui.components.liquid

import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize

/** Refraction needs AGSL runtime shaders, which arrived in Android 13. */
val LiquidGlassSupported: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

/**
 * A recording of whatever is drawn behind liquid glass. This plays the role the ARKit
 * camera frame plays in Pixelux-Glass: the texture that the glass bends.
 *
 * The glass must be a sibling of the recorded content, never a child of it. A glass
 * inside its own backdrop would be recorded into the picture it is about to draw.
 */
@Stable
class LiquidBackdrop internal constructor(internal val layer: GraphicsLayer) {
    /** Size of the last recording; glass reads it to size the layer it draws this into. */
    internal var size by mutableStateOf(IntSize.Zero)

    /**
     * Not snapshot state on purpose. A change here must not invalidate drawing: each
     * glass is told directly and only nudges a layer translation.
     */
    internal var positionInRoot = Offset.Zero
        set(value) {
            if (field == value) return
            field = value
            for (i in positionListeners.indices) positionListeners[i]()
        }

    private val positionListeners = mutableListOf<() -> Unit>()

    internal fun addPositionListener(listener: () -> Unit) {
        positionListeners += listener
        listener()
    }

    internal fun removePositionListener(listener: () -> Unit) {
        positionListeners -= listener
    }
}

/** Null below Android 13, so callers fall back to their plain look. */
@Composable
fun rememberLiquidBackdrop(): LiquidBackdrop? {
    if (!LiquidGlassSupported) return null
    val layer = rememberGraphicsLayer()
    return remember(layer) { LiquidBackdrop(layer) }
}

/** The backdrop that glass in this part of the tree refracts, if any. */
val LocalLiquidBackdrop = compositionLocalOf<LiquidBackdrop?> { null }

/** Records this node's content into [backdrop] and still draws it normally. */
fun Modifier.liquidBackdropSource(backdrop: LiquidBackdrop?): Modifier {
    if (backdrop == null) return this
    return this
        .onGloballyPositioned { backdrop.positionInRoot = it.localToRoot(Offset.Zero) }
        .drawWithContent {
            backdrop.layer.record { this@drawWithContent.drawContent() }
            backdrop.size = backdrop.layer.size
            drawLayer(backdrop.layer)
        }
}

/**
 * A [Box] whose background is available to the glass inside it. [background] and
 * [backdropContent] are drawn first and recorded; [content] sits on top as their
 * sibling and sees the recording through [LocalLiquidBackdrop].
 */
@Composable
fun LiquidBackdropBox(
    modifier: Modifier = Modifier,
    background: Modifier = Modifier,
    backdropContent: (@Composable BoxScope.() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val backdrop = rememberLiquidBackdrop()
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .liquidBackdropSource(backdrop)
                .then(background)
        ) {
            backdropContent?.invoke(this)
        }
        CompositionLocalProvider(LocalLiquidBackdrop provides backdrop) {
            content()
        }
    }
}

internal data class LiquidMergeMember(val bounds: Rect, val cornerRadius: Float)

/**
 * Glasses that share a group flow into each other when they are close, the way
 * `.interactive(true)` glasses do in Pixelux-Glass.
 */
@Stable
class LiquidMergeGroup internal constructor() {
    internal val members = mutableStateMapOf<Any, LiquidMergeMember>()
}

@Composable
fun rememberLiquidMergeGroup(): LiquidMergeGroup = remember { LiquidMergeGroup() }
