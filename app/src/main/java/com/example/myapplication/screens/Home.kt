package com.example.myapplication.screens

import android.util.Log

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.example.myapplication.*
import com.example.myapplication.R
import com.example.myapplication.database.RicettePreview
import com.example.myapplication.database.RicetteViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Home(model: RicetteViewModel, navController: NavController, listView: MutableState<Boolean>) {

    val ricette by model.ricette.observeAsState()

    val isPreferiti = model.getTipologia()

    var tipologia: String? = "tmp"

    if(isPreferiti != null) {
        if (isPreferiti)
            tipologia = "Preferiti"
        else
            tipologia = "Home"
    }

    val expanded by model.expanded.observeAsState(false)
    val searching by model.searching.observeAsState(false)
    val longPressed by model.longPressed.observeAsState(false)

    Scaffold(
        topBar = {
            when{
                longPressed -> LongPress(navController, onLongPress = {model.onInvertPress()}, onBinClick = {model.onBinClick()}, onModify = {model.modifyRecipe()})
                searching -> Searching(model, navController, tipologia as String) { model.onSearch(false) }
                else -> TopBar(navController , model, tipologia as String, expanded, onExpand = {model.onExpand(true)}, onDeExpand = {model.onExpand(false)}, onSearch = {model.onSearch(true)}, onApplicaClick = {model.onApplicaClick(tipologia)})
            }
        },
    )
    {
        if(ricette != null) {
            if(ricette!!.isEmpty())
                tipologia?.let { it1 -> showEmpty(str = it1) }
            else
                ScrollableList(model, navController, ricette as List<RicettePreview>, longPressed, listView)
        }
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
    onSearch: () -> Unit,
    onApplicaClick: () -> Unit
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
            DropDown(
                model,
                navController,
                filtri,
                tipologia,
                expanded,
                onDeExpand = onDeExpand,
                onApplicaClick = onApplicaClick
            )
        }
    )
}

// Funzione che gestisce l'icona del filtro
@Composable
fun DropDown(
    model: RicetteViewModel,
    navController: NavController,
    filtri: List<Filtro>,
    tipologia: String,
    expanded: Boolean,
    onDeExpand: () -> Unit,
    onApplicaClick: () -> Unit
) {

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDeExpand,
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxWidth(0.6f)
    ) {

            for (filtro in filtri.listIterator()) {

                var selected by remember { mutableStateOf(filtro.checked) }

                DropdownMenuItem(
                    onClick = {
                    selected = !selected
                    filtro.checked = selected
                    },
                    modifier = Modifier.wrapContentWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        //horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        if (selected)
                            Icon(Icons.Rounded.CheckBox, "")
                        else
                            Icon(Icons.Rounded.CropSquare, "")

                        Text(filtro.name, modifier = Modifier.padding(5.dp))
                    }
                }
            }

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = {

                //onApplicaClick()

                if (tipologia == "Home") {
                    model.onHomeClick()
                    navController.navigate(Screen.Home.route)
                } else {
                    model.onPreferitiClick()
                    navController.navigate(Screen.Preferiti.route)
                }

                model.onExpand(false)

            },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                Text(stringResource(R.string.Applica))
            }
        }
    }
}

// Funzione che gestisce l'icona della ricerca
@Composable
fun Searching(model: RicetteViewModel, navController: NavController,  tipologia: String, onSearch: () -> Unit) {

    val input = remember{ mutableStateOf(TextFieldValue())}

    val openDialog = remember { mutableStateOf(false)  }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(Color.Transparent)
                .fillMaxWidth()
        ){
            // Bottone <-: premendolo si esce dalla ricerca
            IconButton(
                onClick = {

                model.onBackFromSearch(tipologia)

                if(tipologia == "Home")
                    navController.navigate(Screen.Home.route)
                else
                    navController.navigate(Screen.Preferiti.route)
                },
                modifier = Modifier.padding(top = 5.dp)
                )
            {
                Icon(Icons.Rounded.ArrowBack, contentDescription = null)
            }

            // TextField in cui memorizzare il testo digitato dall'utente
            OutlinedTextField(
                value = input.value,
                onValueChange = {
                    if(it.text.length <= 20)
                        input.value = it
                                },
                placeholder = { Text(stringResource(R.string.Search)) },
                singleLine = true,
                modifier = Modifier.weight(3f)
            )

            // Bottone "cerca" per avviare la ricerca
            Button(
                onClick = {

                if(!input.value.text.contains("%")) {
                    model.onDisplaySearch(tipologia, input.value.text + "%")

                    if (tipologia == "Home")
                        navController.navigate(Screen.Home.route)
                    else
                        navController.navigate(Screen.Preferiti.route)
                }
                else{
                    openDialog.value = true
                }
                          },
                modifier = Modifier
                    .padding(5.dp)
                    .weight(1f)
            )
            {
                Text("cerca")
            }

            if(openDialog.value) {
                AlertDialog(
                    onDismissRequest = { openDialog.value = false },
                    title = { Text(text = "Errore nei caratteri inseriti") },
                    text = { Text(text = "Non è possibile usare il carattere % per la ricerca") },
                    confirmButton = {
                        Button(
                            onClick = {
                                openDialog.value = false
                            }) {
                            Text("Capito")
                        }
                    },
                    backgroundColor = MaterialTheme.colors.background
                )
            }
        }
}

