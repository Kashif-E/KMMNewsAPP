package com.kashif.kmmnewsapp.android.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.kashif.kmmnewsapp.android.components.PaginatedHeadlinesList
import com.kashif.kmmnewsapp.android.components.NewsCard
import com.kashif.kmmnewsapp.android.design.Spacing
import com.kashif.kmmnewsapp.core.pagination.PaginationState
import com.kashif.kmmnewsapp.feature.headlines.domain.Headline
import com.kashif.kmmnewsapp.feature.headlines.presentation.mvi.HeadlinesIntent
import com.kashif.kmmnewsapp.feature.headlines.presentation.vm.HeadlinesViewModel
import org.koin.androidx.compose.koinViewModel

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinViewModel<HeadlinesViewModel>()

        val paginationState = viewModel.paginationState.collectAsState()

        HomeScreenContent(
            paginationState = paginationState,
            onHeadlineClick = { /* Handle headline click */ },
            onLoadMore = { viewModel.send(HeadlinesIntent.LoadNextPage) }
        )
    }
}

@Composable
private fun HomeScreenContent(
    onHeadlineClick: (Headline) -> Unit,
    onLoadMore: () -> Unit,
    paginationState: State<PaginationState<Headline>>
) {

    PaginatedHeadlinesList(
        state = paginationState.value,
        onLoadMore = onLoadMore,
        onRefresh = { /* Handle refresh */ },
        onRetry = { /* Handle retry */ },
        modifier = Modifier.fillMaxWidth(),
        listState = rememberLazyListState()
    )
}


@Composable
fun FeaturedHeadlines(
    headlines: List<Headline>,
    onHeadlineClick: (Headline) -> Unit
) {
    Column {
        Text(
            text = "Featured",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = Spacing.md)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(Spacing.cardSpacing)
        ) {
            items(headlines) { headline ->
                NewsCard(
                    headline = headline,
                    onClick = { onHeadlineClick(headline) },
                    modifier = Modifier.width(280.dp)
                )
            }
        }
    }
} 