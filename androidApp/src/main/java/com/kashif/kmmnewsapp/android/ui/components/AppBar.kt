package com.kashif.kmmnewsapp.android.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kashif.kmmnewsapp.android.R

@Composable
fun AppBar(
    title: String,
    modifier: Modifier = Modifier,
    backPressed: () -> Unit
) {

    TopAppBar(modifier = modifier, elevation = 1.dp, title = {

        Text(
            text = title,
            style = MaterialTheme.typography.h5,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )

    }, backgroundColor = Color.White, navigationIcon = {

        IconButton(onClick = { backPressed() }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(id = R.string.back)
            )
        }

    })


}
