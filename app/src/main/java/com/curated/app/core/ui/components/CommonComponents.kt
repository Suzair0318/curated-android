package com.curated.app.core.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.unit.sp
import com.curated.app.R
import com.curated.app.core.ui.components.liquid.LiquidBackdrop
import com.curated.app.core.ui.components.liquid.LiquidBackdropBox
import com.curated.app.core.ui.components.liquid.LiquidGlassStyle
import com.curated.app.core.ui.components.liquid.LiquidMergeGroup
import com.curated.app.core.ui.components.liquid.LocalLiquidBackdrop
import com.curated.app.core.ui.components.liquid.liquidGlass
import com.curated.app.core.ui.theme.Badge
import com.curated.app.core.ui.theme.BorderLight
import com.curated.app.core.ui.theme.CreamSurface
import com.curated.app.core.ui.theme.GlassFillStrong
import com.curated.app.core.ui.theme.MauveInk
import com.curated.app.core.ui.theme.MauvePressed
import com.curated.app.core.ui.theme.MauvePrimary
import com.curated.app.core.ui.theme.MauveSoft
import com.curated.app.core.ui.theme.RadiusSm
import com.curated.app.core.ui.theme.RoseAccent
import com.curated.app.core.ui.theme.RoseAccentDeep
import com.curated.app.core.ui.theme.SansUi
import com.curated.app.core.ui.theme.SerifDisplay
import com.curated.app.core.ui.theme.ShadowTint
import com.curated.app.core.ui.theme.TextPrimary
import com.curated.app.core.ui.theme.TextSecondary
import com.curated.app.data.mock.MockCatalog
import com.curated.app.data.model.Category
import com.curated.app.data.model.MainTab
import com.curated.app.data.model.Outfit
import com.curated.app.data.model.Product
import dev.chrisbanes.haze.HazeState

private val PillShape = RoundedCornerShape(50)

@Composable
fun CuratedLogo(
    modifier: Modifier = Modifier,
    height: Dp = 40.dp
) {
    Image(
        painter = painterResource(R.drawable.curated_logo_wordmark),
        contentDescription = "Curated",
        modifier = modifier.height(height),
        contentScale = ContentScale.Fit
    )
}

/** Rose gradient circle with a white arrow; the submit button on glass fields. */
@Composable
fun MauveArrowButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .softShadow(CircleShape, RoseAccentDeep.copy(alpha = 0.4f), blur = 8.dp, offsetY = 3.dp)
            .clip(CircleShape)
            .background(Brush.verticalGradient(listOf(Color(0xFFEDAEBE), Color(0xFFD4849A))))
            .border(1.dp, Color.White.copy(alpha = 0.55f), CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = CuratedIcons.ArrowRight,
            contentDescription = "Go",
            tint = Color.White,
            modifier = Modifier.size(size * 0.5f)
        )
    }
}

@Composable
fun GlassSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = CuratedIcons.Sparkle,
    singleLine: Boolean = true,
    minHeight: Dp = 48.dp,
    italicPlaceholder: Boolean = false,
    strong: Boolean = false
) {
    GlassSurface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        strong = strong,
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(minHeight)
                .padding(start = 16.dp, end = 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                Icon(leadingIcon, contentDescription = null, tint = MauveInk, modifier = Modifier.size(21.dp))
                Spacer(Modifier.width(10.dp))
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = singleLine,
                textStyle = TextStyle(fontFamily = SansUi, fontSize = 13.5.sp, color = TextPrimary),
                cursorBrush = SolidColor(RoseAccentDeep),
                modifier = Modifier.weight(1f),
                decorationBox = { inner ->
                    if (value.isEmpty()) {
                        Text(
                            placeholder,
                            maxLines = if (singleLine) 1 else Int.MAX_VALUE,
                            overflow = TextOverflow.Ellipsis,
                            style = TextStyle(
                                fontFamily = SansUi,
                                fontSize = 13.5.sp,
                                fontStyle = if (italicPlaceholder) FontStyle.Italic else FontStyle.Normal,
                                color = TextSecondary
                            )
                        )
                    }
                    inner()
                }
            )
            Spacer(Modifier.width(8.dp))
            MauveArrowButton(onClick = onSubmit, size = (minHeight - 14.dp).coerceIn(30.dp, 36.dp))
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    onSeeAll: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontFamily = SerifDisplay,
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp,
                color = TextPrimary
            )
        )
        if (onSeeAll != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onSeeAll
                )
            ) {
                Text(
                    text = "See all",
                    style = TextStyle(fontFamily = SansUi, fontSize = 12.sp, color = TextPrimary)
                )
                Spacer(Modifier.width(6.dp))
                Icon(CuratedIcons.ArrowRight, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(15.dp))
            }
        }
    }
}

