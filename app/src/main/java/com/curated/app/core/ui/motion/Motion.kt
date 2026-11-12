package com.curated.app.core.ui.motion

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalAccessibilityManager

object CuratedMotion {
    val softSpring = spring<Float>(
        dampingRatio = 0.85f,
        stiffness = Spring.StiffnessLow
    )
    val cardPress = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium
    )
    fun fadeMs(reduced: Boolean): Int = if (reduced) 120 else 220
    fun sheetMs(reduced: Boolean): Int = if (reduced) 160 else 280
    fun fadeSpec(reduced: Boolean) = tween<Float>(fadeMs(reduced))
}

@Composable
fun rememberReducedMotion(): Boolean {
    // AccessibilityManager doesn't expose reduce-motion directly on all APIs;
    // keep a single place to tighten animations later.
    LocalAccessibilityManager.current
    return false
}
