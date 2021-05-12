package com.example.myapplication

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavConfig(navController: NavHostController){
    val home = stringResource(R.string.home)
    val preferiti = stringResource(R.string.preferiti)
    val carrello = stringResource(R.string.carrello)
    val impostazioni = stringResource(R.string.impostazioni)

    NavHost(navController, startDestination = home) {
        composable(home) { MainScreen(home) }
        composable(preferiti) { MainScreen(preferiti) }
        composable(carrello) { MainScreen(carrello) }
        composable(impostazioni) { MainScreen(impostazioni) }
    }

}