package com.example.myapplication.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE

@Entity (
    primaryKeys=["titolo","categoria"],
    foreignKeys = [
        ForeignKey(entity = Categoria::class,
        parentColumns = ["categoria"],
        childColumns = ["categoria"]),
    ForeignKey(entity = RicettePreview::class,
        parentColumns = ["titolo"],
        childColumns = ["titolo"],
        onDelete = CASCADE
        )
    ]
        )
data class RicettaCategorie(
    val titolo:String,
    val categoria: String
)
