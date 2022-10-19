package com.kashif.kmmnewsapp.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.kashif.kmmnewsapp.android.ui.home.NavGraphs
import com.kashif.kmmnewsapp.android.ui.theme.KMMNewsTheme
import com.ramcosta.composedestinations.DestinationsNavHost


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KMMNewsTheme {


               DestinationsNavHost(navGraph = NavGraphs.root)

            }
        }


    }
}

