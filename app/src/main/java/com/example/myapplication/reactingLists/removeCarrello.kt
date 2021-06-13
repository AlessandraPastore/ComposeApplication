package com.example.myapplication.reactingLists

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.ShowEmpty
import com.example.myapplication.database.Ingrediente
import com.example.myapplication.database.RicetteViewModel

/*
Composable che gestisce la lista degli ingredienti nel Carrello
 */
@Composable
fun RemoveCarrello(model: RicetteViewModel) {

    // Lista degli ingredienti
    val ingredientList by model.listaCarrello.observeAsState()

    // Se la lista è vuota, mostro messaggio e immagine, altrimenti mostro la lista
    if(ingredientList != null) {
        if(ingredientList!!.isEmpty())
            ShowEmpty(stringResource(R.string.carrello))
        else
            ShowList(model, ingredientList!!)
    }
}

// Lista degli ingredienti nel carrello
@Composable
fun ShowList(model: RicetteViewModel, ingredientList: List<Ingrediente>) {

    // Funzione che istanzia la lista di ingredienti
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(items = ingredientList,
            itemContent = { _, ingredient ->

                // Mostra il nome dell'ingrediente e il pulsante elimina
                Card(
                    elevation = 5.dp,
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(start = 10.dp, top = 5.dp, bottom = 5.dp, end = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillParentMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            ingredient.ingrediente,
                            style = TextStyle(
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier.padding(16.dp)
                        )
                        IconButton(
                            onClick = {
                                model.updateCarrello(ingredient.ingrediente, false)
                            }
                        ) {
                            Icon(Icons.Rounded.Delete, "")
                        }
                    }
                }
            })

        // Box trasparente che permette lo scroll degli item fino a sopra la BottomBar, rendendoli tutti visibili
        item {
            Box(
                modifier = Modifier
                    .background(Color.Transparent)
                    .height(100.dp)
                    .fillMaxWidth()
            ) {}
        }
    }
}

