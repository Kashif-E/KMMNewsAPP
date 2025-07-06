package com.kashif.kmmnewsapp.android.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.kashif.kmmnewsapp.android.components.*
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
        var selectedCategory by remember { mutableStateOf("") }
        var searchText by remember { mutableStateOf("") }

        HomeScreenContent(
            paginationState = paginationState,
            selectedCategory = selectedCategory,
            searchText = searchText,
            onHeadlineClick = { /* Handle headline click */ },
            onLoadMore = { viewModel.send(HeadlinesIntent.LoadNextPage) },
            onCategorySelected = { selectedCategory = it },
            onSearchTextChange = { searchText = it },
            onSearchClick = { /* Handle search */ }
        )
    }
}

@Composable
private fun HomeScreenContent(
    paginationState: State<PaginationState<Headline>>,
    selectedCategory: String,
    searchText: String,
    onHeadlineClick: (Headline) -> Unit,
    onLoadMore: () -> Unit,
    onCategorySelected: (String) -> Unit,
    onSearchTextChange: (String) -> Unit,
    onSearchClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        item {
            WelcomeHeader(modifier = Modifier.padding(16.dp))
        }

        item {
            SearchBar(
                searchText = searchText,
                onSearchTextChange = onSearchTextChange,
                onSearchClick = onSearchClick,
                modifier = Modifier.padding(16.dp)
            )
        }

        item {
            CategoryChips(
                selectedCategory = selectedCategory,
                onCategorySelected = onCategorySelected
            )
        }

        item {
            FeaturedArticlesSection(
                headlines = paginationState.value.items.take(10),
                onHeadlineClick = onHeadlineClick
            )
        }

        item {
            ShortForYouSection(
                modifier = Modifier,
                headlines = paginationState.value.items.drop(3).take(5),
                onViewAllClick = { /* Handle view all */ }
            )
        }

        item {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                text = "Latest Headlines",
                style = MaterialTheme.typography.headlineMedium
            )
        }
        paging(
            items = paginationState.value.items,
            currentPage = paginationState.value.currentPage,
            threshold = 4,
            pageSize = 20,
            fetch = onLoadMore
        ) { headline, index ->
            NewsCard(
                headline = headline,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)

            )
        }

        if (paginationState.value.isLoadingMore) {
            item(key = "loading_more") {
                LoadMoreIndicator(
                    progress = paginationState.value.loadingProgress,
                    accessibilityLabel = "Loading more headlines"
                )
            }
        }

        if (paginationState.value.error != null && paginationState.value.items.isNotEmpty()) {
            item(key = "error_footer") {
                ErrorFooter(
                    error = paginationState.value.error!!,
                    onRetry = onLoadMore,
                    canRetry = paginationState.value.error!!.isRecoverable
                )
            }
        }

        if (!paginationState.value.hasMore && paginationState.value.items.isNotEmpty()) {
            item(key = "end_of_list") {
                EndOfListIndicator(totalItems = paginationState.value.totalItems)
            }
        }

    }
}


@Composable
fun FeaturedArticlesSection(
    headlines: List<Headline>,
    onHeadlineClick: (Headline) -> Unit
) {
    if (headlines.isNotEmpty()) {
        Column(
            modifier = Modifier,
        ) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(16.dp),
            ) {
                items(
                    headlines
                ) { headline ->
                    FeatureCard(
                        headline = headline,
                        onClick = { onHeadlineClick(headline) },
                        modifier = Modifier.weight(2f)
                    )
                }
            }
        }
    }
} 