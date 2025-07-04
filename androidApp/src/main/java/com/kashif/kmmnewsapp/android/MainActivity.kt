package com.kashif.kmmnewsapp.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kashif.kmmnewsapp.SampleObservableViewModel
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                HeadlinesScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeadlinesScreenX() {
    val viewModel: SampleObservableViewModel = koinInject()
    val counter by viewModel.counter.collectAsState()
    val doubled by viewModel.doubled.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("KMP Observable Counter") })
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    label = { Text("Counter") },
                    icon = {}
                )
            }
        }
    ) { padding ->
        padding.hashCode()
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Counter: $counter", style = MaterialTheme.typography.headlineMedium)
            Text("Doubled: $doubled", style = MaterialTheme.typography.bodyLarge)
            Button(onClick = { viewModel.increment() }) {
                Text("Increment")
            }
        }
    }
}

@Preview
@Composable
fun PreviewHeadlinesScreen() {
    MaterialTheme {
        HeadlinesScreen()
    }
}
