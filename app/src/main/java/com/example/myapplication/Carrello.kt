package com.example.myapplication

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun Carrello(){
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = stringResource(R.string.carrello))
            })
        }
    ){
        Text(stringResource(R.string.carrello))
    }
}