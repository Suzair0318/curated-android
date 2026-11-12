package com.curated.app.feature.account

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.curated.app.core.ui.components.PrimaryCta
import com.curated.app.core.ui.theme.CreamSurface
import com.curated.app.core.ui.theme.MauvePressed
import com.curated.app.core.ui.theme.MauvePrimary
import com.curated.app.core.ui.theme.RoseAccentDeep
import com.curated.app.core.ui.theme.SansUi
import com.curated.app.core.ui.theme.TextPrimary
import com.curated.app.core.ui.theme.TextSecondary
import com.curated.app.data.mock.MockCatalog
import com.curated.app.data.model.Order

/**
 * Placeholder address on a reserved domain, so nothing is delivered anywhere by
 * mistake. Replace it with the real support inbox before release.
 */
private const val SUPPORT_EMAIL = "support@curated.example"

private val BodyText = TextStyle(fontFamily = SansUi, fontSize = 13.sp, color = TextSecondary)
private val RowTitle = TextStyle(
    fontFamily = SansUi,
    fontWeight = FontWeight.SemiBold,
    fontSize = 14.sp,
    color = TextPrimary
)

@Composable
fun OrdersScreen(
    onBack: () -> Unit,
    onStartShopping: () -> Unit,
    viewModel: AccountViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AccountPage(title = "My Orders", onBack = onBack) {
        if (state.orders.isEmpty()) {
            EmptyState("No orders yet", "Orders you place will show up here.")
            PrimaryCta(text = "Start shopping", icon = null, onClick = onStartShopping)
        }
        state.orders.forEach { order -> OrderCard(order) }
    }
}

@Composable
fun OrderCard(order: Order, modifier: Modifier = Modifier) {
    SectionCard(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Order ${order.id}", style = RowTitle)
                Text(formatDateTime(order.placedAt), style = BodyText.copy(fontSize = 12.sp))
            }
            Text(
                "Placed",
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(MauvePrimary)
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                style = TextStyle(
                    fontFamily = SansUi,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                    color = CreamSurface
                )
            )
        }
        Spacer(Modifier.height(10.dp))
        order.lines.forEach { line ->
            Row(modifier = Modifier.padding(vertical = 3.dp)) {
                Text(
                    "${line.qty} \u00D7 ${line.name}",
                    modifier = Modifier.weight(1f),
                    style = TextStyle(fontFamily = SansUi, fontSize = 13.sp, color = TextPrimary)
                )
                Spacer(Modifier.width(8.dp))
                Text(MockCatalog.formatPkr(line.pricePkr * line.qty), style = BodyText)
            }
        }
        Spacer(Modifier.height(8.dp))
        Row {
            Text("Total", modifier = Modifier.weight(1f), style = RowTitle)
            Text(MockCatalog.formatPkr(order.total), style = RowTitle.copy(color = MauvePressed))
        }
        Spacer(Modifier.height(8.dp))
        Text("${order.recipient}\n${order.addressText}", style = BodyText.copy(fontSize = 12.sp))
        Text(order.paymentTitle, style = BodyText.copy(fontSize = 12.sp))
    }
}

@Composable
fun NotificationsScreen(
    onBack: () -> Unit,
    viewModel: AccountViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    // Opening the page reads everything; keep which ones were new so they still stand out.
    val newIds = remember { state.notifications.filter { !it.read }.map { it.id }.toSet() }
    LaunchedEffect(Unit) { viewModel.markNotificationsRead() }

    AccountPage(title = "Notifications", onBack = onBack) {
        if (state.notifications.isEmpty()) {
            EmptyState("No notifications yet", "Updates about your orders will appear here.")
        }
        state.notifications.forEach { note ->
            SectionCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (note.id in newIds) {
                        Box(
                            Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(RoseAccentDeep)
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                    Text(note.title, modifier = Modifier.weight(1f), style = RowTitle)
                }
                Spacer(Modifier.height(4.dp))
                Text(note.body, style = BodyText)
                Spacer(Modifier.height(4.dp))
                Text(formatDateTime(note.at), style = BodyText.copy(fontSize = 11.sp))
            }
        }
    }
}

