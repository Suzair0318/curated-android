package com.curated.app.feature.brand

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.curated.app.core.ui.components.BackCircleButton
import com.curated.app.core.ui.components.CircleIconButton
import com.curated.app.core.ui.components.CuratedIcons
import com.curated.app.core.ui.components.GlassSurface
import com.curated.app.core.ui.components.ProductLookCard
import com.curated.app.core.ui.components.SectionHeader
import com.curated.app.core.ui.components.ShopTheLookRow
import com.curated.app.core.ui.components.satinBackground
import com.curated.app.core.ui.theme.SansUi
import com.curated.app.core.ui.theme.SerifDisplay
import com.curated.app.core.ui.theme.TextPrimary
import com.curated.app.core.ui.theme.TextSecondary

private val ScreenPadding = 18.dp

@Composable
fun BrandScreen(
    brandId: String,
    onBack: () -> Unit,
    onOpenPdp: (String) -> Unit,
    onOpenLook: (String) -> Unit,
    onOpenCart: () -> Unit,
    viewModel: BrandViewModel = hiltViewModel()
) {
    LaunchedEffect(brandId) { viewModel.load(brandId) }
    val state by viewModel.ui.collectAsStateWithLifecycle()
    val wishlistIds by viewModel.wishlistIds.collectAsStateWithLifecycle()
    val brand = state.brand

    Box(
        modifier = Modifier
            .fillMaxSize()
            .satinBackground()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackCircleButton(onClick = onBack)
                val logoRes = brand?.logoRes
                if (logoRes != null) {
                    // The brand's own logo stands in for the title.
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(logoRes),
                            contentDescription = brand?.name,
                            modifier = Modifier
                                // Stacked logos fill the height, long wordmarks the width.
                                .height(40.dp)
                                .widthIn(max = 150.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                } else {
                    Text(
                        text = brand?.name.orEmpty(),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 12.dp),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            fontFamily = SerifDisplay,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 22.sp,
                            color = TextPrimary
                        )
                    )
                }
                CircleIconButton(CuratedIcons.Bag, onClick = onOpenCart)
            }

            when {
                !state.loaded -> Unit

                brand == null -> Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Brand not found", color = TextPrimary)
                }

                state.products.isEmpty() && state.outfits.isEmpty() -> Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Nothing here yet",
                        style = TextStyle(fontFamily = SerifDisplay, fontSize = 20.sp, color = TextPrimary)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "New pieces from ${brand.name} are on their way.",
                        textAlign = TextAlign.Center,
                        style = TextStyle(fontFamily = SansUi, fontSize = 13.sp, color = TextSecondary)
                    )
                }

                else -> Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .navigationBarsPadding()
                        .padding(top = 8.dp, bottom = 24.dp)
                ) {
                    if (state.products.isNotEmpty()) {
                        SectionHeader(title = "Pieces")
                        Spacer(Modifier.height(10.dp))
                        GlassSurface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = ScreenPadding),
                            shape = RoundedCornerShape(22.dp),
                            strong = true
                        ) {
                            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)) {
                                state.products.forEach { product ->
                                    ShopTheLookRow(
                                        product = product,
                                        brandName = brand.name,
                                        onAdd = { viewModel.addProduct(product.id) },
                                        onClick = { onOpenPdp(product.id) }
                                    )
                                }
                            }
                        }
                        Spacer(Modifier.height(18.dp))
                    }

                    if (state.outfits.isNotEmpty()) {
                        SectionHeader(title = "Looks featuring ${brand.name}")
                        Spacer(Modifier.height(10.dp))
                        // Two-column rows built by hand: a lazy grid cannot nest in this scroll.
                        state.outfits.chunked(2).forEach { rowItems ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = ScreenPadding, vertical = 6.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                rowItems.forEach { outfit ->
                                    ProductLookCard(
                                        outfit = outfit,
                                        onClick = { onOpenLook(outfit.id) },
                                        onWishlist = { viewModel.toggleWishlist(outfit.id) },
                                        onAddToCart = { viewModel.addOutfitToCart(outfit.id) },
                                        wishlisted = outfit.id in wishlistIds,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                if (rowItems.size == 1) {
                                    Spacer(Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
