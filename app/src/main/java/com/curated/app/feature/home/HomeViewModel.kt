package com.curated.app.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.curated.app.data.model.AccountState
import com.curated.app.data.model.Brand
import com.curated.app.data.model.Category
import com.curated.app.data.model.Outfit
import com.curated.app.data.repo.AccountRepository
import com.curated.app.data.repo.CartRepository
import com.curated.app.data.repo.CatalogRepository
import com.curated.app.data.repo.WishlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val search: String = "",
    val aiPrompt: String = "",
    val brands: List<Brand> = emptyList(),
    val outfits: List<Outfit> = emptyList(),
    val categories: List<Category> = Category.entries
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val catalog: CatalogRepository,
    private val cart: CartRepository,
    private val wishlist: WishlistRepository,
    accountRepository: AccountRepository
) : ViewModel() {

    /** Feeds the greeting and the dot on the bell. */
    val account: StateFlow<AccountState> = accountRepository.state

    private val _ui = MutableStateFlow(
        HomeUiState(
            brands = catalog.brands(),
            outfits = catalog.outfits().take(4)
        )
    )
    val ui: StateFlow<HomeUiState> = _ui.asStateFlow()

    val wishlistIds: StateFlow<Set<String>> = wishlist.outfitIds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())

    fun onSearchChange(value: String) {
        _ui.value = _ui.value.copy(search = value)
    }

    fun onAiPromptChange(value: String) {
        _ui.value = _ui.value.copy(aiPrompt = value)
    }

    fun toggleWishlist(outfitId: String) {
        wishlist.toggleOutfit(outfitId)
    }

    fun addOutfitToCart(outfitId: String) {
        viewModelScope.launch {
            val outfit = catalog.outfit(outfitId) ?: return@launch
            cart.addProducts(catalog.productsForOutfit(outfit))
        }
    }
}
