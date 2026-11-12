package com.curated.app.feature.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.curated.app.core.ui.components.GlassSurface
import com.curated.app.core.ui.components.ScreenHeader
import com.curated.app.core.ui.components.liquid.LiquidBackdropBox
import com.curated.app.core.ui.theme.MauvePrimary
import com.curated.app.core.ui.theme.RoseAccentDeep
import com.curated.app.core.ui.theme.RoseBg
import com.curated.app.core.ui.theme.RoseBgEnd
import com.curated.app.core.ui.theme.SansUi
import com.curated.app.core.ui.theme.SerifDisplay
import com.curated.app.core.ui.theme.TextPrimary
import com.curated.app.core.ui.theme.TextSecondary
import com.curated.app.data.model.UserProfile
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Frame shared by the account and checkout pages: header, a scrolling column of cards
 * and an optional button pinned under it.
 */
@Composable
fun AccountPage(
    title: String,
    onBack: () -> Unit,
    footer: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    // The page background is the backdrop the glass on this screen refracts.
    LiquidBackdropBox(
        modifier = Modifier.fillMaxSize(),
        background = Modifier.background(Brush.verticalGradient(listOf(RoseBg, RoseBgEnd)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
        ) {
            ScreenHeader(title = title, onBack = onBack)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                content = content
            )
            if (footer != null) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp)
                ) { footer() }
            }
        }
    }
}

@Composable
fun SectionCard(
    modifier: Modifier = Modifier,
    title: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    GlassSurface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        strong = true
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (title != null) {
                Text(
                    title,
                    style = TextStyle(
                        fontFamily = SerifDisplay,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 17.sp,
                        color = TextPrimary
                    )
                )
                Spacer(Modifier.height(10.dp))
            }
            content()
        }
    }
}

@Composable
fun LabeledField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    error: String? = null
) {
    val shape = RoundedCornerShape(14.dp)
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            label,
            style = TextStyle(fontFamily = SansUi, fontWeight = FontWeight.Medium, fontSize = 12.sp, color = TextSecondary)
        )
        Spacer(Modifier.height(5.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = TextStyle(fontFamily = SansUi, fontSize = 15.sp, color = TextPrimary),
            cursorBrush = SolidColor(RoseAccentDeep),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, capitalization = capitalization),
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { inner ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape)
                        .background(Color.White.copy(alpha = 0.62f))
                        .border(1.dp, if (error != null) RoseAccentDeep else Color.White.copy(alpha = 0.9f), shape)
                        .padding(horizontal = 14.dp, vertical = 13.dp)
                ) {
                    if (value.isEmpty() && placeholder.isNotEmpty()) {
                        Text(
                            placeholder,
                            maxLines = 1,
                            style = TextStyle(fontFamily = SansUi, fontSize = 15.sp, color = TextSecondary.copy(alpha = 0.7f))
                        )
                    }
                    inner()
                }
            }
        )
        if (error != null) {
            Spacer(Modifier.height(4.dp))
            Text(error, style = TextStyle(fontFamily = SansUi, fontSize = 11.sp, color = RoseAccentDeep))
        }
    }
}

/** A choice in a list where one item is picked: ring on the left, text, optional action. */
@Composable
fun ChoiceRow(
    selected: Boolean,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    trailing: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .border(1.5.dp, if (selected) MauvePrimary else TextSecondary.copy(alpha = 0.6f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (selected) {
                Box(
                    Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(MauvePrimary)
                )
            }
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                style = TextStyle(fontFamily = SansUi, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = TextPrimary)
            )
            if (!subtitle.isNullOrBlank()) {
                Text(subtitle, style = TextStyle(fontFamily = SansUi, fontSize = 12.sp, color = TextSecondary))
            }
        }
        if (trailing != null) {
            Spacer(Modifier.width(8.dp))
            trailing()
        }
    }
}

@Composable
fun TextLink(text: String, onClick: () -> Unit, modifier: Modifier = Modifier, color: Color = MauvePrimary) {
    Text(
        text,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 6.dp, vertical = 6.dp),
        style = TextStyle(
            fontFamily = SansUi,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            color = color,
            textDecoration = TextDecoration.Underline
        )
    )
}

@Composable
fun EmptyState(title: String, subtitle: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            title,
            textAlign = TextAlign.Center,
            style = TextStyle(fontFamily = SerifDisplay, fontSize = 20.sp, color = TextPrimary)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            subtitle,
            textAlign = TextAlign.Center,
            style = TextStyle(fontFamily = SansUi, fontSize = 13.sp, color = TextSecondary)
        )
    }
}

/** The user's own photo when they picked one, otherwise the placeholder avatar. */
@Composable
fun ProfileAvatar(profile: UserProfile, size: Dp, modifier: Modifier = Modifier) {
    val shaped = modifier
        .size(size)
        .clip(CircleShape)
    val path = profile.avatarPath
    if (path != null) {
        AsyncImage(
            model = File(path),
            contentDescription = "Profile photo",
            modifier = shaped,
            contentScale = ContentScale.Crop
        )
    } else {
        Image(
            painter = painterResource(profile.avatarRes),
            contentDescription = "Profile photo",
            modifier = shaped,
            contentScale = ContentScale.Crop
        )
    }
}

fun formatDateTime(millis: Long): String =
    SimpleDateFormat("d MMM yyyy, h:mm a", Locale.getDefault()).format(Date(millis))
