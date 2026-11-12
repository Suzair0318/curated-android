package com.curated.app.data.mock

import androidx.annotation.DrawableRes
import com.curated.app.R
import com.curated.app.data.model.Brand
import com.curated.app.data.model.Category
import com.curated.app.data.model.Product
import com.curated.app.data.model.ProductColor

/**
 * 84 more Pakistani brands, 6 pieces each, read from the brands' own online stores.
 *
 * Photo, name, price and sizes are the store's: the price is what a visitor from Pakistan
 * was shown when the store was read (sale prices included), and names only had stock codes
 * trimmed off. Misha Lakhani and Ali Xeeshan sell in US dollars only, so their prices are
 * converted at 280.84 rupees to the dollar.
 *
 * Each piece carries one colour. Where the store names it, that name is used with a
 * representative tone; otherwise both name and tone are estimated from the photo, so
 * treat them as approximate. Ratings were not collected and stay at zero so none is shown.
 *
 * [Category.More] marks pieces that fit none of the product-type filters (bags, menswear,
 * kidswear); they still show under "All" and on their brand's page.
 *
 * Generated file: one function per brand keeps every method well under the JVM size limit.
 */
internal object StoreFeedCatalog {

    val brands: List<Brand> = listOf(
        Brand("gulahmed", "GUL AHMED", "GUL AHMED", logoRes = R.drawable.logo_brand_gulahmed),
        Brand("alkaram", "ALKARAM STUDIO", "ALKARAM STUDIO", logoRes = R.drawable.logo_brand_alkaram),
        Brand("nishat", "NISHAT LINEN", "NISHAT LINEN", logoRes = R.drawable.logo_brand_nishat),
        Brand("limelight", "LIMELIGHT", "LIMELIGHT", logoRes = R.drawable.logo_brand_limelight),
        Brand("bonanza", "BONANZA SATRANGI", "BONANZA SATRANGI", logoRes = R.drawable.logo_brand_bonanza),
        Brand("generation", "GENERATION", "GENERATION", logoRes = R.drawable.logo_brand_generation),
        Brand("beechtree", "BEECHTREE", "BEECHTREE", logoRes = R.drawable.logo_brand_beechtree),
        Brand("zellbury", "ZELLBURY", "ZELLBURY", logoRes = R.drawable.logo_brand_zellbury),
        Brand("jdot", "J.", "J.", logoRes = R.drawable.logo_brand_jdot),
        Brand("crossstitch", "CROSS STITCH", "CROSS STITCH", logoRes = R.drawable.logo_brand_crossstitch),
        Brand("chinyere", "CHINYERE", "CHINYERE", logoRes = R.drawable.logo_brand_chinyere),
        Brand("sokamal", "SO KAMAL", "SO KAMAL", logoRes = R.drawable.logo_brand_sokamal),
        Brand("almirah", "ALMIRAH", "ALMIRAH", logoRes = R.drawable.logo_brand_almirah),
        Brand("lulusar", "LULUSAR", "LULUSAR", logoRes = R.drawable.logo_brand_lulusar),
        Brand("saya", "SAYA", "SAYA", logoRes = R.drawable.logo_brand_saya),
        Brand("edenrobe", "EDENROBE", "EDENROBE", logoRes = R.drawable.logo_brand_edenrobe),
        Brand("zeen", "ZEEN", "ZEEN", logoRes = R.drawable.logo_brand_zeen),
        Brand("shaposh", "SHA POSH", "SHA POSH", logoRes = R.drawable.logo_brand_shaposh),
        Brand("gulaal", "GULAAL", "GULAAL", logoRes = R.drawable.logo_brand_gulaal),
        Brand("batik", "BATIK", "BATIK", logoRes = R.drawable.logo_brand_batik),
        Brand("mausummery", "MAUSUMMERY", "MAUSUMMERY", logoRes = R.drawable.logo_brand_mausummery),
        Brand("ego", "EGO", "EGO", logoRes = R.drawable.logo_brand_ego),
        Brand("krosskulture", "KROSS KULTURE", "KROSS KULTURE", logoRes = R.drawable.logo_brand_krosskulture),
        Brand("outfitters", "OUTFITTERS", "OUTFITTERS", logoRes = R.drawable.logo_brand_outfitters),
        Brand("breakout", "BREAKOUT", "BREAKOUT", logoRes = R.drawable.logo_brand_breakout),
        Brand("diners", "DINERS", "DINERS", logoRes = R.drawable.logo_brand_diners),
        Brand("engine", "ENGINE", "ENGINE", logoRes = R.drawable.logo_brand_engine),
        Brand("furor", "FUROR", "FUROR", logoRes = R.drawable.logo_brand_furor),
        Brand("one", "ONE", "ONE", logoRes = R.drawable.logo_brand_one),
        Brand("baroque", "BAROQUE", "BAROQUE", isDesigner = true, logoRes = R.drawable.logo_brand_baroque),
        Brand("zarashahjahan", "ZARA SHAHJAHAN", "ZARA SHAHJAHAN", isDesigner = true, logoRes = R.drawable.logo_brand_zarashahjahan),
        Brand("saniamaskatiya", "SANIA MASKATIYA", "SANIA MASKATIYA", isDesigner = true, logoRes = R.drawable.logo_brand_saniamaskatiya),
        Brand("asimjofa", "ASIM JOFA", "ASIM JOFA", isDesigner = true, logoRes = R.drawable.logo_brand_asimjofa),
        Brand("hsy", "HSY", "HSY", isDesigner = true, logoRes = R.drawable.logo_brand_hsy),
        Brand("aghanoor", "AGHA NOOR", "AGHA NOOR", isDesigner = true, logoRes = R.drawable.logo_brand_aghanoor),
        Brand("afrozeh", "AFROZEH", "AFROZEH", isDesigner = true, logoRes = R.drawable.logo_brand_afrozeh),
        Brand("hussainrehar", "HUSSAIN REHAR", "HUSSAIN REHAR", isDesigner = true, logoRes = R.drawable.logo_brand_hussainrehar),
        Brand("republic", "REPUBLIC WOMENSWEAR", "REPUBLIC WOMENSWEAR", isDesigner = true, logoRes = R.drawable.logo_brand_republic),
        Brand("zainabchottani", "ZAINAB CHOTTANI", "ZAINAB CHOTTANI", isDesigner = true, logoRes = R.drawable.logo_brand_zainabchottani),
        Brand("sairashakira", "SAIRA SHAKIRA", "SAIRA SHAKIRA", isDesigner = true, logoRes = R.drawable.logo_brand_sairashakira),
        Brand("alixeeshan", "ALI XEESHAN", "ALI XEESHAN", isDesigner = true, logoRes = R.drawable.logo_brand_alixeeshan),
        Brand("zaha", "ZAHA", "ZAHA", isDesigner = true, logoRes = R.drawable.logo_brand_zaha),
        Brand("iznik", "IZNIK", "IZNIK", isDesigner = true, logoRes = R.drawable.logo_brand_iznik),
        Brand("jazmin", "JAZMIN", "JAZMIN", isDesigner = true, logoRes = R.drawable.logo_brand_jazmin),
        Brand("qalamkar", "QALAMKAR", "QALAMKAR", isDesigner = true, logoRes = R.drawable.logo_brand_qalamkar),
        Brand("maryumnmaria", "MARYUM N MARIA", "MARYUM N MARIA", isDesigner = true, logoRes = R.drawable.logo_brand_maryumnmaria),
        Brand("motifz", "MOTIFZ", "MOTIFZ", isDesigner = true, logoRes = R.drawable.logo_brand_motifz),
        Brand("suffuse", "SUFFUSE", "SUFFUSE", isDesigner = true, logoRes = R.drawable.logo_brand_suffuse),
        Brand("mnr", "MOHSIN NAVEED RANJHA", "MOHSIN NAVEED RANJHA", isDesigner = true, logoRes = R.drawable.logo_brand_mnr),
        Brand("mishalakhani", "MISHA LAKHANI", "MISHA LAKHANI", isDesigner = true, logoRes = R.drawable.logo_brand_mishalakhani),
        Brand("nidaazwer", "NIDA AZWER", "NIDA AZWER", isDesigner = true, logoRes = R.drawable.logo_brand_nidaazwer),
        Brand("kanwalmalik", "KANWAL MALIK", "KANWAL MALIK", isDesigner = true, logoRes = R.drawable.logo_brand_kanwalmalik),
        Brand("tenadurrani", "TENA DURRANI", "TENA DURRANI", isDesigner = true, logoRes = R.drawable.logo_brand_tenadurrani),
        Brand("saadiaasad", "NOOR BY SAADIA ASAD", "NOOR BY SAADIA ASAD", isDesigner = true, logoRes = R.drawable.logo_brand_saadiaasad),
        Brand("akbaraslam", "AKBAR ASLAM", "AKBAR ASLAM", isDesigner = true, logoRes = R.drawable.logo_brand_akbaraslam),
        Brand("rangrasiya", "RANG RASIYA", "RANG RASIYA", isDesigner = true, logoRes = R.drawable.logo_brand_rangrasiya),
        Brand("uniworth", "UNIWORTH", "UNIWORTH", logoRes = R.drawable.logo_brand_uniworth),
        Brand("cambridge", "CAMBRIDGE", "CAMBRIDGE", logoRes = R.drawable.logo_brand_cambridge),
        Brand("royaltag", "ROYAL TAG", "ROYAL TAG", logoRes = R.drawable.logo_brand_royaltag),
        Brand("monark", "MONARK", "MONARK", logoRes = R.drawable.logo_brand_monark),
        Brand("lama", "LAMA", "LAMA", logoRes = R.drawable.logo_brand_lama),
        Brand("amiradnan", "AMIR ADNAN", "AMIR ADNAN", logoRes = R.drawable.logo_brand_amiradnan),
        Brand("ismailfarid", "ISMAIL FARID", "ISMAIL FARID", logoRes = R.drawable.logo_brand_ismailfarid),
        Brand("humayunalamgir", "HUMAYUN ALAMGIR", "HUMAYUN ALAMGIR", logoRes = R.drawable.logo_brand_humayunalamgir),
        Brand("stylo", "STYLO", "STYLO", logoRes = R.drawable.logo_brand_stylo),
        Brand("bata", "BATA", "BATA", isLocal = false, logoRes = R.drawable.logo_brand_bata),
        Brand("servis", "SERVIS", "SERVIS", logoRes = R.drawable.logo_brand_servis),
        Brand("borjan", "BORJAN", "BORJAN", logoRes = R.drawable.logo_brand_borjan),
        Brand("insignia", "INSIGNIA", "INSIGNIA", logoRes = R.drawable.logo_brand_insignia),
        Brand("hushpuppies", "HUSH PUPPIES", "HUSH PUPPIES", isLocal = false, logoRes = R.drawable.logo_brand_hushpuppies),
        Brand("ecs", "ECS", "ECS", logoRes = R.drawable.logo_brand_ecs),
        Brand("ndure", "NDURE", "NDURE", logoRes = R.drawable.logo_brand_ndure),
        Brand("unze", "UNZE LONDON", "UNZE LONDON", logoRes = R.drawable.logo_brand_unze),
        Brand("clive", "CLIVE", "CLIVE", logoRes = R.drawable.logo_brand_clive),
        Brand("urbansole", "URBANSOLE", "URBANSOLE", logoRes = R.drawable.logo_brand_urbansole),
        Brand("walkeaze", "WALKEAZE", "WALKEAZE", logoRes = R.drawable.logo_brand_walkeaze),
        Brand("almas", "ALMAS", "ALMAS", logoRes = R.drawable.logo_brand_almas),
        Brand("sputnik", "SPUTNIK", "SPUTNIK", logoRes = R.drawable.logo_brand_sputnik),
        Brand("jafferjees", "JAFFERJEES", "JAFFERJEES", logoRes = R.drawable.logo_brand_jafferjees),
        Brand("hub", "HUB", "HUB", logoRes = R.drawable.logo_brand_hub),
        Brand("astore", "ASTORE", "ASTORE", logoRes = R.drawable.logo_brand_astore),
        Brand("esfir", "ESFIR JEWELS", "ESFIR JEWELS", logoRes = R.drawable.logo_brand_esfir),
        Brand("minnieminors", "MINNIE MINORS", "MINNIE MINORS", logoRes = R.drawable.logo_brand_minnieminors),
        Brand("cocobee", "COCOBEE", "COCOBEE", logoRes = R.drawable.logo_brand_cocobee)
    )

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

    private fun gulahmed() = listOf(
        piece("p_gulahmed_01", "gulahmed", "Khaddar Slub Embroidered Waist Coat", 6990, R.drawable.img_gulahmed_01, Category.Women, "Plum", 0xFF941E56, listOf("X-Small", "Small", "Medium", "Large", "X-Large")),
        piece("p_gulahmed_02", "gulahmed", "Relaxed Fit Co-Ord Set", 7990, R.drawable.img_gulahmed_02, Category.Women, "Chocolate", 0xFF492229, listOf("X-Small", "Small", "Medium", "Large")),
        piece("p_gulahmed_03", "gulahmed", "3 Piece Embroidered Pashmina Suit", 13490, R.drawable.img_gulahmed_03, Category.Women, "Chocolate", 0xFF411116, listOf("Unstitched")),
        piece("p_gulahmed_04", "gulahmed", "Crinkle Georgette Embroidered Abaya And Dyed Trouser", 11990, R.drawable.img_gulahmed_04, Category.Women, "Black", 0xFF0A1415, listOf("Small To Medium", "Medium To Large")),
        piece("p_gulahmed_05", "gulahmed", "Regular Fit Sweater", 3743, R.drawable.img_gulahmed_05, Category.Women, "Black", 0xFF0C0C0C, listOf("Small", "Medium", "Large", "X-Large")),
        piece("p_gulahmed_06", "gulahmed", "Beige Wallet", 3990, R.drawable.img_gulahmed_06, Category.More, "Beige", 0xFFD6C4A6, listOf("One Size"))
    )

    private fun alkaram() = listOf(
        piece("p_alkaram_01", "alkaram", "Cotton Silk Shirt, Trouser & Dupatta", 6354, R.drawable.img_alkaram_01, Category.Women, "Pink", 0xFFEC96B4, listOf("6", "8", "10", "12", "14", "16")),
        piece("p_alkaram_02", "alkaram", "Dyed Kurti", 1794, R.drawable.img_alkaram_02, Category.Women, "Purple", 0xFF643282, listOf("6", "8", "10", "12", "14", "16")),
        piece("p_alkaram_03", "alkaram", "Cambric Trouser", 1183, R.drawable.img_alkaram_03, Category.Women, "Beige", 0xFFD6C4A6, listOf("6", "8", "10", "12", "14", "16")),
        piece("p_alkaram_04", "alkaram", "Cambric Shalwar", 1743, R.drawable.img_alkaram_04, Category.Women, "Black", 0xFF1F1F1F, listOf("6", "8", "10", "12", "14", "16")),
        piece("p_alkaram_05", "alkaram", "Lawn Shirt & Trouser", 1953, R.drawable.img_alkaram_05, Category.Women, "Beige", 0xFFD6C4A6, listOf("Unstitched")),
        piece("p_alkaram_06", "alkaram", "Blended Kameez & Shalwar", 8490, R.drawable.img_alkaram_06, Category.More, "Navy Blue", 0xFF1F2A4D, listOf("Unstitched"))
    )

    private fun nishat() = listOf(
        piece("p_nishat_01", "nishat", "2 Piece - Embroidered Suit", 6990, R.drawable.img_nishat_01, Category.Women, "Maroon", 0xFF812534, listOf("Unstitched")),
        piece("p_nishat_02", "nishat", "Black Long Coat", 17990, R.drawable.img_nishat_02, Category.Women, "Black", 0xFF1F1F1F, listOf("S", "M", "L", "XL")),
        piece("p_nishat_03", "nishat", "3 Piece - Embroidered Suit", 34990, R.drawable.img_nishat_03, Category.Women, "Maroon", 0xFF450B0A, listOf("XS", "S", "M", "L", "XL")),
        piece("p_nishat_04", "nishat", "3 Piece - Printed Embroidered Suit", 7990, R.drawable.img_nishat_04, Category.Women, "Charcoal", 0xFF3D3D52, listOf("Unstitched")),
        piece("p_nishat_05", "nishat", "Beige Long Coat", 17990, R.drawable.img_nishat_05, Category.Women, "Beige", 0xFFD6C4A6, listOf("S", "M", "L", "XL")),
        piece("p_nishat_06", "nishat", "Embroidered Waistcoat", 11990, R.drawable.img_nishat_06, Category.More, "Black", 0xFF1B121F, listOf("XS", "S", "M", "L", "XL"))
    )

