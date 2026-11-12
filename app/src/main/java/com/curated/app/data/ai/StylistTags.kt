package com.curated.app.data.ai

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

/** What a product is, as far as putting an outfit together goes. */
enum class PieceType(val key: String) {
    Outfit("outfit"),
    Top("top"),
    Bottom("bottom"),
    Outerwear("outerwear"),
    Scarf("scarf"),
    Shoes("shoes"),
    Bag("bag"),
    Jewellery("jewellery"),
    Other("other")
}

/**
 * What the stylist knows about one product. A vision model wrote these from the product's
 * photo and name (see `assets/stylist_tags.json`), so they are good guesses, not facts.
 */
data class ProductTags(
    val type: PieceType,
    /** "women", "men" or "kids". */
    val audience: String,
    /** How the photo shows the product: "standing", "seated", "cropped" or "flat". */
    val view: String,
    /** Any of "casual", "work", "party", "wedding", "festive". */
    val occasions: Set<String>,
    /** "desi", "western" or "fusion". */
    val style: String,
    /** "summer", "winter" or "all". */
    val season: String,
    val colour: String,
    val description: String
) {
    /** Whether the image model can see the whole product in its photo. */
    val goodReference: Boolean get() = view == "standing" || view == "flat"
}

@Singleton
class StylistTags @Inject constructor(
    @ApplicationContext private val context: Context
) {
    // Parsed on first use, which is always off the main thread (the stylist runs on IO).
    private val tags: Map<String, ProductTags> by lazy { load() }

    fun of(productId: String): ProductTags? = tags[productId]

    private fun load(): Map<String, ProductTags> = runCatching {
        val root = JSONObject(context.assets.open(ASSET).bufferedReader().use { it.readText() })
        buildMap {
            for (id in root.keys()) {
                val entry = root.getJSONObject(id)
                val occasions = entry.optJSONArray("o")
                put(
                    id,
                    ProductTags(
                        type = PieceType.entries.firstOrNull { it.key == entry.optString("t") } ?: PieceType.Other,
                        audience = entry.optString("f", "women"),
                        view = entry.optString("v", "cropped"),
                        occasions = buildSet {
                            if (occasions != null) for (i in 0 until occasions.length()) add(occasions.optString(i))
                        },
                        style = entry.optString("s", "fusion"),
                        season = entry.optString("w", "all"),
                        colour = entry.optString("c"),
                        description = entry.optString("d")
                    )
                )
            }
        }
    }.getOrDefault(emptyMap())

    private companion object {
        const val ASSET = "stylist_tags.json"
    }
}
