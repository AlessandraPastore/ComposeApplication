package com.example.myapplication.database

import androidx.compose.ui.focus.FocusOrder
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity (
    primaryKeys=["ricetta","categoria"],
    foreignKeys = [
        ForeignKey(entity = Categoria::class,
        parentColumns = ["categoria"],
        childColumns = ["categoria"]),
    ForeignKey(entity = RicettePreview::class,
        parentColumns = ["titolo"],
        childColumns = ["ricetta"],
        onDelete = CASCADE
        )
    ]
        )
data class RicettaCategorie(
    val ricetta:String,
    val categoria: String
)