/** Square glass tile with the icon and label inside, as in the Home mockup. */
@Composable
fun CategoryTile(
    category: Category,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.94f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "catScale"
    )
    GlassSurface(
        modifier = modifier
            .scale(scale)
            .aspectRatio(1f)
            .clickable(interactionSource = interaction, indication = null, onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = 6.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(23.dp))
            Spacer(Modifier.height(3.dp))
            Text(
                category.label,
                maxLines = 1,
                softWrap = false,
                style = TextStyle(fontFamily = SerifDisplay, fontSize = 9.5.sp, color = TextPrimary)
            )
        }
    }
}

/**
 * Round glass bubble carrying a brand's logo, or a text wordmark when [logoRes] is null.
 * The text wordmarks are sized for the small Home bubbles; [wordmarkScale] enlarges them
 * in step when the bubble is drawn bigger. Logos scale with the bubble on their own.
 */
@Composable
fun BrandBubble(
    brandId: String,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    wordmarkScale: Float = 1f,
    @DrawableRes logoRes: Int? = null
) {
    if (logoRes != null) {
        GlassBubble(onClick = onClick, modifier = modifier) {
            // The box is wide and shallow so long wordmarks and squarer marks both stay
            // clear of the bubble's rim.
            Image(
                painter = painterResource(logoRes),
                contentDescription = label.replace('\n', ' '),
                modifier = Modifier
                    .fillMaxWidth(0.76f)
                    .fillMaxHeight(0.4f),
                contentScale = ContentScale.Fit
            )
        }
        return
    }
    val base = brandWordmarkStyle(brandId)
    val style = if (wordmarkScale == 1f) base else base.copy(
        fontSize = base.fontSize * wordmarkScale,
        lineHeight = if (base.lineHeight.isSpecified) base.lineHeight * wordmarkScale else base.lineHeight,
        letterSpacing = base.letterSpacing * wordmarkScale
    )
    GlassBubble(onClick = onClick, modifier = modifier) {
        Text(
            text = if (brandId == "sana") label.replaceFirst(" ", "\n") else label,
            textAlign = TextAlign.Center,
            maxLines = 2,
            softWrap = brandId == "sana",
            style = style
        )
    }
}

@Composable
fun MoreBubble(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassBubble(onClick = onClick, modifier = modifier) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(CuratedIcons.Plus, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(16.dp))
            Text("More", style = TextStyle(fontFamily = SansUi, fontSize = 8.sp, color = TextPrimary))
        }
    }
}

@Composable
private fun GlassBubble(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.94f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "bubbleScale"
    )
    GlassSurface(
        modifier = modifier
            .scale(scale)
            .aspectRatio(1f)
            .clickable(interactionSource = interaction, indication = null, onClick = onClick),
        shape = CircleShape,
        strong = true,
        elevation = 6.dp
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { content() }
    }
}

/** Type treatments that echo each label's own wordmark. */
private fun brandWordmarkStyle(brandId: String): TextStyle = when (brandId) {
    "sana" -> TextStyle(
        fontFamily = SerifDisplay, fontWeight = FontWeight.SemiBold,
        fontSize = 7.sp, lineHeight = 9.sp, letterSpacing = 0.5.sp, color = TextPrimary
    )
    "khaadi" -> TextStyle(
        fontFamily = SansUi, fontWeight = FontWeight.Bold,
        fontSize = 9.sp, letterSpacing = 0.3.sp, color = TextPrimary
    )
    "mariab" -> TextStyle(
        fontFamily = SansUi, fontWeight = FontWeight.Medium,
        fontSize = 7.sp, letterSpacing = 1.1.sp, color = TextPrimary
    )
    "elan" -> TextStyle(
        fontFamily = SerifDisplay, fontWeight = FontWeight.Normal,
        fontSize = 12.sp, letterSpacing = 0.4.sp, color = TextPrimary
    )
    "zara" -> TextStyle(
        fontFamily = SerifDisplay, fontWeight = FontWeight.Bold,
        fontSize = 13.5.sp, letterSpacing = (-0.6).sp, color = TextPrimary
    )
    else -> TextStyle(
        fontFamily = SansUi, fontWeight = FontWeight.SemiBold,
        fontSize = 7.sp, letterSpacing = 0.3.sp, color = TextPrimary
    )
}

