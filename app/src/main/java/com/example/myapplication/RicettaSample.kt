package com.example.myapplication

import com.example.myapplication.database.Ingrediente
import com.example.myapplication.database.IngredienteRIcetta

data class RicettaSample(var titolo: String, var descrizione: String, var ingredienti: MutableList<IngredienteRIcetta>)
