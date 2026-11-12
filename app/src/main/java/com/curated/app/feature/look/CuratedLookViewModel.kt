package com.curated.app.feature.look

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.curated.app.data.ai.AiLookStore
import com.curated.app.data.ai.LookImageGenerator
import com.curated.app.data.ai.LookImageStatus
import com.curated.app.data.ai.LookSubject
import com.curated.app.data.model.Outfit
import com.curated.app.data.model.Product
import com.curated.app.data.repo.CartRepository
import com.curated.app.data.repo.CatalogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Where the picture of an AI look stands. Catalogue looks are always [Ready]. */
sealed interface LookImage {
    data object Ready : LookImage
    data object Generating : LookImage

    /** No picture: it failed ([message] says why) or was never finished. */
    data class Missing(val message: String?) : LookImage
}

data class LookUiState(
    val outfit: Outfit? = null,
    val products: List<Product> = emptyList(),
    val image: LookImage = LookImage.Ready,
    /** Who an AI look's picture is drawn on; null for catalogue looks. */
    val subject: LookSubject? = null
)

@HiltViewModel
class CuratedLookViewModel @Inject constructor(
    private val catalog: CatalogRepository,
    private val cart: CartRepository,
    private val aiLooks: AiLookStore,
    private val images: LookImageGenerator
) : ViewModel() {

    private val _ui = MutableStateFlow(LookUiState())
    val ui: StateFlow<LookUiState> = _ui.asStateFlow()

    private var outfitId: String? = null
    private var watching: Job? = null

    fun load(outfitId: String) {
        this.outfitId = outfitId
        watching?.cancel()
        // Filled in at once so the screen never opens on "not found".
        _ui.value = stateFor(outfitId, images.status.value[outfitId])
        // An AI look changes while it is open: its picture arrives a little after the pieces.
        watching = viewModelScope.launch {
            combine(aiLooks.looks, images.status) { _, status -> status[outfitId] }
                .collect { status -> _ui.value = stateFor(outfitId, status) }
        }
    }

    private fun stateFor(outfitId: String, status: LookImageStatus?): LookUiState {
        val outfit = catalog.outfit(outfitId)
        return LookUiState(
            outfit = outfit,
            products = outfit?.let { catalog.productsForOutfit(it) }.orEmpty(),
            image = when {
                outfit == null || !outfit.isAiLook || outfit.heroImagePath != null -> LookImage.Ready
                status == LookImageStatus.Generating -> LookImage.Generating
                else -> LookImage.Missing((status as? LookImageStatus.Failed)?.message)
            },
            subject = aiLooks.look(outfitId)?.subject
        )
    }

    /** Makes (or retries) the picture of the AI look on screen. */
    fun createImage() {
        outfitId?.let(images::start)
    }

    fun addProduct(productId: String) {
        val product = catalog.product(productId) ?: return
        cart.add(
            productId = product.id,
            size = product.sizes.firstOrNull() ?: "M",
            color = product.colors.firstOrNull()?.name ?: "Default"
        )
    }

    fun addEntireOutfit() {
        cart.addProducts(_ui.value.products)
    }
}
