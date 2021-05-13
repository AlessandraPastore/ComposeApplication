package com.example.myapplication

import androidx.annotation.StringRes

//Classe contente tutte le route delle varie destination
sealed class Screen(val route: String, @StringRes val resId: Int) {
    object Home: Screen("Home",R.string.home)
    object Preferiti: Screen("Preferiti",R.string.preferiti)
    object Carrello: Screen("Carrello",R.string.carrello)
    object Impostazioni: Screen("Impostazioni",R.string.impostazioni)
    object Ricetta: Screen("Ricetta",R.string.ricetta)
}