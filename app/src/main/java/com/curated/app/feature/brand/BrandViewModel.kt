package com.curated.app.feature.brand

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.curated.app.data.model.Brand
import com.curated.app.data.model.Outfit
import com.curated.app.data.model.Product
import com.curated.app.data.repo.CartRepository
import com.curated.app.data.repo.CatalogRepository
import com.curated.app.data.repo.WishlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class BrandUiState(
    // False until load() has run, so the screen does not flash "Brand not found".
    val loaded: Boolean = false,
    val brand: Brand? = null,
    val products: List<Product> = emptyList(),
    val outfits: List<Outfit> = emptyList()
)

@HiltViewModel
class BrandViewModel @Inject constructor(
    private val catalog: CatalogRepository,
    private val cart: CartRepository,
    private val wishlist: WishlistRepository
) : ViewModel() {

    private val _ui = MutableStateFlow(BrandUiState())
    val ui: StateFlow<BrandUiState> = _ui.asStateFlow()

    val wishlistIds: StateFlow<Set<String>> = wishlist.outfitIds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())

    fun load(brandId: String) {
        val brand = catalog.brand(brandId)
        _ui.value = BrandUiState(
            loaded = true,
            brand = brand,
            products = if (brand != null) catalog.productsByBrand(brandId) else emptyList(),
            outfits = if (brand != null) catalog.outfitsForBrand(brandId) else emptyList()
        )
    }

    fun addProduct(productId: String) {
        val product = catalog.product(productId) ?: return
        cart.addProducts(listOf(product))
    }

    fun toggleWishlist(outfitId: String) {
        wishlist.toggleOutfit(outfitId)
    }

    fun addOutfitToCart(outfitId: String) {
        val outfit = catalog.outfit(outfitId) ?: return
        cart.addProducts(catalog.productsForOutfit(outfit))
    }
}