@Composable
fun MoodChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bg = if (selected) MauvePrimary else GlassFillStrong
    val fg = if (selected) CreamSurface else TextPrimary
    Box(
        modifier = modifier
            .softShadow(RoundedCornerShape(24.dp), ShadowTint, blur = 4.dp, offsetY = 1.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(bg)
            .border(1.dp, if (selected) MauvePrimary else BorderLight, RoundedCornerShape(24.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Text(label, style = TextStyle(fontFamily = SansUi, fontSize = 13.sp, color = fg))
    }
}

@Composable
fun PrimaryCta(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = CuratedIcons.Bag,
    enabled: Boolean = true
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.98f else 1f, label = "cta")
    Row(
        modifier = modifier
            .scale(scale)
            .fillMaxWidth()
            .height(54.dp)
            .softShadow(RoundedCornerShape(28.dp), ShadowTint, blur = 10.dp, offsetY = 3.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(if (enabled) MauvePrimary else MauveSoft)
            .clickable(enabled = enabled, interactionSource = interaction, indication = null, onClick = onClick),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, tint = CreamSurface, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
        }
        Text(
            text = text,
            style = TextStyle(
                fontFamily = SansUi,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = CreamSurface
            )
        )
    }
}

@Composable
fun SecondaryGlassCta(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassSurface(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(28.dp),
        strong = true
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = text,
                style = TextStyle(
                    fontFamily = SansUi,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = TextPrimary
                )
            )
        }
    }
}

@Composable
fun QtyStepper(
    qty: Int,
    onChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(GlassFillStrong)
            .padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        Icon(
            Icons.Outlined.Remove,
            contentDescription = "Decrease",
            tint = MauvePrimary,
            modifier = Modifier
                .size(28.dp)
                .clickable { onChange(qty - 1) }
                .padding(4.dp)
        )
        Text(
            "$qty",
            modifier = Modifier.padding(horizontal = 8.dp),
            style = TextStyle(fontFamily = SansUi, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        )
        Icon(
            Icons.Outlined.Add,
            contentDescription = "Increase",
            tint = MauvePrimary,
            modifier = Modifier
                .size(28.dp)
                .clickable { onChange(qty + 1) }
                .padding(4.dp)
        )
    }
}

@Composable
fun ColorSwatches(
    colors: List<Pair<String, Color>>,
    selected: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        colors.forEach { (name, color) ->
            val selectedRing = name == selected
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .border(
                        width = if (selectedRing) 2.dp else 0.dp,
                        color = MauvePrimary,
                        shape = CircleShape
                    )
                    .padding(3.dp)
                    .clip(CircleShape)
                    .background(color)
                    .clickable { onSelect(name) }
            )
        }
    }
}

@Composable
fun SizePill(
    size: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassSurface(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(0.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = 4.dp
    ) {
        Text(
            text = "Size: $size \u25BE",
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            style = TextStyle(fontFamily = SansUi, fontSize = 13.sp, color = TextPrimary)
        )
    }
}

@Composable
fun ProfileMenuRow(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = MauvePrimary, modifier = Modifier.size(22.dp))
        Spacer(Modifier.width(14.dp))
        Text(label, modifier = Modifier.weight(1f), style = TextStyle(fontFamily = SansUi, fontSize = 15.sp, color = TextPrimary))
        Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, contentDescription = null, tint = TextSecondary)
    }
}

/**
 * Round water-glass icon button. Buttons that share a [mergeGroup] flow into each other
 * when they sit side by side.
 */
