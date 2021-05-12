package com.example.myapplication

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun Preferiti(){
Scaffold(
    content = {Text(stringResource(R.string.preferiti))}
)
}