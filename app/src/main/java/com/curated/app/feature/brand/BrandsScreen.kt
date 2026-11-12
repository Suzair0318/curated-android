package com.curated.app.feature.brand

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.curated.app.core.ui.components.BackCircleButton
import com.curated.app.core.ui.components.BrandBubble
import com.curated.app.core.ui.components.satinBackground
import com.curated.app.core.ui.theme.SerifDisplay
import com.curated.app.core.ui.theme.TextPrimary

private val ScreenPadding = 18.dp
private const val COLUMNS = 4

// Bubbles here are about 1.6x the Home ones, so the wordmarks grow with them.
private const val WORDMARK_SCALE = 1.5f

@Composable
fun BrandsScreen(
    onBack: () -> Unit,
    onOpenBrand: (String) -> Unit,
    viewModel: BrandsViewModel = hiltViewModel()
) {
    val state by viewModel.ui.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .satinBackground()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackCircleButton(onClick = onBack)
                Text(
                    "Shop by Brand",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontFamily = SerifDisplay,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 22.sp,
                        color = TextPrimary
                    )
                )
                // Balances the back button so the title stays centred.
                Spacer(Modifier.size(40.dp))
            }

            // Lazy: with some ninety brands, only the rows on screen are composed and drawn.
            val rows = remember(state.brands) { state.brands.chunked(COLUMNS) }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .navigationBarsPadding(),
                contentPadding = PaddingValues(
                    start = ScreenPadding,
                    end = ScreenPadding,
                    top = 12.dp,
                    bottom = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(rows, key = { row -> row.first().id }) { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        rowItems.forEach { brand ->
                            BrandBubble(
                                brandId = brand.id,
                                // One word per line so two-word names fit inside the circle.
                                label = brand.shortLabel.replace(' ', '\n'),
                                onClick = { onOpenBrand(brand.id) },
                                modifier = Modifier.weight(1f),
                                wordmarkScale = WORDMARK_SCALE,
                                logoRes = brand.logoRes
                            )
                        }
                        repeat(COLUMNS - rowItems.size) {
                            Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}
