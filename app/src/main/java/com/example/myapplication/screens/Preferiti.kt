package com.example.myapplication.screens

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.example.myapplication.Screen
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

    // Gestisco il pulsante back: se premuto si ritorna alla Home
    BackHandler {

        model.onHomeClick()     // Carico tutte le ricette

        model.updateTipologia(false)  // Aggiorno la tipologia

        navController.navigate(Screen.Home.route) {
            popUpTo = navController.graph.startDestination
            launchSingleTop = true
        }
    }

    Home(model, navController, listView)
}