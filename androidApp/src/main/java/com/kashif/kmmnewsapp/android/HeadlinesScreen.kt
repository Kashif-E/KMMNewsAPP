package com.kashif.kmmnewsapp.android

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.kashif.kmmnewsapp.android.components.PaginatedHeadlinesList
import com.kashif.kmmnewsapp.core.pagination.PaginationIntent
import com.kashif.kmmnewsapp.feature.headlines.presentation.mvi.HeadlinesIntent
import com.kashif.kmmnewsapp.feature.headlines.presentation.vm.HeadlinesViewModel
import org.koin.compose.koinInject
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeadlinesScreen() {
    val viewModel: HeadlinesViewModel = koinInject()
    
    val paginationState by viewModel.paginationState.collectAsState()
    val lastSyncTime: Long? by viewModel.lastSyncTime.collectAsState(null)
    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            "Top Headlines",
                            modifier = Modifier.semantics {
                                contentDescription = "Top Headlines screen. ${paginationState.accessibilityDescription}"
                            }
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                viewModel.sendPaginationIntent(PaginationIntent.Refresh)
                            },
                            modifier = Modifier.semantics {
                                contentDescription = "Refresh headlines"
                            }
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null)
                        }
                    }
                )
                // Last Sync Time View
                if (lastSyncTime != null) {
                    val formatted = remember(lastSyncTime) {
                        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
                            .format(Date(lastSyncTime!!))
                    }
                    Text(
                        text = "Last synced: $formatted",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 4.dp)
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            PaginatedHeadlinesList(
                state = paginationState,
                onLoadMore = {
                    viewModel.sendPaginationIntent(PaginationIntent.LoadMore)
                },
                onRefresh = {
                    viewModel.sendPaginationIntent(PaginationIntent.Refresh)
                },
                onRetry = {
                    viewModel.retryLastOperation() 
                },
                listState = listState
            )
        }
    }
}
