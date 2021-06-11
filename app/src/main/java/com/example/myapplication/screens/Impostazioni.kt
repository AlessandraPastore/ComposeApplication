package com.example.myapplication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplication.R

/*
Composable che gestisce l'interfaccia delle impostazioni
 */
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

//Funzione che permette la scelta tra GridView e ListView
@Composable
fun ListView(listView: MutableState<Boolean>){

    Text(
        text = stringResource(R.string.Visualizzazione),
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
                text = stringResource(R.string.GridView),
                textAlign = TextAlign.Center,
            )

            //modifica la visibilità del pulsante selezionato o meno
            val gridState = ContentAlpha.disabled
            val contentAlpha = remember { mutableStateOf(gridState) }
            if(!listView.value)
                contentAlpha.value = ContentAlpha.high
            else
                contentAlpha.value = gridState

            IconButton(onClick = { listView.value = false }) {
                CompositionLocalProvider(LocalContentAlpha provides contentAlpha.value) {
                    Icon(painterResource(R.drawable.ic_round_grid_view_24), "", modifier = Modifier.scale(2F))
                }
            }
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = stringResource(R.string.ListView),
                textAlign = TextAlign.Center,
            )

            //modifica la visibilità del pulsante selezionato o meno
            val listState = ContentAlpha.disabled
            val contentAlpha = remember { mutableStateOf(listState) }
            if(listView.value)
                contentAlpha.value = ContentAlpha.high
            else
                contentAlpha.value = listState

            IconButton(onClick = { listView.value = true }) {
                CompositionLocalProvider(LocalContentAlpha provides contentAlpha.value) {
                    Icon(painterResource(R.drawable.ic_round_view_list_24), "", modifier = Modifier.scale(2F))
                }
            }
        }
    }
}

//Funzione che permette la scelta tra DarkMode o LightMode
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
            text = stringResource(R.string.Darkmode),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h6
        )

        Spacer(modifier = Modifier.height(15.dp))

        if(enableDarkMode.value)   {
            Icon(painterResource(R.drawable.ic_round_dark_mode_24), contentDescription = "", modifier = Modifier.size(64.dp))}
        else {
            Icon(painterResource(R.drawable.ic_round_light_mode_24), contentDescription = "", modifier = Modifier.size(64.dp))}

        Spacer(modifier = Modifier.height(30.dp))

        Switch(
            checked = enableDarkMode.value,
            onCheckedChange= {
                enableDarkMode.value = !enableDarkMode.value
            })
    }
}