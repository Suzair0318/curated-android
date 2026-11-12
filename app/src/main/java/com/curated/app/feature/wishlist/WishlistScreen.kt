package com.curated.app.feature.wishlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.curated.app.core.ui.components.ProductCard
import com.curated.app.core.ui.components.ProductLookCard
import com.curated.app.core.ui.components.ScreenHeader
import com.curated.app.core.ui.components.liquid.LiquidBackdropBox
import com.curated.app.core.ui.theme.RoseBg
import com.curated.app.core.ui.theme.RoseBgEnd
import com.curated.app.core.ui.theme.SansUi
import com.curated.app.core.ui.theme.SerifDisplay
import com.curated.app.core.ui.theme.TextPrimary
import com.curated.app.core.ui.theme.TextSecondary

@Composable
fun WishlistScreen(
    onBack: () -> Unit,
    onOpenLook: (String) -> Unit,
    onOpenPdp: (String) -> Unit,
    viewModel: WishlistViewModel = hiltViewModel()
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
            ScreenHeader(title = "Wishlist", onBack = onBack)

            if (state.outfits.isEmpty() && state.products.isEmpty()) {
                Text(
                    "Save looks and pieces you love.",
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                    style = TextStyle(fontFamily = SansUi, fontSize = 14.sp, color = TextSecondary)
                )
            }

            state.outfits.chunked(2).forEach { row ->
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
                            onWishlist = { viewModel.toggleOutfit(outfit.id) },
                            onAddToCart = { viewModel.addOutfit(outfit.id) },
                            wishlisted = true,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (row.size == 1) Spacer(Modifier.weight(1f))
                }
            }

            if (state.products.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                Text(
                    "Saved pieces",
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp),
                    style = TextStyle(fontFamily = SerifDisplay, fontSize = 18.sp, color = TextPrimary)
                )
                state.products.chunked(2).forEach { row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        row.forEach { product ->
                            ProductCard(
                                product = product,
                                onClick = { onOpenPdp(product.id) },
                                onWishlist = { viewModel.toggleProduct(product.id) },
                                wishlisted = true,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (row.size == 1) Spacer(Modifier.weight(1f))
                    }
                }
            }
        }
    }
}
