package com.curated.app.feature.look

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddAPhoto
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.curated.app.core.ui.components.CircleIconButton
import com.curated.app.core.ui.components.GlassSurface
import com.curated.app.core.ui.components.OutfitImage
import com.curated.app.core.ui.components.PrimaryCta
import com.curated.app.core.ui.components.ScreenHeader
import com.curated.app.core.ui.components.ShopTheLookRow
import com.curated.app.core.ui.components.liquid.LiquidBackdropBox
import com.curated.app.core.ui.components.shareText
import com.curated.app.core.ui.theme.CreamSurface
import com.curated.app.core.ui.theme.RoseBg
import com.curated.app.core.ui.theme.RoseBgEnd
import com.curated.app.core.ui.theme.SansUi
import com.curated.app.core.ui.theme.ScriptAccent
import com.curated.app.core.ui.theme.SerifDisplay
import com.curated.app.core.ui.theme.TextPrimary
import com.curated.app.core.ui.theme.TextSecondary
import com.curated.app.data.ai.LookSubject
import com.curated.app.data.mock.MockCatalog

@Composable
fun CuratedLookScreen(
    outfitId: String,
    onBack: () -> Unit,
    onOpenPdp: (String) -> Unit,
    onOpenCart: () -> Unit,
    viewModel: CuratedLookViewModel = hiltViewModel()
) {
    LaunchedEffect(outfitId) { viewModel.load(outfitId) }
    val state by viewModel.ui.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var tryOnUri by remember { mutableStateOf<android.net.Uri?>(null) }
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> tryOnUri = uri }

    // The page background is the backdrop the glass on this screen refracts.
    LiquidBackdropBox(
        modifier = Modifier.fillMaxSize(),
        background = Modifier.background(Brush.verticalGradient(listOf(RoseBg, RoseBgEnd)))
    ) {
        val outfit = state.outfit
        if (outfit == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
            ) {
                ScreenHeader(title = "Your Curated Look", onBack = onBack)
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Look not found", color = TextPrimary)
                }
            }
            return@LiquidBackdropBox
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Title bar above the photo, as in the mockup.
            ScreenHeader(
                title = "Your Curated Look",
                onBack = onBack,
                trailing = {
                    CircleIconButton(
                        icon = Icons.Outlined.Share,
                        onClick = {
                            val pieces = state.products.joinToString("\n") { product ->
                                "\u2022 ${MockCatalog.brandName(product.brandId)} ${product.name}, " +
                                    MockCatalog.formatPkr(product.pricePkr)
                            }
                            context.shareText("${outfit.title}, a look from Curated:\n$pieces")
                        }
                    )
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .navigationBarsPadding()
            ) {
                // The hero photo is the backdrop the water button on top of it bends.
                // An AI look's picture is a full-length portrait, so its frame keeps that shape.
                LiquidBackdropBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(
                            if (outfit.isAiLook) Modifier.aspectRatio(AI_LOOK_ASPECT_RATIO)
                            else Modifier.height(400.dp)
                        ),
                    backdropContent = {
                        if (tryOnUri != null) {
                            AsyncImage(
                                model = tryOnUri,
                                contentDescription = "Try-on preview",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            OutfitImage(
                                outfit = outfit,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        // Darkens the foot of the photo so the page dots stay visible.
                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        0.7f to Color.Transparent,
                                        1f to Color(0x66000000)
                                    )
                                )
                        )
                    }
                ) {
                    // The shadow keeps the script readable on light and dark photos alike.
                    Text(
                        "Just for you",
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 14.dp, end = 18.dp),
                        style = TextStyle(
                            fontFamily = ScriptAccent,
                            fontSize = 26.sp,
                            color = CreamSurface,
                            shadow = Shadow(Color(0x99000000), Offset(0f, 2f), blurRadius = 8f)
                        )
                    )
                    if (tryOnUri != null) {
                        Text(
                            "Try-on preview (demo)",
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp),
                            style = TextStyle(fontFamily = SansUi, fontSize = 11.sp, color = CreamSurface)
                        )
                    } else if (outfit.isAiLook) {
                        when (val image = state.image) {
                            LookImage.Ready -> Text(
                                "AI preview \u2022 details may differ",
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(16.dp),
                                style = TextStyle(
                                    fontFamily = SansUi,
                                    fontSize = 11.sp,
                                    color = CreamSurface,
                                    shadow = Shadow(Color(0x99000000), Offset(0f, 1f), blurRadius = 6f)
                                )
                            )
                            LookImage.Generating -> LookImageNotice(
                                text = if (state.subject == LookSubject.Model) {
                                    "Dressing the model\u2026"
                                } else {
                                    "Dressing the mannequin\u2026"
                                },
                                detail = "This takes about 15 seconds. The pieces are ready below.",
                                working = true
                            )
                            is LookImage.Missing -> LookImageNotice(
                                text = image.message ?: "The look picture is not ready yet.",
                                detail = null,
                                working = false,
                                action = if (image.message == null) "Create look picture" else "Try again",
                                onAction = viewModel::createImage
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 14.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(3) { i ->
                            Box(
                                Modifier
                                    .size(if (i == 0) 8.dp else 6.dp)
                                    .clip(CircleShape)
                                    .background(if (i == 0) CreamSurface else CreamSurface.copy(alpha = 0.4f))
                            )
                        }
                    }
                    // Not in the mockup: picks a photo of yours for the demo try-on preview.
                    CircleIconButton(
                        icon = Icons.Outlined.AddAPhoto,
                        onClick = { photoPicker.launch("image/*") },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(14.dp)
                    )
                }

                GlassSurface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 12.dp),
                    shape = RoundedCornerShape(24.dp),
                    strong = true
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Shop the Look",
                            style = TextStyle(
                                fontFamily = SerifDisplay,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 20.sp,
                                color = TextPrimary
                            )
                        )
                        if (outfit.isAiLook) {
                            Spacer(Modifier.height(6.dp))
                            Text(
                                outfit.title,
                                style = TextStyle(
                                    fontFamily = SansUi,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp,
                                    color = TextPrimary
                                )
                            )
                            if (outfit.note.isNotBlank()) {
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    outfit.note,
                                    style = TextStyle(
                                        fontFamily = SansUi,
                                        fontSize = 12.5.sp,
                                        lineHeight = 18.sp,
                                        color = TextSecondary
                                    )
                                )
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        state.products.forEach { product ->
                            androidx.compose.animation.AnimatedVisibility(
                                visible = true,
                                enter = fadeIn(tween(300)) + slideInVertically { it / 4 }
                            ) {
                                ShopTheLookRow(
                                    product = product,
                                    brandName = MockCatalog.brandName(product.brandId),
                                    onAdd = { viewModel.addProduct(product.id) },
                                    onClick = { onOpenPdp(product.id) }
                                )
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                        PrimaryCta(
                            text = "Add Entire Outfit to Cart",
                            onClick = {
                                viewModel.addEntireOutfit()
                                onOpenCart()
                            }
                        )
                    }
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

/** Width to height of the pictures the image model makes for AI looks (768 x 1024). */
private const val AI_LOOK_ASPECT_RATIO = 0.75f

/**
 * Shown over the cover of an AI look while its picture is being made, or when it could
 * not be made; the cover underneath is dimmed so the words stay readable.
 */
@Composable
private fun LookImageNotice(
    text: String,
    detail: String?,
    working: Boolean,
    action: String? = null,
    onAction: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x73000000)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (working) {
                CircularProgressIndicator(
                    color = CreamSurface,
                    strokeWidth = 2.5.dp,
                    modifier = Modifier.size(30.dp)
                )
                Spacer(Modifier.height(14.dp))
            }
            Text(
                text,
                textAlign = TextAlign.Center,
                style = TextStyle(fontFamily = SerifDisplay, fontSize = 18.sp, lineHeight = 24.sp, color = CreamSurface)
            )
            if (detail != null) {
                Spacer(Modifier.height(6.dp))
                Text(
                    detail,
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontFamily = SansUi, fontSize = 12.sp, lineHeight = 17.sp, color = CreamSurface)
                )
            }
            if (action != null) {
                Spacer(Modifier.height(16.dp))
                Text(
                    action,
                    modifier = Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .background(CreamSurface)
                        .clickable(onClick = onAction)
                        .padding(horizontal = 20.dp, vertical = 11.dp),
                    style = TextStyle(
                        fontFamily = SansUi,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = TextPrimary
                    )
                )
            }
        }
    }
}
