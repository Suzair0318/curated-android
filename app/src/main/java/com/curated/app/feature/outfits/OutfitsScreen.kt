package com.curated.app.feature.outfits

import androidx.compose.foundation.background
import com.curated.app.core.ui.components.liquid.LiquidBackdropBox
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.curated.app.core.ui.components.ProductLookCard
import com.curated.app.core.ui.components.ScreenHeader
import com.curated.app.core.ui.theme.MauvePrimary
import com.curated.app.core.ui.theme.RoseBg
import com.curated.app.core.ui.theme.RoseBgEnd
import com.curated.app.core.ui.theme.SansUi
import com.curated.app.core.ui.theme.TextSecondary

@Composable
fun OutfitsScreen(
    onBack: () -> Unit,
    onOpenLook: (String) -> Unit,
    viewModel: OutfitsViewModel = hiltViewModel()
) {
    val state by viewModel.ui.collectAsStateWithLifecycle()

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
            ScreenHeader(title = "My Outfits", onBack = onBack)

            // Two tabs sharing the width, each with its underline, as in the mockup.
            Row(modifier = Modifier.padding(horizontal = 20.dp)) {
                TabLabel(
                    text = "Saved Looks",
                    selected = state.tab == OutfitsTab.Saved,
                    onClick = { viewModel.selectTab(OutfitsTab.Saved) },
                    modifier = Modifier.weight(1f)
                )
                TabLabel(
                    text = "My Creations",
                    selected = state.tab == OutfitsTab.Creations,
                    onClick = { viewModel.selectTab(OutfitsTab.Creations) },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(Modifier.height(16.dp))

            val list = if (state.tab == OutfitsTab.Saved) state.saved else state.creations
            if (list.isEmpty()) {
                Text(
                    if (state.tab == OutfitsTab.Saved) "No saved looks yet." else "Generate a look to see it here.",
                    modifier = Modifier.padding(horizontal = 20.dp),
                    style = TextStyle(fontFamily = SansUi, fontSize = 14.sp, color = TextSecondary)
                )
            }
            list.chunked(2).forEach { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    row.forEach { outfit ->
                        ProductLookCard(
                            outfit = outfit,
                            onClick = { onOpenLook(outfit.id) },
                            onWishlist = { viewModel.toggleWishlist(outfit.id) },
                            onAddToCart = { viewModel.addOutfit(outfit.id) },
                            wishlisted = outfit.id in state.wishlisted,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (row.size == 1) Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun TabLabel(text: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text,
            modifier = Modifier.padding(vertical = 8.dp),
            style = TextStyle(
                fontFamily = SansUi,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                fontSize = 14.sp,
                color = if (selected) MauvePrimary else TextSecondary
            )
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (selected) 2.dp else 1.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(if (selected) MauvePrimary else TextSecondary.copy(alpha = 0.25f))
        )
    }
}
