package com.example.myapplication

import androidx.compose.animation.ExperimentalAnimationApi
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
    navController: NavHostController,
    enableDarkMode: MutableState<Boolean>,
    model: RicetteViewModel,
){
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { Home(model, navController) }
        composable(Screen.Preferiti.route) { Preferiti(model, navController) }
        composable(Screen.Carrello.route) { Carrello(model) }
        composable(Screen.Impostazioni.route) { Impostazioni(enableDarkMode) }
        composable(Screen.NuovaRicetta.route) { NuovaRicetta(model, navController) }
        composable("${Screen.RicettaDetail.route}/{ricetta}",
            arguments = listOf(navArgument("ricetta") { type = NavType.StringType }))
        {
            backStackEntry ->

            RicettaDetail(model, navController, backStackEntry.arguments?.getString("ricetta"))
        }
    }
}