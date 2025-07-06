package com.kashif.kmmnewsapp.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import cafe.adriel.voyager.navigator.Navigator
import com.kashif.kmmnewsapp.android.design.KmmNewsTheme
import com.kashif.kmmnewsapp.android.screens.home.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KmmNewsTheme {
                Navigator(HomeScreen())

            }
        }
    }
}
