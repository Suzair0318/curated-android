package com.curated.app.data.repo

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WishlistRepository @Inject constructor(
    private val prefs: PrefsStore
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val productIds: StateFlow<Set<String>> = prefs.wishlistProductIds
        .stateIn(scope, SharingStarted.Eagerly, setOf("p_shirt", "p_dupatta"))

    val outfitIds: StateFlow<Set<String>> = prefs.wishlistOutfitIds
        .stateIn(scope, SharingStarted.Eagerly, setOf("o_graceful", "o_dinner"))

    fun toggleProduct(id: String) {
        val next = if (id in productIds.value) productIds.value - id else productIds.value + id
        scope.launch { prefs.setWishlistProducts(next) }
    }

    fun toggleOutfit(id: String) {
        val next = if (id in outfitIds.value) outfitIds.value - id else outfitIds.value + id
        scope.launch { prefs.setWishlistOutfits(next) }
    }

    fun isProductWishlisted(id: String): Boolean = id in productIds.value
    fun isOutfitWishlisted(id: String): Boolean = id in outfitIds.value
}
