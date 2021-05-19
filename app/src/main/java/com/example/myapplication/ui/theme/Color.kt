package com.example.myapplication.ui.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color
/*
val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)*/

val Purple200 = Color(0xFFFFF9C4)
val Purple500 = Color(0xFF6A1B9A)
val Purple700 = Color(0xFF4A148C)
val Teal200 = Color(0xFFF9A825) //bleah




val LightColors = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200,
    surface = Purple200,

    /* Other default colors to override
    background = Color.White,

    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

//da sistemare
val DarkColors = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)