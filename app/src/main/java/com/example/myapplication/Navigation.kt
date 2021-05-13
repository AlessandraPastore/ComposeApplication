package com.example.myapplication

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument

@Composable
fun NavConfig(navController: NavHostController){

    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { Home() }
        composable(Screen.Preferiti.route) { Preferiti() }
        composable(Screen.Carrello.route) { Carrello() }
        composable(Screen.Impostazioni.route) { Impostazioni() }
        //composable(Screen.Ricetta.route,
        // arguments = listOf(navArgument("name") { type = NavType.StringType}){

        //}
    }

}