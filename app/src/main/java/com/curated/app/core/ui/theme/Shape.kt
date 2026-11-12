package com.curated.app.core.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val RadiusXs = 12.dp
val RadiusSm = 16.dp
val RadiusMd = 20.dp
val RadiusLg = 24.dp
val RadiusXl = 28.dp

val CuratedShapes = Shapes(
    extraSmall = RoundedCornerShape(RadiusXs),
    small = RoundedCornerShape(RadiusSm),
    medium = RoundedCornerShape(RadiusMd),
    large = RoundedCornerShape(RadiusLg),
    extraLarge = RoundedCornerShape(RadiusXl)
)
