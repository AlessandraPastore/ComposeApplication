package com.example.myapplication

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
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
        composable(Screen.Modalità.route) { Impostazioni(enableDarkMode, listView) }
        composable(Screen.NuovaRicetta.route) { NuovaRicetta(model, navController) }
        composable("${Screen.RicettaDetail.route}/{ricetta}",
            arguments = listOf(navArgument("ricetta") { type = NavType.StringType }))
        {
            backStackEntry ->

            RicettaDetail(model, navController, backStackEntry.arguments?.getString("ricetta"))
        }
    }
}
