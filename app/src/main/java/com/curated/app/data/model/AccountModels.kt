package com.curated.app.data.model

data class Address(
    val id: String,
    val label: String,
    val fullName: String,
    val phone: String,
    val line: String,
    val city: String
) {
    val summary: String get() = "$line, $city"
}

enum class PaymentKind { CashOnDelivery, Card }

/**
 * A way to pay. Cards keep only what is needed to tell them apart: the full number is
 * never stored and no payment is ever taken with them.
 */
data class PaymentMethod(
    val id: String,
    val kind: PaymentKind,
    val holder: String = "",
    val last4: String = "",
    val expiry: String = ""
) {
    val title: String
        get() = if (kind == PaymentKind.CashOnDelivery) "Cash on Delivery" else "Card ending $last4"

    companion object {
        const val COD_ID = "cod"
        val CashOnDelivery = PaymentMethod(COD_ID, PaymentKind.CashOnDelivery)
    }
}

data class OrderLine(
    val productId: String,
    val brand: String,
    val name: String,
    val size: String,
    val color: String,
    val qty: Int,
    val pricePkr: Int
)

/** Address and payment are copied in, so later edits do not rewrite past orders. */
data class Order(
    val id: String,
    val placedAt: Long,
    val lines: List<OrderLine>,
    val total: Int,
    val recipient: String,
    val addressText: String,
    val paymentTitle: String
)

data class AppNotification(
    val id: String,
    val title: String,
    val body: String,
    val at: Long,
    val read: Boolean = false
)

/** Everything the account screens keep on the device. */
data class AccountState(
    val profile: UserProfile = UserProfile(),
    val addresses: List<Address> = emptyList(),
    val defaultAddressId: String? = null,
    val cards: List<PaymentMethod> = emptyList(),
    val defaultPaymentId: String = PaymentMethod.COD_ID,
    val orders: List<Order> = emptyList(),
    val notifications: List<AppNotification> = emptyList(),
    val orderUpdates: Boolean = true
) {
    /** Cash on delivery is always offered, ahead of any saved cards. */
    val payments: List<PaymentMethod> get() = listOf(PaymentMethod.CashOnDelivery) + cards

    val defaultAddress: Address?
        get() = addresses.firstOrNull { it.id == defaultAddressId } ?: addresses.firstOrNull()

    val defaultPayment: PaymentMethod
        get() = payments.firstOrNull { it.id == defaultPaymentId } ?: PaymentMethod.CashOnDelivery

    val unreadNotifications: Int get() = notifications.count { !it.read }
}
