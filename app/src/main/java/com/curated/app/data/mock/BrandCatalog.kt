package com.curated.app.data.mock

import androidx.annotation.DrawableRes
import com.curated.app.R
import com.curated.app.data.model.Category
import com.curated.app.data.model.Product
import com.curated.app.data.model.ProductColor

/**
 * Pieces taken from the brands' own stores. The photo, name and price are the store's
 * (the price is what the store charged when it was read, sale prices included).
 *
 * The stores' sizes, colour options and ratings were not collected: sizes and the single
 * colour here are stand-ins, and rating and reviewCount stay at zero so nothing is shown.
 *
 * [Category.More] marks pieces that fit none of the product-type filters (bags,
 * menswear, kidswear); they still show under "All" and on their brand's page.
 */
internal object BrandCatalog {

    private val ReadyToWear = listOf("XS", "S", "M", "L", "XL")
    private val Menswear = listOf("S", "M", "L", "XL", "XXL")
    private val Waist = listOf("30", "32", "34", "36", "38")
    private val Kidswear = listOf("4-5Y", "6-7Y", "8-9Y", "10-11Y")
    private val OneSize = listOf("One Size")
    private val MadeToOrder = listOf("Made to Order")

    private fun piece(
        id: String,
        brandId: String,
        name: String,
        pricePkr: Int,
        @DrawableRes imageRes: Int,
        category: Category,
        colour: String,
        hex: Long,
        sizes: List<String>
    ) = Product(
        id = id,
        brandId = brandId,
        name = name,
        pricePkr = pricePkr,
        rating = 0f,
        reviewCount = 0,
        colors = listOf(ProductColor(colour, hex)),
        sizes = sizes,
        imageRes = imageRes,
        category = category
    )

    // p_shirt, p_dupatta and p_bag keep the ids the looks and the wishlist already use.
    private val khaadi = listOf(
        piece("p_shirt", "khaadi", "Black V-Neck Shirt", 6000, R.drawable.img_khaadi_01, Category.Women, "Black", 0xFF1F1F1F, ReadyToWear),
        piece("p_khaadi_02", "khaadi", "Velvet Tailored 2-Piece", 18000, R.drawable.img_khaadi_02, Category.Women, "Black", 0xFF1F1F1F, ReadyToWear),
        piece("p_khaadi_03", "khaadi", "Black Poplin Shirt", 2250, R.drawable.img_khaadi_03, Category.Women, "Black", 0xFF1F1F1F, ReadyToWear),
        piece("p_khaadi_04", "khaadi", "Floral Band-Collar Shirt", 2250, R.drawable.img_khaadi_04, Category.Women, "Off White", 0xFFEFEADF, ReadyToWear),
        piece("p_khaadi_05", "khaadi", "Longline Silk Kurta", 9500, R.drawable.img_khaadi_05, Category.Women, "Pink", 0xFFE0489B, ReadyToWear),
        piece("p_khaadi_06", "khaadi", "Green Longline Kurta", 9000, R.drawable.img_khaadi_06, Category.Women, "Green", 0xFF1F4A3A, ReadyToWear),
        piece("p_khaadi_07", "khaadi", "Silk Tailored 3-Piece", 35000, R.drawable.img_khaadi_07, Category.Women, "Grey", 0xFF7C8794, ReadyToWear),
        piece("p_khaadi_08", "khaadi", "Shirt", 1050, R.drawable.img_khaadi_08, Category.Women, "Beige", 0xFFE9D9BF, ReadyToWear),
        piece("p_khaadi_09", "khaadi", "Pants", 1800, R.drawable.img_khaadi_09, Category.Women, "Maroon", 0xFF6E1F3A, ReadyToWear),
        piece("p_khaadi_10", "khaadi", "Tailored 2-Piece Set", 20000, R.drawable.img_khaadi_10, Category.Women, "Black", 0xFF1F1F1F, ReadyToWear),
        piece("p_khaadi_11", "khaadi", "Pants", 1500, R.drawable.img_khaadi_11, Category.Women, "Yellow", 0xFFE6DCA0, ReadyToWear),
        piece("p_khaadi_12", "khaadi", "Pants", 1200, R.drawable.img_khaadi_12, Category.Women, "Mint", 0xFFA9D8C4, ReadyToWear)
    )

