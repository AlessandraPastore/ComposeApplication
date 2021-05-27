package com.example.myapplication.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.example.myapplication.R
import com.example.myapplication.RicettaSample
import com.example.myapplication.Screen
import com.example.myapplication.database.IngredienteRIcetta
import com.example.myapplication.database.RicetteViewModel
import com.example.myapplication.reactingLists.addIngredientCard


// Cornice per lo schermo
@Composable
fun NuovaRicetta(
    model: RicetteViewModel,
    navController: NavHostController,
    ricettaVuota: RicettaSample
) {
    /*
    val titolo = model.titolo.observeAsState("")
    val ingrediente = model.ingrediente.observeAsState("")
    val quantità = model.quantità.observeAsState("")
     */
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.nuova)) },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(Screen.Home.route){

                            popUpTo = navController.graph.startDestination
                            launchSingleTop = true

                        }
                    })
                    {
                    Icon(Icons.Rounded.ArrowBack, contentDescription = "")
                    }
                },
            )
        },
    ){
        Content(model ,ricettaVuota)
    }
}

//, titolo: String, ingrediente: String, quantità: String
// Funzione che gestisce il contenuto
@Composable
fun Content(model: RicetteViewModel, ricettaVuota: RicettaSample) {
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
                IconButton(onClick = { /*TODO*/ }) {
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

        // val ingredientList = ricettaVuota.ingredienti

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

        ricettaVuota.ingredienti = listState

        // Tasto +
        IconButton(
            // Aggiunge un elemento a ingredientList
            onClick = {
                    //ingredientList.add(IngredienteRIcetta("","",""))
                    listState.add(IngredienteRIcetta("","empty",""))
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