package com.example.myapplication.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Categoria(
    @PrimaryKey
    val categoria:String
)
