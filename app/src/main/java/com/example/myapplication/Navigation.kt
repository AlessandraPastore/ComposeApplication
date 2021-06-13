package com.example.myapplication

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import com.example.myapplication.database.RicetteViewModel
import com.example.myapplication.screens.*


@Composable
fun NavConfig(
    model: RicetteViewModel,
    navController: NavHostController,
    enableDarkMode: MutableState<Boolean>,
    listView: MutableState<Boolean>,
){
    // Funzione che crea il Navigation Graph dell'applicazione
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { Home(model, navController, listView) }
        composable(Screen.Preferiti.route) { Preferiti(model, navController, listView) }
        composable(Screen.Carrello.route) { Carrello(model, navController) }
        composable(Screen.Modality.route) { Impostazioni(model, navController, enableDarkMode, listView) }
        composable(Screen.NuovaRicetta.route) { NuovaRicetta(model, navController) }

        // Navigazione con argomenti, in quanto RicettaDetail necessita di un parametro, il titolo
        // della ricetta, che le deve essere passato quando si effettua la navigazione
        composable("${Screen.RicettaDetail.route}/{ricetta}",
            arguments = listOf(navArgument("ricetta") { type = NavType.StringType }))
        {
            backStackEntry ->

            RicettaDetail(model, navController, backStackEntry.arguments?.getString("ricetta"))
        }
    }
}
