package com.example.myapplication.screens

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
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.example.myapplication.Filtro
import com.example.myapplication.R
import com.example.myapplication.Screen
import com.example.myapplication.database.RicettePreview
import com.example.myapplication.database.RicetteViewModel
import com.example.myapplication.getFilters
import kotlinx.coroutines.launch


@Composable
fun Home(model: RicetteViewModel, navController: NavController, tipologia: String = "Home") {

    val ricette by model.ricette.observeAsState()

    val expanded by model.expanded.observeAsState(false)
    val searching by model.searching.observeAsState(false)
    val longPressed by model.longPressed.observeAsState(false)

    Scaffold(
        topBar = {
            when{
                longPressed -> LongPress(model, onLongPress = {
                    model.onInvertPress()
                    model.resetSelection()
                })
                searching -> Searching(onSearch = {model.onSearch(false)})
                else -> TopBar(navController , model, tipologia, expanded, onExpand = {model.onExpand(true)}, onDeExpand = {model.onExpand(false)}, onSearch = {model.onSearch(true)})
            }
        },
    )
    {
        if(ricette != null)
            ScrollableLIst(model, navController, ricette as List<RicettePreview>, onLongPress = {model.onInvertPress()})
    }
}

// Funzione che gestisce l'AppBar "ad alto livello"
@Composable
fun TopBar(
    navController: NavController,
    model: RicetteViewModel,
    tipologia: String,
    expanded: Boolean,
    onExpand: () -> Unit,
    onDeExpand: () -> Unit,
    onSearch: () -> Unit
) {
    val filtri by model.filtri.observeAsState(getFilters())

    TopAppBar(
        title = {
            Text(text = tipologia)
        },
        actions = {
            IconButton(onClick = onSearch) {
                Icon(Icons.Rounded.Search, contentDescription = "")
            }
            IconButton(onClick = onExpand) {
                Icon(Icons.Rounded.FilterAlt, contentDescription = "")
            }
            DropDown(navController , model, filtri, expanded, onDeExpand = onDeExpand)
        }
    )
}

// Funzione che gestisce l'icona del filtro
@Composable
fun DropDown(
    navController: NavController,
    model: RicetteViewModel,
    filtri: List<Filtro>,
    expanded: Boolean,
    onDeExpand: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDeExpand,
        modifier = Modifier
            .wrapContentSize()
            .background(MaterialTheme.colors.background)

    ) {

        for(filtro in filtri.listIterator()){

            val selected = remember { mutableStateOf(filtro.checked) }

            DropdownMenuItem(onClick = {
                selected.value = !selected.value
                filtro.checked  = selected.value

                //model.onHomeClick()
                //Log.d("Test",filtro.checked.toString() + filtro.name)
                //checked.value = filter.checked

            }) {
                Row{
                    if(selected.value)
                        Icon(Icons.Rounded.CheckBox, "")
                    else
                        Icon(Icons.Rounded.CropSquare, "")

                    Text(filtro.name)
                }
            }
        }

        Button(onClick = {
            model.onHomeClick()

            model.onExpand(false)

            navController.navigate(Screen.Home.route)

            Log.d("Prova", model.ricette.value.toString())
        }) {
            Text("Applica")
        }


    }
}

// Funzione che gestisce l'icona della ricerca
@Composable
fun Searching(onSearch: () -> Unit) {

    // Forse hoisting? Provare stringa
    val input = remember{ mutableStateOf(TextFieldValue())}

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.background(Color.Transparent)
        ){
            IconButton(onClick = onSearch)
            {
                Icon(Icons.Rounded.ArrowBack, contentDescription = null)
            }
            OutlinedTextField(
                value = input.value,
                onValueChange = {
                    if(it.text.length <= 20) input.value = it
                                },
                placeholder = {Text(text = "Search")},
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                trailingIcon = {Icon(Icons.Rounded.Search, contentDescription = "")},
                singleLine = true,
            )
        }
}

// Funzione che gestisce il longPress di una determinaata ricetta.
// Ho notato che dopo aver premuto una ricetta, se si ripreme la stessa ricetta o
// se ne preme un'altra, lo stato di longPressed cambia: bug o feature?
@Composable
fun LongPress(model: RicetteViewModel, onLongPress: () -> Unit) {

    //val ricetta = model.ricettaSelezionata.observeAsState()

    TopAppBar(
        title = {
            Text(text = stringResource(R.string.selected))
        },
        backgroundColor = MaterialTheme.colors.primaryVariant,
        contentColor = MaterialTheme.colors.onPrimary,
        navigationIcon = {
            IconButton(onClick = onLongPress) {
                Icon(Icons.Rounded.Close, contentDescription = "")
            }
        },
        actions = {
            IconButton(onClick = {  }) {
                Icon(Icons.Rounded.Create, contentDescription = "")
            }
            IconButton(onClick = {
                model.onRicettaDelete()
                model.onInvertPress()
                model.resetSelection()
            }) {
                Icon(Icons.Rounded.Delete, contentDescription = "")
            }
        }
    )
}

// Funzione che gestisce la lista delle ricette
@Composable
fun ScrollableLIst(model: RicetteViewModel, navController: NavController, ricette: List<RicettePreview>, onLongPress: (Offset) -> Unit) {

    val scope = rememberCoroutineScope()
    val longPressed by model.longPressed.observeAsState()
    val ricettaSelezionata by model.ricettaSelezionata.observeAsState()

    var color : Color
    //if(longPressed == true) color = MaterialTheme.colors.secondary

    LazyColumn {
        items(ricette) {   ricetta ->

            val checked = ricetta.preferito
            if(ricettaSelezionata?.titolo.equals(ricetta.titolo)) color = MaterialTheme.colors.secondary
            else color = MaterialTheme.colors.surface

            //Row(modifier = Modifier.fillParentMaxWidth()) {
                Card(
                    backgroundColor = color,
                    elevation = 5.dp,
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(10.dp)
                        .pointerInput(Unit) {
                            // Forse coroutine inutile qua
                            scope.launch {
                                detectTapGestures(
                                    onTap = {
                                        model.getRicetta(ricetta.titolo)
                                        model.selectRicetta(ricetta)
                                        navController.navigate("${Screen.RicettaDetail.route}/${ricetta.titolo}")
                                    },
                                    onLongPress = {
                                        //onLongPress tolto perchè devo chiamare selectRic sorry
                                        model.onInvertPress()
                                        if (longPressed == true)
                                            model.selectRicetta(ricetta)
                                        else
                                            model.resetSelection()
                                    }
                                )
                            }
                        }
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        //sarebbe l'immagine, poi da cambiare
                        Surface(
                            color = MaterialTheme.colors.onSurface,
                            elevation = 19.dp,
                            border = BorderStroke(1.dp, Color.Gray),
                            modifier = Modifier.size(130.dp),
                        ) {}
                        Column(
                            modifier = Modifier
                                .padding(start = 12.dp)
                                .align(Alignment.CenterVertically)
                                //.background(color)
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
                                onCheckedChange = {model.onPreferitoChange(ric = RicettePreview(ricetta.titolo,!checked))}) {
                                if (!checked) Icon(Icons.Rounded.FavoriteBorder, "")
                                else Icon(Icons.Rounded.Favorite, "")
                            }
                        }
                    }
                }
            //}
        }
        item(){
            Box(modifier = Modifier
                .background(Color.Transparent)
                .height(100.dp)
                .fillMaxWidth()
            ){}
        }
    }
}