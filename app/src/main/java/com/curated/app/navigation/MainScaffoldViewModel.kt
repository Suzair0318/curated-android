package com.curated.app.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.curated.app.data.repo.WishlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainScaffoldViewModel @Inject constructor(
    wishlistRepository: WishlistRepository
) : ViewModel() {
    /** Everything the Wishlist tab lists: saved looks plus saved pieces. */
    val wishlistCount: StateFlow<Int> =
        combine(wishlistRepository.outfitIds, wishlistRepository.productIds) { looks, pieces ->
            looks.size + pieces.size
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)
}
