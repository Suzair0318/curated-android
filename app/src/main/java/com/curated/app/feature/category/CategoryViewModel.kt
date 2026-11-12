package com.curated.app.feature.category

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.curated.app.data.model.Category
import com.curated.app.data.model.Product
import com.curated.app.data.repo.CatalogRepository
import com.curated.app.data.repo.WishlistRepository
import com.curated.app.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/** Chip order on the listing. [Category.More] is the "All" filter, so it leads. */
private val Filters = listOf(Category.More) + Category.entries.filter { it != Category.More }

data class CategoryUiState(
    val filters: List<Category> = Filters,
    val selected: Category = Category.More,
    val products: List<Product> = emptyList()
)

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val savedState: SavedStateHandle,
    private val catalog: CatalogRepository,
    private val wishlist: WishlistRepository
) : ViewModel() {

    // The route argument only seeds the filter. From then on the choice lives here, so
    // it survives a trip to a product page and back. An unknown name shows everything.
    private val _ui = MutableStateFlow(
        stateFor(
            Category.entries.find { it.name == savedState.get<String>(Routes.CategoryArg) }
                ?: Category.More
        )
    )
    val ui: StateFlow<CategoryUiState> = _ui.asStateFlow()

    val wishlistIds: StateFlow<Set<String>> = wishlist.productIds

    fun select(category: Category) {
        // Written over the route argument so the choice also survives process death.
        savedState[Routes.CategoryArg] = category.name
        _ui.value = stateFor(category)
    }

    fun toggleWishlist(productId: String) {
        wishlist.toggleProduct(productId)
    }

    private fun stateFor(category: Category) = CategoryUiState(
        selected = category,
        products = catalog.productsFor(category)
    )
}
