package com.curated.app.feature.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.curated.app.data.model.CartItem
import com.curated.app.data.model.Product
import com.curated.app.data.repo.CartRepository
import com.curated.app.data.repo.CatalogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class CartLine(
    val item: CartItem,
    val product: Product
)

data class CartUiState(
    val lines: List<CartLine> = emptyList(),
    val subtotal: Int = 0
)

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cart: CartRepository,
    private val catalog: CatalogRepository
) : ViewModel() {

    val ui: StateFlow<CartUiState> = cart.items.map { items ->
        val lines = items.mapNotNull { item ->
            catalog.product(item.productId)?.let { CartLine(item, it) }
        }
        CartUiState(
            lines = lines,
            subtotal = lines.sumOf { it.product.pricePkr * it.item.qty }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), CartUiState())

    fun updateQty(productId: String, size: String, color: String, qty: Int) {
        cart.updateQty(productId, size, color, qty)
    }

    fun remove(productId: String, size: String, color: String) {
        cart.remove(productId, size, color)
    }

    fun clear() = cart.clear()
}
