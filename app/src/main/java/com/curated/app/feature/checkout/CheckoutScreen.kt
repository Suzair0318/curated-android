package com.curated.app.feature.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.curated.app.core.ui.components.PrimaryCta
import com.curated.app.core.ui.components.SecondaryGlassCta
import com.curated.app.core.ui.theme.CreamSurface
import com.curated.app.core.ui.theme.MauvePressed
import com.curated.app.core.ui.theme.MauvePrimary
import com.curated.app.core.ui.theme.SansUi
import com.curated.app.core.ui.theme.SerifDisplay
import com.curated.app.core.ui.theme.TextPrimary
import com.curated.app.core.ui.theme.TextSecondary
import com.curated.app.data.mock.MockCatalog
import com.curated.app.data.model.PaymentKind
import com.curated.app.feature.account.AccountPage
import com.curated.app.feature.account.AccountViewModel
import com.curated.app.feature.account.ChoiceRow
import com.curated.app.feature.account.DemoPaymentNote
import com.curated.app.feature.account.EmptyState
import com.curated.app.feature.account.OrderCard
import com.curated.app.feature.account.SectionCard
import com.curated.app.feature.account.TextLink

private val BodyText = TextStyle(fontFamily = SansUi, fontSize = 13.sp, color = TextSecondary)
private val StrongText = TextStyle(
    fontFamily = SansUi,
    fontWeight = FontWeight.SemiBold,
    fontSize = 14.sp,
    color = TextPrimary
)

@Composable
fun CheckoutScreen(
    onBack: () -> Unit,
    onManageAddresses: () -> Unit,
    onManagePayments: () -> Unit,
    onOrderPlaced: (orderId: String) -> Unit,
    viewModel: CheckoutViewModel = hiltViewModel()
) {
    val state by viewModel.ui.collectAsStateWithLifecycle()

    AccountPage(
        title = "Checkout",
        onBack = onBack,
        footer = {
            PrimaryCta(
                text = "Place Order \u2022 ${MockCatalog.formatPkr(state.total)}",
                icon = null,
                enabled = state.canPlace,
                onClick = { viewModel.placeOrder()?.let(onOrderPlaced) }
            )
        }
    ) {
        SectionCard(title = "Delivery address") {
            if (state.addresses.isEmpty()) {
                Text("Add an address to place your order.", style = BodyText)
                Spacer(Modifier.height(10.dp))
                SecondaryGlassCta(text = "Add address", onClick = onManageAddresses)
            } else {
                state.addresses.forEach { address ->
                    ChoiceRow(
                        selected = address.id == state.address?.id,
                        title = "${address.label} \u2022 ${address.fullName}",
                        subtitle = "${address.summary}\n${address.phone}",
                        onClick = { viewModel.pickAddress(address.id) }
                    )
                }
                TextLink("Manage addresses", onClick = onManageAddresses)
            }
        }

        SectionCard(title = "Payment method") {
            state.payments.forEach { method ->
                ChoiceRow(
                    selected = method.id == state.payment.id,
                    title = method.title,
                    subtitle = when (method.kind) {
                        PaymentKind.CashOnDelivery -> "Pay when your order arrives"
                        PaymentKind.Card -> "${method.holder} \u2022 Expires ${method.expiry}"
                    },
                    onClick = { viewModel.pickPayment(method.id) }
                )
            }
            TextLink("Manage payment methods", onClick = onManagePayments)
            if (state.payment.kind == PaymentKind.Card) {
                DemoPaymentNote(modifier = Modifier.padding(top = 4.dp))
            }
        }

        SectionCard(title = "Order summary") {
            if (state.lines.isEmpty()) {
                Text("Your bag is empty.", style = BodyText)
            }
            state.lines.forEach { line ->
                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("${line.qty} \u00D7 ${line.name}", style = StrongText.copy(fontWeight = FontWeight.Medium))
                        Text("${line.brand} \u2022 ${line.size} \u2022 ${line.color}", style = BodyText.copy(fontSize = 11.sp))
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(MockCatalog.formatPkr(line.pricePkr * line.qty), style = BodyText)
                }
            }
            Spacer(Modifier.height(8.dp))
            Row {
                Text("Subtotal", modifier = Modifier.weight(1f), style = BodyText)
                Text(MockCatalog.formatPkr(state.total), style = BodyText)
            }
            Row {
                Text("Delivery", modifier = Modifier.weight(1f), style = BodyText)
                Text("Free", style = BodyText)
            }
            Spacer(Modifier.height(6.dp))
            Row {
                Text("Total", modifier = Modifier.weight(1f), style = StrongText)
                Text(MockCatalog.formatPkr(state.total), style = StrongText.copy(color = MauvePressed))
            }
        }
    }
}

@Composable
fun OrderPlacedScreen(
    orderId: String,
    onViewOrders: () -> Unit,
    onContinueShopping: () -> Unit,
    viewModel: AccountViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val order = state.orders.firstOrNull { it.id == orderId }

    AccountPage(
        title = "Order Placed",
        onBack = onContinueShopping,
        footer = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                PrimaryCta(text = "View my orders", icon = null, onClick = onViewOrders)
                SecondaryGlassCta(text = "Continue shopping", onClick = onContinueShopping)
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(MauvePrimary),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.Check, contentDescription = null, tint = CreamSurface, modifier = Modifier.size(34.dp))
            }
            Spacer(Modifier.height(14.dp))
            Text(
                "Thank you!",
                style = TextStyle(
                    fontFamily = SerifDisplay,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 26.sp,
                    color = TextPrimary
                )
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "Your order has been saved to My Orders.",
                textAlign = TextAlign.Center,
                style = BodyText
            )
        }
        if (order != null) {
            OrderCard(order)
        } else {
            // The order is written off the main thread, so it can trail this screen by a moment.
            EmptyState("Order $orderId", "Loading the details...")
        }
    }
}
