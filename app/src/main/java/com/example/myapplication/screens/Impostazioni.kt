package com.example.myapplication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.ViewList
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplication.R


@Composable
fun Impostazioni(enableDarkMode: MutableState<Boolean>, listView: MutableState<Boolean>) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = stringResource(R.string.modalità))
            })
        }
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            ListView(listView)

            Divider(
                modifier = Modifier.padding(10.dp)
            )

            DarkMode(enableDarkMode)
        }

    }
}

@Composable
fun ListView(listView: MutableState<Boolean>){

    Text(
        text = "Visualizzazione",
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h6
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly

    ){
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = "GridView",
                textAlign = TextAlign.Center,
            )

            //modifica la visibilità del pulsante selezionato o non
            val gridState = ContentAlpha.disabled
            val contentAlpha = remember { mutableStateOf(gridState) }
            if(!listView.value)
                contentAlpha.value = ContentAlpha.high
            else
                contentAlpha.value = gridState

            IconButton(onClick = { listView.value = false }) {
                CompositionLocalProvider(LocalContentAlpha provides contentAlpha.value) {
                    Icon(Icons.Rounded.GridView, "", modifier = Modifier.scale(2F))
                }
            }
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = "ListView",
                textAlign = TextAlign.Center,
            )

            //modifica la visibilità del pulsante selezionato o non
            val listState = ContentAlpha.disabled
            val contentAlpha = remember { mutableStateOf(listState) }
            if(listView.value)
                contentAlpha.value = ContentAlpha.high
            else
                contentAlpha.value = listState

            IconButton(onClick = { listView.value = true }) {
                CompositionLocalProvider(LocalContentAlpha provides contentAlpha.value) {
                    Icon(Icons.Rounded.ViewList, "", modifier = Modifier.scale(2F))
                }
            }
        }
    }
}

@Composable
fun DarkMode(enableDarkMode: MutableState<Boolean>){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ){

        Text(
            text = "Darkmode",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h6
        )

        Spacer(modifier = Modifier.height(15.dp))

        if(enableDarkMode.value)   {
            Icon(Icons.Rounded.DarkMode, contentDescription = "", modifier = Modifier.size(64.dp))}
        else {
            Icon(Icons.Rounded.LightMode, contentDescription = "", modifier = Modifier.size(64.dp))}

        Spacer(modifier = Modifier.height(30.dp))

        Switch(
            checked = enableDarkMode.value,
            onCheckedChange= {
                enableDarkMode.value = !enableDarkMode.value
            })
    }
}