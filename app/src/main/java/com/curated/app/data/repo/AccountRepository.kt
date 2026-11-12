package com.curated.app.data.repo

import android.content.Context
import android.net.Uri
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.curated.app.data.model.AccountState
import com.curated.app.data.model.Address
import com.curated.app.data.model.AppNotification
import com.curated.app.data.model.Order
import com.curated.app.data.model.OrderLine
import com.curated.app.data.model.PaymentKind
import com.curated.app.data.model.PaymentMethod
import com.curated.app.data.model.UserProfile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.accountDataStore by preferencesDataStore(name = "curated_account")

/**
 * Profile, addresses, payment methods, orders, notifications and settings, kept on the
 * device as one JSON document. There is no server: an order placed here is only a
 * record in this file.
 */
@Singleton
class AccountRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val key = stringPreferencesKey("account_state")

    val state: StateFlow<AccountState> = context.accountDataStore.data
        .catch { error -> if (error is IOException) emit(emptyPreferences()) else throw error }
        .map { decode(it[key]) }
        .stateIn(scope, SharingStarted.Eagerly, AccountState())

    /** Read-modify-write inside one DataStore transaction, so edits never race. */
    private fun mutate(transform: (AccountState) -> AccountState) {
        scope.launch {
            context.accountDataStore.edit { prefs ->
                prefs[key] = encode(transform(decode(prefs[key])))
            }
        }
    }

    fun saveProfile(name: String, email: String, phone: String) = mutate {
        it.copy(profile = it.profile.copy(name = name.trim(), email = email.trim(), phone = phone.trim()))
    }

    /** Copies the picked photo into the app's files; the picker's own link does not last. */
    fun setAvatar(uri: Uri) {
        scope.launch {
            val file = File(context.filesDir, "avatar_${System.currentTimeMillis()}")
            val copied = runCatching {
                context.contentResolver.openInputStream(uri)?.use { input ->
                    file.outputStream().use { output -> input.copyTo(output) }
                } != null
            }.getOrDefault(false)
            if (!copied) {
                file.delete()
                return@launch
            }
            context.accountDataStore.edit { prefs ->
                val current = decode(prefs[key])
                current.profile.avatarPath?.let { File(it).delete() }
                prefs[key] = encode(current.copy(profile = current.profile.copy(avatarPath = file.path)))
            }
        }
    }

    /** Adds the address, or replaces the one with the same id. The first one becomes the default. */
    fun saveAddress(address: Address) = mutate { state ->
        val exists = state.addresses.any { it.id == address.id }
        val next = if (exists) {
            state.addresses.map { if (it.id == address.id) address else it }
        } else {
            state.addresses + address
        }
        state.copy(addresses = next, defaultAddressId = state.defaultAddressId ?: address.id)
    }

    fun deleteAddress(id: String) = mutate { state ->
        val next = state.addresses.filterNot { it.id == id }
        state.copy(
            addresses = next,
            defaultAddressId = if (state.defaultAddressId == id) next.firstOrNull()?.id else state.defaultAddressId
        )
    }

    fun setDefaultAddress(id: String) = mutate { it.copy(defaultAddressId = id) }

    fun addCard(holder: String, last4: String, expiry: String) = mutate { state ->
        val card = PaymentMethod(
            id = newId("card"),
            kind = PaymentKind.Card,
            holder = holder.trim(),
            last4 = last4,
            expiry = expiry
        )
        state.copy(cards = state.cards + card)
    }

    fun deleteCard(id: String) = mutate { state ->
        state.copy(
            cards = state.cards.filterNot { it.id == id },
            defaultPaymentId = if (state.defaultPaymentId == id) PaymentMethod.COD_ID else state.defaultPaymentId
        )
    }

    fun setDefaultPayment(id: String) = mutate { it.copy(defaultPaymentId = id) }

    /** Records the order and, when order updates are on, a notification about it. */
    fun placeOrder(lines: List<OrderLine>, address: Address, payment: PaymentMethod): Order {
        val now = System.currentTimeMillis()
        val order = Order(
            id = "CU" + (now / 1000 % 1_000_000).toString().padStart(6, '0'),
            placedAt = now,
            lines = lines,
            total = lines.sumOf { it.pricePkr * it.qty },
            recipient = "${address.fullName}, ${address.phone}",
            addressText = address.summary,
            paymentTitle = payment.title
        )
        mutate { state ->
            val notifications = if (state.orderUpdates) {
                val itemCount = lines.sumOf { it.qty }
                val items = if (itemCount == 1) "1 item" else "$itemCount items"
                val note = AppNotification(
                    id = newId("note"),
                    title = "Order ${order.id} placed",
                    body = "$items for delivery to ${address.city}. See My Orders for the details.",
                    at = now
                )
                listOf(note) + state.notifications
            } else {
                state.notifications
            }
            state.copy(orders = listOf(order) + state.orders, notifications = notifications)
        }
        return order
    }

    fun markNotificationsRead() = mutate { state ->
        if (state.unreadNotifications == 0) state
        else state.copy(notifications = state.notifications.map { it.copy(read = true) })
    }

    fun setOrderUpdates(enabled: Boolean) = mutate { it.copy(orderUpdates = enabled) }

    /** Back to a fresh account: profile, addresses, cards, orders and notifications all go. */
    fun clearAll() = mutate { state ->
        state.profile.avatarPath?.let { File(it).delete() }
        AccountState()
    }

    private fun newId(prefix: String) = "${prefix}_${System.nanoTime()}"

    private fun encode(state: AccountState): String = JSONObject().apply {
        put("name", state.profile.name)
        put("email", state.profile.email)
        put("phone", state.profile.phone)
        putOpt("avatarPath", state.profile.avatarPath)
        put("addresses", JSONArray(state.addresses.map { a ->
            JSONObject().apply {
                put("id", a.id)
                put("label", a.label)
                put("fullName", a.fullName)
                put("phone", a.phone)
                put("line", a.line)
                put("city", a.city)
            }
        }))
        putOpt("defaultAddressId", state.defaultAddressId)
        put("cards", JSONArray(state.cards.map { c ->
            JSONObject().apply {
                put("id", c.id)
                put("holder", c.holder)
                put("last4", c.last4)
                put("expiry", c.expiry)
            }
        }))
        put("defaultPaymentId", state.defaultPaymentId)
        put("orders", JSONArray(state.orders.map { o ->
            JSONObject().apply {
                put("id", o.id)
                put("placedAt", o.placedAt)
                put("total", o.total)
                put("recipient", o.recipient)
                put("addressText", o.addressText)
                put("paymentTitle", o.paymentTitle)
                put("lines", JSONArray(o.lines.map { l ->
                    JSONObject().apply {
                        put("productId", l.productId)
                        put("brand", l.brand)
                        put("name", l.name)
                        put("size", l.size)
                        put("color", l.color)
                        put("qty", l.qty)
                        put("pricePkr", l.pricePkr)
                    }
                }))
            }
        }))
        put("notifications", JSONArray(state.notifications.map { n ->
            JSONObject().apply {
                put("id", n.id)
                put("title", n.title)
                put("body", n.body)
                put("at", n.at)
                put("read", n.read)
            }
        }))
        put("orderUpdates", state.orderUpdates)
    }.toString()

    /** Nothing saved yet, or a document that cannot be read, is a fresh account. */
    private fun decode(json: String?): AccountState {
        if (json.isNullOrBlank()) return AccountState()
        return try {
            val root = JSONObject(json)
            val defaults = UserProfile()
            AccountState(
                profile = defaults.copy(
                    name = root.optString("name", defaults.name),
                    email = root.optString("email"),
                    phone = root.optString("phone"),
                    avatarPath = root.optString("avatarPath").takeIf { it.isNotBlank() && File(it).exists() }
                ),
                addresses = root.objects("addresses").map { a ->
                    Address(
                        id = a.getString("id"),
                        label = a.optString("label"),
                        fullName = a.optString("fullName"),
                        phone = a.optString("phone"),
                        line = a.optString("line"),
                        city = a.optString("city")
                    )
                },
                defaultAddressId = root.optString("defaultAddressId").takeIf { it.isNotBlank() },
                cards = root.objects("cards").map { c ->
                    PaymentMethod(
                        id = c.getString("id"),
                        kind = PaymentKind.Card,
                        holder = c.optString("holder"),
                        last4 = c.optString("last4"),
                        expiry = c.optString("expiry")
                    )
                },
                defaultPaymentId = root.optString("defaultPaymentId", PaymentMethod.COD_ID),
                orders = root.objects("orders").map { o ->
                    Order(
                        id = o.getString("id"),
                        placedAt = o.optLong("placedAt"),
                        lines = o.objects("lines").map { l ->
                            OrderLine(
                                productId = l.optString("productId"),
                                brand = l.optString("brand"),
                                name = l.optString("name"),
                                size = l.optString("size"),
                                color = l.optString("color"),
                                qty = l.optInt("qty", 1),
                                pricePkr = l.optInt("pricePkr")
                            )
                        },
                        total = o.optInt("total"),
                        recipient = o.optString("recipient"),
                        addressText = o.optString("addressText"),
                        paymentTitle = o.optString("paymentTitle")
                    )
                },
                notifications = root.objects("notifications").map { n ->
                    AppNotification(
                        id = n.getString("id"),
                        title = n.optString("title"),
                        body = n.optString("body"),
                        at = n.optLong("at"),
                        read = n.optBoolean("read")
                    )
                },
                orderUpdates = root.optBoolean("orderUpdates", true)
            )
        } catch (e: JSONException) {
            AccountState()
        }
    }

    private fun JSONObject.objects(name: String): List<JSONObject> {
        val array = optJSONArray(name) ?: return emptyList()
        return (0 until array.length()).map { array.getJSONObject(it) }
    }
}
