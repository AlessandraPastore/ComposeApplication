package com.example.myapplication.screens

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.example.myapplication.*
import com.example.myapplication.R
import com.example.myapplication.database.IngredienteRIcetta
import com.example.myapplication.database.RicetteViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/*
Composable che gestisce l'interfaccia delle ricette in Dettaglio
 */
@Composable
fun RicettaDetail(model: RicetteViewModel ,navController: NavController, ricetta: String?){

    val isPreferiti = model.getTipologia()

    var tipologia: String? = stringResource(R.string.home)

    if(isPreferiti != null) {
        tipologia = if (isPreferiti)
            stringResource(R.string.preferiti)
        else
            stringResource(R.string.home)
    }

    val modify = model.getModify()

    // Recupero la reference a MainActivity
    val main=MainActivity.get()

    //carica la ricetta da mostrare
    val ricettaCompleta :RicettaSample
    runBlocking {
         ricettaCompleta = model.getRicettaCompleta()
    }

    //gestisce il caso in cui l'utente utilizzi il tasto back del telefono
    BackHandler {
        backPress(main, model, navController as NavHostController, modify, ricettaCompleta.titolo)
    }

    val scrollState = rememberScrollState()

    Scaffold{
        Box(modifier = Modifier.fillMaxSize()){
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .fillMaxSize()
                    .background(colors.background)
            ){
                //contiene l'immagine
                Box(
                    modifier = Modifier
                        .clip(RectangleShape)
                        .background(Color.Red)
                        .fillMaxWidth()
                        .height(300.dp)
                )
                {
                    val cat = model.getCategoria(ricettaCompleta.titolo)

                   if(ricettaCompleta.uri.isNullOrEmpty())
                       RicettaImage(null,true,cat)
                    else
                        RicettaImage( Uri.parse(ricettaCompleta.uri),true,cat )
                }

                //contiene tutti i detail della ricetta
                Column(
                    modifier = Modifier.padding(25.dp)
                ){
                    //titolo
                    Text(
                        text ="$ricetta",
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    //filtri
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ){
                        SimpleFlowRow(
                            alignment = Alignment.End,
                        ) {
                            for(item in ricettaCompleta.filtri){
                                Box(
                                    modifier = Modifier.padding(5.dp)
                                ){
                                    Text(
                                        text = item.name,
                                        color = colors.onSecondary,
                                        style = MaterialTheme.typography.caption,
                                        fontWeight = FontWeight.Bold,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier
                                            .background(
                                                color = colors.secondary,
                                                shape = RoundedCornerShape(15.dp)
                                            )
                                            .padding(7.dp)
                                    )
                                }
                            }
                        }
                    }


                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(R.string.ingredienti),
                        style = MaterialTheme.typography.h5
                    )

                    Spacer(modifier = Modifier.height(13.dp))

                    //lista degli ingredienti
                    Surface(
                        shape = RoundedCornerShape(15.dp)
                    ){
                        Column(
                            modifier = Modifier.padding(10.dp)
                        ) {

                            for(item in ricettaCompleta.ingredienti){
                                IngredientItem(model, item)
                            }
                        }
                    }


                    //Preparazione della ricetta
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = stringResource(R.string.preparazione),
                        style = MaterialTheme.typography.h5
                    )
                    Spacer(modifier = Modifier.height(16.dp))


                    Text(ricettaCompleta.descrizione)

                    Box(modifier = Modifier
                        .background(Color.Transparent)
                        .height(100.dp)
                        .fillMaxWidth()
                    )
                }
            }

            //Custom TopBar che parte trasparente ma torna visibile allo scroll dell'utente
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

    val home = stringResource(R.string.home)

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
                .background(color = colors.primary)
        )

        //Pulsante back
        IconButton(
            onClick = {
                model.resetSelection()
                model.resetComplete()

                if(tipologia == home) {
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
            Icon(
                Icons.Rounded.ArrowBack,
                contentDescription = "",
                modifier = Modifier
                    .background(color = colors.primary, shape = CircleShape)
                    .padding(8.dp),
                tint = colors.onPrimary.copy(alpha = LocalContentAlpha.current)
            )
        }

        //Pulsanti Action della TopBar
        val scope = rememberCoroutineScope()
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ){
            // Pulsante di modifica
            IconButton(onClick = {

                scope.launch{
                    model.isFromDetails()
                    model.modifyRecipe()
                    delay(1000)
                }


                navController.navigate(Screen.NuovaRicetta.route){

                    popUpTo = navController.graph.startDestination
                    launchSingleTop = true

                }

            }
            ) {
                Icon(
                    Icons.Rounded.Create,
                    contentDescription = "",
                    modifier = Modifier
                        .background(color = colors.primary, shape = CircleShape)
                        .padding(8.dp),
                    tint = colors.onPrimary.copy(alpha = LocalContentAlpha.current)
                )
            }

            //Pulsante di elimina
            IconButton(
                onClick = {
                    model.onRicettaDelete()
                    model.resetComplete()

                    if(tipologia == home) {
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
            )
            {
                Icon(
                    Icons.Rounded.Delete,
                    contentDescription = "",
                    modifier = Modifier
                        .background(color = colors.primary, shape = CircleShape)
                        .padding(8.dp),
                    tint = colors.onPrimary.copy(alpha = LocalContentAlpha.current)
                )
            }
        }

    }
}

//mostra il singolo ingrediente e l'icona aggiungi a carrello
@Composable
fun IngredientItem(model: RicetteViewModel, item: IngredienteRIcetta) {

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