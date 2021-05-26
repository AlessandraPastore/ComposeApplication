package com.example.myapplication.ui.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color
/*
val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)*/



val Gray800 = Color(0xFF424242)
val Gray900 = Color(0xFF212121)


val DeepPurple500 = Color(0xFF673AB7)
val DeepPurple700 = Color(0xFF512DA8)
val DeepPurple100 = Color(0xFFD1C4E9)


val Complementary = Color(0xFF89B73A)





val LightColors = lightColors(


    primary = DeepPurple500,
    primaryVariant = DeepPurple700,
    secondary = Complementary,
    surface = DeepPurple100

    /* Other default colors to override
    background = Color.White,

    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)


val DarkColors = darkColors(
    primary = Gray800,
    primaryVariant = Gray900,
    secondary = Complementary,
    onPrimary = Color.White
)