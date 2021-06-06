package com.example.myapplication

sealed class Filtro (val name : String, var checked: Boolean = false ){
    object Antipasto : Filtro("Antipasto")
    object Primo : Filtro("Primo piatto")
    object Secondo : Filtro("Secondo piatto")
    object Dessert : Filtro("Dolce")
    object Vegetariano : Filtro("Vegetariano")
    object Vegano : Filtro("Vegano")
    object GlutenFree : Filtro("Gluten free")
}

fun getFilters() : List<Filtro>{
    val list = listOf(Filtro.Antipasto , Filtro.Primo, Filtro.Secondo, Filtro.Dessert, Filtro.Vegetariano, Filtro.Vegano, Filtro.GlutenFree);

    return list
}

