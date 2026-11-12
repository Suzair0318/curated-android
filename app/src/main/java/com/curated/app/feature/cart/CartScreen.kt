package com.curated.app.feature.cart

import androidx.compose.foundation.background
import com.curated.app.core.ui.components.liquid.LiquidBackdropBox
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.curated.app.core.ui.components.BackCircleButton
import com.curated.app.core.ui.components.CircleIconButton
import com.curated.app.core.ui.components.FashionImage
import com.curated.app.core.ui.components.GlassSurface
import com.curated.app.core.ui.components.PrimaryCta
import com.curated.app.core.ui.components.QtyStepper
import com.curated.app.core.ui.theme.RoseBg
import com.curated.app.core.ui.theme.RoseBgEnd
import com.curated.app.core.ui.theme.SansUi
import com.curated.app.core.ui.theme.SerifDisplay
import com.curated.app.core.ui.theme.TextPrimary
import com.curated.app.core.ui.theme.TextSecondary
import com.curated.app.data.mock.MockCatalog

@Composable
fun CartScreen(
    onBack: () -> Unit,
    onCheckout: () -> Unit,
    /** Room to leave at the bottom for a tab bar floating over this screen. */
    bottomBarSpace: Dp = 0.dp,
    viewModel: CartViewModel = hiltViewModel()
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
                .navigationBarsPadding()
                .padding(bottom = 16.dp + bottomBarSpace)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackCircleButton(onClick = onBack)
                Text(
                    "My Cart",
                    style = TextStyle(
                        fontFamily = SerifDisplay,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 22.sp,
                        color = TextPrimary
                    )
                )
                CircleIconButton(Icons.Outlined.DeleteOutline, onClick = viewModel::clear)
            }

            if (state.lines.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Your bag is empty",
                        style = TextStyle(fontFamily = SerifDisplay, fontSize = 20.sp, color = TextPrimary)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Curate a look and add pieces here.",
                        style = TextStyle(fontFamily = SansUi, fontSize = 13.sp, color = TextSecondary)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.lines, key = { "${it.item.productId}-${it.item.size}-${it.item.color}" }) { line ->
                        GlassSurface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            strong = true
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                FashionImage(
                                    resId = line.product.imageRes,
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                )
                                Spacer(Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        MockCatalog.brandName(line.product.brandId),
                                        style = TextStyle(
                                            fontFamily = SansUi,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp
                                        )
                                    )
                                    Text(line.product.name, style = TextStyle(fontFamily = SansUi, fontSize = 14.sp))
                                    Text(
                                        MockCatalog.formatPkr(line.product.pricePkr),
                                        style = TextStyle(
                                            fontFamily = SansUi,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 13.sp,
                                            color = TextPrimary
                                        )
                                    )
                                    Spacer(Modifier.height(6.dp))
                                    QtyStepper(
                                        qty = line.item.qty,
                                        onChange = {
                                            viewModel.updateQty(
                                                line.item.productId,
                                                line.item.size,
                                                line.item.color,
                                                it
                                            )
                                        }
                                    )
                                }
                                Icon(
                                    Icons.Outlined.Close,
                                    contentDescription = "Remove",
                                    tint = TextSecondary,
                                    modifier = Modifier
                                        .size(22.dp)
                                        .clickable {
                                            viewModel.remove(
                                                line.item.productId,
                                                line.item.size,
                                                line.item.color
                                            )
                                        }
                                )
                            }
                        }
                    }
                }

                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Subtotal",
                            style = TextStyle(
                                fontFamily = SerifDisplay,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp
                            )
                        )
                        Text(
                            MockCatalog.formatPkr(state.subtotal),
                            style = TextStyle(
                                fontFamily = SansUi,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    PrimaryCta(
                        text = "Proceed to Checkout →",
                        onClick = onCheckout,
                        icon = null
                    )
                }
            }
        }
    }
}
