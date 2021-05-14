package com.example.myapplication

data class Filtro (val name : String, var checked: Boolean = false ){}

fun getFilters() : List<Filtro>{
    val primo = Filtro("Primi piatti")
    val secondo = Filtro("Secondi piatti")
    val vegetariano = Filtro("Vegetariano")
    val dessert = Filtro("Dessert")
    val list = listOf<Filtro>(primo, secondo, vegetariano, dessert);

    return list
}

