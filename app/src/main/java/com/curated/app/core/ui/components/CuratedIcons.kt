package com.curated.app.core.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.dp

/**
 * Thin-line icon set drawn to match the Curated mockups (Material's outlined icons are
 * too heavy for this look). All icons use a 24x24 viewport and take their colour from
 * the `tint` passed to `Icon`.
 */
object CuratedIcons {
    private const val SPARKLE =
        "M12 3 Q13.2 10.8 21 12 Q13.2 13.2 12 21 Q10.8 13.2 3 12 Q10.8 10.8 12 3 Z"
    private const val HEART =
        "M12 20 C12 20 3.6 15.2 3.6 9.2 C3.6 6.6 5.6 4.6 8.1 4.6 C9.7 4.6 11.2 5.5 12 6.8 " +
            "C12.8 5.5 14.3 4.6 15.9 4.6 C18.4 4.6 20.4 6.6 20.4 9.2 C20.4 15.2 12 20 12 20 Z"
    private const val HOME_BODY =
        "M3.8 10.6 L12 3.8 L20.2 10.6 V19.2 C20.2 19.8 19.8 20.2 19.2 20.2 H4.8 " +
            "C4.2 20.2 3.8 19.8 3.8 19.2 Z"

    val Sparkle: ImageVector by lazy { stroke("Sparkle", SPARKLE) }
    val SparkleFilled: ImageVector by lazy { fill("SparkleFilled", SPARKLE) }

    val Heart: ImageVector by lazy { stroke("Heart", HEART) }
    val HeartFilled: ImageVector by lazy { fill("HeartFilled", HEART) }

    val Home: ImageVector by lazy { stroke("Home", HOME_BODY, "M9.8 20.2 V14.4 H14.2 V20.2") }
    val HomeFilled: ImageVector by lazy {
        fill("HomeFilled", "$HOME_BODY M10 20.2 V14.6 H14 V20.2 Z", PathFillType.EvenOdd)
    }

    val Hanger: ImageVector by lazy {
        stroke(
            "Hanger",
            "M12 9 V8.2 C12 7.4 12.6 7 13.2 6.6 C13.9 6.1 14.3 5.5 14.3 4.8 " +
                "C14.3 3.5 13.3 2.6 12 2.6 C10.8 2.6 9.8 3.5 9.7 4.7",
            "M12 9 L3.3 15.4 C2.3 16.2 2.8 17.8 4.1 17.8 H19.9 C21.2 17.8 21.7 16.2 20.7 15.4 Z"
        )
    }

    val Sneaker: ImageVector by lazy {
        stroke(
            "Sneaker",
            "M2.6 16.2 V11.2 C3.8 11.9 5.3 11.9 6.4 11.2 C7.6 10.4 8.3 9 8.9 7.6 L10.9 8.5 " +
                "C11.8 10.6 13.6 12 15.9 12.6 L18.9 13.4 C20.4 13.8 21.4 14.9 21.4 16.2 Z",
            "M2.6 16.2 V17.6 C2.6 18.2 3 18.6 3.6 18.6 H20.4 C21 18.6 21.4 18.2 21.4 17.6 V16.2",
            "M10.6 11 L12.3 10 M12.4 12.4 L14.1 11.4"
        )
    }

    val Diamond: ImageVector by lazy {
        stroke(
            "Diamond",
            "M7 4.2 H17 L21.3 9.6 L12 20.2 L2.7 9.6 Z",
            "M2.7 9.6 H21.3",
            "M9.2 4.2 L8 9.6 L12 20.2 L16 9.6 L14.8 4.2"
        )
    }

    val Mannequin: ImageVector by lazy {
        stroke(
            "Mannequin",
            "M10.6 2.8 H13.4 V4.4 C13.4 5.6 15.2 6.2 16.2 7.4 C17.4 8.9 15.6 10.6 15.6 12.4 " +
                "C15.6 13.9 16.6 15 16.6 16.6 H7.4 C7.4 15 8.4 13.9 8.4 12.4 " +
                "C8.4 10.6 6.6 8.9 7.8 7.4 C8.8 6.2 10.6 5.6 10.6 4.4 Z",
            "M12 16.6 V20.8",
            "M8.8 21.2 H15.2"
        )
    }

    val Bag: ImageVector by lazy {
        stroke(
            "Bag",
            "M5.2 8.4 H18.8 L19.8 20.2 H4.2 Z",
            "M9 8.4 V7.2 C9 5.4 10.3 4 12 4 C13.7 4 15 5.4 15 7.2 V8.4"
        )
    }

    val Grid: ImageVector by lazy {
        stroke(
            "Grid",
            "M4.2 4.2 H10.2 V10.2 H4.2 Z",
            "M13.8 4.2 H19.8 V10.2 H13.8 Z",
            "M4.2 13.8 H10.2 V19.8 H4.2 Z",
            "M13.8 13.8 H19.8 V19.8 H13.8 Z"
        )
    }

    val Person: ImageVector by lazy {
        stroke(
            "Person",
            "M12 11.4 C14.2 11.4 16 9.6 16 7.4 C16 5.2 14.2 3.4 12 3.4 C9.8 3.4 8 5.2 8 7.4 " +
                "C8 9.6 9.8 11.4 12 11.4 Z",
            "M4.4 20.4 C4.4 16.9 7.8 14.6 12 14.6 C16.2 14.6 19.6 16.9 19.6 20.4 Z"
        )
    }

    val ArrowRight: ImageVector by lazy {
        stroke("ArrowRight", "M4.5 12 H19.5", "M13.8 6.3 L19.5 12 L13.8 17.7", width = 1.7f)
    }

    val Plus: ImageVector by lazy { stroke("Plus", "M12 5.5 V18.5", "M5.5 12 H18.5") }

    private fun builder(name: String) = ImageVector.Builder(
        name = name,
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    )

    private fun stroke(name: String, vararg paths: String, width: Float = 1.4f): ImageVector =
        builder(name).apply {
            paths.forEach { data ->
                addPath(
                    pathData = addPathNodes(data),
                    stroke = SolidColor(Color.Black),
                    strokeLineWidth = width,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round
                )
            }
        }.build()

    private fun fill(
        name: String,
        path: String,
        fillType: PathFillType = PathFillType.NonZero
    ): ImageVector =
        builder(name).apply {
            addPath(
                pathData = addPathNodes(path),
                fill = SolidColor(Color.Black),
                pathFillType = fillType
            )
        }.build()
}
