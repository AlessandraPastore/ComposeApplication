package com.example.myapplication


//Classe che contiene i filtri
sealed class Filtro(val name: String, var checked: Boolean = false ){
    object Antipasto : Filtro(MainActivity.get()?.applicationContext?.getString(R.string.antipasto)!!)
    object Primo : Filtro(MainActivity.get()?.applicationContext?.getString(R.string.primo)!!)
    object Secondo : Filtro(MainActivity.get()?.applicationContext?.getString(R.string.secondo)!!)
    object Dessert : Filtro(MainActivity.get()?.applicationContext?.getString(R.string.dolce)!!)
    object Vegetariano : Filtro(MainActivity.get()?.applicationContext?.getString(R.string.veg)!!)
    object Vegano : Filtro(MainActivity.get()?.applicationContext?.getString(R.string.vegano)!!)
    object GlutenFree : Filtro(MainActivity.get()?.applicationContext?.getString(R.string.gfree)!!)
}

fun getFilters(): List<Filtro> {

    return listOf(
        Filtro.Antipasto,
        Filtro.Primo,
        Filtro.Secondo,
        Filtro.Dessert,
        Filtro.Vegetariano,
        Filtro.Vegano,
        Filtro.GlutenFree
    )
}

