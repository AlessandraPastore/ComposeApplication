package com.example.myapplication.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State

// Perchè State<Boolean?> in darkTheme
@Composable
fun MyApplicationTheme(
    darkTheme: MutableState<Boolean>,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme.value) {
        DarkColors
    } else {
        LightColors
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}