package com.example.myapplication.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.myapplication.R
import com.example.myapplication.database.RicetteViewModel
import com.example.myapplication.reactingLists.RemoveCarrello


@Composable
fun Carrello(model: RicetteViewModel){
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = stringResource(R.string.carrello))
            })
        }
    ){
        RemoveCarrello(model)
    }
}

