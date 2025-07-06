package com.kashif.kmmnewsapp.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import com.kashif.kmmnewsapp.android.components.BottomNavigationBar
import com.kashif.kmmnewsapp.android.theme.KmmNewsTheme
import com.kashif.kmmnewsapp.android.screens.home.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KmmNewsTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { paddingValues ->
        when (selectedTab) {
            0 -> Box(modifier = Modifier.padding(paddingValues)) {
                Navigator(screen = HomeScreen())
            }
            1 -> {
                // TODO: Implement Bookmarks Screen
                Surface(
                    modifier = Modifier.padding(paddingValues)
                ) {
                    Text("Bookmarks Screen - Coming Soon")
                }
            }
            2 -> {
                // TODO: Implement Notifications Screen
                Surface(
                    modifier = Modifier.padding(paddingValues)
                ) {
                    Text("Notifications Screen - Coming Soon")
                }
            }
            3 -> {
                // TODO: Implement Profile Screen
                Surface(
                    modifier = Modifier.padding(paddingValues)
                ) {
                    Text("Profile Screen - Coming Soon")
                }
            }
        }
    }
}
