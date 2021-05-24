package com.example.myapplication

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.myapplication.database.RicettePreview

@Composable
fun RicettaDetail(navController: NavController, ricetta: String?){
    Text("Ricetta ${ricetta}")
}