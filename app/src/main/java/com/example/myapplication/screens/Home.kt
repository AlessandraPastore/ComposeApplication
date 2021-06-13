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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.example.myapplication.R
import com.example.myapplication.Screen
import com.example.myapplication.ShowEmpty
import com.example.myapplication.database.RicettePreview
import com.example.myapplication.database.RicetteViewModel
import com.example.myapplication.getFilters
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/*
Composable che gestisce l'interfaccia della Home e dei Preferiti
 */
@Composable
fun Home(model: RicetteViewModel, navController: NavController, listView: MutableState<Boolean>) {

    val ricette by model.ricette.observeAsState()   //lista delle ricette

    val isPreferiti by model.isPreferiti.observeAsState(false)     //capisce se ci troviamo in Home o nei Preferiti

    Log.d("Pref", isPreferiti.toString())

    var tipologia: String? = ""

    // Assegna il titolo mostrato nella TopBar di Home e Preferiti
    tipologia = if (isPreferiti)
        stringResource(R.string.preferiti)
    else
        stringResource(R.string.home)

    val expanded by model.expanded.observeAsState(false)
    val searching by model.searching.observeAsState(false)
    val longPressed by model.longPressed.observeAsState(false)

    Scaffold(
        topBar = {
            when{
                longPressed -> LongPress(navController, onLongPress = {model.onInvertPress()}, onBinClick = {model.onBinClick()}, onModify = {model.modifyRecipe()})
                searching -> Searching(model, navController, tipologia, onSearch = {model.onDisplaySearch(tipologia)}, onBack = {model.onBackFromSearch(tipologia)})
                else -> TopBar(model, navController , tipologia, expanded, onExpand = {model.onExpand(true)}, onDeExpand = {model.onExpand(false)}, onSearch = {model.onSearch(true)}, onApplicaClick = {model.onApplicaClick(tipologia)})
            }
        },
    )
    {

        // Se la lista di ricette è vuota, mostro il messaggio e l'immagine, altrimenti mostro la lista
        if(ricette != null) {
            if(ricette!!.isEmpty())
                ShowEmpty(str = tipologia)
            else
                ScrollableList(model, navController, ricette as List<RicettePreview>, longPressed, listView)
        }
    }
}

