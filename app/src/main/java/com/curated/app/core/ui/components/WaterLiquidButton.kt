package com.curated.app.core.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.curated.app.core.ui.components.liquid.LiquidBackdrop
import com.curated.app.core.ui.components.liquid.LiquidGlassStyle
import com.curated.app.core.ui.components.liquid.LiquidMergeGroup
import com.curated.app.core.ui.components.liquid.LocalLiquidBackdrop
import com.curated.app.core.ui.components.liquid.liquidGlass

object WaterLiquidButtonDefaults {
    /**
     * Clear water: almost no tint, so the bent backdrop is what you see. The glow is kept
     * low because this app's backgrounds are light and added light soon turns them white.
     */
    val Clear = LiquidGlassStyle(
        tint = Color.White.copy(alpha = 0.06f),
        dispersion = 0.1f,
        glow = 0.35f,
        shadowColor = Color(0xFF6E3F4E).copy(alpha = 0.2f),
        shadowBlur = 10.dp,
        shadowOffsetY = 3.dp
    )

    /** Slightly milky water with a touch of blur, for small icon buttons over photos. */
    val Frosted = Clear.copy(
        tint = Color.White.copy(alpha = 0.26f),
        blur = 1.5.dp
    )
}

/**
 * A button made of liquid glass: it bends whatever is behind it, has a glossy rim and
 * swells a little while pressed. Buttons that share a [mergeGroup] flow into each other
 * when they sit close together.
 *
 * It refracts [backdrop], which defaults to the nearest
 * [com.curated.app.core.ui.components.liquid.LiquidBackdropBox]. Without one, or below
 * Android 13, it falls back to the app's drawn glass.
 */
@Composable
fun WaterLiquidButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
    enabled: Boolean = true,
    backdrop: LiquidBackdrop? = LocalLiquidBackdrop.current,
    mergeGroup: LiquidMergeGroup? = null,
    style: LiquidGlassStyle = WaterLiquidButtonDefaults.Clear,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: @Composable BoxScope.() -> Unit
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val press by animateFloatAsState(
        targetValue = if (pressed) 1f else 0f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 380f),
        label = "waterPress"
    )
    val click = Modifier.clickable(
        interactionSource = interaction,
        indication = null,
        enabled = enabled,
        role = Role.Button,
        onClick = onClick
    )

    if (backdrop == null) {
        GlassSurface(
            modifier = modifier
                .graphicsLayer {
                    val s = 1f - 0.06f * press
                    scaleX = s
                    scaleY = s
                }
                .then(click),
            shape = shape,
            strong = true,
            elevation = 6.dp,
            backdrop = null
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(contentPadding),
                contentAlignment = Alignment.Center,
                content = content
            )
        }
        return
    }

    Box(
        modifier = modifier
            .graphicsLayer {
                val s = 1f + 0.06f * press
                scaleX = s
                scaleY = s
            }
            // The glass draws its shadow and merge bridges outside the bounds, so the
            // clip for the content comes after it.
            .liquidGlass(
                backdrop = backdrop,
                shape = shape,
                style = style.copy(glow = style.glow * (1f + 0.6f * press.coerceAtLeast(0f))),
                mergeGroup = mergeGroup
            )
            .clip(shape)
            .then(click)
            .padding(contentPadding),
        contentAlignment = Alignment.Center,
        content = content
    )
}
