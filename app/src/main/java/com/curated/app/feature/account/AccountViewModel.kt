package com.curated.app.feature.account

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.curated.app.data.model.AccountState
import com.curated.app.data.model.Address
import com.curated.app.data.repo.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/** Shared by the account screens: they all read and edit the same saved account. */
@HiltViewModel
class AccountViewModel @Inject constructor(
    private val account: AccountRepository
) : ViewModel() {

    val state: StateFlow<AccountState> = account.state

    fun saveProfile(name: String, email: String, phone: String) = account.saveProfile(name, email, phone)

    fun setAvatar(uri: Uri) = account.setAvatar(uri)

    fun saveAddress(address: Address) = account.saveAddress(address)

    fun deleteAddress(id: String) = account.deleteAddress(id)

    fun setDefaultAddress(id: String) = account.setDefaultAddress(id)

    fun addCard(holder: String, last4: String, expiry: String) = account.addCard(holder, last4, expiry)

    fun deleteCard(id: String) = account.deleteCard(id)

    fun setDefaultPayment(id: String) = account.setDefaultPayment(id)

    fun markNotificationsRead() = account.markNotificationsRead()

    fun setOrderUpdates(enabled: Boolean) = account.setOrderUpdates(enabled)

    fun clearAll() = account.clearAll()
}