private val Faqs = listOf(
    "How does the AI Stylist work?" to
        "Describe the occasion or pick a mood and Curated puts a look together from its catalogue.",
    "How do I save a look?" to
        "Tap the heart on any look or piece. Saved items are in the Wishlist tab, and looks you " +
        "create are under My Outfits.",
    "How do I place an order?" to
        "Add pieces to your cart, choose a delivery address and a payment method at checkout, " +
        "then place the order. It appears under My Orders.",
    "Which payment methods can I use?" to
        "Cash on delivery. Cards can be saved, but card payments are not processed in this version.",
    "Can I change my delivery address?" to
        "Yes. Manage your addresses from Profile, or pick a different one at checkout."
)

@Composable
fun HelpSupportScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    var open by rememberSaveable { mutableStateOf(-1) }
    var noMailApp by rememberSaveable { mutableStateOf(false) }

    AccountPage(title = "Help & Support", onBack = onBack) {
        SectionCard(title = "Frequently asked") {
            Faqs.forEachIndexed { index, (question, answer) ->
                val expanded = open == index
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { open = if (expanded) -1 else index }
                        .padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(question, modifier = Modifier.weight(1f), style = RowTitle)
                    Icon(
                        if (expanded) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
                        contentDescription = if (expanded) "Collapse" else "Expand",
                        tint = TextSecondary
                    )
                }
                AnimatedVisibility(visible = expanded) {
                    Text(answer, modifier = Modifier.padding(bottom = 10.dp), style = BodyText)
                }
            }
        }
        SectionCard(title = "Contact us") {
            Text("Still stuck? Write to us and we will get back to you.", style = BodyText)
            Spacer(Modifier.height(4.dp))
            Text(SUPPORT_EMAIL, style = RowTitle)
            Spacer(Modifier.height(12.dp))
            PrimaryCta(
                text = "Email support",
                icon = null,
                onClick = {
                    val mail = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$SUPPORT_EMAIL"))
                        .putExtra(Intent.EXTRA_SUBJECT, "Curated app support")
                    noMailApp = try {
                        context.startActivity(mail)
                        false
                    } catch (e: ActivityNotFoundException) {
                        true
                    }
                }
            )
            if (noMailApp) {
                Spacer(Modifier.height(8.dp))
                Text("No email app is set up on this phone.", style = BodyText.copy(color = RoseAccentDeep))
            }
        }
    }
}

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: AccountViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val version = remember {
        runCatching { context.packageManager.getPackageInfo(context.packageName, 0).versionName }.getOrNull()
    }
    var confirmClear by rememberSaveable { mutableStateOf(false) }

    AccountPage(title = "Settings", onBack = onBack) {
        SectionCard(title = "Notifications") {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Order updates", style = RowTitle)
                    Text("Add a notification when an order is placed", style = BodyText.copy(fontSize = 12.sp))
                }
                Spacer(Modifier.width(12.dp))
                Switch(
                    checked = state.orderUpdates,
                    onCheckedChange = viewModel::setOrderUpdates,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = MauvePrimary,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = TextSecondary.copy(alpha = 0.35f),
                        uncheckedBorderColor = Color.Transparent
                    )
                )
            }
        }
        SectionCard(title = "Your data") {
            Text(
                "Your profile, addresses, saved cards, orders and notifications are kept on this phone only.",
                style = BodyText
            )
            Spacer(Modifier.height(6.dp))
            TextLink("Clear account data", onClick = { confirmClear = true }, color = RoseAccentDeep)
        }
        SectionCard(title = "About") {
            Row {
                Text("Version", modifier = Modifier.weight(1f), style = RowTitle)
                Text(version ?: "Unknown", style = BodyText)
            }
        }
    }

    if (confirmClear) {
        AlertDialog(
            onDismissRequest = { confirmClear = false },
            containerColor = CreamSurface,
            titleContentColor = TextPrimary,
            textContentColor = TextSecondary,
            title = { Text("Clear account data?", fontFamily = SansUi, fontWeight = FontWeight.SemiBold) },
            text = {
                Text(
                    "This removes your profile changes, addresses, saved cards, orders and notifications from this phone.",
                    fontFamily = SansUi
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.clearAll()
                    confirmClear = false
                }) { Text("Clear", color = RoseAccentDeep) }
            },
            dismissButton = {
                TextButton(onClick = { confirmClear = false }) { Text("Cancel", color = TextPrimary) }
            }
        )
    }
}
