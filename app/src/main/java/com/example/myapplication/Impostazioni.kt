package com.example.myapplication

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign

@Composable
fun Impostazioni(enableDarkMode: MutableState<Boolean>) {
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
            Switch(
                checked = enableDarkMode.value,
                onCheckedChange= { enableDarkMode.value = !enableDarkMode.value  }
                )
        }
    }
}