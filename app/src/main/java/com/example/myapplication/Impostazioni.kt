package com.example.myapplication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign

@Composable
fun Impostazioni(){
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = stringResource(R.string.impostazioni))
            })
        }
    ){
        //Text(stringResource(R.string.impostazioni))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            //Icon(Icons.Rounded.Favorite, "")
            Text(
                text = "clicca per cambiare modalità",
                textAlign = TextAlign.Center
                )
        }
    }
}