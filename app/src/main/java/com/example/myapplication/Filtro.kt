package com.example.myapplication

sealed class Filtro (val name : String, var checked: Boolean = false ){
    object primo : Filtro("Primi piatti")
    object secondo : Filtro("Secondi piatti")
    object vegetariano : Filtro("Vegetariano")
    object dessert : Filtro("Dessert")
}

fun getFilters() : List<Filtro>{
    val list = listOf<Filtro>(Filtro.primo, Filtro.secondo, Filtro.vegetariano, Filtro.dessert);

    return list
}

