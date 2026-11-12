package com.curated.app.feature.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.curated.app.data.mock.MockCatalog
import com.curated.app.data.model.Address
import com.curated.app.data.model.OrderLine
import com.curated.app.data.model.PaymentMethod
import com.curated.app.data.repo.AccountRepository
import com.curated.app.data.repo.CartRepository
import com.curated.app.data.repo.CatalogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class CheckoutUiState(
    val lines: List<OrderLine> = emptyList(),
    val total: Int = 0,
    val addresses: List<Address> = emptyList(),
    val address: Address? = null,
    val payments: List<PaymentMethod> = listOf(PaymentMethod.CashOnDelivery),
    val payment: PaymentMethod = PaymentMethod.CashOnDelivery
) {
    val canPlace: Boolean get() = lines.isNotEmpty() && address != null
}

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val cart: CartRepository,
    private val catalog: CatalogRepository,
    private val account: AccountRepository
) : ViewModel() {

    // What the shopper picked on this visit; until they pick, the saved defaults apply.
    private val pickedAddressId = MutableStateFlow<String?>(null)
    private val pickedPaymentId = MutableStateFlow<String?>(null)

    val ui: StateFlow<CheckoutUiState> = combine(
        cart.items,
        account.state,
        pickedAddressId,
        pickedPaymentId
    ) { items, saved, addressId, paymentId ->
        val lines = items.mapNotNull { item ->
            catalog.product(item.productId)?.let { product ->
                OrderLine(
                    productId = product.id,
                    brand = MockCatalog.brandName(product.brandId),
                    name = product.name,
                    size = item.size,
                    color = item.color,
                    qty = item.qty,
                    pricePkr = product.pricePkr
                )
            }
        }
        CheckoutUiState(
            lines = lines,
            total = lines.sumOf { it.pricePkr * it.qty },
            addresses = saved.addresses,
            address = saved.addresses.firstOrNull { it.id == addressId } ?: saved.defaultAddress,
            payments = saved.payments,
            payment = saved.payments.firstOrNull { it.id == paymentId } ?: saved.defaultPayment
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), CheckoutUiState())

    fun pickAddress(id: String) {
        pickedAddressId.value = id
    }

    fun pickPayment(id: String) {
        pickedPaymentId.value = id
    }

    /** Records the order and empties the cart. Returns the new order's id, or null if it cannot be placed. */
    fun placeOrder(): String? {
        val state = ui.value
        val address = state.address ?: return null
        if (state.lines.isEmpty()) return null
        val order = account.placeOrder(state.lines, address, state.payment)
        cart.clear()
        return order.id
    }
}