    private fun limelight() = listOf(
        piece("p_limelight_01", "limelight", "Grip Shirt (Pret)", 2099, R.drawable.img_limelight_01, Category.Women, "Silver", 0xFFC5C0BD, listOf("Extra Small", "Small", "Medium", "Large")),
        piece("p_limelight_02", "limelight", "Satin Scarf", 979, R.drawable.img_limelight_02, Category.Women, "Rust", 0xFF9D4349, listOf("One Size")),
        piece("p_limelight_03", "limelight", "Slub Khaddar Co-ord Set - Embroidered", 4829, R.drawable.img_limelight_03, Category.Women, "Teal", 0xFF025F53, listOf("Extra Small", "Small", "Medium", "Large")),
        piece("p_limelight_04", "limelight", "Viscose Scarf", 1099, R.drawable.img_limelight_04, Category.Women, "Blue", 0xFF495B73, listOf("One Size")),
        piece("p_limelight_05", "limelight", "Stone Kurti-Embroidered (Pret)", 3149, R.drawable.img_limelight_05, Category.Women, "Tea Pink", 0xFFE1D6D2, listOf("Extra Small", "Small", "Medium", "Large")),
        piece("p_limelight_06", "limelight", "Dyed Cotton Shalwar", 699, R.drawable.img_limelight_06, Category.More, "Ivory", 0xFFF2E8D5, listOf("Small", "Medium", "Large", "Extra Large"))
    )

    private fun bonanza() = listOf(
        piece("p_bonanza_01", "bonanza", "Green Khaddar 3 Piece", 6990, R.drawable.img_bonanza_01, Category.Women, "Green", 0xFF34784A, listOf("8", "10", "12", "14", "16")),
        piece("p_bonanza_02", "bonanza", "Grey Oversized T-Shirt", 1192, R.drawable.img_bonanza_02, Category.Women, "Grey", 0xFF808080, listOf("S", "M", "L", "XL")),
        piece("p_bonanza_03", "bonanza", "Mesh - 3 Pc", 3690, R.drawable.img_bonanza_03, Category.Women, "Black", 0xFF1F1F1F, listOf("Unstitched")),
        piece("p_bonanza_04", "bonanza", "Ornate - 3 Pc", 8990, R.drawable.img_bonanza_04, Category.Women, "Black", 0xFF1F1F1F, listOf("Unstitched")),
        piece("p_bonanza_05", "bonanza", "Brick Wing Collar Cardigan Full Sleeves Sweater", 10990, R.drawable.img_bonanza_05, Category.Women, "Brick", 0xFF9C4A3C, listOf("S", "M")),
        piece("p_bonanza_06", "bonanza", "Bean Turtle Neck Zipper Full Sleeves Sweater", 12590, R.drawable.img_bonanza_06, Category.More, "Bean", 0xFF6D4843, listOf("S", "M", "L", "XL", "XXL"))
    )

    private fun generation() = listOf(
        piece("p_generation_01", "generation", "Gulab 2 PC Set", 5698, R.drawable.img_generation_01, Category.Women, "Jet Black", 0xFF1A1A1A, listOf("8", "10", "12", "14", "16")),
        piece("p_generation_02", "generation", "Floral Jacquard Saree", 20998, R.drawable.img_generation_02, Category.Women, "Pastel Yellow", 0xFFF0D446, listOf("8", "10", "12", "14", "16")),
        piece("p_generation_03", "generation", "Mandala Embroidered Koti Set", 15698, R.drawable.img_generation_03, Category.Women, "Fuchsia Pink", 0xFFD23C8C, listOf("8", "10", "12", "14", "16")),
        piece("p_generation_04", "generation", "Floral Folk Set", 6698, R.drawable.img_generation_04, Category.Women, "Burgundy", 0xFF6A1830, listOf("8", "10", "12", "14", "16")),
        piece("p_generation_05", "generation", "Floral Muse Printed Set", 5498, R.drawable.img_generation_05, Category.Women, "Jet Black", 0xFF1A1A1A, listOf("8", "10", "12", "14", "16")),
        piece("p_generation_06", "generation", "Naqsh Khussa", 4498, R.drawable.img_generation_06, Category.Footwear, "Golden", 0xFFC8A25A, listOf("6", "7", "8", "9", "10", "11"))
    )

    private fun beechtree() = listOf(
        piece("p_beechtree_01", "beechtree", "3 Piece Printed Lawn Suit-Indigo Glaze (Unstitched)", 2495, R.drawable.img_beechtree_01, Category.Women, "Multi", 0xFFB0AFA3, listOf("Unstitched")),
        piece("p_beechtree_02", "beechtree", "2 Piece Printed Lawn Suit (Pret)", 2745, R.drawable.img_beechtree_02, Category.Women, "Rust", 0xFFD04A4D, listOf("8", "10", "12", "14", "16")),
        piece("p_beechtree_03", "beechtree", "Embroidered Raw Silk Shirt (Luxury Pret)", 3890, R.drawable.img_beechtree_03, Category.Women, "Grey Blue", 0xFF7C8794, listOf("8", "10", "12", "14", "16")),
        piece("p_beechtree_04", "beechtree", "Scarf", 1192, R.drawable.img_beechtree_04, Category.Women, "Pink", 0xFFEC96B4, listOf("One Size")),
        piece("p_beechtree_05", "beechtree", "Crew Neck Sweater", 6392, R.drawable.img_beechtree_05, Category.Women, "Brown", 0xFF6E4830, listOf("XS", "S", "M", "L")),
        piece("p_beechtree_06", "beechtree", "2 Piece Printed Suit", 2449, R.drawable.img_beechtree_06, Category.More, "Multi", 0xFFD3C2B9, listOf("1-2Y", "2-3Y", "3-4Y", "4-5Y", "5-6Y"))
    )

    private fun zellbury() = listOf(
        piece("p_zellbury_01", "zellbury", "Embroidered Shirt Shalwar Shawl", 4990, R.drawable.img_zellbury_01, Category.Women, "Black", 0xFF0A0C0F, listOf("Unstitched")),
        piece("p_zellbury_02", "zellbury", "Embroidered Shirt Shalwar Dupatta", 4490, R.drawable.img_zellbury_02, Category.Women, "Charcoal", 0xFF2E4D4A, listOf("Unstitched")),
        piece("p_zellbury_03", "zellbury", "Mock Neck Zipper Sweater", 3990, R.drawable.img_zellbury_03, Category.More, "Black", 0xFF1F1F1F, listOf("S", "M", "L", "XL")),
        piece("p_zellbury_04", "zellbury", "Tote Bag - Black", 2490, R.drawable.img_zellbury_04, Category.More, "Black", 0xFF1F1F1F, listOf("One Size")),
        piece("p_zellbury_05", "zellbury", "Embroidered Shirt Shalwar Shawl - 0357", 4490, R.drawable.img_zellbury_05, Category.Women, "Grey", 0xFF9C6A69, listOf("Unstitched")),
        piece("p_zellbury_06", "zellbury", "Embroidered Shirt Shalwar Dupatta - 0351", 4490, R.drawable.img_zellbury_06, Category.Women, "Mauve", 0xFF9781AA, listOf("Unstitched"))
    )

    private fun jdot() = listOf(
        piece("p_jdot_01", "jdot", "Teal Embroidered Chiffon Unstitched 3Pc", 14990, R.drawable.img_jdot_01, Category.Women, "Teal", 0xFF1F6E73, listOf("Unstitched")),
        piece("p_jdot_02", "jdot", "Green Embroidered Jacquard Unstitched 3Pc", 8490, R.drawable.img_jdot_02, Category.Women, "Green", 0xFF34784A, listOf("Unstitched")),
        piece("p_jdot_03", "jdot", "Light Blue Printed Khaddar Slub Stitched 3Pc", 7192, R.drawable.img_jdot_03, Category.Women, "Light Blue", 0xFFA9C9E8, listOf("XS", "S", "M", "L", "XL", "XXL")),
        piece("p_jdot_04", "jdot", "Beige Lawn Embroidered Stitched 3PC", 7194, R.drawable.img_jdot_04, Category.Women, "Beige", 0xFFD6C4A6, listOf("XS", "S", "M", "L", "XL", "XXL")),
        piece("p_jdot_05", "jdot", "Matt Black Plain Peshawari Sandals", 3192, R.drawable.img_jdot_05, Category.Footwear, "Black", 0xFF1F1F1F, listOf("7", "8", "9", "10", "11", "12", "13", "14")),
        piece("p_jdot_06", "jdot", "Purple Formal Kurta Trousers With Waistcoat", 10392, R.drawable.img_jdot_06, Category.More, "Purple", 0xFF643282, listOf("12 Y", "14 Y", "16 Y", "18 Y"))
    )

    private fun crossstitch() = listOf(
        piece("p_crossstitch_01", "crossstitch", "Ivory Charm - Scarf", 3950, R.drawable.img_crossstitch_01, Category.Women, "Red", 0xFFC02A32, listOf("One Size")),
        piece("p_crossstitch_02", "crossstitch", "Silver Rose 2PC (Shirt & Dupatta)", 11950, R.drawable.img_crossstitch_02, Category.Women, "Pink", 0xFFEC96B4, listOf("XS", "S", "M", "L")),
        piece("p_crossstitch_03", "crossstitch", "Espresso Dream - 3PC Chiffon Embroidered Suit", 15950, R.drawable.img_crossstitch_03, Category.Women, "Brown", 0xFF6E4830, listOf("Unstitched")),
        piece("p_crossstitch_04", "crossstitch", "Garnet Grace 3PC (Shirt, Trouser & Dupatta)", 18950, R.drawable.img_crossstitch_04, Category.Women, "Peach", 0xFFF6BEA0, listOf("XS", "S", "M", "L")),
        piece("p_crossstitch_05", "crossstitch", "Urban Noir 2PC (Shirt & Trouser)", 8450, R.drawable.img_crossstitch_05, Category.Women, "Black", 0xFF1F1F1F, listOf("XS", "S", "M", "L")),
        piece("p_crossstitch_06", "crossstitch", "Eira", 3950, R.drawable.img_crossstitch_06, Category.Footwear, "Beige", 0xFFD6C4A6, listOf("36", "37", "38", "39", "40", "41"))
    )

    private fun chinyere() = listOf(
        piece("p_chinyere_01", "chinyere", "3 Piece Lawn Karandi Embroidered Suit", 15900, R.drawable.img_chinyere_01, Category.Women, "Rose", 0xFFB7705D, listOf("S", "M", "L")),
        piece("p_chinyere_02", "chinyere", "3 Piece Chiffon Printed Suit", 22900, R.drawable.img_chinyere_02, Category.Women, "Peach", 0xFFEAC6AE, listOf("S", "M", "L")),
        piece("p_chinyere_03", "chinyere", "3 Piece Cotton Silk Embroidered Suit", 21500, R.drawable.img_chinyere_03, Category.Women, "Plum", 0xFF673534, listOf("S", "M", "L")),
        piece("p_chinyere_04", "chinyere", "3 Piece Velvet Embroidered Suit", 29900, R.drawable.img_chinyere_04, Category.Women, "Black", 0xFF2A0210, listOf("S", "M", "L")),
        piece("p_chinyere_05", "chinyere", "2 Piece Cotton Karandi Embroidered Suit", 8900, R.drawable.img_chinyere_05, Category.Women, "Navy", 0xFF20322C, listOf("S", "M", "L")),
        piece("p_chinyere_06", "chinyere", "2 Piece Synthetic Organza Embroidered Suit", 16740, R.drawable.img_chinyere_06, Category.Women, "Silver", 0xFFBCC1AB, listOf("S", "M", "L"))
    )

    private fun sokamal() = listOf(
        piece("p_sokamal_01", "sokamal", "3 - PC Karandi Suit (Embroidered Dupatta)", 6900, R.drawable.img_sokamal_01, Category.Women, "Black", 0xFF16120D, listOf("Unstitched")),
        piece("p_sokamal_02", "sokamal", "Midnight Sapphire - Rawsilk Pants", 5000, R.drawable.img_sokamal_02, Category.Women, "Dark Blue", 0xFF1F3566, listOf("S", "M", "L")),
        piece("p_sokamal_03", "sokamal", "3Pc Premium Lawn Suit", 7170, R.drawable.img_sokamal_03, Category.Women, "Mauve", 0xFFB69FA1, listOf("Unstitched")),
        piece("p_sokamal_04", "sokamal", "Embroidered - 3 Pc Linen Suit", 5850, R.drawable.img_sokamal_04, Category.Women, "Chocolate", 0xFF4B181D, listOf("Unstitched")),
        piece("p_sokamal_05", "sokamal", "Midnight Lavender - Linen Trouser", 3000, R.drawable.img_sokamal_05, Category.Women, "Black", 0xFF1F1F1F, listOf("S", "M", "L")),
        piece("p_sokamal_06", "sokamal", "Printed - 3 Pc Karandi Suit", 5500, R.drawable.img_sokamal_06, Category.Women, "Black", 0xFF2D251C, listOf("Unstitched"))
    )

    private fun almirah() = listOf(
        piece("p_almirah_01", "almirah", "Black Cotton Trousers", 3650, R.drawable.img_almirah_01, Category.Women, "Black", 0xFF1F1F1F, listOf("XS", "S", "M", "L", "XL", "XXL")),
        piece("p_almirah_02", "almirah", "Plum Silk Lawn 3Pc Stitched", 8155, R.drawable.img_almirah_02, Category.Women, "Plum", 0xFF682C54, listOf("XS", "S", "M", "L", "XL", "XXL")),
        piece("p_almirah_03", "almirah", "Navy Blue Flamint Zari Formal 3Pc Stitched", 16950, R.drawable.img_almirah_03, Category.Women, "Navy Blue", 0xFF1F2A4D, listOf("XS", "S", "M", "L", "XL", "XXL")),
        piece("p_almirah_04", "almirah", "Black Dobby Unstitched 3PC", 7595, R.drawable.img_almirah_04, Category.Women, "Black", 0xFF1F1F1F, listOf("Unstitched")),
        piece("p_almirah_05", "almirah", "Peach Poly Slub 2Pc Stitched", 5915, R.drawable.img_almirah_05, Category.Women, "Peach", 0xFFF6BEA0, listOf("XS", "S", "M", "L", "XL")),
        piece("p_almirah_06", "almirah", "Black Prince Coat", 34000, R.drawable.img_almirah_06, Category.More, "Black", 0xFF1F1F1F, listOf("Made to Order"))
    )

    private fun lulusar() = listOf(
        piece("p_lulusar_01", "lulusar", "Mirzal Top", 5519, R.drawable.img_lulusar_01, Category.Women, "Mustard", 0xFFC0B92C, listOf("XS", "S", "M", "L", "XL")),
        piece("p_lulusar_02", "lulusar", "Sarkul Pants", 2760, R.drawable.img_lulusar_02, Category.Women, "Bottle Green", 0xFF0D3823, listOf("XS", "S", "M", "L", "XL")),
        piece("p_lulusar_03", "lulusar", "Raniz Skirt", 4799, R.drawable.img_lulusar_03, Category.Women, "Camel", 0xFFD49083, listOf("XS", "S", "M", "L", "XL")),
        piece("p_lulusar_04", "lulusar", "Naurai Jumpsuit (Sleeveless)", 2800, R.drawable.img_lulusar_04, Category.Women, "Black", 0xFF111112, listOf("XS", "S", "M", "L", "XL")),
        piece("p_lulusar_05", "lulusar", "Atrif Dupatta", 2099, R.drawable.img_lulusar_05, Category.Women, "Burgundy", 0xFF961724, listOf("One Size")),
        piece("p_lulusar_06", "lulusar", "Dargil Dress", 3920, R.drawable.img_lulusar_06, Category.Women, "Maroon", 0xFF7E1833, listOf("XS", "S", "M", "L", "XL"))
    )

    private fun saya() = listOf(
        piece("p_saya_01", "saya", "Printed Lawn Stitched 2 Piece (Shirt/Trouser)", 3198, R.drawable.img_saya_01, Category.Women, "Cream", 0xFFEEE2C6, listOf("X-Small", "Small", "Medium", "Large", "X-Large")),
        piece("p_saya_02", "saya", "Printed Embroidered Cotton Dobby 2 Piece (Shirt/Trouser)", 2999, R.drawable.img_saya_02, Category.Women, "Black", 0xFF1F1F1F, listOf("Small", "Medium", "Large")),
        piece("p_saya_03", "saya", "Luxury Pret Embroidered Cotton Jacquard Shirt", 6999, R.drawable.img_saya_03, Category.Women, "Yellow", 0xFFF0D446, listOf("X-Small", "Medium", "X-Large")),
        piece("p_saya_04", "saya", "Jacquard Cotton Stitched Shirt", 3799, R.drawable.img_saya_04, Category.Women, "Purple", 0xFF643282, listOf("Small", "Medium", "Large")),
        piece("p_saya_05", "saya", "Solid Cross Hatch Stitched 2 Piece (Shirt/Trouser)", 4999, R.drawable.img_saya_05, Category.Women, "Blue", 0xFF3565B0, listOf("X-Small", "Small", "Medium")),
        piece("p_saya_06", "saya", "Printed Premium Lawn Stitched Shirt For Kids", 1498, R.drawable.img_saya_06, Category.More, "Green", 0xFF34784A, listOf("3-4 Y", "5-6 Y", "7-8 Y", "9-10 Y"))
    )

