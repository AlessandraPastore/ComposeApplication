package com.example.myapplication.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    primaryKeys=["ricetta","ingrediente"],
    foreignKeys = [
    ForeignKey(entity = RicettePreview::class,
     parentColumns = ["titolo"],
     childColumns = ["ricetta"],
        onDelete = CASCADE
    ),
    ForeignKey(entity = Ingrediente::class,
        parentColumns = ["ingrediente"],
        childColumns = ["ingrediente"])
])
data class IngredienteRIcetta(
    val ricetta:String,
    val ingrediente: String,
    @ColumnInfo(name="qta")
    val qta:String
)
