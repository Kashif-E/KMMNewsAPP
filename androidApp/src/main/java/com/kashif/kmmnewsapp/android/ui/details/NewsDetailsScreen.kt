package com.kashif.kmmnewsapp.android.ui.details

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import com.kashif.kmmnewsapp.android.R
import com.kashif.kmmnewsapp.android.ui.components.AppBar
import com.kashif.kmmnewsapp.android.ui.theme.KmmNewsTheme
import com.kashif.kmmnewsapp.domain.domain_model.HeadlineDomainModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Destination
@Composable
fun NewsDetailsScreen(destinationsNavigator: DestinationsNavigator, headline: HeadlineDomainModel) {


    Scaffold(scaffoldState = rememberScaffoldState(), topBar = {
        AppBar(title = stringResource(id = R.string.news_details)) {
            destinationsNavigator.navigateUp()
        }
    }, floatingActionButton = {
        ReadLaterButton()
    }, floatingActionButtonPosition = FabPosition.End) { padp ->
        padp.calculateBottomPadding()

        NewsDetails(headline)
    }


}

@Composable
fun NewsDetails(headline: HeadlineDomainModel) {
    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize()
    ) {
        HeadlineHeader(
            headline = headline
        )
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun HeadlineHeader(headline: HeadlineDomainModel) {
    val webviewState = rememberWebViewState(url = headline.url)
    LazyColumn(
        modifier = Modifier
            .wrapContentSize()
            .scrollable(rememberScrollState(), Orientation.Vertical),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        item {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                model = ImageRequest.Builder(
                    LocalContext.current
                )
                    .scale(Scale.FILL)
                    .data(headline.urlToImage)
                    .crossfade(true)
                    .build(),
                contentDescription = headline.title,
                contentScale = ContentScale.FillBounds
            )
        }
        item {

            Text(
                text = headline.title,
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Text(
                    text = headline.author,
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.body2
                )

                Text(
                    text = headline.source,
                    color = MaterialTheme.colors.primarySurface,
                    style = MaterialTheme.typography.body2
                )

                Text(
                    text = headline.publishedAt,
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.body2
                )

            }
        }

        item {
            Text(
                text = headline.description,
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
            )
        }

        item {
            WebView(

                state = webviewState,
                onCreated = { it.settings.javaScriptEnabled = true },
                captureBackPresses = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()

            )
        }


    }
}

@Preview
@Composable
fun ReadLaterButton() {
    Card(backgroundColor = MaterialTheme.colors.primary,
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .wrapContentSize(align = Alignment.Center)
            .clickable {

            }) {

        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = stringResource(id = R.string.read_later),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
fun NewsDetailScreenAppBarPreview() {
    KmmNewsTheme {
        //  NewsDetails(headline)
    }

}