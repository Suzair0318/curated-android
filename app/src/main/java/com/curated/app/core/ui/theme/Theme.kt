package com.curated.app.core.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush

private val CuratedColorScheme = lightColorScheme(
    primary = MauvePrimary,
    onPrimary = CreamSurface,
    primaryContainer = MauveSoft,
    onPrimaryContainer = TextPrimary,
    secondary = MauveSoft,
    onSecondary = TextPrimary,
    background = RoseBg,
    onBackground = TextPrimary,
    surface = CreamSurface,
    onSurface = TextPrimary,
    surfaceVariant = GlassFillStrong,
    onSurfaceVariant = TextSecondary,
    outline = BorderLight
)

@Composable
fun CuratedTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CuratedColorScheme,
        typography = CuratedTypography,
        shapes = CuratedShapes,
        content = content
    )
}

@Composable
fun RoseBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(RoseBg, RoseBgEnd)))
    ) {
        content()
    }
}
