package com.example.myapplication

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun AddRicetta(){
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.nuova)) },
                navigationIcon = {
                    IconButton(onClick = { /* doSomething() */ }) {
                    Icon(Icons.Rounded.ArrowBack, contentDescription = null)
                }
            },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {  }) {
                Icon( Icons.Rounded.Done, "")
            }
        },
    ){

    }
}