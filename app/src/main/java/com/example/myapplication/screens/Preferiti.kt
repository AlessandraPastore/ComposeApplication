package com.example.myapplication.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavController
import com.example.myapplication.database.RicetteViewModel

/*
Composable che gestisce l'interfaccia dei Preferiti
 */
@Composable
fun Preferiti(
    model: RicetteViewModel,
    navController: NavController,
    listView: MutableState<Boolean>
){
    //Informa il model che la chiamata avviene dai Preferiti
    model.updateTipologia(true)

    Home(model, navController, listView)
}