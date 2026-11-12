package com.curated.app.data.model

import androidx.annotation.DrawableRes
import com.curated.app.R

enum class Category(val label: String) {
    Women("Women"),
    Footwear("Footwear"),
    Jewellery("Jewellery"),
    Designers("Designers"),
    Local("Local"),
    More("More")
}

data class Brand(
    val id: String,
    val name: String,
    val shortLabel: String = name,
    val isDesigner: Boolean = false,
    val isLocal: Boolean = true,
    /** The brand's own logo; brands without one fall back to a text wordmark. */
    @DrawableRes val logoRes: Int? = null
)

data class Product(
    val id: String,
    val brandId: String,
    val name: String,
    val pricePkr: Int,
    /** Zero [reviewCount] means the piece has no ratings yet and none are shown. */
    val rating: Float,
    val reviewCount: Int,
    val colors: List<ProductColor>,
    val sizes: List<String>,
    @DrawableRes val imageRes: Int,
    val category: Category,
    val description: String = ""
)

data class ProductColor(
    val name: String,
    val hex: Long
)

data class Outfit(
    val id: String,
    val title: String,
    val tags: List<String>,
    @DrawableRes val heroImageRes: Int,
    val productIds: List<String>,
    val brandChips: List<String>,
    val isCreation: Boolean = false,
    val vibe: String = "",
    /** A look image the AI Stylist made, stored in the app's files; null shows [heroImageRes]. */
    val heroImagePath: String? = null,
    /** The stylist's short reason for the look; empty for the catalogue's own looks. */
    val note: String = "",
    /** True for looks the AI Stylist put together for this user. */
    val isAiLook: Boolean = false
)

data class CartItem(
    val productId: String,
    val size: String,
    val color: String,
    val qty: Int
)

data class UserProfile(
    val name: String = "Zarah Ahmed",
    @DrawableRes val avatarRes: Int = R.drawable.ic_avatar_placeholder,
    val email: String = "",
    val phone: String = "",
    /** A photo the user picked, copied into the app's files; null shows [avatarRes]. */
    val avatarPath: String? = null
) {
    val firstName: String get() = name.trim().substringBefore(' ')
}

enum class MainTab(val route: String, val label: String) {
    Home("home", "Home"),
    Ai("ai", "AI Stylist"),
    Outfits("outfits", "Outfits"),
    Wishlist("wishlist", "Wishlist"),
    Profile("profile", "Profile")
}
