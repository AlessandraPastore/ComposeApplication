package com.example.myapplication

import com.example.myapplication.database.Ingrediente
import com.example.myapplication.database.IngredienteRIcetta

data class RicettaSample(val titolo: String, val descrizione: String?, val ingredienti: MutableList<IngredienteRIcetta>?)
