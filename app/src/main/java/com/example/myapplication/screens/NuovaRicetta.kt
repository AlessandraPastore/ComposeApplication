package com.example.myapplication.screens
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.example.myapplication.*
import com.example.myapplication.R
import com.example.myapplication.database.IngredienteRIcetta
import com.example.myapplication.database.RicetteViewModel
import com.example.myapplication.reactingLists.DialogIngredient
import com.example.myapplication.reactingLists.NewIngredient
import kotlinx.coroutines.runBlocking


/*
Composable che gestisce l'interfaccia di Nuova Ricetta o Modifica Ricetta
 */
@Synchronized
@Composable
fun NuovaRicetta(
    model: RicetteViewModel,
    navController: NavHostController,
) {

    // Lista dei filtri
    val filtri by model.filtri.observeAsState(getFilters())

    var filterList = mutableListOf<Filtro>()
    var titolo = stringResource(R.string.nuova)


    val modify = model.getModify()

    val ricettaCompleta : RicettaSample

    val ricettaVuota : RicettaSample

    runBlocking {
         ricettaCompleta = model.getRicettaCompleta()
         ricettaVuota = model.getRicettaVuota()
    }

    if(modify) {
        titolo = stringResource(R.string.modifica)
    }

    // Recupero la reference a MainActivity
    val main=MainActivity.get()

    //gestisce il caso in cui l'utente utilizzi il tasto indietro del telefono
    BackHandler {
        backPress(main, model, navController, modify, ricettaCompleta.titolo)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = titolo) },
                navigationIcon = {
                    IconButton(onClick = {
                        backPress(main, model, navController, modify, ricettaCompleta.titolo)
                    })
                    {
                    Icon(Icons.Rounded.ArrowBack, contentDescription = "")
                    }
                },
            )
        },
    ){
        //inizializzazione delle variabili
        val empty = rememberSaveable { mutableStateOf(true) }
        if(modify && empty.value)
        {
            empty.value = false
            model.onTitoloInsert(ricettaCompleta.titolo)
            model.onFilterInsert(ricettaCompleta.filtri)
            model.onDescrizioneInsert(ricettaCompleta.descrizione)
            model.onIngredientsInsert(ricettaCompleta.ingredienti)
            model.onImageInsert(ricettaCompleta.uri)

            filterList = ricettaCompleta.filtri
        }
        Content(model, ricettaVuota, modify, filtri, filterList, main)
    }
}

//funzione che gestisce le backPress
fun backPress(
    main: MainActivity?,
    model: RicetteViewModel,
    navController: NavHostController,
    modify: Boolean,
    titolo: String
) {
    main?.resetUri()    //resetta l'uri se esce

    model.restartFilters()  //toglie i check dai filtri


    if(modify)
        model.resetModify()   //resetta la variabile

    if(model.getFromDetails() == true) {
        model.isFromDetails()
        navController.navigate("${Screen.RicettaDetail.route}/${titolo}") {

            popUpTo = navController.graph.startDestination
            launchSingleTop = true

        }
    }
    else {
        model.updateTipologia(false)
        model.onHomeClick()
        navController.navigate(Screen.Home.route) {

            popUpTo = navController.graph.startDestination
            launchSingleTop = true
        }
    }
}

//Funzione che gestisce la UI di NuovaRicetta
@Composable
fun Content(
    model: RicetteViewModel,
    ricettaVuota: RicettaSample,
    modify: Boolean,
    filtri: List<Filtro>,
    filterList: MutableList<Filtro>,
    main: MainActivity?
) {

    //mantiene lo stato
    val ingredientList = remember { mutableStateListOf<IngredienteRIcetta>() }
    for(item in ricettaVuota.ingredienti)
        ingredientList.add(item)


    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
    ){


        item{
            // titolo e immagine
            TitleAndImage(main, model, modify, ricettaVuota)

            //header dei filtri a chips selezionabili
            Divider(
                modifier = Modifier.padding(top = 5.dp, start = 15.dp, end = 15.dp, bottom = 5.dp)
            )
            Text(
                text = stringResource(R.string.Filtri),
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .padding(start = 15.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(2.dp))

            //chips dei filtri
            FilterGrid(model, filtri, filterList)


            //header ingredienti
            Divider(
                modifier = Modifier.padding(top = 5.dp, start = 15.dp, end = 15.dp, bottom = 5.dp)
            )
            Text(
                text = stringResource(R.string.ingredienti),
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .padding(start = 15.dp)
                    .fillMaxWidth()
            )
        }

        //lista reattiva degli ingredienti
        itemsIndexed(items = ingredientList,
            itemContent = { _, ingredient ->
                NewIngredient(ingredientList = ingredientList, ingredient = ingredient, model)
            }
        )


        item{
            val openDialog = remember { mutableStateOf(false) }
            val ingrediente = IngredienteRIcetta("", "", "")

            // Tasto + (aggiunge gli ingredienti)
            IconButton(
                // Aggiunge un elemento a ingredientList
                onClick = {
                    openDialog.value = true
                }
            ) {
                Icon(Icons.Rounded.Add, "")
            }

            //dialog dell'aggiunta ingrediente
            if (openDialog.value)
                NewDialog(model, ingredientList, openDialog, ingrediente)

            Divider(
                modifier = Modifier.padding(top = 5.dp, start = 15.dp, end = 15.dp, bottom = 5.dp)
            )


            //descrizione
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    //.weight(3f)
                    .padding(end = 10.dp),
            ) {
                MyTextField(
                    model,
                    stringResource(R.string.descrizione),
                    300,
                    false,
                    ricettaVuota,
                    modify
                )
            }

            //Box trasparente che permette lo scroll degli item fino a sopra la BottomBar, rendendoli tutti visibili
            Box(
                modifier = Modifier
                    .background(Color.Transparent)
                    .height(100.dp)
                    .fillMaxWidth()
            )
        }

    }
}

