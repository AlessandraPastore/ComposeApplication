package com.example.myapplication

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource


@Composable
fun Preferiti(){
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = stringResource(R.string.preferiti))
            })
        }
    ){
        Text(stringResource(R.string.preferiti))
    }
}