// Funzione che gestisce il longPress di una determinaata ricetta.
@Composable
fun LongPress(navController: NavController, onLongPress: () -> Unit, onBinClick: () -> Unit, onModify: () -> Unit) {

    val scope = rememberCoroutineScope()

    TopAppBar(
        title = {
            Text(text = stringResource(R.string.selected))
        },
        backgroundColor = MaterialTheme.colors.primaryVariant,
        contentColor = MaterialTheme.colors.onPrimary,

        // Bottone X
        navigationIcon = {
            IconButton(onClick = onLongPress) {
                Icon(Icons.Rounded.Close, contentDescription = "")
            }
        },
        actions = {

            // Bottone della matita
            IconButton(onClick = {

                scope.launch{
                    onModify()
                    delay(1000)
                }


                navController.navigate(Screen.NuovaRicetta.route){

                    popUpTo = navController.graph.startDestination
                    launchSingleTop = true

                }

            }) {
                Icon(Icons.Rounded.Create, contentDescription = "")
            }

            // Bottone del cestino
            IconButton(onClick = {

                onBinClick()

            }) {
                Icon(Icons.Rounded.Delete, contentDescription = "")
            }
        }
    )
}

// Funzione che gestisce la lista delle ricette
@Composable
fun ScrollableList(
    model: RicetteViewModel,
    navController: NavController,
    ricette: List<RicettePreview>,
    longPressed: Boolean,
    listView: MutableState<Boolean>
) {

    val ricettaSelezionata by model.ricettaSelezionata.observeAsState()
    var color : Color

    LazyColumn {
        items(ricette){ ricetta ->

            key(ricetta.titolo) {

                val checked = ricetta.preferito

                color = if(ricettaSelezionata?.titolo.equals(ricetta.titolo) && longPressed)
                    MaterialTheme.colors.secondary
                else
                    MaterialTheme.colors.surface

                //Row(modifier = Modifier.fillParentMaxWidth()) {
                Card(
                    backgroundColor = color,
                    elevation = 5.dp,
                    shape = RoundedCornerShape(5.dp),
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(10.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {

                                    //Log.d("Tap",longPressed.toString())

                                    if (!model.getLongPressed()) {
                                        model.selectRicetta(ricetta)
                                        model.getRicetta(ricetta.titolo)

                                        navController.navigate("${Screen.RicettaDetail.route}/${ricetta.titolo}")
                                    } else {
                                        //if(!ricettaSelezionata?.titolo.equals(ricetta.titolo)){
                                        model.onInvertPress()
                                        model.resetSelection()
                                        //}
                                    }
                                },
                                onLongPress = {

                                    if (!model.getLongPressed()) {
                                        model.onInvertPress()
                                        model.selectRicetta(ricetta)
                                    } else {
                                        if (ricettaSelezionata?.titolo.equals(ricetta.titolo)) {
                                            model.onInvertPress()
                                            model.resetSelection()
                                        }
                                    }
                                    Log.d("Tap", longPressed.toString())
                                }
                            )
                        }
                ) {

                    if(listView.value)
                        ListVariant(
                            model = model,
                            ricetta = ricetta,
                            checked = checked
                        )
                    else
                        GridVariant(
                        model = model,
                        ricetta = ricetta,
                        checked = checked
                        )
                }
            }
        }
        item{
            Box(modifier = Modifier
                .background(Color.Transparent)
                .height(100.dp)
                .fillMaxWidth()
            ){}
        }
    }
}