package com.example.myapplication.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RicettePreview(
    @PrimaryKey
    val titolo:String,
    @ColumnInfo(name="preferito")
    val preferito:Boolean=false,
    @ColumnInfo
    val uri:String?=null

)
