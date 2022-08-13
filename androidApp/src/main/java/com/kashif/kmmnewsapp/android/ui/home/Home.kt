package com.kashif.kmmnewsapp.android.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.kashif.kmmnewsapp.domain.domain_model.HeadlineDomainModel
import com.kashif.kmmnewsapp.presentation.home.HomeScreenSideEffects
import com.kashif.kmmnewsapp.presentation.home.HomeScreenViewModel
import org.koin.androidx.compose.get


@Composable
fun Home(viewModel: HomeScreenViewModel = get()) {


    LaunchedEffect(key1 = 1) {
        viewModel.onIntent(HomeScreenSideEffects.GetHeadlines)
    }

    val state by viewModel.state.collectAsState()
    Surface(modifier = Modifier.fillMaxSize()) {

        when {
            state.error.isError -> {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = state.error.errorMessage,
                        color = MaterialTheme.colors.onSurface,
                        style = MaterialTheme.typography.h5
                    )

                }

            }

            state.isSuccess -> {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    content = {
                        items(state.headlines, key = {
                            it.title
                        }, contentType = {
                            it::class.java
                        }) { item ->
                            HeadLinesCard(item)

                        }


                    }
                )
            }

            state.isLoading -> {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    CircularProgressIndicator()

                }


            }
        }

    }
}


@Composable
fun HeadLinesCard(item: HeadlineDomainModel) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.large,
        elevation = 2.dp
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ) {

            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f)
                    .clip(MaterialTheme.shapes.large),
                model = ImageRequest.Builder(
                    LocalContext.current
                )
                    .scale(Scale.FILL)
                    .data(item.urlToImage)
                    .crossfade(true)
                    .build(),
                contentDescription = item.title,
                contentScale = ContentScale.FillBounds
            )

            Text(
                text = item.title,
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Row(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Text(
                    text = item.author,
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.body2
                )

                Text(
                    text = item.source,
                    color = MaterialTheme.colors.primarySurface,
                    style = MaterialTheme.typography.body2
                )

                Text(
                    text = item.publishedAt,
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.body2
                )

            }

            Text(
                text = item.description,
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(start = 16.dp , end = 16.dp, top = 8.dp, bottom = 16.dp)
            )


        }

    }

}

@Preview
@Composable
fun HomePreview() {
    HeadLinesCard(
        item = HeadlineDomainModel(
            "k",
            "hhjj",
            "klhjkh",
            "bbc",
            "jjij",
            "bbc",
            "vv",
            "vv"
        )
    )
}