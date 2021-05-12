package com.example.myapplication

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun Impostazioni(){
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = stringResource(R.string.impostazioni))
            })
        }
    ){
        Text(stringResource(R.string.impostazioni))
    }
}