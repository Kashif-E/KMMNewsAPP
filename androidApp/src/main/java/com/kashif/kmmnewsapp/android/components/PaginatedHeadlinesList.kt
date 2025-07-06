package com.kashif.kmmnewsapp.android.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import com.composables.icons.lucide.Lucide
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.composables.icons.lucide.RotateCcw
import com.composables.icons.lucide.X
import com.kashif.kmmnewsapp.android.screens.home.FeaturedArticlesSection
import com.kashif.kmmnewsapp.core.pagination.PaginationState
import com.kashif.kmmnewsapp.feature.headlines.domain.Headline



@Composable
fun LoadMoreIndicator(
    progress: Float,
    accessibilityLabel: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (progress > 0) {
                LinearProgressIndicator(progress = { progress })
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Loading ${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.bodySmall
                )
            } else {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Loading more headlines...",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}


@Composable
fun ErrorFooter(
    error: com.kashif.kmmnewsapp.core.pagination.PaginationError,
    onRetry: () -> Unit,
    canRetry: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Failed to load more headlines",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )

            if (canRetry) {
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = onRetry) {
                    Text("Retry")
                }
            }
        }
    }
}



@Composable
fun EndOfListIndicator(
    totalItems: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "All $totalItems headlines loaded",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

inline fun <T> LazyListScope.paging(
    items: List<T>,
    currentPage: Int,
    threshold: Int = 4,
    pageSize: Int = 20,
    crossinline fetch: () -> Unit,
    crossinline itemContent: @Composable (item: T, index: Int) -> Unit,
) {
    itemsIndexed(
        items = items,
        key = { _, item -> item.hashCode() },
        contentType = { _, item -> item!!::class.java.name }
    ) { index, item ->
        itemContent(item, index)

        if ((index + threshold + 1) >= pageSize * (currentPage - 1)) {
            fetch()
        }
    }
}