    private fun edenrobe() = listOf(
        piece("p_edenrobe_01", "edenrobe", "Printed Khaddar Suit", 3500, R.drawable.img_edenrobe_01, Category.Women, "Beige", 0xFFD6C4A6, listOf("Unstitched")),
        piece("p_edenrobe_02", "edenrobe", "Embroidered Viscose Suit", 10000, R.drawable.img_edenrobe_02, Category.Women, "Purple", 0xFF643282, listOf("Unstitched")),
        piece("p_edenrobe_03", "edenrobe", "Embroidered Khaddar Co-Ord Set", 8000, R.drawable.img_edenrobe_03, Category.Women, "Rust", 0xFFAA4E2C, listOf("XS", "S", "M", "L", "XL", "XXL")),
        piece("p_edenrobe_04", "edenrobe", "Solid Viscose Co-Ord Set", 9000, R.drawable.img_edenrobe_04, Category.Women, "Blue", 0xFF3565B0, listOf("XS", "S", "M", "L", "XL", "XXL")),
        piece("p_edenrobe_05", "edenrobe", "Printed Lawn Co-Ord Set", 4000, R.drawable.img_edenrobe_05, Category.Women, "Olive", 0xFF6E6E38, listOf("XS", "S", "M", "L", "XL", "XXL")),
        piece("p_edenrobe_06", "edenrobe", "Full Sleeves Henley Tee", 7000, R.drawable.img_edenrobe_06, Category.More, "Brown", 0xFF6E4830, listOf("S", "M", "L", "XL", "XXL"))
    )

    private fun zeen() = listOf(
        piece("p_zeen_01", "zeen", "Stitched 2-Piece", 8990, R.drawable.img_zeen_01, Category.Women, "Navy Blue", 0xFF1F2A4D, listOf("XS (8)", "S (10)", "M (12)", "L (14)", "XL (16)")),
        piece("p_zeen_02", "zeen", "Unstitched 3-Piece", 6990, R.drawable.img_zeen_02, Category.Women, "Blue", 0xFF3565B0, listOf("Unstitched")),
        piece("p_zeen_03", "zeen", "Co-Ord Set", 4595, R.drawable.img_zeen_03, Category.Women, "Light Blue", 0xFFA9C9E8, listOf("XS (8)", "S (10)", "M (12)", "L (14)", "XL (16)", "XXL (18)")),
        piece("p_zeen_04", "zeen", "Stitched Embroidered Organza Suit", 7745, R.drawable.img_zeen_04, Category.Women, "Mint Green", 0xFFB0DEC8, listOf("XS (8)", "S (10)", "M (12)", "L (14)", "XL (16)", "XXL (18)")),
        piece("p_zeen_05", "zeen", "Stitched Chikankari Shirt Bottom", 4995, R.drawable.img_zeen_05, Category.Women, "Plum", 0xFF682C54, listOf("XS (8)", "S (10)", "M (12)", "L (14)", "XL (16)", "XXL (18)")),
        piece("p_zeen_06", "zeen", "Stitched 2 Piece Embroidered Raw Silk Suit", 6495, R.drawable.img_zeen_06, Category.Women, "Red", 0xFFC02A32, listOf("XS (8)", "S (10)", "M (12)", "L (14)", "XL (16)", "XXL (18)"))
    )

    private fun shaposh() = listOf(
        piece("p_shaposh_01", "shaposh", "3 Piece Karandi Linen", 6790, R.drawable.img_shaposh_01, Category.Women, "Off White", 0xFFEFEADF, listOf("Small", "Medium", "Large", "Extra Large")),
        piece("p_shaposh_02", "shaposh", "Women Plain Paper Katan 3 Pcs", 6392, R.drawable.img_shaposh_02, Category.Women, "Grey", 0xFF808080, listOf("S", "M", "L", "XL")),
        piece("p_shaposh_03", "shaposh", "Women Khaddar 2 Pcs", 3520, R.drawable.img_shaposh_03, Category.Women, "Blue", 0xFF3565B0, listOf("S", "M", "L", "XL")),
        piece("p_shaposh_04", "shaposh", "Dark Pink Velvet 3 Piece Suit for Women", 19120, R.drawable.img_shaposh_04, Category.Women, "Dusty Mustard", 0xFFD4A42C, listOf("Small", "Medium", "Large", "Extra Large")),
        piece("p_shaposh_05", "shaposh", "Unstitched Khaddar 2PC", 2396, R.drawable.img_shaposh_05, Category.Women, "Grey", 0xFF808080, listOf("Unstitched")),
        piece("p_shaposh_06", "shaposh", "Kids Raw Silk 3 Pcs", 4600, R.drawable.img_shaposh_06, Category.More, "Navy Blue", 0xFF1F2A4D, listOf("20", "22", "24", "26", "28", "30", "32", "34"))
    )

    private fun gulaal() = listOf(
        piece("p_gulaal_01", "gulaal", "Miraas", 21450, R.drawable.img_gulaal_01, Category.Women, "Camel", 0xFFAE9471, listOf("Unstitched", "S", "M", "L")),
        piece("p_gulaal_02", "gulaal", "Plum Noir", 12950, R.drawable.img_gulaal_02, Category.Women, "Chocolate", 0xFF48243E, listOf("Unstitched", "S", "M", "L", "XL")),
        piece("p_gulaal_03", "gulaal", "Sofiya", 24450, R.drawable.img_gulaal_03, Category.Women, "Black", 0xFF1E110F, listOf("Unstitched", "S", "M", "L", "XL")),
        piece("p_gulaal_04", "gulaal", "Sahar", 5250, R.drawable.img_gulaal_04, Category.Women, "Rococco Red", 0xFFC02A32, listOf("Small", "Medium", "Large", "X-Large")),
        piece("p_gulaal_05", "gulaal", "Sorelle", 5850, R.drawable.img_gulaal_05, Category.Women, "Charcoal", 0xFF4D4946, listOf("Small", "Medium", "Large", "X-Large")),
        piece("p_gulaal_06", "gulaal", "Lueur", 17450, R.drawable.img_gulaal_06, Category.Women, "Deep Navy Blue", 0xFF1F2A4D, listOf("Unstitched", "S", "M", "L", "XL"))
    )

    private fun batik() = listOf(
        piece("p_batik_01", "batik", "Ayal 3Pc - Velvet Formal Dress", 21990, R.drawable.img_batik_01, Category.Women, "Deep Maroon", 0xFF6E1824, listOf("XS", "S", "M", "L", "XL")),
        piece("p_batik_02", "batik", "Forest Spruce 3Pc - Festive Luxe Pret", 18490, R.drawable.img_batik_02, Category.Women, "Mehndi Green", 0xFF77803A, listOf("S", "M", "L")),
        piece("p_batik_03", "batik", "Rory 3Pc - Festive Luxe Pret", 16990, R.drawable.img_batik_03, Category.Women, "Burgundy", 0xFF6A1830, listOf("S", "M", "L")),
        piece("p_batik_04", "batik", "Mint Vanessa 3Pc - Festive Luxe Pret", 16490, R.drawable.img_batik_04, Category.Women, "Mint Green", 0xFFB0DEC8, listOf("XS", "S", "M", "L")),
        piece("p_batik_05", "batik", "Copper 3Pc - Festive Luxe Pret", 19990, R.drawable.img_batik_05, Category.Women, "Dark Navy", 0xFF1F2A4D, listOf("XS", "S", "M", "L")),
        piece("p_batik_06", "batik", "Rupert 3Pc - Embroidered Khaddar Pret", 12490, R.drawable.img_batik_06, Category.Women, "Fox Glove", 0xFFCA939D, listOf("XS", "S", "M", "L", "XL"))
    )

    private fun mausummery() = listOf(
        piece("p_mausummery_01", "mausummery", "Linen 2 Piece Suit", 3825, R.drawable.img_mausummery_01, Category.Women, "Black", 0xFF080604, listOf("S", "M", "L", "XL")),
        piece("p_mausummery_02", "mausummery", "Tulip Shalwar", 2760, R.drawable.img_mausummery_02, Category.Women, "Tea Pink", 0xFFE6D3D2, listOf("S", "M", "L", "XL")),
        piece("p_mausummery_03", "mausummery", "Primrose - Embroidered Co-Ord", 3975, R.drawable.img_mausummery_03, Category.Women, "Peach", 0xFFE5B37C, listOf("S", "M", "L", "XL")),
        piece("p_mausummery_04", "mausummery", "Harem Pants with Pearl Buttons", 2760, R.drawable.img_mausummery_04, Category.Women, "Lilac", 0xFFC0AAA5, listOf("S", "M", "L", "XL")),
        piece("p_mausummery_05", "mausummery", "Viscose Coord Set", 4650, R.drawable.img_mausummery_05, Category.Women, "Grey", 0xFF886866, listOf("S", "M", "L", "XL")),
        piece("p_mausummery_06", "mausummery", "Harem Pants", 2760, R.drawable.img_mausummery_06, Category.Women, "Silver", 0xFFCEBFBD, listOf("S", "M", "L", "XL"))
    )

    private fun ego() = listOf(
        piece("p_ego_01", "ego", "Glazed 2 piece", 7990, R.drawable.img_ego_01, Category.Women, "Sky Blue", 0xFFA2C8BA, listOf("XS", "S", "M", "L", "XL")),
        piece("p_ego_02", "ego", "Oriental 3 piece", 3816, R.drawable.img_ego_02, Category.Women, "Rose", 0xFFCC7288, listOf("Unstitched")),
        piece("p_ego_03", "ego", "Marigold 2 piece", 5592, R.drawable.img_ego_03, Category.Women, "Camel", 0xFFBB9C6A, listOf("XS", "S", "M", "L", "XL")),
        piece("p_ego_04", "ego", "Basic Wraps", 1400, R.drawable.img_ego_04, Category.Women, "Camel", 0xFFC68E68, listOf("One Size")),
        piece("p_ego_05", "ego", "Flora Culottes", 3590, R.drawable.img_ego_05, Category.Women, "Silver", 0xFFBFBBAB, listOf("XS", "S", "M", "L", "XL")),
        piece("p_ego_06", "ego", "Dew 2 piece", 4590, R.drawable.img_ego_06, Category.More, "Sage", 0xFF82B59B, listOf("2-3Y", "3-4Y", "4-5Y", "5-6Y", "6-7Y", "7-8Y"))
    )

    private fun krosskulture() = listOf(
        piece("p_krosskulture_01", "krosskulture", "Meher", 18890, R.drawable.img_krosskulture_01, Category.Women, "Black", 0xFF0F0508, listOf("XS", "S", "M", "L", "XL")),
        piece("p_krosskulture_02", "krosskulture", "Zita", 18890, R.drawable.img_krosskulture_02, Category.Women, "Brown", 0xFF72573D, listOf("XS", "S", "M", "L", "XL")),
        piece("p_krosskulture_03", "krosskulture", "Beyaz", 18490, R.drawable.img_krosskulture_03, Category.Women, "Silver", 0xFFB8B3A9, listOf("XS", "S", "M", "L", "XL")),
        piece("p_krosskulture_04", "krosskulture", "Astur", 18890, R.drawable.img_krosskulture_04, Category.Women, "Tan", 0xFFA07E5A, listOf("XS", "S", "M", "L", "XL")),
        piece("p_krosskulture_05", "krosskulture", "Sevgi", 18890, R.drawable.img_krosskulture_05, Category.Women, "Olive", 0xFF7F5E47, listOf("XS", "S", "M", "L", "XL")),
        piece("p_krosskulture_06", "krosskulture", "Valari", 18490, R.drawable.img_krosskulture_06, Category.Women, "Tan", 0xFF996F4C, listOf("XS", "S", "M", "L", "XL"))
    )

    private fun outfitters() = listOf(
        piece("p_outfitters_01", "outfitters", "Wide Leg Trousers In Brushed Fabric", 1290, R.drawable.img_outfitters_01, Category.Women, "Beige", 0xFFD6C4A6, listOf("XS", "S", "M", "L")),
        piece("p_outfitters_02", "outfitters", "Wide Leg Jeans", 1990, R.drawable.img_outfitters_02, Category.More, "Blue Ice", 0xFF3565B0, listOf("06-12M", "12-18M", "18-24M", "02-03Y", "03-04Y", "04-05Y", "05-06Y")),
        piece("p_outfitters_03", "outfitters", "Suede Clogs", 3490, R.drawable.img_outfitters_03, Category.Footwear, "Beige", 0xFFD6C4A6, listOf("40", "41", "42", "43", "44", "45")),
        piece("p_outfitters_04", "outfitters", "Striped Shirt", 2690, R.drawable.img_outfitters_04, Category.More, "Sage", 0xFFB0A597, listOf("S", "M", "L", "XL")),
        piece("p_outfitters_05", "outfitters", "Square Tinted Sunglasses", 1890, R.drawable.img_outfitters_05, Category.More, "Dark Grey", 0xFF808080, listOf("One Size")),
        piece("p_outfitters_06", "outfitters", "Flower Stud Earrings", 890, R.drawable.img_outfitters_06, Category.Jewellery, "White", 0xFFF4F1EA, listOf("One Size"))
    )

    private fun breakout() = listOf(
        piece("p_breakout_01", "breakout", "Girls Fur Jacket", 5599, R.drawable.img_breakout_01, Category.More, "Lilac", 0xFFBEAADC, listOf("1-2Y", "2-3Y", "3-4Y", "4-5Y", "5-6Y", "6-7Y", "7-8Y", "8-9Y")),
        piece("p_breakout_02", "breakout", "Basic Button Down Cardigan", 4549, R.drawable.img_breakout_02, Category.Women, "Black", 0xFF1F1F1F, listOf("S", "M", "L", "XL")),
        piece("p_breakout_03", "breakout", "Pullover Graphic Hoodie", 5249, R.drawable.img_breakout_03, Category.More, "Olive", 0xFF6E6E38, listOf("XS", "S", "M", "L", "XL")),
        piece("p_breakout_04", "breakout", "Girls Gloves", 1539, R.drawable.img_breakout_04, Category.More, "Blue", 0xFF3565B0, listOf("One Size")),
        piece("p_breakout_05", "breakout", "Girls V-Neck Layered Embroidered Sweatshirt", 2169, R.drawable.img_breakout_05, Category.More, "Pistachio", 0xFFB7D3A8, listOf("1-2Y", "2-3Y", "3-4Y", "4-5Y", "5-6Y", "6-7Y", "7-8Y", "9-10Y")),
        piece("p_breakout_06", "breakout", "Embroidered Zip-Up Jacket", 9799, R.drawable.img_breakout_06, Category.Women, "Burgundy", 0xFF6A1830, listOf("S", "M", "L", "XL"))
    )

    private fun diners() = listOf(
        piece("p_diners_01", "diners", "Dyed Embroidered 3PC", 16490, R.drawable.img_diners_01, Category.Women, "Bottle Green", 0xFF244F59, listOf("XS", "S", "M", "L", "XL")),
        piece("p_diners_02", "diners", "Navy Blue Winter Tech Jacket & Trouser Set", 13118, R.drawable.img_diners_02, Category.More, "Navy", 0xFF1F2A4D, listOf("S", "M", "L", "XL", "XXL", "XXXL")),
        piece("p_diners_03", "diners", "Dark Grey Teens Kameez Shalwar", 5992, R.drawable.img_diners_03, Category.More, "Grey", 0xFF808080, listOf("12Y", "14Y", "16Y", "18Y")),
        piece("p_diners_04", "diners", "Navy Blue Casual Peshawari Sandal", 4868, R.drawable.img_diners_04, Category.Footwear, "Navy", 0xFF1F2A4D, listOf("40", "41", "42", "43", "44", "45")),
        piece("p_diners_05", "diners", "Black Brown Men's Belt", 3490, R.drawable.img_diners_05, Category.More, "Brown", 0xFF6E4830, listOf("32", "34", "36", "38", "40", "42", "44", "46")),
        piece("p_diners_06", "diners", "Dyed Embroidered 2PC", 12990, R.drawable.img_diners_06, Category.Women, "Chocolate", 0xFF312322, listOf("XS", "S", "M", "L"))
    )

