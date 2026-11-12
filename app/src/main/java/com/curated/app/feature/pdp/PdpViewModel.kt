package com.curated.app.feature.pdp

import androidx.lifecycle.ViewModel
import com.curated.app.data.model.Product
import com.curated.app.data.repo.CartRepository
import com.curated.app.data.repo.CatalogRepository
import com.curated.app.data.repo.WishlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class PdpUiState(
    val product: Product? = null,
    val selectedColor: String = "",
    val selectedSize: String = "",
    val wishlisted: Boolean = false
)

@HiltViewModel
class PdpViewModel @Inject constructor(
    private val catalog: CatalogRepository,
    private val cart: CartRepository,
    private val wishlist: WishlistRepository
) : ViewModel() {

    private val _ui = MutableStateFlow(PdpUiState())
    val ui: StateFlow<PdpUiState> = _ui.asStateFlow()

    fun load(productId: String) {
        val product = catalog.product(productId)
        _ui.value = PdpUiState(
            product = product,
            selectedColor = product?.colors?.firstOrNull()?.name.orEmpty(),
            selectedSize = product?.sizes?.firstOrNull { it == "M" } ?: product?.sizes?.firstOrNull().orEmpty(),
            wishlisted = wishlist.isProductWishlisted(productId)
        )
    }

    fun selectColor(color: String) {
        _ui.value = _ui.value.copy(selectedColor = color)
    }

    fun selectSize(size: String) {
        _ui.value = _ui.value.copy(selectedSize = size)
    }

    fun toggleWishlist() {
        val id = _ui.value.product?.id ?: return
        wishlist.toggleProduct(id)
        _ui.value = _ui.value.copy(wishlisted = wishlist.isProductWishlisted(id))
    }

    fun addToCart() {
        val p = _ui.value.product ?: return
        cart.add(p.id, _ui.value.selectedSize, _ui.value.selectedColor)
    }
}
