package com.curated.app.feature.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.curated.app.R
import com.curated.app.core.ui.components.SatinWaveBackground
import com.curated.app.core.ui.theme.SansUi
import com.curated.app.core.ui.theme.TextPrimary
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val scale = remember { Animatable(0.92f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        alpha.animateTo(1f, tween(500))
        scale.animateTo(
            1f,
            spring(dampingRatio = 0.85f, stiffness = Spring.StiffnessLow)
        )
        delay(1600)
        onFinished()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        SatinWaveBackground(Modifier.fillMaxSize())
        Column(
            modifier = Modifier
                // Slightly above centre, as in the mockup.
                .align(BiasAlignment(0f, -0.13f))
                .scale(scale.value)
                .alpha(alpha.value),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.curated_logo_wordmark),
                contentDescription = "Curated",
                modifier = Modifier.fillMaxWidth(0.72f),
                contentScale = ContentScale.Fit
            )
            Spacer(Modifier.height(20.dp))
            Text(
                text = "Dream it. We'll style it.",
                style = TextStyle(
                    fontFamily = SansUi,
                    fontSize = 14.sp,
                    color = TextPrimary.copy(alpha = 0.85f)
                )
            )
        }
    }
}
