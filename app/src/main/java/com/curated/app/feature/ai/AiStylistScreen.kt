package com.curated.app.feature.ai

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import com.curated.app.core.ui.components.liquid.LiquidBackdropBox
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.curated.app.R
import com.curated.app.core.ui.components.CuratedIcons
import com.curated.app.core.ui.components.FashionImage
import com.curated.app.core.ui.components.GlassSearchField
import com.curated.app.core.ui.components.GlassSurface
import com.curated.app.core.ui.components.MoodChip
import com.curated.app.core.ui.components.ScreenHeader
import com.curated.app.core.ui.components.SectionHeader
import com.curated.app.core.ui.theme.MauvePrimary
import com.curated.app.core.ui.theme.RoseAccentDeep
import com.curated.app.core.ui.theme.RoseBg
import com.curated.app.core.ui.theme.RoseBgEnd
import com.curated.app.core.ui.theme.SansUi
import com.curated.app.core.ui.theme.SerifDisplay
import com.curated.app.core.ui.theme.TextPrimary
import com.curated.app.core.ui.theme.TextSecondary
import com.curated.app.data.ai.LookSubject
import com.curated.app.navigation.AiPromptEntryPoint
import dagger.hilt.android.EntryPointAccessors

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AiStylistScreen(
    onBack: () -> Unit,
    onGenerated: (String) -> Unit,
    viewModel: AiStylistViewModel = hiltViewModel()
) {
    val state by viewModel.ui.collectAsStateWithLifecycle()
    val context = LocalContext.current.applicationContext

    LaunchedEffect(Unit) {
        val holder = EntryPointAccessors.fromApplication(
            context,
            AiPromptEntryPoint::class.java
        ).aiPromptHolder()
        val pending = holder.pendingPrompt
        if (pending.isNotBlank()) {
            viewModel.onPromptChange(pending)
            holder.pendingPrompt = ""
        }
    }

    // The page background is the backdrop the glass on this screen refracts.
    LiquidBackdropBox(
        modifier = Modifier.fillMaxSize(),
        background = Modifier.background(Brush.verticalGradient(listOf(RoseBg, RoseBgEnd)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 120.dp)
                .navigationBarsPadding()
        ) {
            ScreenHeader(
                title = "AI Stylist",
                onBack = onBack,
                trailing = {
                    Icon(
                        CuratedIcons.Sparkle,
                        contentDescription = null,
                        tint = TextPrimary,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(24.dp)
                    )
                }
            )
            Spacer(Modifier.height(20.dp))
            Text(
                text = "What are you\nin the mood for?",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontFamily = SerifDisplay,
                    fontWeight = FontWeight.Normal,
                    fontSize = 30.sp,
                    lineHeight = 36.sp,
                    color = TextPrimary
                )
            )
            Spacer(Modifier.height(20.dp))
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.vibeChips.forEach { vibe ->
                    MoodChip(
                        label = vibe,
                        selected = vibe in state.selectedVibes,
                        onClick = { viewModel.toggleVibe(vibe) }
                    )
                }
            }
            Spacer(Modifier.height(18.dp))
            GlassSurface(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .height(140.dp),
                shape = RoundedCornerShape(24.dp),
                strong = true
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    GlassSearchField(
                        value = state.prompt,
                        onValueChange = viewModel::onPromptChange,
                        placeholder = "A classy outfit for a dinner date in Lahore...",
                        onSubmit = { viewModel.generate(onGenerated) },
                        leadingIcon = Icons.Outlined.AutoAwesome,
                        singleLine = false,
                        minHeight = 100.dp
                    )
                }
            }
            state.error?.let { message ->
                Spacer(Modifier.height(10.dp))
                Text(
                    message,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontFamily = SansUi,
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp,
                        color = RoseAccentDeep
                    )
                )
            }
            Spacer(Modifier.height(14.dp))
            // Who the generated look picture is drawn on; remembered between visits.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Show the look on",
                    style = TextStyle(fontFamily = SansUi, fontSize = 13.sp, color = TextSecondary)
                )
                LookSubject.entries.forEach { subject ->
                    MoodChip(
                        label = subject.label,
                        selected = state.subject == subject,
                        onClick = { viewModel.selectSubject(subject) }
                    )
                }
            }
            Spacer(Modifier.height(22.dp))
            SectionHeader(title = "Or get inspired")
            Spacer(Modifier.height(12.dp))
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Each photo opens the look it comes from.
                val inspiration = listOf(
                    "o_dinner" to R.drawable.img_look_dinner,
                    "o_wedding" to R.drawable.img_look_wedding,
                    "o_casual" to R.drawable.img_look_casual,
                    "o_graceful" to R.drawable.img_look_graceful
                )
                items(inspiration) { (outfitId, res) ->
                    FashionImage(
                        resId = res,
                        modifier = Modifier
                            .width(120.dp)
                            .height(180.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { onGenerated(outfitId) }
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            Text(
                "Your looks are kept in Outfits, under My Creations. The look picture is an AI " +
                    "preview, so small details can differ from the real pieces.",
                modifier = Modifier.padding(horizontal = 20.dp),
                style = TextStyle(fontFamily = SansUi, fontSize = 12.sp, color = TextSecondary)
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "Tip: Upload your photo in the look result for a demo try-on preview.",
                modifier = Modifier.padding(horizontal = 20.dp),
                style = TextStyle(fontFamily = SansUi, fontSize = 12.sp, color = TextSecondary)
            )
        }

        AnimatedVisibility(
            visible = state.loading,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            GlassSurface(
                modifier = Modifier.padding(24.dp),
                shape = RoundedCornerShape(24.dp),
                strong = true
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = MauvePrimary)
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Curating your look...",
                        style = TextStyle(fontFamily = SerifDisplay, fontSize = 16.sp, color = TextPrimary)
                    )
                }
            }
        }
    }
}
