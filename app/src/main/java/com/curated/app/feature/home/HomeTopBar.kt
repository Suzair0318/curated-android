package com.curated.app.feature.home

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.FloatState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.curated.app.core.ui.components.CuratedIcons
import com.curated.app.core.ui.components.CuratedLogo
import com.curated.app.core.ui.components.liquid.LiquidBackdrop
import com.curated.app.core.ui.components.liquid.LiquidGlassStyle
import com.curated.app.core.ui.components.liquid.liquidGlass
import com.curated.app.core.ui.components.softShadow
import com.curated.app.core.ui.theme.GlassShadow
import com.curated.app.core.ui.theme.RoseAccentDeep
import com.curated.app.core.ui.theme.SansUi
import com.curated.app.core.ui.theme.TextPrimary
import com.curated.app.core.ui.theme.TextSecondary
import kotlinx.coroutines.flow.first
import java.util.Calendar
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.roundToInt

private val BarTopGap = 4.dp
private val ExpandedHeight = 72.dp
private val CollapsedHeight = 50.dp
private val ExpandedInset = 10.dp
private val CollapsedInset = 22.dp

/** Scroll distance over which the bar goes from expanded to collapsed. */
private val CollapseDistance = 72.dp

private val LogoHeight = 60.dp
private const val LOGO_COLLAPSED_SCALE = 0.72f
private const val ACTION_COLLAPSED_SCALE = 0.8f
private const val GREETING_COLLAPSED_SCALE = 0.9f

/** Width of the moving reflection as a fraction of the bar, and its speed relative to the scroll. */
private const val REFLECTION_WIDTH = 0.36f
private const val REFLECTION_SPEED = 0.8f

/** How far the scrolling content fades out below the top edge of the bar. */
private val FadeBelowBarTop = 20.dp

private val BarShape = RoundedCornerShape(26.dp)

private val BarShadowBlur = 12.dp
private val BarShadowOffsetY = 4.dp

/**
 * Clear glass: the content shows through, bent at the rim and only lightly softened.
 * The shadow comes from the glass shader, which costs nothing extra while the bar
 * changes size; a separately drawn blurred shadow would be rebuilt on every frame.
 */
private val BarGlass = LiquidGlassStyle(
    tint = Color.White.copy(alpha = 0.12f),
    blur = 6.dp,
    dispersion = 0.08f,
    rim = 1f,
    glow = 0.7f,
    shadowColor = GlassShadow,
    shadowBlur = BarShadowBlur,
    shadowOffsetY = BarShadowOffsetY
)

// The collapse spring: critically damped, so it never overshoots. In radians per second;
// about 0.2 s to settle.
private const val SPRING_ANGULAR_FREQUENCY = 22f
private const val SETTLE_DISTANCE = 0.0005f
private const val SETTLE_VELOCITY = 0.01f

/** Longest time one spring step may cover, so a stalled frame cannot fling the bar. */
private const val MAX_STEP_SECONDS = 0.05f

/** Top padding the scrolling content needs to start just below the expanded bar. */
internal val HomeTopBarSpace = BarTopGap + ExpandedHeight + 8.dp

/**
 * Floating glass bar at the top of Home: greeting, logo and the two actions.
 *
 * It refracts [backdrop], the recording of the screen underneath, so it must be a
 * sibling of that content. Scrolling down shrinks the bar, the logo and the actions and
 * brings a reflection into the glass; scrolling back to the top restores them.
 *
 * The collapse follows the scroll position through a spring. Everything that depends
 * on it is read during layout or drawing, so scrolling never recomposes this bar.
 */
