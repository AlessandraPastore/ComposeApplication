package com.example.myapplication.database

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(foreignKeys = [
    ForeignKey(entity = RicettePreview::class,
    parentColumns = ["titolo"],
    childColumns = ["titolo"],
    onDelete = CASCADE)
])
data class RicettaCompleta (
    @PrimaryKey
    val titolo:String,

    @ColumnInfo (name = "descrizione")
    val descrizione:String,

    @ColumnInfo
    val uri:String?=null
    )