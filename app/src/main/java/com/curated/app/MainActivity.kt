package com.curated.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.curated.app.core.ui.theme.CuratedTheme
import com.curated.app.core.ui.theme.RoseBg
import com.curated.app.navigation.CuratedNavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CuratedTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = RoseBg) {
                    CuratedNavGraph()
                }
            }
        }
    }
}