    private val sana = listOf(
        piece("p_dupatta", "sana", "Stitched Embroidered Monark Dupatta", 3299, R.drawable.img_sana_02, Category.Women, "Off White", 0xFFF1E9DA, OneSize),
        piece("p_sana_01", "sana", "Stitched Embroidered Slub Dupatta", 2999, R.drawable.img_sana_01, Category.Women, "White", 0xFFF4F1EA, OneSize),
        piece("p_sana_03", "sana", "Stitched Embroidered Slub Dupatta", 3119, R.drawable.img_sana_03, Category.Women, "Yellow", 0xFFE3C44F, OneSize),
        piece("p_sana_04", "sana", "Stitched Embroidered Lawn Dupatta", 5999, R.drawable.img_sana_04, Category.Women, "Maroon", 0xFF8E1F2A, OneSize),
        piece("p_sana_05", "sana", "Stitched Printed Monark Dupatta", 3199, R.drawable.img_sana_05, Category.Women, "Navy", 0xFF2F3A5C, OneSize),
        piece("p_sana_06", "sana", "Stitched Embroidered Lawn Dupatta", 5199, R.drawable.img_sana_06, Category.Women, "Black", 0xFF2A2A2A, OneSize),
        piece("p_sana_07", "sana", "Stitched Printed Linen Dupatta", 2850, R.drawable.img_sana_07, Category.Women, "Multi", 0xFFD9B79A, OneSize),
        piece("p_sana_08", "sana", "Stitched Printed Linen Dupatta", 2999, R.drawable.img_sana_08, Category.Women, "Plum", 0xFF4B3A44, OneSize),
        piece("p_sana_09", "sana", "Stitched Embroidered Lawn Suit", 21999, R.drawable.img_sana_09, Category.Women, "Yellow", 0xFFE6CF55, ReadyToWear),
        piece("p_sana_10", "sana", "D-491", 730000, R.drawable.img_sana_10, Category.Women, "Rust", 0xFFC2681F, MadeToOrder),
        piece("p_sana_11", "sana", "Stitched Embroidered Cotton Khaddar Shirt + Shalwar", 9499, R.drawable.img_sana_11, Category.Women, "Khaki", 0xFFB89B72, ReadyToWear),
        piece("p_sana_12", "sana", "D-510", 650000, R.drawable.img_sana_12, Category.Women, "Gold", 0xFFB89A4A, MadeToOrder),
        piece("p_sana_13", "sana", "Stitched Printed Raw Silk Kaftan", 9099, R.drawable.img_sana_13, Category.Women, "Orange", 0xFFE0541F, ReadyToWear),
        piece("p_sana_14", "sana", "D-501", 530000, R.drawable.img_sana_14, Category.Women, "Beige", 0xFFC2A878, MadeToOrder),
        piece("p_sana_15", "sana", "Stitched Embroidered Jacquard Shirt + Shalwar", 14799, R.drawable.img_sana_15, Category.Women, "Aqua", 0xFF7CCDCB, ReadyToWear),
        piece("p_sana_16", "sana", "D-500", 450000, R.drawable.img_sana_16, Category.Women, "Ivory", 0xFFEADFC8, MadeToOrder)
    )

    private val mariab = listOf(
        piece("p_bag", "mariab", "Shimmery Boxy Clutch", 8990, R.drawable.img_mariab_03, Category.More, "Brown", 0xFF5A3A2C, OneSize),
        piece("p_mariab_01", "mariab", "Ruched Pearl Clutch", 8990, R.drawable.img_mariab_01, Category.More, "Black", 0xFF1F1F1F, OneSize),
        piece("p_mariab_02", "mariab", "Clutch MBG-W24-10", 18990, R.drawable.img_mariab_02, Category.More, "Gold", 0xFFC9A227, OneSize),
        piece("p_mariab_04", "mariab", "Clutch MBG-W24-09", 11990, R.drawable.img_mariab_04, Category.More, "Gold", 0xFFC9A227, OneSize),
        piece("p_mariab_05", "mariab", "Rhinestone Evening Clutch", 15291, R.drawable.img_mariab_05, Category.More, "Gold", 0xFFC9A227, OneSize),
        piece("p_mariab_06", "mariab", "Vintage Clutch MBG-S25-02", 5841, R.drawable.img_mariab_06, Category.More, "Green", 0xFF1F5A3A, OneSize),
        piece("p_mariab_07", "mariab", "Evening Shell Clutch", 7191, R.drawable.img_mariab_07, Category.More, "Gold", 0xFFC9A227, OneSize),
        piece("p_mariab_08", "mariab", "Clutch MBG-W24-06", 18990, R.drawable.img_mariab_08, Category.More, "Pink", 0xFFC2376B, OneSize),
        piece("p_mariab_09", "mariab", "Emel Earrings", 10990, R.drawable.img_mariab_09, Category.Jewellery, "Pink", 0xFFE7A7B5, OneSize),
        piece("p_mariab_10", "mariab", "3 Piece Embroidered Velvet Suit", 52990, R.drawable.img_mariab_10, Category.Women, "Blue", 0xFF1F2A4A, ReadyToWear),
        piece("p_mariab_11", "mariab", "3 Piece Printed Raw Silk Suit", 21990, R.drawable.img_mariab_11, Category.Women, "Maroon", 0xFF5A1F2A, ReadyToWear),
        piece("p_mariab_12", "mariab", "3 Piece Embroidered Velvet Suit (Kids)", 15990, R.drawable.img_mariab_12, Category.More, "Blue", 0xFF1F4A5A, Kidswear),
        piece("p_mariab_13", "mariab", "3 Piece Unstitched Embroidered Net Suit", 25990, R.drawable.img_mariab_13, Category.Women, "Grey", 0xFFB9AEB0, OneSize),
        piece("p_mariab_14", "mariab", "Embroidered Chiffon Fabric (3Pcs)", 3495, R.drawable.img_mariab_14, Category.Women, "Navy Blue", 0xFF1F2A4A, OneSize),
        piece("p_mariab_15", "mariab", "Maria B. Couture Red MC-051", 74990, R.drawable.img_mariab_15, Category.Women, "Red", 0xFFB01F2A, MadeToOrder),
        piece("p_mariab_16", "mariab", "2 Piece Embroidered Blended Suit (Men)", 12990, R.drawable.img_mariab_16, Category.More, "Purple Dove", 0xFFA89AA0, Menswear)
    )

