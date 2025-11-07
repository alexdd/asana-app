package com.asana.timer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.asana.timer.ui.YogaAsanaTimerApp
import com.asana.timer.ui.theme.AsanaTimerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            AsanaTimerTheme {
                YogaAsanaTimerApp()
            }
        }
    }
}

