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
fun PaginatedHeadlinesList(
    state: PaginationState<Headline>,
    onLoadMore: () -> Unit,
    onRefresh: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(0, 0)
) {
    Box(
        modifier = modifier.semantics {
            contentDescription = state.accessibilityDescription
            if (state.isInitialLoading || state.isLoadingMore) {
                liveRegion = LiveRegionMode.Polite
            }
        }
    ) {
        when {
            state.isInitialLoading && state.items.isEmpty() -> {
                InitialLoadingIndicator()
            }

            state.error != null && state.items.isEmpty() -> {
                ErrorState(
                    error = state.error!!,
                    onRetry = onRetry,
                    canRetry = state.error!!.isRecoverable
                )
            }

            state.items.isEmpty() -> {
                EmptyState(onRefresh = onRefresh)
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = listState,
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    // Featured Headlines Section
                    item {
                        FeaturedArticlesSection(
                            headlines = state.items.take(5),
                            onHeadlineClick = {

                            }
                        )
                    }

                    // Latest Headlines Section
                    item {
                        Text(
                            text = "Latest Headlines",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                    paging(
                        items = state.items,
                        currentPage = state.currentPage,
                        threshold = 4,
                        pageSize = 20,
                        fetch = onLoadMore
                    ) { headline, index ->
                        NewsCard (
                            headline = headline,
                            modifier = Modifier
                                .fillMaxWidth()

                        )
                    }

                    if (state.isLoadingMore) {
                        item(key = "loading_more") {
                            LoadMoreIndicator(
                                progress = state.loadingProgress,
                                accessibilityLabel = "Loading more headlines"
                            )
                        }
                    }

                    if (state.error != null && state.items.isNotEmpty()) {
                        item(key = "error_footer") {
                            ErrorFooter(
                                error = state.error!!,
                                onRetry = onRetry,
                                canRetry = state.error!!.isRecoverable
                            )
                        }
                    }

                    if (!state.hasMore && state.items.isNotEmpty()) {
                        item(key = "end_of_list") {
                            EndOfListIndicator(totalItems = state.totalItems)
                        }
                    }
                }

            }
        }
    }
}

@Composable
private fun HeadlineCard(
    headline: Headline,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            headline.imageUrl?.let { imageUrl ->
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = "Image for headline: ${headline.title}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                text = headline.title,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.semantics { heading() }
            )

            headline.description?.let { description ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = description, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = headline.source,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = headline.publishedAt,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun InitialLoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Loading headlines...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

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
private fun ErrorState(
    error: com.kashif.kmmnewsapp.core.pagination.PaginationError,
    onRetry: () -> Unit,
    canRetry: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Lucide.X,
                contentDescription = "Error icon",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Failed to load headlines",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = error.message,
                style = MaterialTheme.typography.bodyMedium
            )

            if (canRetry) {
                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onRetry,
                    modifier = Modifier.sizeIn(minWidth = 48.dp, minHeight = 48.dp)
                ) {
                    Icon(Lucide.RotateCcw, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Retry")
                }
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
private fun EmptyState(
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "No headlines available",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onRefresh,
                modifier = Modifier.sizeIn(minWidth = 48.dp, minHeight = 48.dp)
            ) {
                Icon(Lucide.RotateCcw, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Refresh")
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