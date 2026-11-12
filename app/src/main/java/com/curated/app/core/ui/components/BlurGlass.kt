package com.curated.app.core.ui.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.curated.app.core.ui.theme.CuratedShapes

/** Alias kept for call sites; liquid glass is implemented by [GlassSurface]. */
@Composable
fun FrostedGlass(
    modifier: Modifier = Modifier,
    shape: Shape = CuratedShapes.large,
    elevation: Dp = 10.dp,
    content: @Composable BoxScope.() -> Unit
) {
    GlassSurface(
        modifier = modifier,
        shape = shape,
        strong = true,
        elevation = elevation,
        content = content
    )
}
