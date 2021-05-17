package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Light
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.ModeNight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun Impostazioni(enableDarkMode: MutableState<Boolean>) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = stringResource(R.string.impostazioni))
            })
        }
    ){

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()



        ){
            if(enableDarkMode.value)    Icon(Icons.Rounded.ModeNight, contentDescription = "", modifier = Modifier.scale(2F))
            else Icon(Icons.Rounded.LightMode, contentDescription = "", modifier = Modifier.scale(2F))

            Spacer(modifier = Modifier.height(20.dp))
            
            Text(
                text = "clicca per cambiare modalità",
                textAlign = TextAlign.Center
                )
            Switch(
                checked = enableDarkMode.value,
                onCheckedChange= { enableDarkMode.value = !enableDarkMode.value  }
                )
        }
    }
}