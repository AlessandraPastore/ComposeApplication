package com.example.myapplication

import com.example.myapplication.database.IngredienteRIcetta

//Data class per la ricetta, contenente tutti i campi utili per una ricetta
data class RicettaSample(
    var titolo: String,
    var descrizione: String,
    var ingredienti: MutableList<IngredienteRIcetta>,
    var filtri : MutableList<Filtro>,var uri:String?
    )
