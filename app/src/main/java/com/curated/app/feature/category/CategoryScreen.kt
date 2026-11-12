package com.curated.app.feature.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.curated.app.core.ui.components.BackCircleButton
import com.curated.app.core.ui.components.MoodChip
import com.curated.app.core.ui.components.ProductCard
import com.curated.app.core.ui.components.liquid.LiquidBackdropBox
import com.curated.app.core.ui.components.satinBackground
import com.curated.app.core.ui.theme.SansUi
import com.curated.app.core.ui.theme.SerifDisplay
import com.curated.app.core.ui.theme.TextPrimary
import com.curated.app.core.ui.theme.TextSecondary
import com.curated.app.data.model.Category

private val ScreenPadding = 18.dp

/** [Category.More] is the tile that opens the whole catalogue, so here it reads "All". */
private val Category.filterLabel: String
    get() = if (this == Category.More) "All" else label

@Composable
fun CategoryScreen(
    onBack: () -> Unit,
    onOpenPdp: (String) -> Unit,
    viewModel: CategoryViewModel = hiltViewModel()
) {
    val state by viewModel.ui.collectAsStateWithLifecycle()
    val wishlistIds by viewModel.wishlistIds.collectAsStateWithLifecycle()

    // The satin is the backdrop the product cards on this screen refract.
    LiquidBackdropBox(
        modifier = Modifier.fillMaxSize(),
        background = Modifier.satinBackground()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                BackCircleButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart))
                Text(
                    state.selected.filterLabel,
                    modifier = Modifier.align(Alignment.Center),
                    style = TextStyle(
                        fontFamily = SerifDisplay,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 22.sp,
                        color = TextPrimary
                    )
                )
            }

            // The six chips are wider than a phone. Start one chip before the selected
            // one so a tile near the end of the Home row does not open with its chip
            // scrolled out of sight.
            val chipsState = rememberLazyListState(
                initialFirstVisibleItemIndex = (state.filters.indexOf(state.selected) - 1).coerceAtLeast(0)
            )
            LazyRow(
                state = chipsState,
                contentPadding = PaddingValues(horizontal = ScreenPadding, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.filters, key = { it.name }) { filter ->
                    MoodChip(
                        label = filter.filterLabel,
                        selected = filter == state.selected,
                        onClick = { viewModel.select(filter) }
                    )
                }
            }

            if (state.products.isEmpty()) {
                Text(
                    "Nothing here yet.",
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                    style = TextStyle(fontFamily = SansUi, fontSize = 14.sp, color = TextSecondary)
                )
            } else {
                // Keyed by filter so each one starts at the top with its own scroll position.
                key(state.selected) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentPadding = PaddingValues(
                            start = ScreenPadding,
                            end = ScreenPadding,
                            top = 8.dp,
                            bottom = 24.dp
                        ),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.products, key = { it.id }) { product ->
                            ProductCard(
                                product = product,
                                onClick = { onOpenPdp(product.id) },
                                onWishlist = { viewModel.toggleWishlist(product.id) },
                                wishlisted = product.id in wishlistIds
                            )
                        }
                    }
                }
            }
        }
    }
}
