package com.curated.app.feature.account

import android.util.Patterns
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.curated.app.core.ui.components.PrimaryCta
import com.curated.app.core.ui.theme.RoseAccentDeep
import com.curated.app.core.ui.theme.SansUi
import com.curated.app.core.ui.theme.TextSecondary
import com.curated.app.data.model.Address
import com.curated.app.data.model.PaymentKind

private fun String.digits(): String = filter { it.isDigit() }

@Composable
fun EditProfileScreen(
    onBack: () -> Unit,
    viewModel: AccountViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val profile = state.profile
    // Keyed on the saved values so the form fills in once the account has loaded.
    var name by rememberSaveable(profile.name) { mutableStateOf(profile.name) }
    var email by rememberSaveable(profile.email) { mutableStateOf(profile.email) }
    var phone by rememberSaveable(profile.phone) { mutableStateOf(profile.phone) }
    var showErrors by rememberSaveable { mutableStateOf(false) }

    val nameError = "Enter your name".takeIf { name.isBlank() }
    val emailError = "Enter a valid email".takeIf {
        email.isNotBlank() && !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
    }
    val phoneError = "Enter a valid phone number".takeIf { phone.isNotBlank() && phone.digits().length < 7 }

    val photoPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) viewModel.setAvatar(uri)
    }

    AccountPage(
        title = "Edit Profile",
        onBack = onBack,
        footer = {
            PrimaryCta(
                text = "Save",
                icon = null,
                onClick = {
                    if (nameError == null && emailError == null && phoneError == null) {
                        viewModel.saveProfile(name, email, phone)
                        onBack()
                    } else {
                        showErrors = true
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileAvatar(
                profile = profile,
                size = 96.dp,
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { photoPicker.launch("image/*") }
            )
            TextLink("Change photo", onClick = { photoPicker.launch("image/*") })
        }
        SectionCard {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                LabeledField(
                    label = "Full name",
                    value = name,
                    onValueChange = { name = it },
                    capitalization = KeyboardCapitalization.Words,
                    error = nameError.takeIf { showErrors }
                )
                LabeledField(
                    label = "Email",
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "you@example.com",
                    keyboardType = KeyboardType.Email,
                    error = emailError.takeIf { showErrors }
                )
                LabeledField(
                    label = "Phone",
                    value = phone,
                    onValueChange = { phone = it },
                    placeholder = "03xx xxxxxxx",
                    keyboardType = KeyboardType.Phone,
                    error = phoneError.takeIf { showErrors }
                )
            }
        }
    }
}

@Composable
fun AddressesScreen(
    onBack: () -> Unit,
    viewModel: AccountViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    // Null shows the list; an address with a blank id is a new one being written.
    var editing by remember { mutableStateOf<Address?>(null) }

    val current = editing
    if (current != null) {
        BackHandler { editing = null }
        AddressForm(
            initial = current,
            onCancel = { editing = null },
            onSave = {
                viewModel.saveAddress(it)
                editing = null
            }
        )
        return
    }

    AccountPage(
        title = "Addresses",
        onBack = onBack,
        footer = {
            PrimaryCta(
                text = "Add new address",
                icon = null,
                onClick = { editing = Address("", "Home", state.profile.name, state.profile.phone, "", "") }
            )
        }
    ) {
        if (state.addresses.isEmpty()) {
            EmptyState("No addresses yet", "Add where you would like your orders delivered.")
        }
        val defaultId = state.defaultAddress?.id
        state.addresses.forEach { address ->
            SectionCard {
                ChoiceRow(
                    selected = address.id == defaultId,
                    title = "${address.label} \u2022 ${address.fullName}",
                    subtitle = "${address.summary}\n${address.phone}",
                    onClick = { viewModel.setDefaultAddress(address.id) }
                )
                Row(modifier = Modifier.padding(start = 26.dp)) {
                    TextLink("Edit", onClick = { editing = address })
                    TextLink("Delete", onClick = { viewModel.deleteAddress(address.id) }, color = RoseAccentDeep)
                    if (address.id == defaultId) {
                        Text(
                            "Default",
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 8.dp),
                            style = TextStyle(fontFamily = SansUi, fontSize = 12.sp, color = TextSecondary)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AddressForm(
    initial: Address,
    onCancel: () -> Unit,
    onSave: (Address) -> Unit
) {
    var label by rememberSaveable { mutableStateOf(initial.label) }
    var fullName by rememberSaveable { mutableStateOf(initial.fullName) }
    var phone by rememberSaveable { mutableStateOf(initial.phone) }
    var line by rememberSaveable { mutableStateOf(initial.line) }
    var city by rememberSaveable { mutableStateOf(initial.city) }
    var showErrors by rememberSaveable { mutableStateOf(false) }

    val nameError = "Enter the recipient's name".takeIf { fullName.isBlank() }
    val phoneError = "Enter a valid phone number".takeIf { phone.digits().length < 7 }
    val lineError = "Enter the street address".takeIf { line.isBlank() }
    val cityError = "Enter the city".takeIf { city.isBlank() }

    AccountPage(
        title = if (initial.id.isBlank()) "New Address" else "Edit Address",
        onBack = onCancel,
        footer = {
            PrimaryCta(
                text = "Save address",
                icon = null,
                onClick = {
                    if (nameError == null && phoneError == null && lineError == null && cityError == null) {
                        onSave(
                            Address(
                                id = initial.id.ifBlank { "addr_${System.currentTimeMillis()}" },
                                label = label.trim().ifBlank { "Home" },
                                fullName = fullName.trim(),
                                phone = phone.trim(),
                                line = line.trim(),
                                city = city.trim()
                            )
                        )
                    } else {
                        showErrors = true
                    }
                }
            )
        }
    ) {
        SectionCard {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                LabeledField(
                    label = "Label",
                    value = label,
                    onValueChange = { label = it },
                    placeholder = "Home, Office...",
                    capitalization = KeyboardCapitalization.Words
                )
                LabeledField(
                    label = "Full name",
                    value = fullName,
                    onValueChange = { fullName = it },
                    capitalization = KeyboardCapitalization.Words,
                    error = nameError.takeIf { showErrors }
                )
                LabeledField(
                    label = "Phone",
                    value = phone,
                    onValueChange = { phone = it },
                    placeholder = "03xx xxxxxxx",
                    keyboardType = KeyboardType.Phone,
                    error = phoneError.takeIf { showErrors }
                )
                LabeledField(
                    label = "Street address",
                    value = line,
                    onValueChange = { line = it },
                    placeholder = "House, street, area",
                    capitalization = KeyboardCapitalization.Words,
                    error = lineError.takeIf { showErrors }
                )
                LabeledField(
                    label = "City",
                    value = city,
                    onValueChange = { city = it },
                    capitalization = KeyboardCapitalization.Words,
                    error = cityError.takeIf { showErrors }
                )
            }
        }
    }
}

@Composable
fun PaymentMethodsScreen(
    onBack: () -> Unit,
    viewModel: AccountViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var addingCard by rememberSaveable { mutableStateOf(false) }

    if (addingCard) {
        BackHandler { addingCard = false }
        CardForm(
            onCancel = { addingCard = false },
            onSave = { holder, last4, expiry ->
                viewModel.addCard(holder, last4, expiry)
                addingCard = false
            }
        )
        return
    }

    AccountPage(
        title = "Payment Methods",
        onBack = onBack,
        footer = { PrimaryCta(text = "Add card", icon = null, onClick = { addingCard = true }) }
    ) {
        val defaultId = state.defaultPayment.id
        SectionCard {
            state.payments.forEach { method ->
                // Cash on delivery is always offered, so only cards can be removed.
                val delete: (@Composable () -> Unit)? = if (method.kind == PaymentKind.Card) {
                    { TextLink("Delete", onClick = { viewModel.deleteCard(method.id) }, color = RoseAccentDeep) }
                } else {
                    null
                }
                ChoiceRow(
                    selected = method.id == defaultId,
                    title = method.title,
                    subtitle = when (method.kind) {
                        PaymentKind.CashOnDelivery -> "Pay when your order arrives"
                        PaymentKind.Card -> "${method.holder} \u2022 Expires ${method.expiry}"
                    },
                    onClick = { viewModel.setDefaultPayment(method.id) },
                    trailing = delete
                )
            }
        }
        DemoPaymentNote()
    }
}

/** Says plainly that cards here are not charged. */
@Composable
fun DemoPaymentNote(modifier: Modifier = Modifier) {
    Text(
        "Cards are saved on this phone for display only. No payment is taken, and only the " +
            "last four digits are kept.",
        modifier = modifier.padding(horizontal = 4.dp),
        style = TextStyle(fontFamily = SansUi, fontSize = 12.sp, color = TextSecondary)
    )
}

@Composable
private fun CardForm(
    onCancel: () -> Unit,
    onSave: (holder: String, last4: String, expiry: String) -> Unit
) {
    var holder by rememberSaveable { mutableStateOf("") }
    // The full number lives only in this form; saving keeps its last four digits.
    var number by remember { mutableStateOf("") }
    var expiry by rememberSaveable { mutableStateOf("") }
    var showErrors by rememberSaveable { mutableStateOf(false) }

    val holderError = "Enter the name on the card".takeIf { holder.isBlank() }
    val numberError = "Enter a 12 to 19 digit card number".takeIf { number.digits().length !in 12..19 }
    val expiryError = "Use MM/YY".takeIf { !Regex("(0[1-9]|1[0-2])/\\d{2}").matches(expiry.trim()) }

    AccountPage(
        title = "Add Card",
        onBack = onCancel,
        footer = {
            PrimaryCta(
                text = "Save card",
                icon = null,
                onClick = {
                    if (holderError == null && numberError == null && expiryError == null) {
                        onSave(holder, number.digits().takeLast(4), expiry.trim())
                    } else {
                        showErrors = true
                    }
                }
            )
        }
    ) {
        SectionCard {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                LabeledField(
                    label = "Name on card",
                    value = holder,
                    onValueChange = { holder = it },
                    capitalization = KeyboardCapitalization.Words,
                    error = holderError.takeIf { showErrors }
                )
                LabeledField(
                    label = "Card number",
                    value = number,
                    onValueChange = { input -> number = input.filter { it.isDigit() || it == ' ' }.take(23) },
                    placeholder = "0000 0000 0000 0000",
                    keyboardType = KeyboardType.Number,
                    error = numberError.takeIf { showErrors }
                )
                LabeledField(
                    label = "Expiry",
                    value = expiry,
                    // Plain keyboard: number pads do not all have a slash.
                    onValueChange = { input -> expiry = input.filter { it.isDigit() || it == '/' }.take(5) },
                    placeholder = "MM/YY",
                    error = expiryError.takeIf { showErrors }
                )
            }
        }
        Spacer(Modifier.height(2.dp))
        DemoPaymentNote()
    }
}
