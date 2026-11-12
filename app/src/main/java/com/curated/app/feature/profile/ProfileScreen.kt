package com.curated.app.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.curated.app.core.ui.components.CuratedIcons
import com.curated.app.core.ui.components.GlassSurface
import com.curated.app.core.ui.components.ProfileMenuRow
import com.curated.app.core.ui.components.ScreenHeader
import com.curated.app.core.ui.components.liquid.LiquidBackdropBox
import com.curated.app.core.ui.theme.MauvePrimary
import com.curated.app.core.ui.theme.RoseBg
import com.curated.app.core.ui.theme.RoseBgEnd
import com.curated.app.core.ui.theme.SansUi
import com.curated.app.core.ui.theme.SerifDisplay
import com.curated.app.core.ui.theme.TextPrimary
import com.curated.app.feature.account.ProfileAvatar

@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onEditProfile: () -> Unit,
    onOpenOrders: () -> Unit,
    onOpenWishlist: () -> Unit,
    onOpenOutfits: () -> Unit,
    onOpenAddresses: () -> Unit,
    onOpenPayments: () -> Unit,
    onOpenHelp: () -> Unit,
    onOpenSettings: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val profile by viewModel.profile.collectAsStateWithLifecycle()

    // The page background is the backdrop the glass on this screen refracts.
    LiquidBackdropBox(
        modifier = Modifier.fillMaxSize(),
        background = Modifier.background(Brush.verticalGradient(listOf(RoseBg, RoseBgEnd)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 120.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ScreenHeader(title = "", onBack = onBack)
            ProfileAvatar(profile = profile, size = 96.dp)
            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    profile.name,
                    style = TextStyle(
                        fontFamily = SerifDisplay,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp,
                        color = TextPrimary
                    )
                )
                Spacer(Modifier.width(6.dp))
                Icon(CuratedIcons.Heart, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(18.dp))
            }
            Spacer(Modifier.height(2.dp))
            Text(
                "Edit Profile",
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onEditProfile)
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                style = TextStyle(
                    fontFamily = SansUi,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = MauvePrimary,
                    textDecoration = TextDecoration.Underline
                )
            )
            Spacer(Modifier.height(16.dp))
            GlassSurface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                strong = true
            ) {
                Column {
                    ProfileMenuRow(Icons.Outlined.LocalShipping, "My Orders", onOpenOrders)
                    ProfileMenuRow(Icons.Outlined.FavoriteBorder, "Wishlist", onOpenWishlist)
                    ProfileMenuRow(Icons.Outlined.Checkroom, "My Outfits", onOpenOutfits)
                    ProfileMenuRow(Icons.Outlined.LocationOn, "Addresses", onOpenAddresses)
                    ProfileMenuRow(Icons.Outlined.CreditCard, "Payment Methods", onOpenPayments)
                    ProfileMenuRow(Icons.AutoMirrored.Outlined.HelpOutline, "Help & Support", onOpenHelp)
                    ProfileMenuRow(Icons.Outlined.Settings, "Settings", onOpenSettings)
                }
            }
        }
    }
}
