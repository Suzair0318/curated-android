package com.curated.app.core.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.curated.app.data.model.Outfit
import java.io.File

/** Fashion shots keep the subject's head near the top, so crops favour the upper part. */
val FashionCropAlignment: Alignment = BiasAlignment(0f, -0.55f)

/** Cropped photo for look and product imagery (bundled JPGs in `drawable-nodpi`). */
@Composable
fun FashionImage(
    @DrawableRes resId: Int,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    alignment: Alignment = FashionCropAlignment
) {
    Image(
        painter = painterResource(resId),
        contentDescription = contentDescription,
        modifier = modifier,
        alignment = alignment,
        contentScale = ContentScale.Crop
    )
}

/**
 * A look's picture: the image the AI Stylist made for it when there is one, otherwise
 * its bundled photo (for an AI look without an image yet, that is its main piece).
 */
@Composable
fun OutfitImage(
    outfit: Outfit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    alignment: Alignment = FashionCropAlignment
) {
    val path = outfit.heroImagePath
    if (path == null) {
        FashionImage(outfit.heroImageRes, modifier, contentDescription, alignment)
    } else {
        AsyncImage(
            model = File(path),
            contentDescription = contentDescription,
            modifier = modifier,
            alignment = alignment,
            contentScale = ContentScale.Crop
        )
    }
}