    private fun engine() = listOf(
        piece("p_engine_01", "engine", "Women Flared Trouser", 4199, R.drawable.img_engine_01, Category.Women, "Black", 0xFF1F1F1F, listOf("S", "M", "L", "XL")),
        piece("p_engine_02", "engine", "Girls Dress", 1650, R.drawable.img_engine_02, Category.More, "Green", 0xFF34784A, listOf("6-9 M", "9-12 M", "12-18 M", "18-24 M")),
        piece("p_engine_03", "engine", "Men Sweatshirt", 2799, R.drawable.img_engine_03, Category.More, "Whisper Green", 0xFF34784A, listOf("S", "M", "L", "XL", "XXL")),
        piece("p_engine_04", "engine", "Women Socks", 629, R.drawable.img_engine_04, Category.More, "Silver", 0xFFA5B7C4, listOf("One Size")),
        piece("p_engine_05", "engine", "Women Suit", 5599, R.drawable.img_engine_05, Category.Women, "Sage Green", 0xFFA0B096, listOf("S", "M", "L", "XL")),
        piece("p_engine_06", "engine", "Boys Upper", 3499, R.drawable.img_engine_06, Category.More, "Sky Blue", 0xFF96C8EB, listOf("2 Y", "3 Y", "4 Y", "5-6 Y"))
    )

    private fun furor() = listOf(
        piece("p_furor_01", "furor", "Classic Elasticated Pants", 9950, R.drawable.img_furor_01, Category.More, "Black", 0xFF1F1F1F, listOf("S", "M", "L", "XL")),
        piece("p_furor_02", "furor", "FJ Core Slides", 2990, R.drawable.img_furor_02, Category.Footwear, "Black", 0xFF1F1F1F, listOf("40", "41", "42", "43", "44", "45")),
        piece("p_furor_03", "furor", "Crossbody Bag - Black", 5950, R.drawable.img_furor_03, Category.More, "Black", 0xFF1F1F1F, listOf("One Size")),
        piece("p_furor_04", "furor", "Relaxed Fit Jeans", 6490, R.drawable.img_furor_04, Category.More, "Blue", 0xFF3565B0, listOf("30", "32", "34", "36", "38", "40")),
        piece("p_furor_05", "furor", "Moc-Toe Wave Sneaker", 13950, R.drawable.img_furor_05, Category.Footwear, "Black", 0xFF1F1F1F, listOf("40", "41", "42", "43", "44", "45")),
        piece("p_furor_06", "furor", "Suede Baseball Cap", 2150, R.drawable.img_furor_06, Category.More, "Grey", 0xFF808080, listOf("One Size"))
    )

    private fun one() = listOf(
        piece("p_one_01", "one", "Oversize Faux Leather Jacket", 17999, R.drawable.img_one_01, Category.Women, "Burgundy", 0xFF6A1830, listOf("S", "M", "L", "XL")),
        piece("p_one_02", "one", "Basic Track Pants", 3999, R.drawable.img_one_02, Category.More, "Grey", 0xFF808080, listOf("2 Y", "3 Y", "4 Y", "5 Y", "6 Y", "7-8 Y", "9-10 Y", "11-12 Y")),
        piece("p_one_03", "one", "Textured Relax Fit Shirt", 7999, R.drawable.img_one_03, Category.More, "Beige", 0xFFD6C4A6, listOf("S", "M", "XL", "2XL")),
        piece("p_one_04", "one", "Faux Leather Bag", 6649, R.drawable.img_one_04, Category.More, "Black", 0xFF1F1F1F, listOf("One Size")),
        piece("p_one_05", "one", "Cropped Double-Breast Jacket", 14999, R.drawable.img_one_05, Category.Women, "Brown", 0xFF6E4830, listOf("S", "M", "L", "XL")),
        piece("p_one_06", "one", "Straight Dungarees", 2095, R.drawable.img_one_06, Category.More, "Black", 0xFF1F1F1F, listOf("2 Y", "3 Y", "4 Y", "5 Y", "6 Y", "7-8 Y", "9-10 Y", "11-12 Y"))
    )

    private fun baroque() = listOf(
        piece("p_baroque_01", "baroque", "Embroidered Lawn", 5370, R.drawable.img_baroque_01, Category.Women, "Mauve", 0xFFBA9994, listOf("Unstitched")),
        piece("p_baroque_02", "baroque", "Embroidered Khaddar", 19950, R.drawable.img_baroque_02, Category.Women, "Maroon", 0xFF6A1B3E, listOf("Stitched")),
        piece("p_baroque_03", "baroque", "Embroidered Chiffon", 16950, R.drawable.img_baroque_03, Category.Women, "Black", 0xFF040906, listOf("XS", "S", "M", "L")),
        piece("p_baroque_04", "baroque", "Embroidered Velvet", 39950, R.drawable.img_baroque_04, Category.Women, "Black", 0xFF0D1011, listOf("Stitched")),
        piece("p_baroque_05", "baroque", "Embroidered Chiffon Stitched", 49950, R.drawable.img_baroque_05, Category.Women, "Black", 0xFF192424, listOf("Stitched")),
        piece("p_baroque_06", "baroque", "Embroidered Jacquard", 11950, R.drawable.img_baroque_06, Category.Women, "Maroon", 0xFF4F141E, listOf("XS", "S", "M", "L"))
    )

    private fun zarashahjahan() = listOf(
        piece("p_zarashahjahan_01", "zarashahjahan", "Lafz", 21998, R.drawable.img_zarashahjahan_01, Category.Women, "Olive", 0xFF55666E, listOf("S", "M", "L", "XL")),
        piece("p_zarashahjahan_02", "zarashahjahan", "Shab", 21998, R.drawable.img_zarashahjahan_02, Category.Women, "Navy", 0xFF152547, listOf("S", "M", "L", "XL")),
        piece("p_zarashahjahan_03", "zarashahjahan", "Diwan", 19998, R.drawable.img_zarashahjahan_03, Category.Women, "Black", 0xFF231418, listOf("S", "M", "L", "XL")),
        piece("p_zarashahjahan_04", "zarashahjahan", "Zaitooni", 13498, R.drawable.img_zarashahjahan_04, Category.Women, "Chocolate", 0xFF3D2714, listOf("S/M", "L/XL")),
        piece("p_zarashahjahan_05", "zarashahjahan", "Amber", 11498, R.drawable.img_zarashahjahan_05, Category.Women, "Black", 0xFF331408, listOf("S", "M", "L", "XL")),
        piece("p_zarashahjahan_06", "zarashahjahan", "Sabz Noor", 65500, R.drawable.img_zarashahjahan_06, Category.Jewellery, "Camel", 0xFFC79671, listOf("One Size"))
    )

    private fun saniamaskatiya() = listOf(
        piece("p_saniamaskatiya_01", "saniamaskatiya", "Vasma", 49500, R.drawable.img_saniamaskatiya_01, Category.Women, "Brown", 0xFF64452B, listOf("XS", "S", "M", "ML", "L", "XL")),
        piece("p_saniamaskatiya_02", "saniamaskatiya", "Ziya", 145000, R.drawable.img_saniamaskatiya_02, Category.Women, "Black", 0xFF171310, listOf("XS", "S", "M", "ML", "L", "XL")),
        piece("p_saniamaskatiya_03", "saniamaskatiya", "Renya", 295000, R.drawable.img_saniamaskatiya_03, Category.Women, "Brown", 0xFF7E4E40, listOf("XS", "S", "M", "ML", "L", "XL")),
        piece("p_saniamaskatiya_04", "saniamaskatiya", "Ayra", 1150000, R.drawable.img_saniamaskatiya_04, Category.Women, "Charcoal", 0xFF4A3D25, listOf("XS", "S", "M", "ML", "L", "XL")),
        piece("p_saniamaskatiya_05", "saniamaskatiya", "Jihan", 69500, R.drawable.img_saniamaskatiya_05, Category.Women, "Camel", 0xFFB99874, listOf("XS", "S", "M", "ML", "L", "XL")),
        piece("p_saniamaskatiya_06", "saniamaskatiya", "Rima", 125000, R.drawable.img_saniamaskatiya_06, Category.Women, "Black", 0xFF0A0A0A, listOf("XS", "S", "M", "ML", "L", "XL"))
    )

    private fun asimjofa() = listOf(
        piece("p_asimjofa_01", "asimjofa", "Embroidered Cambric Stitched 2 Pcs", 9790, R.drawable.img_asimjofa_01, Category.Women, "Navy", 0xFF02213E, listOf("S", "M", "L", "XL")),
        piece("p_asimjofa_02", "asimjofa", "Embroidered Poly Net Unstitched 3 Pcs", 7499, R.drawable.img_asimjofa_02, Category.Women, "Black", 0xFF2E1923, listOf("Unstitched")),
        piece("p_asimjofa_03", "asimjofa", "Embroidered Javeria Net Stitched 3 Pcs", 18399, R.drawable.img_asimjofa_03, Category.Women, "Beige", 0xFFCCBAA0, listOf("XS")),
        piece("p_asimjofa_04", "asimjofa", "Embroidered Twisted Silk Unstitched 2 Pcs", 7399, R.drawable.img_asimjofa_04, Category.Women, "Black", 0xFF2D1D17, listOf("Unstitched")),
        piece("p_asimjofa_05", "asimjofa", "Portofino", 10990, R.drawable.img_asimjofa_05, Category.Women, "Lilac", 0xFFADAEBE, listOf("XS", "S", "M", "L")),
        piece("p_asimjofa_06", "asimjofa", "Embroidered Twisted Silk Unstitched 3 Pcs", 7399, R.drawable.img_asimjofa_06, Category.Women, "Charcoal", 0xFF37363E, listOf("Unstitched"))
    )

    private fun hsy() = listOf(
        piece("p_hsy_01", "hsy", "Sanjh", 175000, R.drawable.img_hsy_01, Category.Women, "Tan", 0xFFA17A5A, listOf("Small", "Medium", "Large")),
        piece("p_hsy_02", "hsy", "Silsila", 150000, R.drawable.img_hsy_02, Category.Women, "Grey", 0xFF95787C, listOf("Small", "Medium", "Large")),
        piece("p_hsy_03", "hsy", "Raazeen", 225000, R.drawable.img_hsy_03, Category.Women, "Charcoal", 0xFF3E3B30, listOf("Small", "Medium", "Large")),
        piece("p_hsy_04", "hsy", "Sanghat", 375000, R.drawable.img_hsy_04, Category.Women, "Rust", 0xFF903F3C, listOf("Small", "Medium", "Large")),
        piece("p_hsy_05", "hsy", "Manzar", 350000, R.drawable.img_hsy_05, Category.Women, "Brown", 0xFF65453B, listOf("Small", "Medium", "Large")),
        piece("p_hsy_06", "hsy", "Timeless Tailored Classic Shalwar Kameez", 20000, R.drawable.img_hsy_06, Category.More, "Black", 0xFF1F1F1F, listOf("One Size"))
    )

    private fun aghanoor() = listOf(
        piece("p_aghanoor_01", "aghanoor", "3 Piece - Pure Embroidered Chiffon Suit", 30000, R.drawable.img_aghanoor_01, Category.Women, "Dusty Purple", 0xFF643282, listOf("X-Small", "Small", "Medium", "Large", "X-Large")),
        piece("p_aghanoor_02", "aghanoor", "2 Piece - Pure Irish Linen Women's Suit", 12500, R.drawable.img_aghanoor_02, Category.Women, "Off White", 0xFFEFEADF, listOf("X-Small", "Small", "Medium", "Large", "X-Large")),
        piece("p_aghanoor_03", "aghanoor", "Pure Cotton Lawn Shalwar", 2000, R.drawable.img_aghanoor_03, Category.Women, "Black", 0xFF1F1F1F, listOf("X-Small", "Small", "Medium", "Large", "X-Large")),
        piece("p_aghanoor_04", "aghanoor", "Pure Cotton Lawn Pant", 2000, R.drawable.img_aghanoor_04, Category.Women, "Black", 0xFF1F1F1F, listOf("X-Small", "Small", "Medium", "Large", "X-Large")),
        piece("p_aghanoor_05", "aghanoor", "Printed Lawn Dupatta", 1500, R.drawable.img_aghanoor_05, Category.Women, "Blue", 0xFF3F4899, listOf("One Size")),
        piece("p_aghanoor_06", "aghanoor", "Kids Embroidered Lawn Shirt (Single Shirt)", 2000, R.drawable.img_aghanoor_06, Category.More, "Yellow", 0xFFF0D446, listOf("4-5 Years", "5-6 Years", "6-7 Years", "7-8 Years", "9-10 Years", "11-12 Years", "13-14 Years", "15-16 Years"))
    )

    private fun afrozeh() = listOf(
        piece("p_afrozeh_01", "afrozeh", "Bloomair", 14900, R.drawable.img_afrozeh_01, Category.Women, "Charcoal", 0xFF2C3B1C, listOf("Extra Small", "Small", "Medium", "Large", "Extra Large")),
        piece("p_afrozeh_02", "afrozeh", "Riyasat", 11900, R.drawable.img_afrozeh_02, Category.Women, "Black", 0xFF0B2326, listOf("Unstitched", "XS", "S", "M", "L", "XL")),
        piece("p_afrozeh_03", "afrozeh", "Aitraaf", 22400, R.drawable.img_afrozeh_03, Category.Women, "Black", 0xFF1C1F34, listOf("Unstitched", "XS", "S", "M", "L", "XL")),
        piece("p_afrozeh_04", "afrozeh", "Liorah", 14900, R.drawable.img_afrozeh_04, Category.Women, "Plum", 0xFF5A3640, listOf("Extra Small", "Small", "Medium", "Large", "Extra Large")),
        piece("p_afrozeh_05", "afrozeh", "Sarmast", 11900, R.drawable.img_afrozeh_05, Category.Women, "Chocolate", 0xFF3B1D18, listOf("Unstitched", "XS", "S", "M", "L", "XL")),
        piece("p_afrozeh_06", "afrozeh", "Vasl", 13900, R.drawable.img_afrozeh_06, Category.Women, "Grey", 0xFF7C8A7A, listOf("Unstitched", "XS", "S", "M", "L", "XL"))
    )

    private fun hussainrehar() = listOf(
        piece("p_hussainrehar_01", "hussainrehar", "Doree", 39900, R.drawable.img_hussainrehar_01, Category.Women, "Sage", 0xFFB8AA9D, listOf("XS", "S", "M", "L")),
        piece("p_hussainrehar_02", "hussainrehar", "Eclat", 69900, R.drawable.img_hussainrehar_02, Category.Women, "Black", 0xFF0D0D0F, listOf("XS", "S", "M", "L")),
        piece("p_hussainrehar_03", "hussainrehar", "Noblesse", 58900, R.drawable.img_hussainrehar_03, Category.Women, "Grey", 0xFF688281, listOf("XS", "S", "M", "L")),
        piece("p_hussainrehar_04", "hussainrehar", "Marquise", 54900, R.drawable.img_hussainrehar_04, Category.Women, "Black", 0xFF171112, listOf("XS", "S", "M", "L")),
        piece("p_hussainrehar_05", "hussainrehar", "Blanche", 54900, R.drawable.img_hussainrehar_05, Category.Women, "Grey", 0xFF937C72, listOf("XS", "S", "M", "L")),
        piece("p_hussainrehar_06", "hussainrehar", "Viridian Coat", 25000, R.drawable.img_hussainrehar_06, Category.More, "Black", 0xFF07090B, listOf("S", "M", "L", "XL"))
    )

    private fun republic() = listOf(
        piece("p_republic_01", "republic", "Floréane", 12000, R.drawable.img_republic_01, Category.Women, "Black", 0xFF081515, listOf("Unstitched")),
        piece("p_republic_02", "republic", "Rose Kaftan", 27500, R.drawable.img_republic_02, Category.Women, "Mauve", 0xFFBF8784, listOf("S", "M", "L", "XL")),
        piece("p_republic_03", "republic", "Célestine", 13000, R.drawable.img_republic_03, Category.Women, "Black", 0xFF060A0E, listOf("Unstitched")),
        piece("p_republic_04", "republic", "Purple Garden", 33000, R.drawable.img_republic_04, Category.Women, "Purple", 0xFF643282, listOf("S", "M", "L", "XL")),
        piece("p_republic_05", "republic", "Adorée", 13500, R.drawable.img_republic_05, Category.Women, "Bottle Green", 0xFF024949, listOf("Unstitched")),
        piece("p_republic_06", "republic", "Pastel Dream", 33000, R.drawable.img_republic_06, Category.Women, "Navy", 0xFF26292B, listOf("S", "M", "L", "XL"))
    )

