package com.kashif.kmmnewsapp.android

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter

import com.kashif.kmmnewsapp.feature.headlines.presentation.mvi.HeadlinesIntent
import com.kashif.kmmnewsapp.feature.headlines.presentation.mvi.HeadlinesState
import com.kashif.kmmnewsapp.feature.headlines.presentation.vm.HeadlinesViewModel
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeadlinesScreen() {
    val viewModel: HeadlinesViewModel = koinInject()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.send(HeadlinesIntent.LoadHeadlines)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Top Headlines") }, actions = {
                IconButton(onClick = { viewModel.send(HeadlinesIntent.RefreshHeadlines) }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                }
            })
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (state) {
                is HeadlinesState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is HeadlinesState.Success -> {
                    val headlines = (state as HeadlinesState.Success).headlines
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(headlines) { headline ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    headline.imageUrl?.let {
                                        Image(
                                            painter = rememberAsyncImagePainter(it),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(180.dp),
                                            contentScale = ContentScale.Crop
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                    Text(headline.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                    headline.description?.let {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(it, fontSize = 14.sp)
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(headline.source, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                                    Text(headline.publishedAt, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
                is HeadlinesState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text((state as HeadlinesState.Error).message, color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}
