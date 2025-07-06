package com.kashif.kmmnewsapp.android.components

import com.composables.icons.lucide.Lucide
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Badge
import com.composables.icons.lucide.Bell
import com.composables.icons.lucide.Bookmark
import com.composables.icons.lucide.User

@Composable
fun BottomNavigationBar(
    selectedTab: Int = 0,
    onTabSelected: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { 
                Icon(
                    imageVector = Lucide.Badge,
                    contentDescription = "Home"
                ) 
            },
            label = { Text("Home") },
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        
        NavigationBarItem(
            icon = { 
                Icon(
                    imageVector = Lucide.Bookmark, 
                    contentDescription = "Bookmark"
                ) 
            },
            label = { Text("Bookmark") },
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        
        NavigationBarItem(
            icon = { 
                BadgedBox(
                    badge = { 
                        Badge(
                            containerColor = MaterialTheme.colorScheme.error
                        ) { 
                            Text(
                                text = "3",
                                color = MaterialTheme.colorScheme.onError
                            ) 
                        } 
                    }
                ) {
                    Icon(
                        imageVector = Lucide.Bell, 
                        contentDescription = "Notifications"
                    )
                }
            },
            label = { Text("Notifications") },
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        
        NavigationBarItem(
            icon = { 
                Icon(
                    imageVector = Lucide.User, 
                    contentDescription = "Profile"
                ) 
            },
            label = { Text("Profile") },
            selected = selectedTab == 3,
            onClick = { onTabSelected(3) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}