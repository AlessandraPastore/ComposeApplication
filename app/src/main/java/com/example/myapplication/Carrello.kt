package com.example.myapplication

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.reactingLists.removeCarrello

@ExperimentalAnimationApi
@Composable
fun Carrello(){
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = stringResource(R.string.carrello))
            })
        }
    ){

        removeCarrello()

    }
}