    private fun zainabchottani() = listOf(
        piece("p_zainabchottani_01", "zainabchottani", "Neelum", 59500, R.drawable.img_zainabchottani_01, Category.Women, "Black", 0xFF081120, listOf("XS", "S", "M", "ML", "L", "XL")),
        piece("p_zainabchottani_02", "zainabchottani", "Violet Eclat", 265000, R.drawable.img_zainabchottani_02, Category.Women, "Black", 0xFF281C2E, listOf("XS", "S", "M", "ML", "L", "XL")),
        piece("p_zainabchottani_03", "zainabchottani", "Noor", 995000, R.drawable.img_zainabchottani_03, Category.Women, "Olive", 0xFF795B43, listOf("Made to Order")),
        piece("p_zainabchottani_04", "zainabchottani", "Masaya", 495000, R.drawable.img_zainabchottani_04, Category.Women, "Black", 0xFF141F1A, listOf("Made to Order")),
        piece("p_zainabchottani_05", "zainabchottani", "Dina", 13000, R.drawable.img_zainabchottani_05, Category.Women, "Navy", 0xFF212046, listOf("XS", "S", "M", "L", "XL")),
        piece("p_zainabchottani_06", "zainabchottani", "Erin - 11", 15960, R.drawable.img_zainabchottani_06, Category.Women, "Brown", 0xFF814528, listOf("Unstitched"))
    )

    private fun sairashakira() = listOf(
        piece("p_sairashakira_01", "sairashakira", "Zelina (2 Piece)", 150000, R.drawable.img_sairashakira_01, Category.Women, "Black", 0xFF0C0B0A, listOf("S", "M", "L")),
        piece("p_sairashakira_02", "sairashakira", "Noir Éclipse", 500000, R.drawable.img_sairashakira_02, Category.Women, "Black", 0xFF160B0A, listOf("Made to Order")),
        piece("p_sairashakira_03", "sairashakira", "Sereia", 1500000, R.drawable.img_sairashakira_03, Category.Women, "Grey", 0xFF978D86, listOf("Made to Order")),
        piece("p_sairashakira_04", "sairashakira", "Tali", 18500, R.drawable.img_sairashakira_04, Category.Women, "Beige", 0xFFDFBDC2, listOf("Unstitched")),
        piece("p_sairashakira_05", "sairashakira", "Ava", 15500, R.drawable.img_sairashakira_05, Category.Women, "Sage", 0xFFB3A4AE, listOf("Unstitched")),
        piece("p_sairashakira_06", "sairashakira", "Cressida", 125000, R.drawable.img_sairashakira_06, Category.Women, "Sage", 0xFFA59D90, listOf("S", "M", "L"))
    )

    private fun alixeeshan() = listOf(
        piece("p_alixeeshan_01", "alixeeshan", "Madame Mirage", 784110, R.drawable.img_alixeeshan_01, Category.Women, "Black", 0xFF2F150B, listOf("XS", "S", "M", "L", "XL")),
        piece("p_alixeeshan_02", "alixeeshan", "Jack of Hearts", 559650, R.drawable.img_alixeeshan_02, Category.Women, "Black", 0xFF050303, listOf("XS", "S", "M", "L", "XL")),
        piece("p_alixeeshan_03", "alixeeshan", "Spiralling", 610520, R.drawable.img_alixeeshan_03, Category.Women, "Tan", 0xFF987756, listOf("XS", "S", "M", "L", "XL")),
        piece("p_alixeeshan_04", "alixeeshan", "Andheri", 279820, R.drawable.img_alixeeshan_04, Category.Women, "Black", 0xFF030302, listOf("XS", "S", "M", "L", "XL")),
        piece("p_alixeeshan_05", "alixeeshan", "Mata Hari", 1678940, R.drawable.img_alixeeshan_05, Category.Women, "Black", 0xFF2F140E, listOf("XS", "S", "M", "L", "XL")),
        piece("p_alixeeshan_06", "alixeeshan", "Dark Knight", 559650, R.drawable.img_alixeeshan_06, Category.More, "Black", 0xFF060403, listOf("XS", "S", "M", "L", "XL"))
    )

    private fun zaha() = listOf(
        piece("p_zaha_01", "zaha", "Lumi", 29950, R.drawable.img_zaha_01, Category.Women, "Silver", 0xFFC0B4AA, listOf("S", "M", "L")),
        piece("p_zaha_02", "zaha", "Roseau", 32450, R.drawable.img_zaha_02, Category.Women, "Camel", 0xFFC3A49D, listOf("S", "M", "L")),
        piece("p_zaha_03", "zaha", "Éléa", 32450, R.drawable.img_zaha_03, Category.Women, "Sage", 0xFF9FA3AA, listOf("S", "M", "L")),
        piece("p_zaha_04", "zaha", "Iris", 32450, R.drawable.img_zaha_04, Category.Women, "Mauve", 0xFFAB9682, listOf("S", "M", "L")),
        piece("p_zaha_05", "zaha", "Sera", 29950, R.drawable.img_zaha_05, Category.Women, "Grey", 0xFF7A7567, listOf("S", "M", "L")),
        piece("p_zaha_06", "zaha", "Léa", 16450, R.drawable.img_zaha_06, Category.Women, "Chocolate", 0xFF4E1D08, listOf("S", "M", "L"))
    )

    private fun iznik() = listOf(
        piece("p_iznik_01", "iznik", "Embroidered Lawn", 5992, R.drawable.img_iznik_01, Category.Women, "Chocolate", 0xFF53293C, listOf("Unstitched", "Stitched")),
        piece("p_iznik_02", "iznik", "Embroidered Chiffon", 12530, R.drawable.img_iznik_02, Category.Women, "Mauve", 0xFFA79890, listOf("Unstitched", "Stitched")),
        piece("p_iznik_03", "iznik", "Printed Fine Khaddar", 7990, R.drawable.img_iznik_03, Category.Women, "Black", 0xFF1C0C0A, listOf("Unstitched")),
        piece("p_iznik_04", "iznik", "Ramen Silk", 28992, R.drawable.img_iznik_04, Category.Women, "Black", 0xFF1E0706, listOf("Unstitched", "Stitched")),
        piece("p_iznik_05", "iznik", "Embroidered Rawsilk", 15920, R.drawable.img_iznik_05, Category.Women, "Teal", 0xFF0D755D, listOf("XS", "S", "M", "L", "XL")),
        piece("p_iznik_06", "iznik", "Embroidered Viscose Jacquard", 9990, R.drawable.img_iznik_06, Category.Women, "Black", 0xFF271017, listOf("Unstitched"))
    )

    private fun jazmin() = listOf(
        piece("p_jazmin_01", "jazmin", "Embroidered Premium Viscose", 9950, R.drawable.img_jazmin_01, Category.Women, "Teal", 0xFF1E597A, listOf("S", "M", "L")),
        piece("p_jazmin_02", "jazmin", "Embroidered Velvet Formal", 26500, R.drawable.img_jazmin_02, Category.Women, "Black", 0xFF020707, listOf("Unstitched", "Stitched")),
        piece("p_jazmin_03", "jazmin", "Digital Printed Lawn", 6570, R.drawable.img_jazmin_03, Category.Women, "Silver", 0xFFBAB2B8, listOf("XS", "S", "M", "L", "XL")),
        piece("p_jazmin_04", "jazmin", "Embroidered Chiffon", 29500, R.drawable.img_jazmin_04, Category.Women, "Chocolate", 0xFF4F3628, listOf("Unstitched", "Stitched")),
        piece("p_jazmin_05", "jazmin", "Handwork Raw Silk", 12950, R.drawable.img_jazmin_05, Category.Women, "Sage", 0xFFA5A28C, listOf("Unstitched", "Stitched")),
        piece("p_jazmin_06", "jazmin", "Embroidered Velvet Embellished Bag", 7500, R.drawable.img_jazmin_06, Category.More, "Tea Pink", 0xFFE6D4C4, listOf("One Size"))
    )

    private fun qalamkar() = listOf(
        piece("p_qalamkar_01", "qalamkar", "Romilly", 33990, R.drawable.img_qalamkar_01, Category.Women, "Sage", 0xFFB8AAA9, listOf("Unstitched", "XS", "S", "M", "L", "XL")),
        piece("p_qalamkar_02", "qalamkar", "Elowen", 30990, R.drawable.img_qalamkar_02, Category.Women, "Tan", 0xFFAB836E, listOf("Unstitched", "XS", "S", "M", "L", "XL")),
        piece("p_qalamkar_03", "qalamkar", "Sloane", 30990, R.drawable.img_qalamkar_03, Category.Women, "Navy", 0xFF272731, listOf("Unstitched", "XS", "S", "M", "L", "XL")),
        piece("p_qalamkar_04", "qalamkar", "Romée", 31990, R.drawable.img_qalamkar_04, Category.Women, "Grey", 0xFF8C7F61, listOf("Unstitched", "XS", "S", "M", "L", "XL")),
        piece("p_qalamkar_05", "qalamkar", "Zelie", 32990, R.drawable.img_qalamkar_05, Category.Women, "Brown", 0xFF79412F, listOf("Unstitched", "XS", "S", "M", "L", "XL")),
        piece("p_qalamkar_06", "qalamkar", "Waistcoat", 12810, R.drawable.img_qalamkar_06, Category.More, "Pink", 0xFFDD9C9D, listOf("Unstitched", "S", "M", "L"))
    )

    private fun maryumnmaria() = listOf(
        piece("p_maryumnmaria_01", "maryumnmaria", "Zarina", 4720, R.drawable.img_maryumnmaria_01, Category.Women, "Cream", 0xFFDEDEDD, listOf("S", "M", "L")),
        piece("p_maryumnmaria_02", "maryumnmaria", "Viora", 10199, R.drawable.img_maryumnmaria_02, Category.Women, "Olive", 0xFF856C57, listOf("S", "M", "L")),
        piece("p_maryumnmaria_03", "maryumnmaria", "Zahara", 12720, R.drawable.img_maryumnmaria_03, Category.Women, "Brown", 0xFF59595F, listOf("S", "M", "L")),
        piece("p_maryumnmaria_04", "maryumnmaria", "Maia", 7120, R.drawable.img_maryumnmaria_04, Category.Women, "Navy", 0xFF1C3E73, listOf("S", "M", "L")),
        piece("p_maryumnmaria_05", "maryumnmaria", "Riva", 7120, R.drawable.img_maryumnmaria_05, Category.Women, "Beige", 0xFFD5C7C2, listOf("S", "M", "L")),
        piece("p_maryumnmaria_06", "maryumnmaria", "Azura", 4500, R.drawable.img_maryumnmaria_06, Category.Women, "Charcoal", 0xFF46475C, listOf("S", "M", "L"))
    )

    private fun motifz() = listOf(
        piece("p_motifz_01", "motifz", "Veira Embroidered Velvet Unstitched", 12593, R.drawable.img_motifz_01, Category.Women, "Black", 0xFF06050A, listOf("Unstitched")),
        piece("p_motifz_02", "motifz", "Ushna Embroidered Khaddar Stitched", 10143, R.drawable.img_motifz_02, Category.Women, "Maroon", 0xFF5E1E27, listOf("Small", "Medium", "Large", "Extra Large")),
        piece("p_motifz_03", "motifz", "Doozy Embroidered Velvet Unstitched", 12243, R.drawable.img_motifz_03, Category.Women, "Black", 0xFF16122D, listOf("Unstitched")),
        piece("p_motifz_04", "motifz", "Suha Embroidered Khaddar Stitched", 10143, R.drawable.img_motifz_04, Category.Women, "Brown", 0xFF855926, listOf("Small", "Medium", "Large", "Extra Large")),
        piece("p_motifz_05", "motifz", "Kiaan Embroidered Velvet Unstitched", 12243, R.drawable.img_motifz_05, Category.Women, "Black", 0xFF2D020A, listOf("Unstitched")),
        piece("p_motifz_06", "motifz", "Fateen Embroidered Khaddar Stitched", 10143, R.drawable.img_motifz_06, Category.Women, "Brown", 0xFF52462C, listOf("Small", "Medium", "Large", "Extra Large"))
    )

    private fun suffuse() = listOf(
        piece("p_suffuse_01", "suffuse", "Fariyn", 654374, R.drawable.img_suffuse_01, Category.Women, "Brown", 0xFF604139, listOf("S", "M", "L")),
        piece("p_suffuse_02", "suffuse", "Nusha", 69924, R.drawable.img_suffuse_02, Category.Women, "Black", 0xFF280607, listOf("S", "M", "L", "XL")),
        piece("p_suffuse_03", "suffuse", "Elora", 21872, R.drawable.img_suffuse_03, Category.Women, "Navy", 0xFF2C2036, listOf("XS", "S", "M", "L", "Unstitched")),
        piece("p_suffuse_04", "suffuse", "Orina", 53655, R.drawable.img_suffuse_04, Category.Women, "Black", 0xFF340405, listOf("XS", "S", "M", "L", "XL")),
        piece("p_suffuse_05", "suffuse", "Eira", 26764, R.drawable.img_suffuse_05, Category.Women, "Gold", 0xFFBFAD7A, listOf("XS", "S", "M", "L", "XL")),
        piece("p_suffuse_06", "suffuse", "Afsanae", 54343, R.drawable.img_suffuse_06, Category.Women, "Black", 0xFF361B12, listOf("S", "M", "L", "XL", "Unstitched"))
    )

    private fun mnr() = listOf(
        piece("p_mnr_01", "mnr", "Rija Mujahid in Roshni", 22740, R.drawable.img_mnr_01, Category.Women, "Gold", 0xFFC8A25A, listOf("Unstitched")),
        piece("p_mnr_02", "mnr", "Zahra Sarfraz in Tabeer", 22140, R.drawable.img_mnr_02, Category.Women, "Brown", 0xFF6E4830, listOf("Unstitched")),
        piece("p_mnr_03", "mnr", "Zarbaft Peshwas", 294000, R.drawable.img_mnr_03, Category.Women, "Chocolate", 0xFF351D21, listOf("S", "M", "L", "XL")),
        piece("p_mnr_04", "mnr", "Zeb un Nissa", 237000, R.drawable.img_mnr_04, Category.Women, "Purple", 0xFF643282, listOf("S", "M", "L", "XL")),
        piece("p_mnr_05", "mnr", "Ronaq jahan choli custom", 315000, R.drawable.img_mnr_05, Category.Women, "Maroon", 0xFF690F31, listOf("S", "M", "L", "XL")),
        piece("p_mnr_06", "mnr", "Green Heritage", 390000, R.drawable.img_mnr_06, Category.More, "Green", 0xFF34784A, listOf("S", "M", "L", "XL"))
    )

    private fun mishalakhani() = listOf(
        piece("p_mishalakhani_01", "mishalakhani", "Alisha & Skinny Shalwar", 50550, R.drawable.img_mishalakhani_01, Category.Women, "Tan", 0xFFA8826A, listOf("XS", "S", "M", "L", "XL")),
        piece("p_mishalakhani_02", "mishalakhani", "Keenu", 47740, R.drawable.img_mishalakhani_02, Category.Women, "Mauve", 0xFFA47C87, listOf("One Size")),
        piece("p_mishalakhani_03", "mishalakhani", "Leia", 117950, R.drawable.img_mishalakhani_03, Category.Women, "Grey", 0xFF858879, listOf("XS", "S", "M", "L", "XL")),
        piece("p_mishalakhani_04", "mishalakhani", "Culottes", 16850, R.drawable.img_mishalakhani_04, Category.Women, "Black", 0xFF050719, listOf("XS", "S", "M", "L", "XL")),
        piece("p_mishalakhani_05", "mishalakhani", "Lily Top & Skinny Shalwar", 129190, R.drawable.img_mishalakhani_05, Category.Women, "Black", 0xFF050505, listOf("XS", "S", "M", "L", "XL")),
        piece("p_mishalakhani_06", "mishalakhani", "Rani Jacket & Palazzo", 160080, R.drawable.img_mishalakhani_06, Category.Women, "Beige", 0xFFDCBAA5, listOf("XS", "S", "M", "L", "XL"))
    )

