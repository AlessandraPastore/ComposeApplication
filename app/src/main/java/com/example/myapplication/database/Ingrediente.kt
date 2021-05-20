package com.example.myapplication.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Ingrediente(
    @PrimaryKey
    val ingrediente: String,

    @ColumnInfo(name="inCarrello")
    val inCarrello:Boolean
)
