package com.curated.app.data.mock

import com.curated.app.R
import com.curated.app.data.model.Brand
import com.curated.app.data.model.Category
import com.curated.app.data.model.Outfit
import com.curated.app.data.model.Product
import com.curated.app.data.model.ProductColor

object MockCatalog {
    val brands = listOf(
        Brand("sana", "SANA SAFINAZ", "SANA SAFINAZ", isDesigner = true, logoRes = R.drawable.logo_brand_sana),
        Brand("khaadi", "KHAADI", "KHAADI", logoRes = R.drawable.logo_brand_khaadi),
        Brand("mariab", "MARIA.B.", "MARIA.B.", isDesigner = true, logoRes = R.drawable.logo_brand_mariab),
        Brand("elan", "ÉLAN", "ÉLAN", isDesigner = true, logoRes = R.drawable.logo_brand_elan),
        Brand("zara", "ZARA", "ZARA", isLocal = false, logoRes = R.drawable.logo_brand_zara),
        Brand("charcoal", "CHARCOAL", "CHARCOAL", logoRes = R.drawable.logo_brand_charcoal),
        // No logo could be sourced for this one, so it keeps its text wordmark.
        Brand("stepping", "STEPPING STONES", "STEPPING STONES"),
        Brand("mango", "MANGO", "MANGO", isLocal = false, logoRes = R.drawable.logo_brand_mango)
    ) + StoreFeedCatalog.brands

    /**
     * The four pieces below are still mock: their brands sell nothing like them (or
     * their stores could not be read). Everything else comes from [BrandCatalog] and
     * [StoreFeedCatalog].
     */
    val products = listOf(
        Product(
            id = "p_heels",
            brandId = "charcoal",
            name = "Heels",
            pricePkr = 9000,
            rating = 4.6f,
            reviewCount = 890,
            colors = listOf(
                ProductColor("Cream", 0xFFE8DCC8),
                ProductColor("Black", 0xFF2A2A2A)
            ),
            sizes = listOf("36", "37", "38", "39", "40"),
            imageRes = R.drawable.img_product_heels,
            category = Category.Footwear
        ),
        Product(
            id = "p_earrings",
            brandId = "elan",
            name = "Statement Earrings",
            pricePkr = 4200,
            rating = 4.9f,
            reviewCount = 540,
            colors = listOf(ProductColor("Gold", 0xFFC9A227)),
            sizes = listOf("One Size"),
            imageRes = R.drawable.img_product_earrings,
            category = Category.Jewellery
        ),
        Product(
            id = "p_trouser",
            brandId = "mango",
            name = "Wide-Leg Trouser",
            pricePkr = 5800,
            rating = 4.4f,
            reviewCount = 760,
            colors = listOf(
                ProductColor("Ivory", 0xFFF5F0E8),
                ProductColor("Black", 0xFF2A2A2A)
            ),
            sizes = listOf("XS", "S", "M", "L"),
            imageRes = R.drawable.img_product_trouser,
            category = Category.Women
        ),
        Product(
            id = "p_top",
            brandId = "zara",
            name = "Maroon Blouse",
            pricePkr = 4900,
            rating = 4.3f,
            reviewCount = 410,
            colors = listOf(ProductColor("Maroon", 0xFF5C3A42)),
            sizes = listOf("XS", "S", "M", "L"),
            imageRes = R.drawable.img_product_top,
            category = Category.Women
        )
    ) + BrandCatalog.products + StoreFeedCatalog.products

    val outfits = listOf(
        Outfit(
            id = "o_graceful",
            title = "Graceful Glam",
            tags = listOf("Traditional", "Modern", "You"),
            heroImageRes = R.drawable.img_look_graceful,
            productIds = listOf("p_shirt", "p_dupatta", "p_heels"),
            brandChips = listOf("SANA SAFINAZ", "STEPPING STONES", "CHARCOAL", "KHAADI"),
            vibe = "desi"
        ),
        Outfit(
            id = "o_effortless",
            title = "Effortless Chic",
            tags = listOf("Minimal", "Trendy", "Timeless"),
            heroImageRes = R.drawable.img_look_effortless,
            productIds = listOf("p_top", "p_trouser", "p_heels"),
            brandChips = listOf("KHAADI", "MANGO", "CHARCOAL", "ZARA"),
            vibe = "minimal"
        ),
        Outfit(
            id = "o_dinner",
            title = "Chic Dinner Look",
            tags = listOf("Elegant", "Evening", "Jewellery"),
            heroImageRes = R.drawable.img_look_dinner,
            productIds = listOf("p_shirt", "p_earrings", "p_bag", "p_heels"),
            brandChips = listOf("KHAADI", "ÉLAN", "MARIA.B.", "CHARCOAL"),
            vibe = "dinner"
        ),
        Outfit(
            id = "o_wedding",
            title = "Wedding Guest",
            tags = listOf("Festive", "Desi", "Glam"),
            heroImageRes = R.drawable.img_look_wedding,
            productIds = listOf("p_shirt", "p_dupatta", "p_earrings", "p_heels"),
            brandChips = listOf("SANA SAFINAZ", "KHAADI", "ÉLAN"),
            vibe = "wedding"
        ),
        Outfit(
            id = "o_casual",
            title = "Casual Day Out",
            tags = listOf("Relaxed", "Street", "Easy"),
            heroImageRes = R.drawable.img_look_casual,
            productIds = listOf("p_top", "p_trouser", "p_bag"),
            brandChips = listOf("ZARA", "MANGO", "MARIA.B."),
            vibe = "casual",
            isCreation = true
        )
    )

    val vibeChips = listOf(
        "Chic dinner look",
        "Casual day out",
        "Wedding guest",
        "Minimal & elegant",
        "Trendy street style",
        "Something fun!"
    )

    fun brandName(brandId: String): String =
        brands.firstOrNull { it.id == brandId }?.name ?: brandId.uppercase()

    fun formatPkr(amount: Int): String =
        "Rs. %,d".format(amount)
}