    private fun nidaazwer() = listOf(
        piece("p_nidaazwer_01", "nidaazwer", "Tia", 210000, R.drawable.img_nidaazwer_01, Category.Women, "Black", 0xFF010508, listOf("XS", "S", "SM", "M", "ML", "L", "XL")),
        piece("p_nidaazwer_02", "nidaazwer", "Nimr", 580000, R.drawable.img_nidaazwer_02, Category.Women, "Black", 0xFF111318, listOf("XS", "S", "SM", "M", "ML", "L", "XL", "XXL")),
        piece("p_nidaazwer_03", "nidaazwer", "Zashane", 385000, R.drawable.img_nidaazwer_03, Category.Women, "Black", 0xFF2C251E, listOf("XS", "S", "SM", "M", "ML", "L", "XL", "XXL")),
        piece("p_nidaazwer_04", "nidaazwer", "Ruhaam", 850000, R.drawable.img_nidaazwer_04, Category.Women, "Chocolate", 0xFF4A2326, listOf("XS", "S", "SM", "M", "ML", "L", "XL", "XXL")),
        piece("p_nidaazwer_05", "nidaazwer", "Mah-e-Noor", 140000, R.drawable.img_nidaazwer_05, Category.Women, "Mauve", 0xFF9E987D, listOf("XS", "S", "SM", "M", "ML", "L", "XL", "XXL")),
        piece("p_nidaazwer_06", "nidaazwer", "Marsla", 55000, R.drawable.img_nidaazwer_06, Category.Women, "Navy", 0xFF192531, listOf("XS", "S", "SM", "M", "ML", "L", "XL"))
    )

    private fun kanwalmalik() = listOf(
        piece("p_kanwalmalik_01", "kanwalmalik", "Tammana", 108000, R.drawable.img_kanwalmalik_01, Category.Women, "Bottle Green", 0xFF0C3F3E, listOf("XS", "S", "M", "L", "XL")),
        piece("p_kanwalmalik_02", "kanwalmalik", "Kavya", 210000, R.drawable.img_kanwalmalik_02, Category.Women, "Nude", 0xFFDDBFA6, listOf("S", "M", "L")),
        piece("p_kanwalmalik_03", "kanwalmalik", "Jiana", 60000, R.drawable.img_kanwalmalik_03, Category.Women, "Bottle Green", 0xFF263D35, listOf("XS", "S", "M", "L", "XL")),
        piece("p_kanwalmalik_04", "kanwalmalik", "Deja", 52000, R.drawable.img_kanwalmalik_04, Category.Women, "Camel", 0xFFCAAB91, listOf("XS", "S", "M", "L", "XL")),
        piece("p_kanwalmalik_05", "kanwalmalik", "Rimel", 49162, R.drawable.img_kanwalmalik_05, Category.Women, "Ice Blue", 0xFFCFE3EE, listOf("XS", "S", "M", "L", "XL")),
        piece("p_kanwalmalik_06", "kanwalmalik", "Arham", 55000, R.drawable.img_kanwalmalik_06, Category.More, "Black", 0xFF0A0705, listOf("S", "M", "L", "XL"))
    )

    private fun tenadurrani() = listOf(
        piece("p_tenadurrani_01", "tenadurrani", "Azmeh", 1400000, R.drawable.img_tenadurrani_01, Category.Women, "Multi", 0xFFE3C6A8, listOf("Made to Order")),
        piece("p_tenadurrani_02", "tenadurrani", "Aphrodite", 2200000, R.drawable.img_tenadurrani_02, Category.Women, "Rust", 0xFFA03E2C, listOf("One Size")),
        piece("p_tenadurrani_03", "tenadurrani", "Ameerah", 1300000, R.drawable.img_tenadurrani_03, Category.Women, "Multi", 0xFFCAA88E, listOf("Made to Order")),
        piece("p_tenadurrani_04", "tenadurrani", "Elham", 1300000, R.drawable.img_tenadurrani_04, Category.Women, "Multi", 0xFF674930, listOf("Made to Order")),
        piece("p_tenadurrani_05", "tenadurrani", "Ziya", 1275000, R.drawable.img_tenadurrani_05, Category.Women, "Multi", 0xFFC1A17C, listOf("Made to Order")),
        piece("p_tenadurrani_06", "tenadurrani", "Azalea", 1600000, R.drawable.img_tenadurrani_06, Category.Women, "Tan", 0xFF9E7265, listOf("One Size"))
    )

    private fun saadiaasad() = listOf(
        piece("p_saadiaasad_01", "saadiaasad", "Grey Aari", 16990, R.drawable.img_saadiaasad_01, Category.Women, "Grey", 0xFF808080, listOf("Unstitched")),
        piece("p_saadiaasad_02", "saadiaasad", "Aubergine", 34500, R.drawable.img_saadiaasad_02, Category.Women, "Black", 0xFF14050D, listOf("XS", "S", "M", "L", "XL")),
        piece("p_saadiaasad_03", "saadiaasad", "Sapphire Heritage", 17850, R.drawable.img_saadiaasad_03, Category.Women, "Black", 0xFF191924, listOf("Unstitched")),
        piece("p_saadiaasad_04", "saadiaasad", "Royal Merlot", 34500, R.drawable.img_saadiaasad_04, Category.Women, "Black", 0xFF21010A, listOf("XS", "S", "M", "L", "XL")),
        piece("p_saadiaasad_05", "saadiaasad", "Black Paisley", 17850, R.drawable.img_saadiaasad_05, Category.Women, "Black", 0xFF1F1F1F, listOf("Unstitched")),
        piece("p_saadiaasad_06", "saadiaasad", "Chestnut", 34500, R.drawable.img_saadiaasad_06, Category.Women, "Chocolate", 0xFF4E2917, listOf("XS", "S", "M", "L", "XL"))
    )

    private fun akbaraslam() = listOf(
        piece("p_akbaraslam_01", "akbaraslam", "Veya", 34950, R.drawable.img_akbaraslam_01, Category.Women, "Silver", 0xFFC3B1B5, listOf("S", "M", "L")),
        piece("p_akbaraslam_02", "akbaraslam", "Willow", 38150, R.drawable.img_akbaraslam_02, Category.Women, "Tan", 0xFFA08275, listOf("One Size")),
        piece("p_akbaraslam_03", "akbaraslam", "Solace", 6500, R.drawable.img_akbaraslam_03, Category.Women, "Magenta", 0xFFC0416E, listOf("S", "M", "L")),
        piece("p_akbaraslam_04", "akbaraslam", "Isnora", 3000, R.drawable.img_akbaraslam_04, Category.Women, "Brown", 0xFF585157, listOf("Small", "Medium", "Large")),
        piece("p_akbaraslam_05", "akbaraslam", "Makira", 19950, R.drawable.img_akbaraslam_05, Category.Women, "Tan", 0xFFA98748, listOf("One Size")),
        piece("p_akbaraslam_06", "akbaraslam", "Rivah", 11450, R.drawable.img_akbaraslam_06, Category.Women, "Bottle Green", 0xFF054950, listOf("Small", "Medium", "Large"))
    )

    private fun rangrasiya() = listOf(
        piece("p_rangrasiya_01", "rangrasiya", "Vintage Wine", 10990, R.drawable.img_rangrasiya_01, Category.Women, "Deep Maroon", 0xFF6E1824, listOf("Unstitched", "S", "M", "L", "XL")),
        piece("p_rangrasiya_02", "rangrasiya", "Ruby Frost", 16990, R.drawable.img_rangrasiya_02, Category.Women, "Deep Maroon", 0xFF6E1824, listOf("Unstitched", "S", "M", "L", "XL")),
        piece("p_rangrasiya_03", "rangrasiya", "Fern", 22990, R.drawable.img_rangrasiya_03, Category.Women, "Mint", 0xFFB0DEC8, listOf("Unstitched", "S", "M", "L", "XL")),
        piece("p_rangrasiya_04", "rangrasiya", "Mauve", 8995, R.drawable.img_rangrasiya_04, Category.Women, "Plum", 0xFF682C54, listOf("Unstitched", "S", "M", "L", "XL")),
        piece("p_rangrasiya_05", "rangrasiya", "Bloom", 8995, R.drawable.img_rangrasiya_05, Category.Women, "Dusty Pink", 0xFFEC96B4, listOf("Unstitched", "S", "M", "L", "XL")),
        piece("p_rangrasiya_06", "rangrasiya", "Velora", 8995, R.drawable.img_rangrasiya_06, Category.Women, "Black", 0xFF1F1F1F, listOf("Unstitched", "S", "M", "L", "XL"))
    )

    private fun uniworth() = listOf(
        piece("p_uniworth_01", "uniworth", "Beige Plain Modern Bomber Jacket", 7996, R.drawable.img_uniworth_01, Category.More, "Beige", 0xFFD6C4A6, listOf("S", "M", "L", "XL", "XXL")),
        piece("p_uniworth_02", "uniworth", "Navy Printed Tailored Smart Fit Shirt", 7980, R.drawable.img_uniworth_02, Category.More, "Navy", 0xFF1F2A4D, listOf("14½", "15", "15½", "16", "16½", "17", "17½", "18")),
        piece("p_uniworth_03", "uniworth", "White Textured Embroidered Jacquard Sweatshirt", 5895, R.drawable.img_uniworth_03, Category.More, "White", 0xFFF4F1EA, listOf("S", "M", "L", "XL", "XXL")),
        piece("p_uniworth_04", "uniworth", "Black Polo Collar Button-Up Cardigan", 7975, R.drawable.img_uniworth_04, Category.More, "Black", 0xFF1F1F1F, listOf("S", "M", "L", "XL", "XXL")),
        piece("p_uniworth_05", "uniworth", "Black Textured Hooded Zipper Sweater", 7495, R.drawable.img_uniworth_05, Category.More, "Black", 0xFF1F1F1F, listOf("S", "M", "L", "XL", "XXL")),
        piece("p_uniworth_06", "uniworth", "Chrome Plain Pocket Detail Half Zipper", 6995, R.drawable.img_uniworth_06, Category.More, "Chrome", 0xFFDDDBE1, listOf("S", "M", "L", "XL", "XXL"))
    )

    private fun cambridge() = listOf(
        piece("p_cambridge_01", "cambridge", "Henley Raglan Sweatshirt", 2348, R.drawable.img_cambridge_01, Category.More, "Olive", 0xFF6E6E38, listOf("XS", "S", "M", "L", "XL", "XXL", "XXXL")),
        piece("p_cambridge_02", "cambridge", "Leather Wallet", 3395, R.drawable.img_cambridge_02, Category.More, "Black", 0xFF1F1F1F, listOf("One Size")),
        piece("p_cambridge_03", "cambridge", "Graphic T-Shirt", 1748, R.drawable.img_cambridge_03, Category.More, "Burgundy", 0xFF6A1830, listOf("XS", "S", "M", "L", "XL", "XXL", "XXXL")),
        piece("p_cambridge_04", "cambridge", "Mercerized Socks (Single Pair)", 1795, R.drawable.img_cambridge_04, Category.More, "Light Brown", 0xFFB08A62, listOf("One Size")),
        piece("p_cambridge_05", "cambridge", "Graphic T-Shirt - Blue", 1698, R.drawable.img_cambridge_05, Category.More, "Blue", 0xFF3565B0, listOf("XS", "S", "M", "L", "XL", "XXL", "XXXL")),
        piece("p_cambridge_06", "cambridge", "Leather Wallet-Navy Blue", 5595, R.drawable.img_cambridge_06, Category.More, "Navy Blue", 0xFF1F2A4D, listOf("One Size"))
    )

    private fun royaltag() = listOf(
        piece("p_royaltag_01", "royaltag", "Wind Duffel Bag", 29500, R.drawable.img_royaltag_01, Category.More, "Black", 0xFF1F1F1F, listOf("One Size")),
        piece("p_royaltag_02", "royaltag", "Pink Dress Shirt", 3960, R.drawable.img_royaltag_02, Category.More, "Pink", 0xFFEC96B4, listOf("15", "15½", "16", "16½", "17", "17½")),
        piece("p_royaltag_03", "royaltag", "Ties", 2120, R.drawable.img_royaltag_03, Category.More, "Charcoal", 0xFF353349, listOf("One Size")),
        piece("p_royaltag_04", "royaltag", "The Contour", 7560, R.drawable.img_royaltag_04, Category.More, "Black", 0xFF1C171D, listOf("30", "32", "34", "36", "38", "40", "42")),
        piece("p_royaltag_05", "royaltag", "Pocket Square", 1320, R.drawable.img_royaltag_05, Category.More, "Olive", 0xFF705A75, listOf("One Size")),
        piece("p_royaltag_06", "royaltag", "Charcoal Grey Refined Suit", 27600, R.drawable.img_royaltag_06, Category.More, "Grey", 0xFF808080, listOf("36", "38", "40", "42", "44"))
    )

    private fun monark() = listOf(
        piece("p_monark_01", "monark", "Beige Textured Coat", 19600, R.drawable.img_monark_01, Category.More, "Beige", 0xFFD6C4A6, listOf("36", "38", "40", "42")),
        piece("p_monark_02", "monark", "Green Knitted Beanie", 2250, R.drawable.img_monark_02, Category.More, "Green", 0xFF34784A, listOf("One Size")),
        piece("p_monark_03", "monark", "Coffee Brown 2 Piece Suit", 23600, R.drawable.img_monark_03, Category.More, "Brown", 0xFF6E4830, listOf("36", "38", "40", "42")),
        piece("p_monark_04", "monark", "Black Cotton Socks", 600, R.drawable.img_monark_04, Category.More, "Black", 0xFF1F1F1F, listOf("One Size")),
        piece("p_monark_05", "monark", "Black Regular Fit Jeans", 4680, R.drawable.img_monark_05, Category.More, "Black", 0xFF1F1F1F, listOf("S", "M", "L", "XL")),
        piece("p_monark_06", "monark", "Grey Knitted Beanie", 2250, R.drawable.img_monark_06, Category.More, "Grey", 0xFF808080, listOf("One Size"))
    )

    private fun lama() = listOf(
        piece("p_lama_01", "lama", "Morocco Rib Sleeve Sweater", 10950, R.drawable.img_lama_01, Category.Women, "Olive", 0xFF6E6E38, listOf("XS", "S", "M", "L", "XL")),
        piece("p_lama_02", "lama", "Palm & Pebble Leather Loafers", 8450, R.drawable.img_lama_02, Category.Footwear, "Camel", 0xFFC19A6B, listOf("36", "37", "38", "39", "40", "41")),
        piece("p_lama_03", "lama", "Grilled Cheese Society Sweatshirt", 6950, R.drawable.img_lama_03, Category.More, "Ivory", 0xFFF0EAD6, listOf("S", "M", "L", "XL", "XXL")),
        piece("p_lama_04", "lama", "Ribbed Ankle Socks", 550, R.drawable.img_lama_04, Category.More, "Black", 0xFF1F1F1F, listOf("One Size")),
        piece("p_lama_05", "lama", "Luxury Long Suede Coat", 17950, R.drawable.img_lama_05, Category.Women, "Khaki", 0xFFBDAA78, listOf("XS", "S", "M", "L")),
        piece("p_lama_06", "lama", "Minimal Leather Sneaker", 13950, R.drawable.img_lama_06, Category.Footwear, "Dark Brown", 0xFF46301F, listOf("40", "41", "42", "43", "44", "45", "46"))
    )

    private fun amiradnan() = listOf(
        piece("p_amiradnan_01", "amiradnan", "Classic Fit Band Collar Plain Kameez Shalwar", 13500, R.drawable.img_amiradnan_01, Category.More, "Black", 0xFF1F1F1F, listOf("S", "M", "L")),
        piece("p_amiradnan_02", "amiradnan", "Blue Leather Peshawari Sandal", 9000, R.drawable.img_amiradnan_02, Category.Footwear, "Blue", 0xFF3565B0, listOf("8", "9", "10", "11", "12")),
        piece("p_amiradnan_03", "amiradnan", "Basic Woolen Almond Buff Traditional Shawl", 2900, R.drawable.img_amiradnan_03, Category.More, "Black", 0xFF151315, listOf("One Size")),
        piece("p_amiradnan_04", "amiradnan", "Jute Raw Hide Traditional Waistcoat", 12000, R.drawable.img_amiradnan_04, Category.More, "Brown", 0xFF5B443A, listOf("S", "M", "L", "XL")),
        piece("p_amiradnan_05", "amiradnan", "Slim Fit Shirt Collar Plain Kurta Pajama", 11250, R.drawable.img_amiradnan_05, Category.More, "Blue", 0xFF3565B0, listOf("S", "M", "L", "XL")),
        piece("p_amiradnan_06", "amiradnan", "Poly Viscose Taupe Brown Fabric", 9000, R.drawable.img_amiradnan_06, Category.More, "Brown", 0xFF6E4830, listOf("Unstitched"))
    )

