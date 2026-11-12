package com.curated.app.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.curated.app.core.ui.components.AiHeroCard
import com.curated.app.core.ui.components.BrandBubble
import com.curated.app.core.ui.components.CategoryTile
import com.curated.app.core.ui.components.CuratedIcons
import com.curated.app.core.ui.components.GlassSearchField
import com.curated.app.core.ui.components.GlassSurface
import com.curated.app.core.ui.components.MoreBubble
import com.curated.app.core.ui.components.ProductLookCard
import com.curated.app.core.ui.components.SectionHeader
import com.curated.app.core.ui.components.categoryIcon
import com.curated.app.core.ui.components.liquid.LiquidBackdropBox
import com.curated.app.core.ui.components.liquid.liquidBackdropSource
import com.curated.app.core.ui.components.liquid.rememberLiquidBackdrop
import com.curated.app.core.ui.components.satinBackground
import com.curated.app.core.ui.theme.RoseAccent
import com.curated.app.core.ui.theme.SansUi
import com.curated.app.core.ui.theme.SerifDisplay
import com.curated.app.core.ui.theme.TextPrimary
import com.curated.app.core.ui.theme.TextSecondary
import com.curated.app.data.model.Category

private val ScreenPadding = 18.dp
private const val HOME_BRAND_COUNT = 5

@Composable
fun HomeScreen(
    onOpenAi: (prompt: String) -> Unit,
    onOpenNotifications: () -> Unit,
    onOpenCart: () -> Unit,
    onOpenAllLooks: () -> Unit,
    onOpenCategory: (Category) -> Unit,
    onOpenLook: (String) -> Unit,
    onOpenPdp: (String) -> Unit,
    onOpenBrand: (String) -> Unit,
    onOpenBrands: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.ui.collectAsStateWithLifecycle()
    val wishlistIds by viewModel.wishlistIds.collectAsStateWithLifecycle()
    val account by viewModel.account.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()
    // What the top bar refracts: everything on this screen underneath it. The bar is a
    // sibling of that content, never part of it.
    val topBarBackdrop = rememberLiquidBackdrop()

    Box(modifier = Modifier.fillMaxSize()) {
        // The satin is the backdrop every glass tile and card on this screen refracts.
        LiquidBackdropBox(
            modifier = Modifier
                .fillMaxSize()
                .liquidBackdropSource(topBarBackdrop),
            background = Modifier.satinBackground()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .statusBarsPadding()
                    .padding(top = HomeTopBarSpace, bottom = 96.dp)
                    .navigationBarsPadding()
            ) {
                GlassSearchField(
                    value = state.search,
                    onValueChange = viewModel::onSearchChange,
                    placeholder = "Tell us what you're looking for...",
                    onSubmit = { onOpenAi(state.search) },
                    modifier = Modifier.padding(horizontal = ScreenPadding)
                )

                Spacer(Modifier.height(16.dp))
                AiHeroCard(
                    onClick = { onOpenAi("") },
                    modifier = Modifier.padding(horizontal = ScreenPadding)
                )

                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ScreenPadding),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    state.categories.forEach { cat ->
                        CategoryTile(
                            category = cat,
                            icon = categoryIcon(cat),
                            onClick = { onOpenCategory(cat) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(Modifier.height(14.dp))
                CreateOutfitCard(
                    prompt = state.aiPrompt,
                    onPromptChange = viewModel::onAiPromptChange,
                    onSubmit = {
                        onOpenAi(state.aiPrompt.ifBlank { "a chic dinner look with jewellery" })
                    },
                    modifier = Modifier.padding(horizontal = ScreenPadding)
                )

                Spacer(Modifier.height(18.dp))
                SectionHeader(title = "Shop by Brand", onSeeAll = onOpenBrands)
                Spacer(Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ScreenPadding),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    state.brands.take(HOME_BRAND_COUNT).forEach { brand ->
                        BrandBubble(
                            brandId = brand.id,
                            label = brand.shortLabel,
                            onClick = { onOpenBrand(brand.id) },
                            modifier = Modifier.weight(1f),
                            logoRes = brand.logoRes
                        )
                    }
                    MoreBubble(onClick = onOpenBrands, modifier = Modifier.weight(1f))
                }

                Spacer(Modifier.height(18.dp))
                SectionHeader(title = "Curated for You", onSeeAll = onOpenAllLooks)
                Spacer(Modifier.height(10.dp))

                // Two-column rows built by hand: a lazy grid cannot nest in this scroll.
                state.outfits.chunked(2).forEach { rowItems ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = ScreenPadding, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowItems.forEach { outfit ->
                            ProductLookCard(
                                outfit = outfit,
                                onClick = { onOpenLook(outfit.id) },
                                onWishlist = { viewModel.toggleWishlist(outfit.id) },
                                onAddToCart = { viewModel.addOutfitToCart(outfit.id) },
                                wishlisted = outfit.id in wishlistIds,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (rowItems.size == 1) {
                            Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }

            // The content now scrolls up behind the status bar; fade it out there.
            HomeTopFade(background = Modifier.satinBackground())
        }

        // Greeting on the left, logo centred, actions on the right, as in the mockup.
        // The bag is not in the mockup; it stays because it is the way to the cart.
        HomeTopBar(
            scrollState = scrollState,
            backdrop = topBarBackdrop,
            firstName = account.profile.firstName,
            hasUnreadNotifications = account.unreadNotifications > 0,
            onOpenNotifications = onOpenNotifications,
            onOpenCart = onOpenCart
        )
    }
}

@Composable
private fun CreateOutfitCard(
    prompt: String,
    onPromptChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassSurface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(modifier = Modifier.padding(start = 14.dp, end = 14.dp, top = 12.dp, bottom = 14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    CuratedIcons.SparkleFilled,
                    contentDescription = null,
                    tint = RoseAccent,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Create Your Outfit with AI",
                        style = TextStyle(
                            fontFamily = SerifDisplay,
                            fontWeight = FontWeight.Normal,
                            fontSize = 17.sp,
                            color = TextPrimary
                        )
                    )
                    Text(
                        "Describe your vibe \u2014 elegant, casual, desi, minimal...",
                        style = TextStyle(fontFamily = SansUi, fontSize = 10.5.sp, color = TextSecondary)
                    )
                }
                Box(modifier = Modifier.size(width = 30.dp, height = 28.dp)) {
                    Icon(
                        CuratedIcons.SparkleFilled,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .size(20.dp)
                    )
                    Icon(
                        CuratedIcons.SparkleFilled,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(10.dp)
                    )
                }
            }
            Spacer(Modifier.height(10.dp))
            GlassSearchField(
                value = prompt,
                onValueChange = onPromptChange,
                placeholder = "e.g. a chic dinner look with jewellery",
                onSubmit = onSubmit,
                leadingIcon = null,
                minHeight = 44.dp,
                italicPlaceholder = true,
                strong = true
            )
        }
    }
}
