package com.example.myapplication.screens

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.example.myapplication.RicettaImage
import com.example.myapplication.Screen
import com.example.myapplication.database.RicettePreview
import com.example.myapplication.database.RicetteViewModel

@Composable
fun GridVariant(
    model: RicetteViewModel,
    ricetta: RicettePreview,
    checked : Boolean
){
    Column(){

        //contiene l'immagine
        Surface(
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.height(250.dp).fillMaxWidth()
        ) {
            if(ricetta.uri!=null) {
                RicettaImage(Uri.parse(ricetta.uri))
                Log.d("Testuri",Uri.parse(ricetta.uri).toString())
            }
            else
                RicettaImage(urStr = null)
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ){
            Text(
                text = ricetta.titolo,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(15.dp),
            )

            IconToggleButton(
                checked = checked,
                onCheckedChange = {
                    model.onPreferitoChange(ric = RicettePreview(ricetta.titolo, !checked,ricetta.uri)
                    )
                }) {
                if (!checked) Icon(Icons.Rounded.FavoriteBorder, "")
                else Icon(Icons.Rounded.Favorite, "")
            }
        }
    }
}

@Composable
fun ListVariant(
    model: RicetteViewModel,
    ricetta: RicettePreview,
    checked : Boolean
){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {

        //contiene l'immagine
        Surface(
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.size(130.dp),
        ) {
            if(ricetta.uri!=null) {
                RicettaImage(Uri.parse(ricetta.uri))
                Log.d("Testuri",Uri.parse(ricetta.uri).toString())
            }
            else
                RicettaImage(urStr = null)
        }
        Column(
            modifier = Modifier
                .padding(start = 12.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = ricetta.titolo,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(15.dp),
            )
        }
        Column(
            modifier = Modifier
                .padding(end = 12.dp)
                .align(Alignment.CenterVertically)
        ) {
            IconToggleButton(
                checked = checked,
                onCheckedChange = {
                    model.onPreferitoChange(ric = RicettePreview(ricetta.titolo, !checked,ricetta.uri)
                    )
                }) {
                if (!checked) Icon(Icons.Rounded.FavoriteBorder, "")
                else Icon(Icons.Rounded.Favorite, "")
            }
        }
    }
}
