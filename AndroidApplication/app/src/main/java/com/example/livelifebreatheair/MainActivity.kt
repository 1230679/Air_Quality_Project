package com.example.livelifebreatheair

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.livelifebreatheair.ui.screens.AppRoot
import com.example.livelifebreatheair.ui.theme.LiveLifeBreatheAirTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LiveLifeBreatheAirTheme {
                AppRoot()
            }
        }
    }
}
