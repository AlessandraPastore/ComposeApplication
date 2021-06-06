package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun ShowEmpty(str: String){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
    ){
        Icon(painterResource(R.drawable.ic_round_visibility_off_24)/*Icons.Rounded.VisibilityOff*/, "", modifier = Modifier.size(128.dp))
        Spacer(modifier = Modifier.height(30.dp))
        Text("$str è vuoto", style = MaterialTheme.typography.h5)
    }
}