package com.curated.app.feature.look

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.curated.app.core.ui.components.ProductLookCard
import com.curated.app.core.ui.components.ScreenHeader
import com.curated.app.core.ui.components.liquid.LiquidBackdropBox
import com.curated.app.core.ui.components.satinBackground
import com.curated.app.data.model.Outfit
import com.curated.app.data.repo.CartRepository
import com.curated.app.data.repo.CatalogRepository
import com.curated.app.data.repo.WishlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AllLooksViewModel @Inject constructor(
    private val catalog: CatalogRepository,
    private val cart: CartRepository,
    private val wishlist: WishlistRepository
) : ViewModel() {

    val outfits: List<Outfit> = catalog.outfits()

    val wishlistIds: StateFlow<Set<String>> = wishlist.outfitIds

    fun toggleWishlist(outfitId: String) = wishlist.toggleOutfit(outfitId)

    fun addOutfitToCart(outfitId: String) {
        val outfit = catalog.outfit(outfitId) ?: return
        cart.addProducts(catalog.productsForOutfit(outfit))
    }
}

/** Every look in the catalogue: where Home's "Curated for You, See all" leads. */
@Composable
fun AllLooksScreen(
    onBack: () -> Unit,
    onOpenLook: (String) -> Unit,
    viewModel: AllLooksViewModel = hiltViewModel()
) {
    val wishlistIds by viewModel.wishlistIds.collectAsStateWithLifecycle()

    // The satin is the backdrop the look cards on this screen refract.
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
            ScreenHeader(title = "Curated for You", onBack = onBack)
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 8.dp, bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(viewModel.outfits, key = { it.id }) { outfit ->
                    ProductLookCard(
                        outfit = outfit,
                        onClick = { onOpenLook(outfit.id) },
                        onWishlist = { viewModel.toggleWishlist(outfit.id) },
                        onAddToCart = { viewModel.addOutfitToCart(outfit.id) },
                        wishlisted = outfit.id in wishlistIds
                    )
                }
            }
        }
    }
}