@Composable
fun CircleIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = TextPrimary,
    mergeGroup: LiquidMergeGroup? = null
) {
    WaterLiquidButton(
        onClick = onClick,
        modifier = modifier.size(40.dp),
        shape = CircleShape,
        mergeGroup = mergeGroup,
        style = WaterLiquidButtonDefaults.Frosted
    ) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(20.dp))
    }
}

@Composable
fun ShopTheLookRow(
    product: Product,
    brandName: String,
    onAdd: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FashionImage(
            resId = product.imageRes,
            contentDescription = product.name,
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(RadiusSm))
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                brandName,
                style = TextStyle(
                    fontFamily = SansUi,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = TextPrimary
                )
            )
            Text(product.name, style = TextStyle(fontFamily = SansUi, fontSize = 14.sp, color = TextPrimary))
            Text(
                MockCatalog.formatPkr(product.pricePkr),
                style = TextStyle(fontFamily = SansUi, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = MauvePressed)
            )
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(MauvePrimary)
                .clickable(onClick = onAdd)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text("Add", style = TextStyle(fontFamily = SansUi, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = CreamSurface))
        }
    }
}

/**
 * Look card from the Home mockup: photo on top with a heart and a stack of glass
 * brand chips, then the title, tags and an "Add to Cart" pill on the glass footer.
 */
@Composable
fun ProductLookCard(
    outfit: Outfit,
    onClick: () -> Unit,
    onWishlist: () -> Unit,
    onAddToCart: () -> Unit,
    wishlisted: Boolean,
    modifier: Modifier = Modifier
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.98f else 1f, label = "card")
    GlassSurface(
        modifier = modifier
            .scale(scale)
            .clickable(interactionSource = interaction, indication = null, onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        strong = true,
        elevation = 8.dp
    ) {
        Column {
            // The photo is the backdrop for the heart and the brand chips on top of it.
            LiquidBackdropBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(152.dp),
                backdropContent = {
                    OutfitImage(
                        outfit = outfit,
                        contentDescription = outfit.title,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(36.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onWishlist),
                    contentAlignment = Alignment.Center
                ) {
                    // Frosted dot keeps the heart readable on light and dark photos alike.
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .photoGlass(CircleShape, fallback = SolidColor(Color.White.copy(alpha = 0.62f))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (wishlisted) CuratedIcons.HeartFilled else CuratedIcons.Heart,
                            contentDescription = if (wishlisted) "Remove from wishlist" else "Add to wishlist",
                            tint = if (wishlisted) RoseAccentDeep else TextPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(top = 26.dp, end = 7.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val shown = outfit.brandChips.take(3)
                    shown.forEach { chip -> LookBrandChip(chip) }
                    val extra = outfit.brandChips.size - shown.size
                    if (extra > 0) {
                        LookBrandChip("+$extra more", bold = false)
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 9.dp, end = 7.dp, top = 7.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        outfit.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            fontFamily = SansUi,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 10.5.sp,
                            color = TextPrimary
                        )
                    )
                    Text(
                        outfit.tags.joinToString(" \u2022 "),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(fontFamily = SansUi, fontSize = 7.5.sp, color = TextSecondary)
                    )
                }
                Spacer(Modifier.width(4.dp))
                Row(
                    modifier = Modifier
                        .clip(PillShape)
                        .background(Color.White.copy(alpha = 0.5f))
                        .border(1.dp, TextPrimary.copy(alpha = 0.28f), PillShape)
                        .clickable(onClick = onAddToCart)
                        .padding(horizontal = 7.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(CuratedIcons.Bag, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(11.dp))
                    Spacer(Modifier.width(3.dp))
                    Text(
                        "Add to Cart",
                        maxLines = 1,
                        style = TextStyle(
                            fontFamily = SansUi,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 8.5.sp,
                            color = TextPrimary
                        )
                    )
                }
            }
        }
    }
}

/**
 * Product tile for listings, in the look card's language: photo with a wishlist heart,
 * then brand, name and price. Size and colour are picked on the product page, so there
 * is no add-to-cart here.
 */
@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit,
    onWishlist: () -> Unit,
    wishlisted: Boolean,
    modifier: Modifier = Modifier
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.98f else 1f, label = "productCard")
    GlassSurface(
        modifier = modifier
            .scale(scale)
            .clickable(interactionSource = interaction, indication = null, onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        strong = true,
        elevation = 8.dp
    ) {
        Column {
            // The photo is the backdrop for the heart on top of it.
            LiquidBackdropBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.85f),
                backdropContent = {
                    FashionImage(
                        resId = product.imageRes,
                        contentDescription = product.name,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(36.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onWishlist),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .photoGlass(CircleShape, fallback = SolidColor(Color.White.copy(alpha = 0.62f))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (wishlisted) CuratedIcons.HeartFilled else CuratedIcons.Heart,
                            contentDescription = if (wishlisted) "Remove from wishlist" else "Add to wishlist",
                            tint = if (wishlisted) RoseAccentDeep else TextPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 10.dp)
            ) {
                Text(
                    MockCatalog.brandName(product.brandId),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontFamily = SansUi,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp,
                        letterSpacing = 0.4.sp,
                        color = TextSecondary
                    )
                )
                Text(
                    product.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontFamily = SansUi,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.5.sp,
                        color = TextPrimary
                    )
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    MockCatalog.formatPkr(product.pricePkr),
                    style = TextStyle(
                        fontFamily = SansUi,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = MauvePressed
                    )
                )
            }
        }
    }
}

