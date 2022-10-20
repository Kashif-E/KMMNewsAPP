package com.kashif.kmmnewsapp.android.ui.readlater

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kashif.kmmnewsapp.android.R
import com.kashif.kmmnewsapp.android.ui.components.KmmNewsAPPTopBar
import com.kashif.kmmnewsapp.android.ui.home.headlines
import com.kashif.kmmnewsapp.android.ui.home.placeholder
import com.kashif.kmmnewsapp.presentation.home.HomeScreenState
import com.kashif.kmmnewsapp.presentation.readlater.ReadLaterState
import com.kashif.kmmnewsapp.presentation.readlater.ReadLaterViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel

@Destination
@Composable
fun ReadLaterScreen(
    destinationsNavigator: DestinationsNavigator,
    viewModel: ReadLaterViewModel = getViewModel()
) {


    val state by viewModel.state.collectAsState()
    ReadLaterBody(state, destinationsNavigator)
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
private fun ReadLaterBody(state: ReadLaterState, destinationsNavigator: DestinationsNavigator) {
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        KmmNewsAPPTopBar(
            titleRes = R.string.read_later,
            navigationIcon = {
                IconButton(onClick = { destinationsNavigator.popBackStack() }) {

                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(id = R.string.back)
                    )

                }
            }
        )
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
                is ReadLaterState.Error -> {
                    item {
                        Text(
                            text = state.message,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }

                }
                ReadLaterState.Loading -> {
                    placeholder()

                }
                is ReadLaterState.Success -> {
                    headlines(state.headlines, destinationsNavigator)

                }
            }
        }


    }
}

