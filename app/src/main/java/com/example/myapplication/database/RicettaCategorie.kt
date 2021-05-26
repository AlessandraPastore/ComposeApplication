package com.example.myapplication.database

import androidx.compose.ui.focus.FocusOrder
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

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
