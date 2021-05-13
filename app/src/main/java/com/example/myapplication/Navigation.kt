package com.example.myapplication

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavConfig(navController: NavHostController, enableDarkMode: MutableState<Boolean>){

    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { Home() }
        composable(Screen.Preferiti.route) { Preferiti() }
        composable(Screen.Carrello.route) { Carrello() }
        composable(Screen.Impostazioni.route) { Impostazioni(enableDarkMode) }
        composable(Screen.NuovaRicetta.route) { NuovaRicetta(navController) }
        //composable(Screen.Ricetta.route,
        // arguments = listOf(navArgument("name") { type = NavType.StringType}){

        //}
    }

}