// Funzione che gestisce l'AppBar "ad alto livello"
@Composable
fun TopBar(
    model: RicetteViewModel,
    navController: NavController,
    tipologia: String,
    expanded: Boolean,
    onExpand: () -> Unit,
    onDeExpand: () -> Unit,
    onSearch: () -> Unit,
    onApplicaClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = tipologia)
        },

        // Pulsanti della TopBar, ricerca e filtri
        actions = {
            IconButton(onClick = onSearch) {
                Icon(Icons.Rounded.Search, contentDescription = "")
            }
            IconButton(onClick = onExpand) {
                Icon(painterResource(R.drawable.ic_round_filter_alt_24), contentDescription = "")
            }

            // Menù a tendina, viene mostrato al click dei filtri
            DropDown(
                model,
                navController,
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
    tipologia: String,
    expanded: Boolean,
    onDeExpand: () -> Unit,
    onApplicaClick: () -> Unit
) {

    // Lista dei filtri
    val filtri by model.filtri.observeAsState(getFilters())

    // Menù a tendina contenente i filtri
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDeExpand,
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxWidth(0.6f)
    ) {

        // Istanzio i singoli elementi del menù e ne gestisco la selezione/deselezione
        for (filtro in filtri.listIterator()) {

            var selected by remember { mutableStateOf(filtro.checked) }

            DropdownMenuItem(
                onClick = {
                    selected = !selected
                    filtro.checked = selected
                    },
                modifier = Modifier.wrapContentWidth()
                ){

                // Singola riga del menù, costituita da nome del filtro + icona di selezione
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ){
                    if (selected)
                        Icon(painterResource(R.drawable.ic_round_check_box_24), "")
                    else
                        Icon(painterResource(R.drawable.ic_round_check_box_outline_blank_24), "")
                     Text(filtro.name, modifier = Modifier.padding(5.dp))
                }
            }
        }

        // Contiene il pulsante per applicare i filtri e la relativa logica
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            val home = stringResource(R.string.home)
            Button(onClick = {

                onApplicaClick()

                if (tipologia == home) {
                    navController.navigate(Screen.Home.route)
                } else {
                    navController.navigate(Screen.Preferiti.route)
                }

                onDeExpand()

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
fun Searching(model: RicetteViewModel, navController: NavController,  tipologia: String, onSearch: () -> Unit, onBack: () -> Unit) {

    // Variabile che memorizza il testo digitato dall'utente
    val ricerca by model.ricerca.observeAsState("")

    // Variabile che gestisce la comparsa/scomparsa del Dialog
    val openDialog = remember { mutableStateOf(false)  }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(Color.Transparent)
                .fillMaxWidth()
        ){

            val home = stringResource(R.string.home)

            // Bottone <-: premendolo si esce dalla ricerca
            IconButton(
                onClick = {


                    onBack()

                    if(tipologia == home)
                        navController.navigate(Screen.Home.route)
                    else
                        navController.navigate(Screen.Preferiti.route)

                },
                modifier = Modifier.padding(top = 5.dp)
                ) {
                Icon(Icons.Rounded.ArrowBack, "")
            }

            // TextField in cui memorizzare il testo digitato dall'utente, lungo al massimo 20 caratteri
            OutlinedTextField(
                value = ricerca ,
                onValueChange = {
                        if(it.length <= 20)
                            model.onClickRicerca(it)
                        },
                placeholder = { Text(stringResource(R.string.Cerca)) },
                singleLine = true,
                modifier = Modifier.weight(3f)
            )

            // Bottone "cerca" per avviare la ricerca
            IconButton(
                onClick = {

                    // Il carattere % non può essere inserito poichè causa problemi
                    // con la query della ricerca
                    if(!ricerca.contains("%")) {
                        onSearch()

                    if (tipologia == home)
                        navController.navigate(Screen.Home.route)
                    else
                        navController.navigate(Screen.Preferiti.route)

                }
                else{
                    openDialog.value = true
                }
                          },
                modifier = Modifier
                    .padding(top = 5.dp)
                    .weight(1f)
            )
            {
                Icon(Icons.Rounded.Search,"")
            }

            // Dialog che viene visualizzato in caso l'utente inserisca il carattere %
            if(openDialog.value) {
                AlertDialog(
                    onDismissRequest = { openDialog.value = false },
                    title = { Text(text = stringResource(R.string.erroreSearch)) },
                    text = { Text(text = stringResource(R.string.erroreSearchDes)) },
                    confirmButton = {
                        Button(
                            onClick = {
                                openDialog.value = false
                            }) {
                            Text(text = stringResource(R.string.capito))
                        }
                    },
                    backgroundColor = MaterialTheme.colors.background
                )
            }
        }
}

// Funzione che gestisce il longPress di una determinaata ricetta
@Composable
fun LongPress(navController: NavController, onLongPress: () -> Unit, onBinClick: () -> Unit, onModify: () -> Unit) {

    // Coroutine utilizzata per gestire onModify()
    val scope = rememberCoroutineScope()

    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.selected),
                color = Color.White
            )
        },
        backgroundColor = MaterialTheme.colors.primaryVariant,
        contentColor = MaterialTheme.colors.onPrimary,

        // Bottone X, permette di "eliminare" il longpress e di tornare alla UI standard
        navigationIcon = {
            IconButton(onClick = onLongPress) {
                Icon(Icons.Rounded.Close, contentDescription = "", tint = Color.White)
            }
        },
        actions = {

            // Bottone della matita, permette di modificare la ricetta selezionata
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
                Icon(Icons.Rounded.Create, contentDescription = "", tint = Color.White.copy(alpha = LocalContentAlpha.current))
            }

            // Bottone del cestino, permette di eliminare la ricetta selezionata
            IconButton(onClick = {

                onBinClick()

            }) {
                Icon(Icons.Rounded.Delete, contentDescription = "", tint = Color.White.copy(alpha = LocalContentAlpha.current))
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

    // Funzione che crea la lista scrollabile di elementi
    LazyColumn {
        items(ricette){ ricetta ->

            /*
            In Compose le funzioni composable sono identificate dal compilatore attraverso
            il proprio call site (il punto nel codice in cui vengono invocate). In liste come questa,
            i singoli elementi della lista condividono lo stesso call site. Per permettere
            l'identificazione e realizzare una Recomposition più funzionale si utilizza la
            funzione key(), che permette di inserire un ulteriore ID.
             */
            key(ricetta.titolo) {

                val checked = ricetta.preferito

                color = if(ricettaSelezionata?.titolo.equals(ricetta.titolo) && longPressed)
                    MaterialTheme.colors.secondary
                else
                    MaterialTheme.colors.surface

                Card(
                    backgroundColor = color,
                    elevation = 5.dp,
                    shape = RoundedCornerShape(5.dp),
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(10.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                //Funzione che viene chiamata al Tap, mostrerà la ricetta in dettaglio
                                onTap = {

                                    if (!model.getLongPressed()) {
                                        model.selectRicetta(ricetta)
                                        model.getRicetta(ricetta.titolo)

                                        navController.navigate("${Screen.RicettaDetail.route}/${ricetta.titolo}")
                                    } else {
                                        model.onInvertPress()
                                        model.resetSelection()
                                    }
                                },
                                //Funzione chiamata al longPress, permetterà l'eliminazione o la modifica della ricetta
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
                                }
                            )
                        }
                ) {

                    // Funzioni che gestiscono i casi in cui l'utente voglia avere una lista a ListView o a GridView,
                    // implementate in HomeVariant.kt
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

        // Box trasparente che permette lo scroll degli item fino a sopra la BottomBar, rendendoli tutti visibili
        item{
            Box(modifier = Modifier
                .background(Color.Transparent)
                .height(100.dp)
                .fillMaxWidth()
            ){}
        }
    }
}