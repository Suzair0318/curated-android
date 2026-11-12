package com.curated.app.feature.outfits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.curated.app.data.ai.AiLookStore
import com.curated.app.data.model.Outfit
import com.curated.app.data.repo.CartRepository
import com.curated.app.data.repo.CatalogRepository
import com.curated.app.data.repo.OutfitRepository
import com.curated.app.data.repo.WishlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

enum class OutfitsTab { Saved, Creations }

data class OutfitsUiState(
    val tab: OutfitsTab = OutfitsTab.Saved,
    val saved: List<Outfit> = emptyList(),
    val creations: List<Outfit> = emptyList(),
    val wishlisted: Set<String> = emptySet()
)

@HiltViewModel
class OutfitsViewModel @Inject constructor(
    private val outfits: OutfitRepository,
    private val catalog: CatalogRepository,
    private val wishlist: WishlistRepository,
    private val cart: CartRepository,
    aiLooks: AiLookStore
) : ViewModel() {

    private val tab = MutableStateFlow(OutfitsTab.Saved)

    // aiLooks.looks is only a trigger: an AI look gets its picture after it is listed,
    // and the list has to pick the new cover up.
    val ui: StateFlow<OutfitsUiState> = combine(
        tab,
        outfits.savedIds,
        outfits.creationIds,
        wishlist.outfitIds,
        aiLooks.looks
    ) { selected, savedIds, creationIds, wishIds, _ ->
        OutfitsUiState(
            tab = selected,
            saved = savedIds.mapNotNull { catalog.outfit(it) },
            creations = creationIds.mapNotNull { catalog.outfit(it) },
            wishlisted = wishIds
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), OutfitsUiState())

    fun selectTab(value: OutfitsTab) {
        tab.update { value }
    }

    fun toggleWishlist(id: String) = wishlist.toggleOutfit(id)

    fun addOutfit(id: String) {
        val outfit = catalog.outfit(id) ?: return
        cart.addProducts(catalog.productsForOutfit(outfit))
    }
}
