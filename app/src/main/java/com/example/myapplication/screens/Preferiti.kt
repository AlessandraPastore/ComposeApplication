package com.example.myapplication.screens

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.database.RicetteViewModel


@Composable
fun Preferiti(model: RicetteViewModel,
              navController: NavController){

    model.updateTipologia(true)

    Home(model, navController)
}