    private fun ismailfarid() = listOf(
        piece("p_ismailfarid_01", "ismailfarid", "Dark Gray Textured Kameez Shalwar", 22500, R.drawable.img_ismailfarid_01, Category.More, "Gray", 0xFF808080, listOf("S", "M", "MC", "L", "XL")),
        piece("p_ismailfarid_02", "ismailfarid", "Dark Olive Textured Kameez Shalwar", 24500, R.drawable.img_ismailfarid_02, Category.More, "Olive", 0xFF6E6E38, listOf("S", "M", "MC", "L", "XL")),
        piece("p_ismailfarid_03", "ismailfarid", "Maroon Textured Kameez Shalwar", 26500, R.drawable.img_ismailfarid_03, Category.More, "Maroon", 0xFF6E1824, listOf("S", "M", "MC", "L", "XL")),
        piece("p_ismailfarid_04", "ismailfarid", "Ivory Chain Rope Embroidered Prince Coat", 135000, R.drawable.img_ismailfarid_04, Category.More, "Ivory", 0xFFF0EAD6, listOf("S", "M", "L", "XL")),
        piece("p_ismailfarid_05", "ismailfarid", "Premium Beige Kameez Shalwar", 24500, R.drawable.img_ismailfarid_05, Category.More, "Beige", 0xFFD6C4A6, listOf("S", "M", "MC", "L", "XL")),
        piece("p_ismailfarid_06", "ismailfarid", "Rich Teal Blue Kameez Shalwar", 26500, R.drawable.img_ismailfarid_06, Category.More, "Blue", 0xFF3565B0, listOf("S", "M", "MC", "L", "XL"))
    )

    private fun humayunalamgir() = listOf(
        piece("p_humayunalamgir_01", "humayunalamgir", "Black & Gold Ornate Embroidered Jacket", 14500, R.drawable.img_humayunalamgir_01, Category.More, "Black", 0xFF1F1F1F, listOf("One Size")),
        piece("p_humayunalamgir_02", "humayunalamgir", "Dark Sandstone Suede Oxfords Shoes", 18500, R.drawable.img_humayunalamgir_02, Category.Footwear, "Black", 0xFF2A0E0E, listOf("40", "41", "42", "43", "44", "45")),
        piece("p_humayunalamgir_03", "humayunalamgir", "Midnight Blue Gold Embroidered Prince Coat", 40000, R.drawable.img_humayunalamgir_03, Category.More, "Blue", 0xFF3565B0, listOf("One Size")),
        piece("p_humayunalamgir_04", "humayunalamgir", "Espresso Royale Croc-Embossed Mules", 16500, R.drawable.img_humayunalamgir_04, Category.Footwear, "Black", 0xFF14070A, listOf("40", "41", "42", "43", "44", "45")),
        piece("p_humayunalamgir_05", "humayunalamgir", "Sea Green Velvet Zip-Up Shacket", 14500, R.drawable.img_humayunalamgir_05, Category.More, "Green", 0xFF34784A, listOf("One Size")),
        piece("p_humayunalamgir_06", "humayunalamgir", "Espresso Croc-Embossed Peshawari Sandals", 14500, R.drawable.img_humayunalamgir_06, Category.Footwear, "Black", 0xFF140405, listOf("40", "41", "42", "43", "44", "45"))
    )

    private fun stylo() = listOf(
        piece("p_stylo_01", "stylo", "Beige Casual Sneaker For Ladies", 3790, R.drawable.img_stylo_01, Category.Footwear, "Beige", 0xFFD6C4A6, listOf("36", "37", "38", "39", "40", "41")),
        piece("p_stylo_02", "stylo", "Pink Pumps For Ladies", 3000, R.drawable.img_stylo_02, Category.Footwear, "Pink", 0xFFEC96B4, listOf("36", "37", "38", "39", "40", "41", "42")),
        piece("p_stylo_03", "stylo", "Pink Moccasin For Ladies", 3950, R.drawable.img_stylo_03, Category.Footwear, "Pink", 0xFFEC96B4, listOf("36", "37", "38", "39", "40", "41")),
        piece("p_stylo_04", "stylo", "Black Court Shoes For Ladies", 2528, R.drawable.img_stylo_04, Category.Footwear, "Black", 0xFF1F1F1F, listOf("36", "37", "38", "39", "40", "41")),
        piece("p_stylo_05", "stylo", "Champagne Back Open For Ladies", 3160, R.drawable.img_stylo_05, Category.Footwear, "Champagne", 0xFFE8D6B8, listOf("36", "37", "38", "39", "40", "41")),
        piece("p_stylo_06", "stylo", "Mustard Winter Acrylic Shawl", 1950, R.drawable.img_stylo_06, Category.Women, "Mustard", 0xFFD4A42C, listOf("One Size"))
    )

    private fun bata() = listOf(
        piece("p_bata_01", "bata", "Weinbrenner - Men", 1199, R.drawable.img_bata_01, Category.Footwear, "Dark-Brown", 0xFF6E4830, listOf("40/6", "41/7", "42/8", "43/9", "44/10", "45/11")),
        piece("p_bata_02", "bata", "Power - Men", 2749, R.drawable.img_bata_02, Category.Footwear, "White", 0xFFF4F1EA, listOf("40/6", "41/7", "42/8", "43/9", "44/10", "45/11")),
        piece("p_bata_03", "bata", "Mocassino - Men", 3749, R.drawable.img_bata_03, Category.Footwear, "Black", 0xFF1F1F1F, listOf("40/6", "41/7", "42/8", "43/9", "44/10", "45/11")),
        piece("p_bata_04", "bata", "Island - Women", 1609, R.drawable.img_bata_04, Category.Footwear, "Pink", 0xFFEC96B4, listOf("36/3", "37/4", "38/5", "39/6", "40/7", "41/8")),
        piece("p_bata_05", "bata", "Bubble Gummers - Kids (5 Years - 10 Years)", 649, R.drawable.img_bata_05, Category.Footwear, "Grey", 0xFF808080, listOf("27/9", "28/10", "30/11", "31/12", "33/13", "34/1", "35/2", "36/3")),
        piece("p_bata_06", "bata", "Bata - Belt", 1259, R.drawable.img_bata_06, Category.More, "Black", 0xFF1F1F1F, listOf("34", "36", "38", "40", "42", "44"))
    )

    private fun servis() = listOf(
        piece("p_servis_01", "servis", "Men Sneakers", 4499, R.drawable.img_servis_01, Category.Footwear, "Black", 0xFF1F1F1F, listOf("40", "41", "42", "43", "44", "45")),
        piece("p_servis_02", "servis", "Women Heels", 7124, R.drawable.img_servis_02, Category.Footwear, "Black", 0xFF1F1F1F, listOf("36", "37", "38", "39", "40", "41")),
        piece("p_servis_03", "servis", "Women Pumps", 6374, R.drawable.img_servis_03, Category.Footwear, "Grey Blue", 0xFF7C8794, listOf("36", "37", "38", "39", "40", "41")),
        piece("p_servis_04", "servis", "Women Moccs", 6749, R.drawable.img_servis_04, Category.Footwear, "Off-White", 0xFFEFEADF, listOf("36", "37", "38", "39", "40", "41")),
        piece("p_servis_05", "servis", "Women Canvas", 1949, R.drawable.img_servis_05, Category.Footwear, "Navy", 0xFF1F2A4D, listOf("36", "37", "38", "39", "40", "41")),
        piece("p_servis_06", "servis", "Infant Close Shoes", 1499, R.drawable.img_servis_06, Category.Footwear, "Pink", 0xFFEC96B4, listOf("3", "4", "5", "6"))
    )

    private fun borjan() = listOf(
        piece("p_borjan_01", "borjan", "Women Court Shoes - Silver", 5100, R.drawable.img_borjan_01, Category.Footwear, "Silver", 0xFFC0C0C0, listOf("36", "37", "38", "39", "40", "41")),
        piece("p_borjan_02", "borjan", "Men Peshawari - Olive", 4620, R.drawable.img_borjan_02, Category.Footwear, "Olive", 0xFF6E6E38, listOf("39", "40", "41", "42", "43", "44")),
        piece("p_borjan_03", "borjan", "Women Ballerina - Bronze", 6000, R.drawable.img_borjan_03, Category.Footwear, "Bronze", 0xFF966E46, listOf("36", "37", "38", "39", "40", "41", "42")),
        piece("p_borjan_04", "borjan", "Women Mule - Black", 4000, R.drawable.img_borjan_04, Category.Footwear, "Black", 0xFF1F1F1F, listOf("36", "37", "38", "39", "40", "41")),
        piece("p_borjan_05", "borjan", "Women Sling Back - Golden", 5200, R.drawable.img_borjan_05, Category.Footwear, "Golden", 0xFFC8A25A, listOf("36", "37", "38", "39", "40", "41")),
        piece("p_borjan_06", "borjan", "Women Scarf - Pink", 1000, R.drawable.img_borjan_06, Category.Women, "Pink", 0xFFEC96B4, listOf("One Size"))
    )

    private fun insignia() = listOf(
        piece("p_insignia_01", "insignia", "Formal Boots Brown", 16957, R.drawable.img_insignia_01, Category.Footwear, "Brown", 0xFF6E4830, listOf("36", "37", "38", "39", "40", "41")),
        piece("p_insignia_02", "insignia", "Party Wear Court Shoes Beige", 11008, R.drawable.img_insignia_02, Category.Footwear, "Beige", 0xFFD6C4A6, listOf("35", "36", "37", "38", "39", "40", "41")),
        piece("p_insignia_03", "insignia", "Party Wear Slip On Silver", 6332, R.drawable.img_insignia_03, Category.Footwear, "Silver", 0xFFC0C0C0, listOf("36", "37", "38", "39", "40", "41")),
        piece("p_insignia_04", "insignia", "Comfort Moccasin Ivory", 7608, R.drawable.img_insignia_04, Category.Footwear, "Ivory", 0xFFF0EAD6, listOf("36", "37", "38", "39", "40", "41")),
        piece("p_insignia_05", "insignia", "Formal Sling Back Off White", 10158, R.drawable.img_insignia_05, Category.Footwear, "Off White", 0xFFEFEADF, listOf("36", "37", "38", "39", "40", "41")),
        piece("p_insignia_06", "insignia", "Bagutte Shoulder Bags Silver", 7608, R.drawable.img_insignia_06, Category.More, "Silver", 0xFFC0C0C0, listOf("One Size"))
    )

    private fun hushpuppies() = listOf(
        piece("p_hushpuppies_01", "hushpuppies", "Non-Slip Munch Surrey Slippers", 4499, R.drawable.img_hushpuppies_01, Category.Footwear, "Beige", 0xFFD6C4A6, listOf("36", "37", "38", "39", "40", "41")),
        piece("p_hushpuppies_02", "hushpuppies", "Be Rico", 4500, R.drawable.img_hushpuppies_02, Category.Footwear, "Black", 0xFF1F1F1F, listOf("40", "41", "42", "43", "44", "45")),
        piece("p_hushpuppies_03", "hushpuppies", "Scott Helix", 4500, R.drawable.img_hushpuppies_03, Category.Footwear, "Black", 0xFF1F1F1F, listOf("40", "41", "42", "43", "44", "45")),
        piece("p_hushpuppies_04", "hushpuppies", "Puzzle Mini", 3999, R.drawable.img_hushpuppies_04, Category.Footwear, "Black", 0xFF1F1F1F, listOf("30", "31", "32", "33", "34", "35", "36", "37")),
        piece("p_hushpuppies_05", "hushpuppies", "Slip Easy Moc", 8399, R.drawable.img_hushpuppies_05, Category.Footwear, "Tan", 0xFFB0865C, listOf("40", "41", "42", "43", "44", "45")),
        piece("p_hushpuppies_06", "hushpuppies", "HP Wallets - Milted Bifold", 2999, R.drawable.img_hushpuppies_06, Category.More, "Black and Blue", 0xFF3565B0, listOf("One Size"))
    )

    private fun ecs() = listOf(
        piece("p_ecs_01", "ecs", "Athleisure Strap Sneakers", 3600, R.drawable.img_ecs_01, Category.Footwear, "White", 0xFFF4F1EA, listOf("30", "31", "32", "33", "34", "35")),
        piece("p_ecs_02", "ecs", "Formal Bow Coat Shoes", 4300, R.drawable.img_ecs_02, Category.Footwear, "Maroon", 0xFF6E1824, listOf("36", "37", "38", "39", "40", "41")),
        piece("p_ecs_03", "ecs", "Evening Patterned Coat Shoes", 5800, R.drawable.img_ecs_03, Category.Footwear, "Tan", 0xFFB0865C, listOf("36", "37", "38", "39", "40", "41")),
        piece("p_ecs_04", "ecs", "Partywear Bow Strap Sandal", 5000, R.drawable.img_ecs_04, Category.Footwear, "Golden", 0xFFC8A25A, listOf("36", "37", "38", "39", "40", "41")),
        piece("p_ecs_05", "ecs", "Ethnic Leaf Khussa", 3000, R.drawable.img_ecs_05, Category.Footwear, "Black", 0xFF1F1F1F, listOf("36", "37", "38", "39", "40", "41", "42")),
        piece("p_ecs_06", "ecs", "Embroidered Khaddar 2 Piece", 8500, R.drawable.img_ecs_06, Category.Women, "Tea Pink", 0xFFF4D0D0, listOf("XS", "S", "M", "L"))
    )

    private fun ndure() = listOf(
        piece("p_ndure_01", "ndure", "Women's Chelsea Ankle Boots", 5599, R.drawable.img_ndure_01, Category.Footwear, "Stone", 0xFFB8B0A0, listOf("36/4", "37/5", "38/6", "39/7", "40/8", "41/9")),
        piece("p_ndure_02", "ndure", "Men's Lace-Up Mesh Sneakers", 3599, R.drawable.img_ndure_02, Category.Footwear, "Beige", 0xFFD6C4A6, listOf("40/6", "41/7", "42/8", "43/9", "44/10", "45/11")),
        piece("p_ndure_03", "ndure", "Girls' Suede Loafers", 3999, R.drawable.img_ndure_03, Category.Footwear, "Brown", 0xFF6E4830, listOf("35", "36", "37", "38")),
        piece("p_ndure_04", "ndure", "Boys' Easy Fit Sneakers", 3599, R.drawable.img_ndure_04, Category.Footwear, "Black", 0xFF1F1F1F, listOf("29", "30", "31", "32", "33", "34", "35", "36")),
        piece("p_ndure_05", "ndure", "Women's Suede Leopard-Print Ballet Flats", 3599, R.drawable.img_ndure_05, Category.Footwear, "Leopard", 0xFFA48D85, listOf("36/4", "37/5", "38/6", "39/7", "40/8", "41/9")),
        piece("p_ndure_06", "ndure", "Ndure x HR Men's Two-Tone Panelled Sneakers", 5599, R.drawable.img_ndure_06, Category.Footwear, "Navy", 0xFF1F2A4D, listOf("40/6", "41/7", "42/8", "43/9", "44/10", "45/11"))
    )

    private fun unze() = listOf(
        piece("p_unze_01", "unze", "Women \"Joslin\" Casual Flat Slingback Sandals", 3300, R.drawable.img_unze_01, Category.Footwear, "Camel", 0xFFC8936B, listOf("36", "37", "38", "39", "40", "41")),
        piece("p_unze_02", "unze", "Women \"Dorra\" Classy Comfortable Moccasins", 4199, R.drawable.img_unze_02, Category.Footwear, "Charcoal", 0xFF363134, listOf("37", "39", "40", "41")),
        piece("p_unze_03", "unze", "Women \"Trine\" Luxe Block Heel Sandals", 4499, R.drawable.img_unze_03, Category.Footwear, "Grey", 0xFF998176, listOf("36", "37", "38", "39", "40", "41")),
        piece("p_unze_04", "unze", "Women \"Vanya\" Exotic Classic Mid High Heel Sandals", 4499, R.drawable.img_unze_04, Category.Footwear, "Beige", 0xFFCAB19D, listOf("36", "37", "38", "39", "40", "41")),
        piece("p_unze_05", "unze", "Women \"Rie\" Synthetic Slip On Court Shoes", 8999, R.drawable.img_unze_05, Category.Footwear, "Black", 0xFF211A1A, listOf("36", "37", "38", "39", "40", "41")),
        piece("p_unze_06", "unze", "\"Ivan\" Comfort Basic Fleece Tracksuit", 6300, R.drawable.img_unze_06, Category.More, "Charcoal", 0xFF30343D, listOf("XS", "S", "M", "L", "XL", "XXL", "XXXL"))
    )

