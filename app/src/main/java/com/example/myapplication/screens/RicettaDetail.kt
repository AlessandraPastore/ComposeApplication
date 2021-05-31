package com.example.myapplication.screens

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.example.myapplication.Screen
import com.example.myapplication.database.IngredienteRIcetta
import com.example.myapplication.database.RicetteViewModel

@Composable
fun RicettaDetail(model: RicetteViewModel ,navController: NavController, ricetta: String?){


    val isPreferiti = model.getTipologia()

    var tipologia: String? = "Home"

    if(isPreferiti != null) {
        if (isPreferiti)
            tipologia = "Preferiti"
        else
            tipologia = "Home"
    }

    //val ricettaCompleta by model.ricettaCompleta.observeAsState()

    val ricettaCompleta = model.getRicettaCompleta()

    val scrollState = rememberScrollState()

    Scaffold{
        Box(modifier = Modifier.fillMaxSize()){
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)
            ){
                //al posto della box andrà l'immagine
                Box(
                    modifier = Modifier
                        .clip(RectangleShape)
                        .background(Color.Red)
                        .fillMaxWidth()
                        .height(300.dp)
                )

                //Spacer(modifier = Modifier.height(10.dp))

                //contiene tutti i detail della ricetta
                Column(
                    modifier = Modifier.padding(25.dp)
                ){
                    Text(
                        text ="${ricetta}",
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                    ){
                        for(item in ricettaCompleta!!.filtri){
                            Box(
                                modifier = Modifier.padding(5.dp)
                            ){
                                Text(
                                    text = item.name,
                                    style = MaterialTheme.typography.caption,
                                    fontWeight = FontWeight.Bold,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.background(
                                        color = colors.secondary,
                                        shape = RoundedCornerShape(15.dp)
                                    ).padding(7.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text ="Ingredienti",
                        style = MaterialTheme.typography.h5
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    //avremo una lista di ingredienti da mostrare
                    Column {

                        for(item in ricettaCompleta!!.ingredienti){
                            IngredientItem(model, navController, item)
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text ="Preparazione",
                        style = MaterialTheme.typography.h5
                    )
                    Spacer(modifier = Modifier.height(16.dp))


                    Text(ricettaCompleta!!.descrizione)

                    Box(modifier = Modifier
                        .background(Color.Transparent)
                        .height(100.dp)
                        .fillMaxWidth()
                    )
                }
            }
            FadingTopBar(model, scrollState, navController, tipologia as String)
        }

    }

}

//Top Bar che compare a seconda della scrollPosition per mostrare l'immagine di copertina in maniera adeguata
@Composable
fun FadingTopBar(
    model: RicetteViewModel,
    scrollState: ScrollState,
    navController: NavController,
    tipologia:String
){
    Box{
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(
                    //fade della topBar
                    kotlin.math.max(
                        0f,
                        scrollState.value / (scrollState.maxValue.toFloat())
                    )
                )
                .height(56.dp)
                .background(color = MaterialTheme.colors.primary),
        )
        IconButton(
            onClick = {
                //model.selectRicetta(RicettePreview("",false))
                model.resetSelection()
                model.resetComplete()

                if(tipologia == "Home") {
                    model.onHomeClick()
                    navController.navigate(Screen.Home.route){
                        popUpTo = navController.graph.startDestination
                        launchSingleTop = true
                    }

                }
                else {
                    model.onPreferitiClick()
                    navController.navigate(Screen.Preferiti.route){
                        popUpTo = navController.graph.startDestination
                        launchSingleTop = true
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.CenterStart)
        )
        {
            Icon(Icons.Rounded.ArrowBack, contentDescription = "")
        }
        IconButton(
            onClick = {
                model.onRicettaDelete()
                model.resetComplete()

                if(tipologia == "Home") {
                    model.onHomeClick()
                    navController.navigate(Screen.Home.route){
                        popUpTo = navController.graph.startDestination
                        launchSingleTop = true
                    }

                }
                else {
                    model.onPreferitiClick()
                    navController.navigate(Screen.Preferiti.route){
                        popUpTo = navController.graph.startDestination
                        launchSingleTop = true
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.CenterEnd)
        )
        {
            Icon(Icons.Rounded.Delete, contentDescription = "")
        }
    }
}

//mostra il singolo ingrediente comprendendo l'icona aggiungi a carrello
@Composable
fun IngredientItem(model: RicetteViewModel, navController: NavController, item: IngredienteRIcetta) {

    val checked = remember{ mutableStateOf(false)}

    checked.value = model.isInCarrello(item.ingrediente)

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ){
        Column{
            Text(item.ingrediente)
            Text(item.qta)
        }
        IconToggleButton(
            checked = checked.value,
            onCheckedChange = {
                checked.value = !checked.value
                model.updateCarrello(item.ingrediente, checked.value)
        }) {
            Log.d("Checked", checked.toString() + item.ingrediente)

            if(checked.value) {
                Icon(Icons.Rounded.Check, "")
            }
            else {
                Icon(Icons.Rounded.Add, "")
            }
        }

    }

    Divider(
        modifier = Modifier.padding(top = 5.dp, start = 15.dp, end = 15.dp, bottom = 5.dp)
    )
}