package com.example.myapplication.screens
import android.net.Uri
import android.util.Log
import androidx.activity.compose.registerForActivityResult
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.example.myapplication.*
import com.example.myapplication.R
import com.example.myapplication.RicettaImage
import com.example.myapplication.database.IngredienteRIcetta
import com.example.myapplication.database.RicetteViewModel
import com.example.myapplication.reactingLists.addIngredientCard
import com.example.myapplication.reactingLists.dialogIngredient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.withLock


// Cornice per lo schermo
@ExperimentalFoundationApi
@Synchronized
@Composable
fun NuovaRicetta(
    model: RicetteViewModel,
    navController: NavHostController,
) {
    val filtri by model.filtri.observeAsState(getFilters())
    val expanded by model.expanded.observeAsState(false)

    var filterList = mutableListOf<Filtro>()
    var titolo = stringResource(R.string.nuova)

    val scope = rememberCoroutineScope()

    val modify = model.getModify()
    //val ricettaCompleta by model.ricettaCompleta.observeAsState()
    val ricettaCompleta = model.getRicettaCompleta()


    if(modify) {
        titolo = stringResource(R.string.modifica)
        model.onTitoloInsert(ricettaCompleta.titolo)

    }


    val main=MainActivity.get()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = titolo) },
                navigationIcon = {
                    IconButton(onClick = {

                        main?.resetUri()    //resetta l'uri se scelto

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
                    })
                    {
                    Icon(Icons.Rounded.ArrowBack, contentDescription = "")
                    }
                },
                //scope per i filtri
                /*actions = {
                    IconButton(onClick = { model.onExpand(true) }) {
                        Icon(Icons.Rounded.FilterAlt,"")
                    }
                    if(modify)
                    {
                        filterList = ricettaCompleta.filtri
                        //model.onFilterInsert(filterList)
                    }

                    Log.d("test", ricettaCompleta.filtri.toString())
                    ShowFilters(model, filtri,filterList, expanded) { model.onExpand(false) }
                } */

            )
        },
    ){

        if(modify)
        {
            model.onFilterInsert(ricettaCompleta.filtri)
            model.onDescrizioneInsert(ricettaCompleta.descrizione)
            model.onIngredientsInsert(ricettaCompleta.ingredienti)
            model.onImageInsert(ricettaCompleta.uri)

            filterList = ricettaCompleta.filtri
            //model.onFilterInsert(filterList)
        }
        Content(model, ricettaCompleta, modify, filtri, filterList, main)
    }
}
private var imageUriState =  mutableStateOf<Uri?>(null)
private val pickImgCode = 100
//, titolo: String, ingrediente: String, quantità: String
// Funzione che gestisce il contenuto
@ExperimentalFoundationApi
@Composable
fun Content(
    model: RicetteViewModel,
    ricettaCompleta: RicettaSample?,
    modify: Boolean,
    filtri: List<Filtro>,
    filterList: MutableList<Filtro>,
    main: MainActivity?
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            //.verticalScroll(rememberScrollState())
    ){
        Row(
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){

            //scope per selezionare l'immagine


            var uri = main?.getUri()

            Log.d("image", ricettaCompleta?.uri.toString() )
            if(modify && !ricettaCompleta?.uri.isNullOrEmpty()) uri = Uri.parse(ricettaCompleta?.uri)

            model.onImageInsert(uri.toString())

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(color = MaterialTheme.colors.surface)
                    .size(55.dp),
                contentAlignment = Alignment.Center
            ){

                Box(
                    modifier = Modifier.alpha(0.85f)
                ){
                    RicettaImage(uri)
                    //RicettaImage(urStr = main?.getUri())
                }



                IconButton(onClick = {

                    main?.loadImage()

                }) {
                    Icon(painterResource(R.drawable.ic_baseline_add_photo_alternate_24), "", tint = Color.White, modifier = Modifier.size(30.dp))
                }

            }
            MyTextField(model, stringResource(R.string.titolo), 25, true, ricettaCompleta, modify)   //modify
        }

        //filtri a chips selezionabili
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

        FilterGrid(model, filtri, filterList) /*{ model.onExpand(false) }*/

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


        val ingredientList = remember { mutableStateListOf<IngredienteRIcetta>() }
        val empty = remember { mutableStateOf(true) }

        //se siamo in funzione di modifica, aggiorna la lista degli ingredienti
        if(modify && empty.value){
            for(item in ricettaCompleta!!.ingredienti){
                ingredientList.add(item)
            }
            empty.value = false
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ){
                addIngredientCard(ingredientList)
        }


        val openDialog = remember { mutableStateOf(false)  }
        val ingrediente = IngredienteRIcetta("","empty","")

        // Tasto +
        IconButton(
            // Aggiunge un elemento a ingredientList
            onClick = {
                    //listState.add(ingrediente)
                    //model.onIngredientsInsert(listState)    //va fatto dopo?
                    openDialog.value = true
            }
        ) {
            Icon(Icons.Rounded.Add,"")
        }
        if(openDialog.value)
            NewDialog(model, ingredientList, openDialog, ingrediente)

        Divider(
            modifier = Modifier.padding(top = 5.dp, start = 15.dp, end = 15.dp, bottom = 5.dp)
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .weight(3f)
                .padding(end = 10.dp),
        ){
            MyTextField(
                model,
                stringResource(R.string.descrizione),
                300,
                false,
                ricettaCompleta,
                modify
            )
        }
        Box(
            modifier = Modifier.fillMaxWidth()
        )

    }
}

