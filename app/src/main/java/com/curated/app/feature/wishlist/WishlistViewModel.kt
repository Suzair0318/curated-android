package com.curated.app.feature.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.curated.app.data.model.Outfit
import com.curated.app.data.model.Product
import com.curated.app.data.repo.CartRepository
import com.curated.app.data.repo.CatalogRepository
import com.curated.app.data.repo.WishlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class WishlistUiState(
    val outfits: List<Outfit> = emptyList(),
    val products: List<Product> = emptyList()
)

@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val wishlist: WishlistRepository,
    private val catalog: CatalogRepository,
    private val cart: CartRepository
) : ViewModel() {

    val ui: StateFlow<WishlistUiState> = combine(
        wishlist.outfitIds,
        wishlist.productIds
    ) { outfitIds, productIds ->
        WishlistUiState(
            outfits = outfitIds.mapNotNull { catalog.outfit(it) },
            products = productIds.mapNotNull { catalog.product(it) }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), WishlistUiState())

    fun toggleOutfit(id: String) = wishlist.toggleOutfit(id)

    fun toggleProduct(id: String) = wishlist.toggleProduct(id)

    fun addOutfit(id: String) {
        val outfit = catalog.outfit(id) ?: return
        cart.addProducts(catalog.productsForOutfit(outfit))
    }
}