// Funzione che gestisce TextField del titolo e Box per l'immagine
@Composable
fun TitleAndImage(
    main: MainActivity?,
    model: RicetteViewModel,
    modify: Boolean,
    ricettaVuota: RicettaSample,
) {
    Row(
        modifier = Modifier.padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        //scope per l'immagine
        val tmp = main?.getUri()
        if(tmp != null) model.onImageInsert(tmp.toString())

        //Box rotonda contenente il pulsante inserisci immagine
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(color = MaterialTheme.colors.surface)
                .size(55.dp),
            contentAlignment = Alignment.Center
        ) {

            Box(
                modifier = Modifier.alpha(0.85f)
            ) {
                if(ricettaVuota.uri.isNullOrEmpty())
                    RicettaImage(null,true,"")
                else
                    RicettaImage(Uri.parse(ricettaVuota.uri),true,"")

            }

            IconButton(onClick = {

                main?.loadImage()

            }) {
                Icon(
                    painterResource(R.drawable.ic_baseline_add_photo_alternate_24),
                    "",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }

        }
        MyTextField(
            model,
            stringResource(R.string.titolo),
            25,
            true,
            ricettaVuota,
            modify
        )
    }
}

//Dialog per l'aggiunta degli ingredienti
@Composable
fun NewDialog(
    model: RicetteViewModel,
    ingredientList: MutableList<IngredienteRIcetta>,
    openDialog: MutableState<Boolean>,
    ingredient: IngredienteRIcetta
) {
    AlertDialog(
        onDismissRequest = {
            ingredientList.add(ingredient)
            model.onIngredientsInsert(ingredientList)
            openDialog.value = false },
        title = { Text(text = "") },
        text = { DialogIngredient(ingredient) },
        confirmButton = {
            Button(
                onClick = {
                    ingredientList.add(ingredient)
                    model.onIngredientsInsert(ingredientList)
                    openDialog.value = false
                }) {
                Text(stringResource(R.string.fatto))
            }
        },
        backgroundColor = MaterialTheme.colors.background
    )
}

//TextField per il titolo e la descrizione
@Composable
fun MyTextField(
    model: RicetteViewModel,
    str: String,
    max: Int,
    singleLine: Boolean,
    ricettaVuota: RicettaSample,
    modify: Boolean
){


    val titolo = stringResource(R.string.titolo)
    var readOnly = false

    //capisce se è chiamato per il titolo o la descrizione
    val title =
        if(str == titolo)
            remember { mutableStateOf(ricettaVuota.titolo) }
        else
            remember{mutableStateOf(ricettaVuota.descrizione)}

    //Il titolo nella modifica è solo readOnly
    if(modify && str == titolo)
        readOnly = true


    OutlinedTextField(
        value = title.value,
        onValueChange = {
            if(it.length <= max)
                title.value = it

            if(str == titolo)
                model.onTitoloInsert(title.value)
            else
                model.onDescrizioneInsert(title.value)
        },
        readOnly = readOnly,  //readOnly se siamo in Modifica
        placeholder = {Text(text = stringResource(R.string.inserire)+" $str")},
        label = {Text(str)},
        modifier = Modifier
            .padding(start = 10.dp)
            .fillMaxWidth(),
        singleLine = singleLine,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = MaterialTheme.colors.primary,
            unfocusedLabelColor = MaterialTheme.colors.primary
        )

    )
}



//Scope di visualizzazione e managing dei filtri
@Composable
fun FilterGrid(
    model: RicetteViewModel,
    filtri: List<Filtro>,
    filterList: MutableList<Filtro>,
){
    val primary = Color.Transparent


    //Row che va a capo in automatico
    SimpleFlowRow(
        alignment = Alignment.CenterHorizontally,
        horizontalGap = 10.dp,
        verticalGap = 5.dp
    ){
        filtri.forEachIndexed { index, filtro ->

            //aggiorna i checked in modifica
            for (flt in filterList) {
                if (flt.name == filtro.name) filtro.checked = true
            }

            val checked = remember { mutableStateOf(filtro.checked) }


            //SUperato "Dolce" ci troviamo tra gli Add-on
            if(index == 4) {
                Divider(modifier = Modifier.padding(top = 3.dp, start = 15.dp, end = 15.dp, bottom = 3.dp))
                Text(
                    text = stringResource(R.string.Adds),
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.padding(2.dp))
            }


            //Gestione dei colori di background delle chip a seconda dello stato
            val bgColor = remember { mutableStateOf(primary) }
            if (checked.value) bgColor.value = MaterialTheme.colors.surface
            else bgColor.value = Color.Transparent

            checked.value = filtro.checked


            Surface(
                shape = RoundedCornerShape(16.dp),
                color = bgColor.value,
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colors.primary,
                ),
                modifier = Modifier
                    .clickable {

                        checked.value = !checked.value
                        filtro.checked = checked.value
                        if (!checked.value) filterList.remove(filtro)
                        else filterList.add(filtro)


                        if (checked.value && index < 4) {
                            for (i in 0..3) {
                                filtri[i].checked = false
                                filterList.remove(filtri[i])
                            }

                            filtro.checked = true
                            filterList.add(filtro)
                        }
                        model.onFilterInsert(filterList)
                    }
                    .wrapContentWidth()

            )
            {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                )
                {
                    if (checked.value)
                        Icon(Icons.Rounded.Check, "", modifier = Modifier.padding(start = 5.dp))

                    Text(
                        text = filtro.name,
                        style = MaterialTheme.typography.caption,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(10.dp),
                    )
                }
            }

        }
    }
}