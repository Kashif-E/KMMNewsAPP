package com.kashif.kmmnewsapp.android

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.kashif.kmmnewsapp.android.ui.home.Home
import com.kashif.kmmnewsapp.android.ui.theme.KmmNewsTheme


class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KmmNewsTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                    TopAppBar {

                        Text(text = "KMM News App", style = MaterialTheme.typography.h3, textAlign = TextAlign.Center)

                    }
                }, scaffoldState = rememberScaffoldState()) {


                    Home()


                }
            }
        }


    }
}

