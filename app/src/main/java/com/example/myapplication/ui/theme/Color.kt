package com.example.myapplication.ui.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color


val Gray900 = Color(0xFF212121)

val Red500 = Color(0xFFD32F2F)
val Red700 = Color(0xFFB71C1C)
val DeepOrange100 = Color(0xFFFFECB3)

val Amber600 = Color(0xFFFFA000)







val LightColors = lightColors(

    primary = Red500,
    primaryVariant = Red700,
    secondary = Amber600,
    surface = DeepOrange100,

    //il resto è di default
)


val DarkColors = darkColors(

    primary = Amber600,
    primaryVariant = Gray900,
    secondary = Amber600,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    surface = Color.Black

    //il resto è di default
)