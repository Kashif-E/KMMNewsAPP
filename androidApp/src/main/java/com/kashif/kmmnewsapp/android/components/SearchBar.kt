package com.kashif.kmmnewsapp.android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import com.composables.icons.lucide.Lucide
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Search

@Composable
fun SearchBar(
    searchText: String = "",
    onSearchTextChange: (String) -> Unit = {},
    onSearchClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf(searchText) }
    
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { 
                text = it
                onSearchTextChange(it) 
            },
            placeholder = { 
                Text(
                    text = "Search for articles...",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ) 
            },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            ),
            singleLine = true
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        IconButton(
            onClick = onSearchClick,
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Lucide.Search,
                contentDescription = "Search",
                tint = Color.White
            )
        }
    }
}