    private val elan = listOf(
        piece("p_elan_01", "elan", "Miel & Neige", 17950, R.drawable.img_elan_01, Category.Women, "Olive", 0xFF5A5F3A, ReadyToWear),
        piece("p_elan_02", "elan", "Chartreuse d'Or", 96950, R.drawable.img_elan_02, Category.Women, "Chartreuse", 0xFF7A8A3A, ReadyToWear),
        piece("p_elan_03", "elan", "Ambre d'Atlas", 16450, R.drawable.img_elan_03, Category.Women, "Amber", 0xFFC9A27A, ReadyToWear),
        piece("p_elan_04", "elan", "Pétrole Nocturne", 92950, R.drawable.img_elan_04, Category.Women, "Teal", 0xFF2F5A6A, ReadyToWear),
        piece("p_elan_05", "elan", "Lalla", 19450, R.drawable.img_elan_05, Category.Women, "Black", 0xFF1F1F1F, ReadyToWear),
        piece("p_elan_06", "elan", "Célestine", 85950, R.drawable.img_elan_06, Category.Women, "Sky Blue", 0xFFA9C4DA, ReadyToWear),
        piece("p_elan_07", "elan", "Ksar Blanc", 17950, R.drawable.img_elan_07, Category.Women, "White", 0xFFF4F1EA, ReadyToWear),
        piece("p_elan_08", "elan", "Pêche Nacrée", 89950, R.drawable.img_elan_08, Category.Women, "Peach", 0xFFE8B4A0, ReadyToWear),
        piece("p_elan_09", "elan", "Lune du Désert", 16950, R.drawable.img_elan_09, Category.Women, "Blue", 0xFF5F7A9A, ReadyToWear),
        piece("p_elan_10", "elan", "Miel Doré", 92950, R.drawable.img_elan_10, Category.Women, "Yellow", 0xFFE0A82A, ReadyToWear)
    )

    // Charcoal is a menswear label; none of its pieces belong under "Women".
    private val charcoal = listOf(
        piece("p_charcoal_01", "charcoal", "Button Down Smart Fit Corduroy Casual Shirt", 3916, R.drawable.img_charcoal_01, Category.More, "Olive", 0xFF4A4F3A, Menswear),
        piece("p_charcoal_02", "charcoal", "Short Sleeve Henley Shirt", 2148, R.drawable.img_charcoal_02, Category.More, "Black", 0xFF1F1F1F, Menswear),
        piece("p_charcoal_03", "charcoal", "Relaxed Fit Corduroy Cargo Trouser", 6396, R.drawable.img_charcoal_03, Category.More, "Navy", 0xFF1F2A44, Waist),
        piece("p_charcoal_04", "charcoal", "Smart Fit Full Sleeve Zipper Polo Shirt", 7196, R.drawable.img_charcoal_04, Category.More, "Green", 0xFF6F8A7A, Menswear),
        piece("p_charcoal_05", "charcoal", "Smart Fit Full Sleeve Reversible Jacket", 11996, R.drawable.img_charcoal_05, Category.More, "Bluish Grey", 0xFFA9AEB5, Menswear),
        piece("p_charcoal_06", "charcoal", "Smart Fit Long Sleeve Cable Knit Sweater", 7196, R.drawable.img_charcoal_06, Category.More, "Beige", 0xFFE0D4C4, Menswear),
        piece("p_charcoal_07", "charcoal", "Blended Kameez Shalwar - Signature Collection", 9996, R.drawable.img_charcoal_07, Category.More, "White", 0xFFF4F4F0, Menswear),
        piece("p_charcoal_08", "charcoal", "Notch Lapel Men's Two-Piece Suit", 26396, R.drawable.img_charcoal_08, Category.More, "Beige", 0xFFD9CDB8, Menswear),
        piece("p_charcoal_09", "charcoal", "Smart Fit Long Sleeve Self Textured Shirt", 6396, R.drawable.img_charcoal_09, Category.More, "Navy", 0xFF1F2A44, Menswear),
        piece("p_charcoal_10", "charcoal", "Cufflinks", 2396, R.drawable.img_charcoal_10, Category.More, "Silver", 0xFFB9BDC2, OneSize)
    )

    val products: List<Product> = khaadi + sana + mariab + elan + charcoal
}
