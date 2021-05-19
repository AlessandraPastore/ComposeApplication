package com.example.myapplication.screens

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.myapplication.R


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