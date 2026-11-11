package com.eliamo.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.eliamo.app.ui.AppRoot
import com.eliamo.app.ui.theme.EliaoTheme
import com.eliamo.app.ui.theme.Paper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EliaoTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Paper) {
                    AppRoot()
                }
            }
        }
    }
}
