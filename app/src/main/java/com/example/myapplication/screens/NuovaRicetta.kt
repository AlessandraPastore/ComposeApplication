package com.example.myapplication.screens
import androidx.activity.*
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.registerForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.example.myapplication.*
import com.example.myapplication.R
import com.example.myapplication.database.IngredienteRIcetta
import com.example.myapplication.database.RicetteViewModel
import com.example.myapplication.reactingLists.addIngredientCard


// Cornice per lo schermo
@Composable
fun NuovaRicetta(
    model: RicetteViewModel,
    navController: NavHostController,
) {
    val filtri by model.filtri.observeAsState(getFilters())
    val expanded by model.expanded.observeAsState(false)

    var filterList = mutableListOf<Filtro>()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.nuova)) },
                navigationIcon = {
                    IconButton(onClick = {
                        model.restartFilters()  //toglie i check dai filtri
                        navController.navigate(Screen.Home.route){

                            popUpTo = navController.graph.startDestination
                            launchSingleTop = true

                        }
                    })
                    {
                    Icon(Icons.Rounded.ArrowBack, contentDescription = "")
                    }
                },
                actions = {
                    IconButton(onClick = { model.onExpand(true) }) {
                        Icon(Icons.Rounded.FilterAlt,"")
                    }
                    ShowFilters(model, filtri,filterList, expanded) { model.onExpand(false) }
                }

            )
        },
    ){
        Content(model)
    }
}

private val pickImgCode = 100
//, titolo: String, ingrediente: String, quantità: String
// Funzione che gestisce il contenuto
@Composable
fun Content(model: RicetteViewModel) {

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
            MyTextField(model, stringResource(R.string.titolo), 20, true)
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
            modifier = Modifier.weight(3f)
        ){
            MyTextField(model, stringResource(R.string.descrizione), 200, false)
        }
        Box(
            modifier = Modifier.fillMaxWidth()
        )

    }
}


@Composable
fun MyTextField(model: RicetteViewModel ,str: String, max: Int, singleLine: Boolean){

    val titolo = stringResource(R.string.titolo)

    val title = remember{ mutableStateOf(TextFieldValue()) }
    OutlinedTextField(
        value = title.value,
        onValueChange = {
            if(it.text.length <= max)
                title.value = it

            if(str.equals(titolo))
                model.onTitoloInsert(title.value.text)
            else
                model.onDescrizioneInsert(title.value.text)
        },
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