@Composable
fun NewDialog(
    model: RicetteViewModel,
    ingredientList: SnapshotStateList<IngredienteRIcetta>,
    openDialog: MutableState<Boolean>,
    ingredient: IngredienteRIcetta
) {
    AlertDialog(
        onDismissRequest = {
            ingredientList.add(ingredient)
            model.onIngredientsInsert(ingredientList)
            openDialog.value = false },
        title = { Text(text = "") },
        text = { dialogIngredient(ingredient) },
        confirmButton = {
            Button(
                onClick = {
                    ingredientList.add(ingredient)
                    model.onIngredientsInsert(ingredientList)
                    openDialog.value = false
                }) {
                Text("Fatto")
            }
        },
        backgroundColor = MaterialTheme.colors.background
    )
}


@Composable
fun MyTextField(
    model: RicetteViewModel,
    str: String,
    max: Int,
    singleLine: Boolean,
    ricettaCompleta: RicettaSample?,
    modify: Boolean
){


    val titolo = stringResource(R.string.titolo)
    var readOnly = false

    //val title = remember{ mutableStateOf(TextFieldValue()) }
    var title = remember{mutableStateOf("")}

    if(modify) {
        if(str.equals(titolo)){
            //title.value = TextFieldValue(ricettaCompleta!!.titolo)
            title = remember {mutableStateOf(ricettaCompleta!!.titolo)}
            //model.onTitoloInsert(title.value.text)
            readOnly = true
        }
        else
            title = remember {mutableStateOf(ricettaCompleta!!.descrizione)}

    }


    OutlinedTextField(
        value = title.value,
        onValueChange = {
            if(it.length <= max)
                title.value = it

            if(str.equals(titolo))
                model.onTitoloInsert(title.value)
            else
                model.onDescrizioneInsert(title.value)
        },
        readOnly = readOnly,  //readOnly se siamo in Modifica
        placeholder = {Text(text = "Inserire $str")},
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



@ExperimentalFoundationApi
@Composable
fun FilterGrid(
    model: RicetteViewModel,
    filtri: List<Filtro>,
    filterList: MutableList<Filtro>,
    //onDeExpand: () -> Unit
){
    val primary = Color.Transparent


    SimpleFlowRow(
        alignment = Alignment.CenterHorizontally,
        horizontalGap = 10.dp,
        verticalGap = 5.dp
    ){
        filtri.forEach { filtro ->


            for (flt in filterList) {
                if(flt.name.equals(filtro.name)) filtro.checked = true
            }

            val checked = remember { mutableStateOf(filtro.checked) }
            val bgColor = remember{ mutableStateOf(primary)}
            if(checked.value) bgColor.value = MaterialTheme.colors.surface
            else bgColor.value = Color.Transparent

            Log.d("test", filtro.checked.toString())
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = bgColor.value,
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colors.primary,
                ),
                modifier = Modifier
                    //.padding(5.dp)
                    .clickable {
                        checked.value = !checked.value
                        filtro.checked = checked.value
                        if (!checked.value) filterList.remove(filtro)
                        else filterList.add(filtro)
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
                    if(checked.value)
                        Icon(Icons.Rounded.Check, "", modifier = Modifier.padding(start = 5.dp))

                    Text(
                        text = filtro.name,
                        style = MaterialTheme.typography.caption,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier/*.background(
                            color = MaterialTheme.colors.secondary,
                            shape = RoundedCornerShape(15.dp),
                        )*/.padding(10.dp)
                    )
                }
            }
        }
    }
}