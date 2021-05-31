package com.example.myapplication.screens
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.example.myapplication.*
import com.example.myapplication.R
import com.example.myapplication.database.IngredienteRIcetta
import com.example.myapplication.database.RicetteViewModel
import com.example.myapplication.reactingLists.addIngredientCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


// Cornice per lo schermo
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
        model.onDescrizioneInsert(ricettaCompleta.descrizione)
        model.onIngredientsInsert(ricettaCompleta.ingredienti)
        model.onFilterInsert(ricettaCompleta.filtri)
    }



    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = titolo) },
                navigationIcon = {
                    IconButton(onClick = {

                        model.restartFilters()  //toglie i check dai filtri
                        model.updateTipologia(false)
                        model.onHomeClick()
                        if(modify)
                            model.resetModify()   //resetta la variabile

                        navController.navigate(Screen.Home.route){

                            popUpTo = navController.graph.startDestination
                            launchSingleTop = true

                        }
                    })
                    {
                    Icon(Icons.Rounded.ArrowBack, contentDescription = "")
                    }
                },
                //scope per i filtri
                actions = {
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
                }

            )
        },
    ){
        Content(model, ricettaCompleta, modify)
    }
}

private val pickImgCode = 100
//, titolo: String, ingrediente: String, quantità: String
// Funzione che gestisce il contenuto
@Composable
fun Content(model: RicetteViewModel, ricettaCompleta: RicettaSample?, modify: Boolean) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
    ){
        Row(
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            //al post di box metteremo un Image
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Red)
                    .size(58.dp)
            ){
                IconButton(onClick = {
                   val main=MainActivity.get()
                    main!!.ImageSelection()
                }) {
                    Icon(Icons.Rounded.Camera, "")
                }
            }
            MyTextField(model, stringResource(R.string.titolo), 20, true, ricettaCompleta, modify)   //modify
        }
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


        val listState = remember { mutableStateListOf<IngredienteRIcetta>() }

        if(modify){
            for(item in ricettaCompleta!!.ingredienti){
                listState.add(item)
            }
            //model.onIngredientsInsert(listState)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ){
                addIngredientCard(listState)
        }



        // Tasto +
        IconButton(
            // Aggiunge un elemento a ingredientList
            onClick = {
                    //ingredientList.add(IngredienteRIcetta("","",""))
                    listState.add(IngredienteRIcetta("","empty",""))
                    model.onIngredientsInsert(listState)
            }
        ) {
            Icon(Icons.Rounded.Add,"")
        }


        Divider(
            modifier = Modifier.padding(top = 5.dp, start = 15.dp, end = 15.dp, bottom = 5.dp)
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Top,
            modifier = Modifier.weight(3f).padding(end = 10.dp),
        ){
            MyTextField(
                model,
                stringResource(R.string.descrizione),
                200,
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

// Funzione che gestisce l'icona del filtro
@Composable
fun ShowFilters(
    model: RicetteViewModel,
    filtri: List<Filtro>,
    filterList: MutableList<Filtro>,
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



        filtri.forEach{


            for (filtro in filterList) {
                if(filtro.name.equals(it.name)) it.checked = true
            }

            val checked = remember { mutableStateOf(it.checked) }



            DropdownMenuItem(onClick = {
                checked.value = !checked.value
                it.checked = checked.value
                if(!checked.value) filterList.remove(it)
                else filterList.add(it)
                model.onFilterInsert(filterList)



            }) {
                Row{
                    if(checked.value)
                        Icon(Icons.Rounded.CheckBox, "")
                    else
                        Icon(Icons.Rounded.CropSquare, "")

                    Text(it.name)
                }
            }
        }
    }
}