@Composable
internal fun HomeTopBar(
    scrollState: ScrollState,
    backdrop: LiquidBackdrop?,
    firstName: String,
    hasUnreadNotifications: Boolean,
    onOpenNotifications: () -> Unit,
    onOpenCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    val collapse = rememberCollapse(scrollState)

    Box(
        modifier = modifier
            .statusBarsPadding()
            .padding(top = BarTopGap)
            .layout { measurable, constraints ->
                val progress = collapse.floatValue
                val inset = lerp(ExpandedInset, CollapsedInset, progress).roundToPx()
                // Shrink two pixels at a time: the centred logo then never sits on a half
                // pixel, which would show as a shimmer while the bar is moving.
                val shrink = ((ExpandedHeight - CollapsedHeight).toPx() * progress / 2f).roundToInt() * 2
                val height = ExpandedHeight.roundToPx() - shrink
                val width = (constraints.maxWidth - inset * 2).coerceAtLeast(0)
                val placeable = measurable.measure(Constraints.fixed(width, height))
                layout(constraints.maxWidth, height) { placeable.place(inset, 0) }
            }
            // Its own layer: while the bar animates, only the bar is drawn again, not the
            // screen around it (which the tab bar below records as its backdrop).
            .graphicsLayer()
            .then(
                // Without a liquid backdrop there is no glass shader to draw the shadow.
                if (backdrop == null) {
                    Modifier.softShadow(BarShape, GlassShadow, BarShadowBlur, BarShadowOffsetY)
                } else {
                    Modifier
                }
            )
            .liquidGlass(backdrop = backdrop, shape = BarShape, style = BarGlass)
            .glassLight(shape = BarShape, frosted = backdrop == null, collapse = { collapse.floatValue })
            // The bar is solid to touch: taps must not reach the content showing through it.
            .pointerInput(Unit) { detectTapGestures() }
    ) {
        // The reflection that glides across as the page scrolls. It is moved as a layer,
        // so following the scroll costs no drawing at all.
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(BarShape)
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .graphicsLayer {
                        val band = size.width * REFLECTION_WIDTH
                        // Wraps around only while the band is fully outside the bar.
                        val travel = size.width + band * 2f
                        translationX = (band * 0.5f + scrollState.value * REFLECTION_SPEED) % travel - band
                    }
                    .reflectionBand(strength = { collapse.floatValue })
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp)
                .graphicsLayer {
                    val scale = lerp(1f, GREETING_COLLAPSED_SCALE, collapse.floatValue)
                    scaleX = scale
                    scaleY = scale
                    transformOrigin = TransformOrigin(0f, 0.5f)
                }
        ) {
            Text(
                "${greeting()},",
                style = TextStyle(fontFamily = SansUi, fontSize = 11.sp, color = TextSecondary)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    firstName,
                    maxLines = 1,
                    style = TextStyle(
                        fontFamily = SansUi,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = TextPrimary
                    )
                )
                Spacer(Modifier.width(4.dp))
                Icon(CuratedIcons.Heart, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(13.dp))
            }
        }

        // Laid out at its full height even when the bar is shorter, and scaled about its
        // centre, so it stays exactly in the middle of the bar the whole way.
        CuratedLogo(
            height = LogoHeight,
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer {
                    val scale = lerp(1f, LOGO_COLLAPSED_SCALE, collapse.floatValue)
                    scaleX = scale
                    scaleY = scale
                }
                .wrapContentHeight(unbounded = true)
        )

        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 4.dp)
        ) {
            val glyphScale = { lerp(1f, ACTION_COLLAPSED_SCALE, collapse.floatValue) }
            BarAction(
                icon = Icons.Outlined.NotificationsNone,
                label = "Notifications",
                onClick = onOpenNotifications,
                glyphScale = glyphScale,
                showDot = hasUnreadNotifications
            )
            BarAction(CuratedIcons.Bag, "Cart", onOpenCart, glyphScale)
        }
    }
}

/**
 * How collapsed the bar is, 0 (expanded) to 1, following the scroll position through a
 * critically damped spring: a slow drag is tracked closely, and a fling that crosses the
 * whole range in a frame or two still plays out as a short, even motion.
 *
 * The spring is stepped once per frame towards wherever the scroll is at that moment.
 * Starting a fresh animation for every scroll change would not work here: each one
 * would be cancelled by the next frame's change before it had moved.
 */
@Composable
private fun rememberCollapse(scrollState: ScrollState): FloatState {
    val distancePx = with(LocalDensity.current) { CollapseDistance.toPx() }
    val collapse = remember {
        // Start where the scroll already is, so coming back to Home does not replay the shrink.
        val start = Snapshot.withoutReadObservation { scrollState.value / distancePx }
        mutableFloatStateOf(start.coerceIn(0f, 1f))
    }
    LaunchedEffect(scrollState, distancePx) {
        fun target() = (scrollState.value / distancePx).coerceIn(0f, 1f)
        var velocity = 0f
        while (true) {
            // Asleep while the bar is where the scroll wants it; no frames are requested.
            snapshotFlow { target() }.first { abs(it - collapse.floatValue) > SETTLE_DISTANCE }
            var lastFrame = -1L
            var settled = false
            while (!settled) {
                withFrameNanos { now ->
                    val dt = if (lastFrame < 0) 1f / 60f else ((now - lastFrame) / 1e9f).coerceIn(0f, MAX_STEP_SECONDS)
                    lastFrame = now
                    val goal = target()
                    // Exact step of a critically damped spring, so the motion does not
                    // depend on the frame rate.
                    val offset = collapse.floatValue - goal
                    val pull = velocity + SPRING_ANGULAR_FREQUENCY * offset
                    val decay = exp(-SPRING_ANGULAR_FREQUENCY * dt)
                    val next = (offset + pull * dt) * decay
                    velocity = (velocity - SPRING_ANGULAR_FREQUENCY * pull * dt) * decay
                    settled = abs(next) < SETTLE_DISTANCE && abs(velocity) < SETTLE_VELOCITY
                    if (settled) velocity = 0f
                    // A fast scroll that stops dead can carry the spring a hair past the
                    // end; the bar must never grow beyond its expanded size.
                    collapse.floatValue = if (settled) goal else (goal + next).coerceIn(0f, 1f)
                }
            }
        }
    }
    return collapse
}

