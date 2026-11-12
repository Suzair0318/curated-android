package com.curated.app.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.curated.app.data.model.UserProfile
import com.curated.app.data.repo.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    account: AccountRepository
) : ViewModel() {
    val profile: StateFlow<UserProfile> = account.state
        .map { it.profile }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), account.state.value.profile)
}
