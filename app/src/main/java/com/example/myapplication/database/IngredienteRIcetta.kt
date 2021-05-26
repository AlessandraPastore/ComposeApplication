package com.example.myapplication.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    primaryKeys=["titolo","ingrediente"],
    foreignKeys = [
    ForeignKey(entity = RicettePreview::class,
     parentColumns = ["titolo"],
     childColumns = ["titolo"],
        onDelete = CASCADE
    ),
    ForeignKey(entity = Ingrediente::class,
        parentColumns = ["ingrediente"],
        childColumns = ["ingrediente"])
])
data class IngredienteRIcetta(
    val titolo:String,
    val ingrediente: String,
    @ColumnInfo(name="qta")
    val qta:String
)