/**
 * Lets the content dissolve into the screen background just above the bar instead of
 * being cut off in a hard line at the status bar. Give it the same [background] as the
 * screen and place it over the scrolling content, filling the same box.
 */
@Composable
internal fun BoxScope.HomeTopFade(background: Modifier) {
    val density = LocalDensity.current
    val statusBar = WindowInsets.statusBars.getTop(density).toFloat()
    val clearAt = statusBar + with(density) { (BarTopGap + FadeBelowBarTop).toPx() }
    Box(
        modifier = Modifier
            .matchParentSize()
            .fadeOutBelow(solidUntil = statusBar, clearAt = clearAt)
            .then(background)
    )
}

/**
 * The 44dp box stays the touch target at every size; only the glyph and its dot scale.
 */
@Composable
private fun BarAction(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    glyphScale: () -> Float,
    showDot: Boolean = false
) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    val scale = glyphScale()
                    scaleX = scale
                    scaleY = scale
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = label, tint = TextPrimary, modifier = Modifier.size(26.dp))
            if (showDot) {
                Box(
                    Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 9.dp, end = 10.dp)
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(RoseAccentDeep)
                )
            }
        }
    }
}

/**
 * Light on the glass, inside [shape]: a soft highlight under the top edge that is always
 * there and, as the bar collapses, a white veil that keeps the labels readable over
 * photos.
 *
 * [frosted] is the look without a liquid backdrop (below Android 13): a translucent
 * white body and a hairline rim in place of the refraction.
 *
 * [collapse] is read while drawing only.
 */
private fun Modifier.glassLight(
    shape: Shape,
    frosted: Boolean,
    collapse: () -> Float
): Modifier = drawWithCache {
    val outline = shape.createOutline(size, layoutDirection, this)
    val path = Path().apply { addOutline(outline) }
    val topLight = Brush.verticalGradient(
        colors = listOf(Color.White.copy(alpha = 0.3f), Color.Transparent),
        endY = size.height * 0.62f
    )
    val rimWidth = 1.dp.toPx()

    onDrawBehind {
        val progress = collapse().coerceIn(0f, 1f)
        clipPath(path) {
            if (frosted) {
                drawRect(Color.White, alpha = 0.45f + 0.3f * progress)
            } else if (progress > 0f) {
                drawRect(Color.White, alpha = 0.16f * progress)
            }
            drawRect(topLight, alpha = 0.4f + 0.6f * progress)
        }
        if (frosted) {
            drawOutline(outline, Color.White.copy(alpha = 0.7f), style = Stroke(rimWidth))
        }
    }
}

/**
 * A slanted streak of light whose left edge sits at x = 0. The caller moves it across
 * the bar with a layer translation and clips it to the bar. [strength] is its opacity,
 * read while drawing; a layer alpha would put the streak through an offscreen buffer.
 */
private fun Modifier.reflectionBand(strength: () -> Float): Modifier = drawWithCache {
    val band = size.width * REFLECTION_WIDTH
    val light = Brush.linearGradient(
        0f to Color.Transparent,
        0.5f to Color.White.copy(alpha = 0.34f),
        1f to Color.Transparent,
        start = Offset.Zero,
        end = Offset(band, size.height * 0.8f)
    )
    // Wider than the band on both sides, because the band is slanted.
    val origin = Offset(-size.height, 0f)
    val area = Size(band + size.height * 2f, size.height)
    onDrawBehind {
        val alpha = strength().coerceIn(0f, 1f)
        if (alpha > 0f) drawRect(light, topLeft = origin, size = area, alpha = alpha)
    }
}

/**
 * Draws this node fully down to [solidUntil] px, then fades it out to nothing at
 * [clearAt] px, and draws nothing below that.
 */
private fun Modifier.fadeOutBelow(solidUntil: Float, clearAt: Float): Modifier = drawWithCache {
    val mask = Brush.verticalGradient(
        0f to Color.Black,
        (solidUntil / clearAt).coerceIn(0f, 1f) to Color.Black,
        1f to Color.Transparent,
        startY = 0f,
        endY = clearAt
    )
    val bounds = Rect(0f, 0f, size.width, clearAt)
    val maskSize = Size(size.width, clearAt)
    val layerPaint = Paint()
    onDrawWithContent {
        drawIntoCanvas { canvas ->
            // Only the strip that is faded goes through an offscreen layer.
            canvas.saveLayer(bounds, layerPaint)
            clipRect(bottom = clearAt) { this@onDrawWithContent.drawContent() }
            drawRect(mask, size = maskSize, blendMode = BlendMode.DstIn)
            canvas.restore()
        }
    }
}

private fun greeting(): String = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
    in 5..11 -> "Good morning"
    in 12..16 -> "Good afternoon"
    else -> "Good evening"
}