/** Milky glass for small labels on photos: enough tint and blur to keep 6sp text readable. */
private val PhotoGlassStyle = LiquidGlassStyle(
    tint = Color.White.copy(alpha = 0.56f),
    blur = 2.dp,
    dispersion = 0.1f,
    glow = 0.5f
)

/**
 * Liquid glass over the photo behind it when there is a backdrop; otherwise the flat
 * translucent fill these chips had before.
 */
@Composable
private fun Modifier.photoGlass(
    shape: Shape,
    fallback: Brush,
    fallbackBorder: Color? = null
): Modifier {
    val backdrop = LocalLiquidBackdrop.current
    return when {
        backdrop != null -> liquidGlass(backdrop, shape, PhotoGlassStyle)
        fallbackBorder != null -> background(fallback).border(1.dp, fallbackBorder, shape)
        else -> background(fallback)
    }
}

@Composable
private fun LookBrandChip(label: String, bold: Boolean = true) {
    val shape = RoundedCornerShape(7.dp)
    Box(
        modifier = Modifier
            .width(46.dp)
            .clip(shape)
            .photoGlass(
                shape,
                fallback = Brush.verticalGradient(
                    listOf(Color.White.copy(alpha = 0.82f), Color.White.copy(alpha = 0.62f))
                ),
                fallbackBorder = Color.White.copy(alpha = 0.9f)
            )
            .padding(horizontal = 3.dp, vertical = if (bold) 5.dp else 3.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            label,
            textAlign = TextAlign.Center,
            maxLines = 2,
            style = TextStyle(
                fontFamily = SansUi,
                fontWeight = if (bold) FontWeight.Bold else FontWeight.Medium,
                fontSize = 6.sp,
                lineHeight = 7.5.sp,
                letterSpacing = 0.2.sp,
                color = TextPrimary
            )
        )
    }
}

/** Satin hero banner: copy on the left, stylist photo melting in from the right. */
@Composable
fun AiHeroCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(22.dp)
    // Satin and photo together are the backdrop the water button bends.
    LiquidBackdropBox(
        modifier = modifier
            .fillMaxWidth()
            .height(158.dp)
            .softShadow(shape, RoseAccentDeep.copy(alpha = 0.3f), blur = 14.dp, offsetY = 5.dp)
            .clip(shape)
            .border(1.dp, Color.White.copy(alpha = 0.6f), shape)
            .clickable(onClick = onClick),
        background = Modifier.satinBackground(intense = true),
        backdropContent = {
            Image(
                painter = painterResource(R.drawable.img_hero_stylist),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
                    .fillMaxWidth(0.56f)
                    .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                    .drawWithContent {
                        drawContent()
                        // Warm the photo towards the satin, then fade its left edge out.
                        drawRect(Color(0xFFE9B0BD).copy(alpha = 0.16f), blendMode = BlendMode.SrcAtop)
                        drawRect(
                            Brush.horizontalGradient(0f to Color.Transparent, 0.42f to Color.Black),
                            blendMode = BlendMode.DstIn
                        )
                    }
            )
        }
    ) {
        Icon(
            CuratedIcons.SparkleFilled,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.85f),
            modifier = Modifier
                .padding(start = 14.dp, top = 34.dp)
                .size(13.dp)
        )
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 34.dp)
        ) {
            Text(
                "Your Personal\nAI Stylist",
                style = TextStyle(
                    fontFamily = SerifDisplay,
                    fontWeight = FontWeight.Normal,
                    fontSize = 23.sp,
                    lineHeight = 27.sp,
                    color = MauveInk
                )
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "Dream it. We'll style it.",
                style = TextStyle(fontFamily = SansUi, fontSize = 11.5.sp, color = MauveInk)
            )
            Spacer(Modifier.height(12.dp))
            WaterLiquidButton(
                onClick = onClick,
                shape = PillShape,
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 9.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Try the AI Stylist",
                        style = TextStyle(
                            fontFamily = SansUi,
                            fontWeight = FontWeight.Medium,
                            fontSize = 10.5.sp,
                            color = TextPrimary
                        )
                    )
                    Spacer(Modifier.width(6.dp))
                    Icon(CuratedIcons.ArrowRight, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(13.dp))
                }
            }
        }
    }
}

