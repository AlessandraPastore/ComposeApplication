package com.example.myapplication

import androidx.annotation.StringRes

//Classe contente tutte le route delle varie destination, definite nel file Strings.xml
sealed class Screen(var route: String, @StringRes val resId: Int) {
    object Home: Screen("Home",R.string.home)
    object Preferiti: Screen("Preferiti",R.string.preferiti)
    object Carrello: Screen("Carrello",R.string.carrello)
    object Modalità: Screen("Modalità",R.string.modalità)
    object RicettaDetail: Screen("Ricetta",R.string.ricetta)
    object NuovaRicetta : Screen("NuovaRicetta",R.string.nuova )
}
