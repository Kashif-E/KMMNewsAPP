package com.kashif.kmmnewsapp.android.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.kashif.kmmnewsapp.android.R
import com.kashif.kmmnewsapp.android.ui.components.KmmNewsAPPTopBar
import com.kashif.kmmnewsapp.android.ui.destinations.NewsDetailsScreenDestination
import com.kashif.kmmnewsapp.android.ui.destinations.ReadLaterScreenDestination
import com.kashif.kmmnewsapp.android.ui.theme.KMMNewsTheme
import com.kashif.kmmnewsapp.domain.domain_model.HeadlineDomainModel
import com.kashif.kmmnewsapp.presentation.home.HomeScreenSideEvent
import com.kashif.kmmnewsapp.presentation.home.HomeScreenState
import com.kashif.kmmnewsapp.presentation.home.HomeScreenViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel


@RootNavGraph(start = true)
@Destination
@Composable
fun Home(
    destinationsNavigator: DestinationsNavigator, viewModel: HomeScreenViewModel = getViewModel()
) {

    LaunchedEffect(key1 = Unit) {
        viewModel.onIntent(HomeScreenSideEvent.GetHeadlines)
    }
    val state by viewModel.state.collectAsState()
    Home(state, destinationsNavigator)
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
private fun Home(state: HomeScreenState, destinationsNavigator: DestinationsNavigator) {
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        KmmNewsAPPTopBar(titleRes = R.string.app_heading, actionIcons = {
            IconButton(onClick = { destinationsNavigator.navigate(ReadLaterScreenDestination()) }) {
                Icon(imageVector = Icons.Default.List, contentDescription = null)
            }
        })
    }) { innerPadding ->

        val listState = rememberLazyListState()
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = listState,
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumedWindowInsets(innerPadding)
        ) {
            item {
                Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
            }

            when (state) {
                is HomeScreenState.Error -> {
                    item {
                        Text(
                            text = state.errorMessage,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }

                }
                HomeScreenState.Idle -> {
                }
                HomeScreenState.Loading -> {
                    placeholder()

                }
                is HomeScreenState.Success -> {
                    headlines(state.headlines, destinationsNavigator)

                }
            }
        }


    }
}


fun LazyListScope.headlines(
    headlines: List<HeadlineDomainModel>, destinationsNavigator: DestinationsNavigator
) {
    items(headlines) { item ->
        HeadlinesCard(headlineDomainModel = item, modifier = Modifier.clickable {
            destinationsNavigator.navigate(NewsDetailsScreenDestination(item))
        })
    }
}

fun LazyListScope.placeholder() {

    //TODO: Add accompanist place holder api

    item {
        CircularProgressIndicator()
    }

}


@Composable
fun HeadlinesCard(
    headlineDomainModel: HeadlineDomainModel, modifier: Modifier = Modifier
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .height(280.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column {
            AsyncImage(
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth(),
                model = ImageRequest.Builder(
                    LocalContext.current
                ).scale(Scale.FILL).data(headlineDomainModel.urlToImage).crossfade(true).build(),
                contentDescription = headlineDomainModel.title,

                )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = headlineDomainModel.title,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = headlineDomainModel.author,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = headlineDomainModel.source,
                    style = MaterialTheme.typography.bodySmall.copy(
                        MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}


@Preview
@Composable
fun HomePreview() {
    KMMNewsTheme {
        HeadlinesCard(headlineDomainModel = HeadlineDomainModel(
            "Kashif Mehmood",
            "KMM has gone beta and is near to the final release with the new memory model.",
            "KMM hits beta",
            "12-10-22",
            "BBC",
            "KMM Is Awesome",
            "https://-",
            "https://=="
        ), modifier = Modifier.clickable {

        })
    }

}