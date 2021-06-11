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
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { Home(model, navController, listView) }
        composable(Screen.Preferiti.route) { Preferiti(model, navController, listView) }
        composable(Screen.Carrello.route) { Carrello(model) }
        composable(Screen.Modality.route) { Impostazioni(enableDarkMode, listView) }
        composable(Screen.NuovaRicetta.route) { NuovaRicetta(model, navController) }
        composable("${Screen.RicettaDetail.route}/{ricetta}",
            arguments = listOf(navArgument("ricetta") { type = NavType.StringType }))
        {
            backStackEntry ->

            RicettaDetail(model, navController, backStackEntry.arguments?.getString("ricetta"))
        }
    }
}