    private fun clive() = listOf(
        piece("p_clive_01", "clive", "Brown Casual Sandal", 4000, R.drawable.img_clive_01, Category.Footwear, "Brown", 0xFF6E4830, listOf("39/6", "40/7", "41/8", "42/9", "43/10", "44/11")),
        piece("p_clive_02", "clive", "Black Formal Shoes", 7799, R.drawable.img_clive_02, Category.Footwear, "Black", 0xFF1F1F1F, listOf("39/6", "40/7", "41/8", "42/9", "43/10", "44/11")),
        piece("p_clive_03", "clive", "Green Casual Chappati", 1799, R.drawable.img_clive_03, Category.Footwear, "Green", 0xFF34784A, listOf("6", "7", "8", "9", "10", "11")),
        piece("p_clive_04", "clive", "Black Casual PU Slipper", 3899, R.drawable.img_clive_04, Category.Footwear, "Black", 0xFF1F1F1F, listOf("6", "7", "8", "9", "10", "11")),
        piece("p_clive_05", "clive", "Black Fancy Slipper Heel", 2000, R.drawable.img_clive_05, Category.Footwear, "Black", 0xFF1F1F1F, listOf("6", "7", "8", "9", "10", "11")),
        piece("p_clive_06", "clive", "Golden Fancy Clutch", 5249, R.drawable.img_clive_06, Category.More, "Golden", 0xFFC8A25A, listOf("One Size"))
    )

    private fun urbansole() = listOf(
        piece("p_urbansole_01", "urbansole", "Men's Slider", 3119, R.drawable.img_urbansole_01, Category.Footwear, "Grey", 0xFF808080, listOf("40", "41", "42", "43", "44", "45")),
        piece("p_urbansole_02", "urbansole", "Women's Court Shoes", 4399, R.drawable.img_urbansole_02, Category.Footwear, "Beige", 0xFFD6C4A6, listOf("36", "37", "38", "39", "40", "41")),
        piece("p_urbansole_03", "urbansole", "Men's Sports Shoes", 5759, R.drawable.img_urbansole_03, Category.Footwear, "Black", 0xFF1F1F1F, listOf("40", "41", "42", "43", "44", "45")),
        piece("p_urbansole_04", "urbansole", "Women's Slippers", 2099, R.drawable.img_urbansole_04, Category.Footwear, "Pink", 0xFFEC96B4, listOf("36", "37", "38", "39", "40", "41")),
        piece("p_urbansole_05", "urbansole", "Women's Moccasins", 4639, R.drawable.img_urbansole_05, Category.Footwear, "Brown", 0xFF6E4830, listOf("36", "37", "38", "39", "40", "41")),
        piece("p_urbansole_06", "urbansole", "Women's Tote Bag", 4399, R.drawable.img_urbansole_06, Category.More, "Brown", 0xFF6E4830, listOf("Large"))
    )

    private fun walkeaze() = listOf(
        piece("p_walkeaze_01", "walkeaze", "Women Formal Slingback", 2275, R.drawable.img_walkeaze_01, Category.Footwear, "Fawn", 0xFFCEB28C, listOf("36", "37", "38", "39", "40", "41")),
        piece("p_walkeaze_02", "walkeaze", "Women Formal Mules", 2025, R.drawable.img_walkeaze_02, Category.Footwear, "Black", 0xFF1F1F1F, listOf("36", "37", "38", "39", "40", "41")),
        piece("p_walkeaze_03", "walkeaze", "Women Formal Slippers", 1750, R.drawable.img_walkeaze_03, Category.Footwear, "White", 0xFFF4F1EA, listOf("36", "37", "38", "39", "40", "41")),
        piece("p_walkeaze_04", "walkeaze", "Women Formal Heel", 4480, R.drawable.img_walkeaze_04, Category.Footwear, "Pink", 0xFFEC96B4, listOf("36", "37", "38", "39", "40", "41")),
        piece("p_walkeaze_05", "walkeaze", "Women Formal Wedge", 2550, R.drawable.img_walkeaze_05, Category.Footwear, "Black", 0xFF1F1F1F, listOf("36", "37", "38", "39", "40", "41")),
        piece("p_walkeaze_06", "walkeaze", "Women Party Bag", 5908, R.drawable.img_walkeaze_06, Category.More, "Black", 0xFF1F1F1F, listOf("Small"))
    )

    private fun almas() = listOf(
        piece("p_almas_01", "almas", "Ideal Trend Sneaker-All/White", 2700, R.drawable.img_almas_01, Category.Footwear, "White", 0xFFF4F1EA, listOf("36", "37", "38", "39", "40", "41")),
        piece("p_almas_02", "almas", "Regal Hand-Stitched Tassel Loafers-Coffee", 4200, R.drawable.img_almas_02, Category.Footwear, "Olive", 0xFF705E5C, listOf("40", "41", "42", "43", "44", "45")),
        piece("p_almas_03", "almas", "Executive Perforated Loafer-Camel", 2200, R.drawable.img_almas_03, Category.Footwear, "Rust", 0xFFA0634E, listOf("36", "37", "38", "39", "40", "41")),
        piece("p_almas_04", "almas", "Luxurious Suede Loafers with Minimalistic Design-Beige", 4200, R.drawable.img_almas_04, Category.Footwear, "Beige", 0xFFD6C4A6, listOf("40", "41", "42", "43", "44", "45")),
        piece("p_almas_05", "almas", "Studded Leather Loafers-Black", 2500, R.drawable.img_almas_05, Category.Footwear, "Black", 0xFF1F1F1F, listOf("36", "37", "38", "39", "40", "41")),
        piece("p_almas_06", "almas", "Luxurious Wool Scarf-Pink", 3500, R.drawable.img_almas_06, Category.Women, "Pink", 0xFFEC96B4, listOf("One Size"))
    )

    private fun sputnik() = listOf(
        piece("p_sputnik_01", "sputnik", "Grey & Black Casual Shoes", 10965, R.drawable.img_sputnik_01, Category.Footwear, "Grey & Black", 0xFF808080, listOf("7", "8", "9", "10", "11", "12")),
        piece("p_sputnik_02", "sputnik", "Black Pumps", 3655, R.drawable.img_sputnik_02, Category.Footwear, "Black", 0xFF1F1F1F, listOf("6", "7", "8", "9", "10", "11")),
        piece("p_sputnik_03", "sputnik", "Cheeko Flat Slipper", 3740, R.drawable.img_sputnik_03, Category.Footwear, "Cheeko", 0xFFE6C3A2, listOf("6", "7", "8", "9", "10", "11")),
        piece("p_sputnik_04", "sputnik", "Fawn Comfort", 3570, R.drawable.img_sputnik_04, Category.Footwear, "Fawn", 0xFFCEB28C, listOf("7", "8", "9", "10", "11", "12", "13", "14")),
        piece("p_sputnik_05", "sputnik", "Brown Sandal", 3315, R.drawable.img_sputnik_05, Category.Footwear, "Brown", 0xFF6E4830, listOf("6", "7", "8", "9", "10", "11")),
        piece("p_sputnik_06", "sputnik", "Fawn Khussa", 4420, R.drawable.img_sputnik_06, Category.Footwear, "Fawn", 0xFFCEB28C, listOf("6", "7", "8", "9", "10", "11", "12"))
    )

    private fun jafferjees() = listOf(
        piece("p_jafferjees_01", "jafferjees", "Notting Hill", 10200, R.drawable.img_jafferjees_01, Category.More, "Blue", 0xFF3565B0, listOf("One Size")),
        piece("p_jafferjees_02", "jafferjees", "Burano Wallet", 4200, R.drawable.img_jafferjees_02, Category.More, "Red", 0xFFC02A32, listOf("One Size")),
        piece("p_jafferjees_03", "jafferjees", "Monaco Keyring", 1300, R.drawable.img_jafferjees_03, Category.More, "Black and Tan", 0xFFB0865C, listOf("One Size")),
        piece("p_jafferjees_04", "jafferjees", "Monaco Card Case", 2100, R.drawable.img_jafferjees_04, Category.More, "Black and Red", 0xFFC02A32, listOf("One Size")),
        piece("p_jafferjees_05", "jafferjees", "Elba", 7200, R.drawable.img_jafferjees_05, Category.More, "Black and Red", 0xFFC02A32, listOf("One Size")),
        piece("p_jafferjees_06", "jafferjees", "Skye", 5200, R.drawable.img_jafferjees_06, Category.More, "Black and Red", 0xFFC02A32, listOf("One Size"))
    )

    private fun hub() = listOf(
        piece("p_hub_01", "hub", "Pattern - Brown Dark Brown", 3590, R.drawable.img_hub_01, Category.More, "Dark Brown", 0xFF46301F, listOf("One Size")),
        piece("p_hub_02", "hub", "Sling - Blue", 14990, R.drawable.img_hub_02, Category.More, "Blue", 0xFF3565B0, listOf("One Size")),
        piece("p_hub_03", "hub", "Resonant - Black", 25990, R.drawable.img_hub_03, Category.More, "Black", 0xFF1F1F1F, listOf("One Size")),
        piece("p_hub_04", "hub", "Fetch - Blue", 4990, R.drawable.img_hub_04, Category.More, "Blue", 0xFF3565B0, listOf("One Size")),
        piece("p_hub_05", "hub", "Scribe - Darkbrown", 2490, R.drawable.img_hub_05, Category.More, "Charcoal", 0xFF3F3A3D, listOf("One Size")),
        piece("p_hub_06", "hub", "Bliss - Saddlebrown", 6490, R.drawable.img_hub_06, Category.More, "Brown", 0xFF7F4036, listOf("One Size"))
    )

    private fun astore() = listOf(
        piece("p_astore_01", "astore", "Noir Ease Beige", 1899, R.drawable.img_astore_01, Category.More, "Beige", 0xFFD6C4A6, listOf("One Size")),
        piece("p_astore_02", "astore", "Cot Bag Peach", 799, R.drawable.img_astore_02, Category.More, "Beige", 0xFFDBB6A5, listOf("One Size")),
        piece("p_astore_03", "astore", "Canvas Classic Pouch Beige With Black Strap", 1249, R.drawable.img_astore_03, Category.More, "Beige", 0xFFD6C4A6, listOf("One Size")),
        piece("p_astore_04", "astore", "Small Classic Bag Boski", 2199, R.drawable.img_astore_04, Category.More, "Beige", 0xFFD3C297, listOf("One Size")),
        piece("p_astore_05", "astore", "Noir Ease Chocolate Bag", 1899, R.drawable.img_astore_05, Category.More, "Black", 0xFF34130D, listOf("One Size")),
        piece("p_astore_06", "astore", "Elara Knit Sweater", 3999, R.drawable.img_astore_06, Category.More, "Burgundy", 0xFF6A1830, listOf("Extra small", "Small", "Medium", "Large", "Extra large"))
    )

    private fun esfir() = listOf(
        piece("p_esfir_01", "esfir", "Choti Chandni", 5000, R.drawable.img_esfir_01, Category.Jewellery, "Gold", 0xFFD6A47B, listOf("4-5 years", "5-6 years", "6-7 years", "7-8 years", "9-10 years", "10-11 years", "12-13 years", "Adult")),
        piece("p_esfir_02", "esfir", "Parcham Ear Cuff", 4500, R.drawable.img_esfir_02, Category.Jewellery, "Beige", 0xFFE3C199, listOf("Single", "Pair")),
        piece("p_esfir_03", "esfir", "Tara Studs", 4200, R.drawable.img_esfir_03, Category.Jewellery, "Beige", 0xFFDBB47E, listOf("One Size")),
        piece("p_esfir_04", "esfir", "Regalia Danglers", 19500, R.drawable.img_esfir_04, Category.Jewellery, "Camel", 0xFFB9A171, listOf("One Size")),
        piece("p_esfir_05", "esfir", "Rio", 9500, R.drawable.img_esfir_05, Category.Jewellery, "Rose", 0xFFC86E82, listOf("One Size")),
        piece("p_esfir_06", "esfir", "Ibis Song (Monochrome)", 13000, R.drawable.img_esfir_06, Category.Jewellery, "Beige", 0xFFDEBA86, listOf("One Size"))
    )

    private fun minnieminors() = listOf(
        piece("p_minnieminors_01", "minnieminors", "Capri Tights", 575, R.drawable.img_minnieminors_01, Category.More, "Tea Pink", 0xFFF7C8C6, listOf("9/12-M", "12/18-M", "18/24-M", "24/36-M", "3/4-Y", "4/5-Y", "5/6-Y", "6/7-Y")),
        piece("p_minnieminors_02", "minnieminors", "Pump", 6210, R.drawable.img_minnieminors_02, Category.Footwear, "Black", 0xFF0D0D0E, listOf("27", "28", "29", "30", "31", "32", "33", "34")),
        piece("p_minnieminors_03", "minnieminors", "Sweatshirt", 2232, R.drawable.img_minnieminors_03, Category.More, "Beige", 0xFFCCC3B4, listOf("9/12-M", "12/18-M", "18/24-M", "24/36-M", "3/4-Y", "4-Y", "5-Y", "6-Y")),
        piece("p_minnieminors_04", "minnieminors", "Joggers", 6750, R.drawable.img_minnieminors_04, Category.Footwear, "Cream", 0xFFDEDEE0, listOf("29", "30", "31", "32", "33", "34", "35", "36")),
        piece("p_minnieminors_05", "minnieminors", "Hand Embroidered Kameez & Trousers", 3512, R.drawable.img_minnieminors_05, Category.More, "Red", 0xFFA31047, listOf("0/3-M", "3/6-M", "6/9-M", "9/12-M", "12/18-M", "18/24-M")),
        piece("p_minnieminors_06", "minnieminors", "Formal Kameez & Trousers", 4761, R.drawable.img_minnieminors_06, Category.More, "Black", 0xFF0D1B35, listOf("3-M", "3/6-M", "6/9-M", "9/12-M", "12/18-M", "18/24-M"))
    )

    private fun cocobee() = listOf(
        piece("p_cocobee_01", "cocobee", "Navy Pinstripe Wide-Leg Trousers", 2093, R.drawable.img_cocobee_01, Category.More, "Navy", 0xFF1F2A4D, listOf("3-4Y", "4-5Y", "5-6Y", "6-7Y", "7-8Y", "9-10Y", "11-12Y", "13-14Y")),
        piece("p_cocobee_02", "cocobee", "Mustard Yellow Embroidered Tie-Neck Top", 3913, R.drawable.img_cocobee_02, Category.More, "Mustard", 0xFFD4A42C, listOf("9-12M", "12-18M", "2-3Y", "3-4Y", "4-5Y", "5-6Y")),
        piece("p_cocobee_03", "cocobee", "Black Batman Graphic Zip-Up Hoodie", 3633, R.drawable.img_cocobee_03, Category.More, "Black", 0xFF1F1F1F, listOf("18-24M", "2-3Y", "3-4Y", "4-5Y", "5-6Y", "6-7Y", "7-8Y", "9-10Y")),
        piece("p_cocobee_04", "cocobee", "Brown Pinstripe Formal Blazer", 5593, R.drawable.img_cocobee_04, Category.More, "Brown", 0xFF6E4830, listOf("2-3Y", "3-4Y", "4-5Y", "5-6Y", "6-7Y", "7-8Y", "9-10Y", "11-12Y")),
        piece("p_cocobee_05", "cocobee", "Purple Grace Knitwear", 4473, R.drawable.img_cocobee_05, Category.More, "Purple", 0xFF643282, listOf("0-3M", "3-6M", "6-9M", "9-12M", "12-18M", "18-24M")),
        piece("p_cocobee_06", "cocobee", "Rustic Rose Color Block Sweater", 3493, R.drawable.img_cocobee_06, Category.More, "Rose", 0xFFC06753, listOf("1-2Y", "2-3Y", "3-4Y", "4-5Y", "5-6Y", "6-7Y", "7-8Y", "9-10Y"))
    )

    val products: List<Product> = listOf(
        gulahmed(), alkaram(), nishat(), limelight(), bonanza(), generation(), beechtree(),
        zellbury(), jdot(), crossstitch(), chinyere(), sokamal(), almirah(), lulusar(),
        saya(), edenrobe(), zeen(), shaposh(), gulaal(), batik(), mausummery(),
        ego(), krosskulture(), outfitters(), breakout(), diners(), engine(), furor(),
        one(), baroque(), zarashahjahan(), saniamaskatiya(), asimjofa(), hsy(), aghanoor(),
        afrozeh(), hussainrehar(), republic(), zainabchottani(), sairashakira(), alixeeshan(), zaha(),
        iznik(), jazmin(), qalamkar(), maryumnmaria(), motifz(), suffuse(), mnr(),
        mishalakhani(), nidaazwer(), kanwalmalik(), tenadurrani(), saadiaasad(), akbaraslam(), rangrasiya(),
        uniworth(), cambridge(), royaltag(), monark(), lama(), amiradnan(), ismailfarid(),
        humayunalamgir(), stylo(), bata(), servis(), borjan(), insignia(), hushpuppies(),
        ecs(), ndure(), unze(), clive(), urbansole(), walkeaze(), almas(),
        sputnik(), jafferjees(), hub(), astore(), esfir(), minnieminors(), cocobee()
    ).flatten()
}
