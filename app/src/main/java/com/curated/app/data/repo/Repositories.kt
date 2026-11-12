package com.curated.app.data.repo

import com.curated.app.data.ai.AiLookStore
import com.curated.app.data.mock.MockCatalog
import com.curated.app.data.model.Brand
import com.curated.app.data.model.CartItem
import com.curated.app.data.model.Category
import com.curated.app.data.model.Outfit
import com.curated.app.data.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton
// WishlistRepository lives in WishlistRepository.kt (DataStore-backed)
// StylistRepository lives in StylistRepository.kt (the AI Stylist)

@Singleton
class CatalogRepository @Inject constructor(
    private val aiLooks: AiLookStore
) {
    fun brands(): List<Brand> = MockCatalog.brands.take(5) + listOf(
        Brand("more", "+ More", "+ More")
    )

    fun allBrands(): List<Brand> = MockCatalog.brands

    fun brand(id: String): Brand? = MockCatalog.brands.find { it.id == id }

    fun products(): List<Product> = MockCatalog.products

    fun product(id: String): Product? = MockCatalog.products.find { it.id == id }

    fun outfits(): List<Outfit> = MockCatalog.outfits

    /** A catalogue look, or one the AI Stylist made for this user. */
    fun outfit(id: String): Outfit? =
        MockCatalog.outfits.find { it.id == id } ?: aiLooks.outfit(id)

    fun productsForOutfit(outfit: Outfit): List<Product> =
        outfit.productIds.mapNotNull { product(it) }

    fun productsByCategory(category: Category): List<Product> =
        MockCatalog.products.filter { it.category == category }

    /**
     * Products behind a Home category tile. Women, Footwear and Jewellery are product
     * types; Designers and Local describe the brand; More is the whole catalogue.
     */
    fun productsFor(category: Category): List<Product> = when (category) {
        Category.Women, Category.Footwear, Category.Jewellery -> productsByCategory(category)
        Category.Designers -> productsOfBrands { it.isDesigner }
        Category.Local -> productsOfBrands { it.isLocal }
        Category.More -> products()
    }

    private fun productsOfBrands(matches: (Brand) -> Boolean): List<Product> {
        val brandIds = MockCatalog.brands.filter(matches).map { it.id }.toSet()
        return MockCatalog.products.filter { it.brandId in brandIds }
    }

    fun productsByBrand(brandId: String): List<Product> =
        MockCatalog.products.filter { it.brandId == brandId }

    /**
     * Looks that carry the brand, either through one of their products or through a
     * brand chip. Chips hold display names and do not always mirror the product list.
     */
    fun outfitsForBrand(brandId: String): List<Outfit> {
        val name = brand(brandId)?.name
        return MockCatalog.outfits.filter { outfit ->
            productsForOutfit(outfit).any { it.brandId == brandId } ||
                (name != null && outfit.brandChips.any { it.equals(name, ignoreCase = true) })
        }
    }
}

@Singleton
class CartRepository @Inject constructor() {
    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    val items: StateFlow<List<CartItem>> = _items.asStateFlow()

    fun add(productId: String, size: String, color: String, qty: Int = 1) {
        _items.update { current ->
            val existing = current.indexOfFirst {
                it.productId == productId && it.size == size && it.color == color
            }
            if (existing >= 0) {
                current.toMutableList().also {
                    val old = it[existing]
                    it[existing] = old.copy(qty = old.qty + qty)
                }
            } else {
                current + CartItem(productId, size, color, qty)
            }
        }
    }

    fun addProducts(products: List<Product>) {
        products.forEach { p ->
            val size = p.sizes.firstOrNull() ?: "M"
            val color = p.colors.firstOrNull()?.name ?: "Default"
            add(p.id, size, color)
        }
    }

    fun updateQty(productId: String, size: String, color: String, qty: Int) {
        _items.update { current ->
            if (qty <= 0) {
                current.filterNot {
                    it.productId == productId && it.size == size && it.color == color
                }
            } else {
                current.map {
                    if (it.productId == productId && it.size == size && it.color == color) {
                        it.copy(qty = qty)
                    } else it
                }
            }
        }
    }

    fun remove(productId: String, size: String, color: String) {
        _items.update { current ->
            current.filterNot {
                it.productId == productId && it.size == size && it.color == color
            }
        }
    }

    fun clear() {
        _items.value = emptyList()
    }

    fun count(): Int = _items.value.sumOf { it.qty }
}

@Singleton
class OutfitRepository @Inject constructor(
    private val catalog: CatalogRepository,
    aiLooks: AiLookStore
) {
    private val _saved = MutableStateFlow(
        catalog.outfits().filter { !it.isCreation }.map { it.id }.toSet()
    )
    val savedIds: StateFlow<Set<String>> = _saved.asStateFlow()

    // The user's AI looks come first, newest on top, then the catalogue's sample creation.
    private val _creations = MutableStateFlow(
        aiLooks.looks.value.map { it.id }.toSet() +
            catalog.outfits().filter { it.isCreation }.map { it.id }
    )
    val creationIds: StateFlow<Set<String>> = _creations.asStateFlow()

    fun saveOutfit(id: String) {
        _saved.update { it + id }
    }

    /** Puts a look the AI Stylist just made at the top of My Creations. */
    fun addCreation(id: String) {
        _creations.update { setOf(id) + it }
    }

    fun savedOutfits(): List<Outfit> =
        _saved.value.mapNotNull { catalog.outfit(it) }

    fun creationOutfits(): List<Outfit> =
        _creations.value.mapNotNull { catalog.outfit(it) }
}
