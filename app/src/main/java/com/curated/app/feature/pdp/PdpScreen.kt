package com.curated.app.feature.pdp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.curated.app.core.ui.components.BackCircleButton
import com.curated.app.core.ui.components.CircleIconButton
import com.curated.app.core.ui.components.ColorSwatches
import com.curated.app.core.ui.components.FashionImage
import com.curated.app.core.ui.components.GlassSurface
import com.curated.app.core.ui.components.PrimaryCta
import com.curated.app.core.ui.components.SizePill
import com.curated.app.core.ui.components.liquid.LiquidBackdropBox
import com.curated.app.core.ui.components.liquid.rememberLiquidMergeGroup
import com.curated.app.core.ui.components.shareText
import com.curated.app.core.ui.theme.MauvePrimary
import com.curated.app.core.ui.theme.SansUi
import com.curated.app.core.ui.theme.SerifDisplay
import com.curated.app.core.ui.theme.StarGold
import com.curated.app.core.ui.theme.TextPrimary
import com.curated.app.core.ui.theme.TextSecondary
import com.curated.app.data.mock.MockCatalog

@Composable
fun PdpScreen(
    productId: String,
    onBack: () -> Unit,
    onOpenCart: () -> Unit,
    viewModel: PdpViewModel = hiltViewModel()
) {
    LaunchedEffect(productId) { viewModel.load(productId) }
    val state by viewModel.ui.collectAsStateWithLifecycle()
    var sizeMenu by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val product = state.product

    if (product == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Product not found", color = TextPrimary)
        }
        return
    }

    // The product photo is the backdrop for the water buttons and the details sheet.
    LiquidBackdropBox(
        modifier = Modifier.fillMaxSize(),
        backdropContent = {
            // Tall enough to run under the details sheet whatever the sheet's height
            // (it is shorter for pieces without ratings), so no bare strip shows between.
            FashionImage(
                resId = product.imageRes,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
            )
        }
    ) {
        // Heart and share sit close enough to flow into one drop of water.
        val actionsGroup = rememberLiquidMergeGroup()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BackCircleButton(onClick = onBack)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                CircleIconButton(
                    icon = if (state.wishlisted) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                    onClick = viewModel::toggleWishlist,
                    tint = if (state.wishlisted) MauvePrimary else TextPrimary,
                    mergeGroup = actionsGroup
                )
                CircleIconButton(
                    icon = Icons.Outlined.Share,
                    onClick = {
                        context.shareText(
                            "${MockCatalog.brandName(product.brandId)} ${product.name}, " +
                                "${MockCatalog.formatPkr(product.pricePkr)}, on Curated"
                        )
                    },
                    mergeGroup = actionsGroup
                )
            }
        }

        GlassSurface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding(),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            strong = true,
            elevation = 16.dp
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    MockCatalog.brandName(product.brandId),
                    style = TextStyle(
                        fontFamily = SansUi,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                )
                Text(
                    product.name,
                    style = TextStyle(
                        fontFamily = SerifDisplay,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp,
                        color = TextPrimary
                    )
                )
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        MockCatalog.formatPkr(product.pricePkr),
                        style = TextStyle(
                            fontFamily = SansUi,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = TextPrimary
                        )
                    )
                    // Pieces read from a brand's store carry no ratings.
                    if (product.reviewCount > 0) {
                        Spacer(Modifier.width(8.dp))
                        androidx.compose.material3.Icon(
                            Icons.Outlined.Star,
                            contentDescription = null,
                            tint = StarGold,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            " ${product.rating} (${String.format("%.1fk", product.reviewCount / 1000f)} reviews)",
                            style = TextStyle(fontFamily = SansUi, fontSize = 12.sp, color = TextSecondary)
                        )
                    }
                }
                Spacer(Modifier.height(14.dp))
                Text(
                    "Colour: ${state.selectedColor}",
                    style = TextStyle(fontFamily = SansUi, fontSize = 13.sp, color = TextPrimary)
                )
                Spacer(Modifier.height(8.dp))
                ColorSwatches(
                    colors = product.colors.map { it.name to Color(it.hex) },
                    selected = state.selectedColor,
                    onSelect = viewModel::selectColor
                )
                Spacer(Modifier.height(14.dp))
                Box {
                    SizePill(size = state.selectedSize, onClick = { sizeMenu = true })
                    DropdownMenu(expanded = sizeMenu, onDismissRequest = { sizeMenu = false }) {
                        product.sizes.forEach { size ->
                            DropdownMenuItem(
                                text = { Text(size) },
                                onClick = {
                                    viewModel.selectSize(size)
                                    sizeMenu = false
                                }
                            )
                        }
                    }
                }
                Spacer(Modifier.height(18.dp))
                PrimaryCta(
                    text = "Add to Cart",
                    onClick = {
                        viewModel.addToCart()
                        onOpenCart()
                    }
                )
            }
        }
    }
}
