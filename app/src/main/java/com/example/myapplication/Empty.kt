package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

//Interfaccia da mostrare quando le varie schermate non hanno nessuna lista da visualizzare
@Composable
fun ShowEmpty(str: String){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
    ){
        Icon(painterResource(R.drawable.ic_round_visibility_off_24), "", modifier = Modifier.size(128.dp))
        Spacer(modifier = Modifier.height(30.dp))
        Text("$str " + stringResource(R.string.vuoto), style = MaterialTheme.typography.h5)
    }
}