/**
 * Floating glass tab bar. Give it the [backdrop] of the content behind it so that
 * content bends and softens as it scrolls underneath; [hazeState] is the blur-only
 * fallback below Android 13. [selected] is null on screens that sit outside the tabs.
 */
@Composable
fun CuratedBottomBar(
    selected: MainTab?,
    wishlistBadge: Int,
    onSelect: (MainTab) -> Unit,
    modifier: Modifier = Modifier,
    hazeState: HazeState? = null,
    backdrop: LiquidBackdrop? = null
) {
    GlassSurface(
        modifier = modifier
            .navigationBarsPadding()
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .fillMaxWidth()
            .height(64.dp),
        shape = RoundedCornerShape(26.dp),
        strong = true,
        elevation = 14.dp,
        hazeState = hazeState,
        backdrop = backdrop,
        // Just enough blur to keep the labels readable; more would hide the refraction.
        backdropBlur = if (backdrop != null) 5.dp else 0.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MainTab.entries.forEach { tab ->
                val active = tab == selected
                val icon = when (tab) {
                    MainTab.Home -> if (active) CuratedIcons.HomeFilled else CuratedIcons.Home
                    MainTab.Ai -> CuratedIcons.Sparkle
                    MainTab.Outfits -> CuratedIcons.Hanger
                    MainTab.Wishlist -> if (active) CuratedIcons.HeartFilled else CuratedIcons.Heart
                    MainTab.Profile -> CuratedIcons.Person
                }
                val tint = if (active) RoseAccentDeep else TextPrimary
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onSelect(tab) }
                ) {
                    Box {
                        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(24.dp))
                        if (tab == MainTab.Wishlist && wishlistBadge > 0) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(14.dp)
                                    .clip(CircleShape)
                                    .background(Badge),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "$wishlistBadge",
                                    style = TextStyle(fontSize = 8.sp, color = CreamSurface, fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(3.dp))
                    Text(
                        tab.label,
                        maxLines = 1,
                        style = TextStyle(
                            fontFamily = SansUi,
                            fontSize = 10.sp,
                            color = tint,
                            fontWeight = if (active) FontWeight.SemiBold else FontWeight.Normal
                        )
                    )
                    Spacer(Modifier.height(4.dp))
                    Box(
                        Modifier
                            .width(26.dp)
                            .height(2.5.dp)
                            .clip(PillShape)
                            .background(if (active) RoseAccent else Color.Transparent)
                    )
                }
            }
        }
    }
}

@Composable
fun BackCircleButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    CircleIconButton(
        icon = Icons.AutoMirrored.Outlined.ArrowBack,
        onClick = onClick,
        modifier = modifier
    )
}

fun categoryIcon(category: Category): ImageVector = when (category) {
    Category.Women -> CuratedIcons.Hanger
    Category.Footwear -> CuratedIcons.Sneaker
    Category.Jewellery -> CuratedIcons.Diamond
    Category.Designers -> CuratedIcons.Mannequin
    Category.Local -> CuratedIcons.Bag
    Category.More -> CuratedIcons.Grid
}
