package com.kashif.kmmnewsapp.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kashif.kmmnewsapp.android.theme.KmmNewsTheme
import com.kashif.kmmnewsapp.presentation.screen.ScreenViewModel
import org.koin.androidx.compose.get


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KmmNewsTheme {
                Screen()
            }
        }


    }
}

@Composable
fun Screen(viewModel: ScreenViewModel = get()) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(viewModel.getSharedMessage(),
            color = MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.h1
        )

    }
}
