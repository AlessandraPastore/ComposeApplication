package com.example.myapplication.screens

import androidx.activity.compose.BackHandler
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.example.myapplication.R
import com.example.myapplication.Screen
import com.example.myapplication.database.RicetteViewModel
import com.example.myapplication.reactingLists.RemoveCarrello

/*
Composable che gestisce l'interfaccia del carrello
 */
@Composable
fun Carrello(model: RicetteViewModel, navController: NavHostController){

    // Gestisco il pulsante back: se premuto si ritorna alla Home
    BackHandler {

        model.onHomeClick()     // Carico tutte le ricette

        model.updateTipologia(false)   // Aggiorno la tipologia

        navController.navigate(Screen.Home.route) {
            popUpTo = navController.graph.startDestination
            launchSingleTop = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = stringResource(R.string.carrello))
            })
        }
    ){
        RemoveCarrello(model) //chiamata alla gestione della lista nel carrello
    }
}

