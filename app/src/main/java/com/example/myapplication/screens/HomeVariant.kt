package com.example.myapplication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplication.RicettaImage
import com.example.myapplication.database.RicettePreview
import com.example.myapplication.database.RicetteViewModel

//Funzione composable che permette la vista GridView degli elementi della lista in Home e Preferiti
@Composable
fun GridVariant(
    model: RicetteViewModel,
    ricetta: RicettePreview,
    checked : Boolean
){
    val cat = model.getCategoria(ricetta.titolo)

    Column{

        //contiene l'immagine
        Surface(
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.height(250.dp).fillMaxWidth()
        ) {
                RicettaImage(null,false,cat)
        }

        //contiene titolo e pulsante per l'aggiunta ai preferiti
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ){
            Text(
                text = ricetta.titolo,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(15.dp)
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

//Funzione composable che permette la vista ListView degli elementi della lista in Home e Preferiti
@Composable
fun ListVariant(
    model: RicetteViewModel,
    ricetta: RicettePreview,
    checked : Boolean
){

    val cat = model.getCategoria(ricetta.titolo)

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {

        //contiene l'immagine
        Surface(
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.size(130.dp),
        ) {

                RicettaImage(null,false,cat)
        }

        //contiene il titolo
        Column(
            modifier = Modifier
                .padding(start = 12.dp)
                .align(Alignment.CenterVertically)
                .weight(2f)
        ) {
            Text(
                text = ricetta.titolo,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(15.dp),
            )
        }

        //contiene il pulsante per l'aggiunta ai preferiti
        Column(
            modifier = Modifier
                .padding(end = 12.dp)
                .align(Alignment.CenterVertically)
                .weight(1f)
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
