package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate


@Composable
fun NuovaRicetta(navController: NavHostController) {
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
        Content()
    }
}

@Composable
fun Content(){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
    ){
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            val title = remember{ mutableStateOf(TextFieldValue()) }
            OutlinedTextField(
                value = title.value,
                onValueChange = {
                    if(it.text.length <= 20) title.value = it
                },
                placeholder = {Text(text = "Inserisci il titolo")},
                label = {Text(stringResource(R.string.titolo))},
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = MaterialTheme.colors.primary,
                    unfocusedLabelColor = MaterialTheme.colors.primary
                )

            )
        }
        Divider(
            modifier = Modifier.padding(top = 5.dp, start = 15.dp, end = 15.dp, bottom = 5.dp)
        )
        Text(
            text = stringResource(R.string.ingredienti),
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.primary,
            //textAlign = Alignment.Start,
            modifier = Modifier
                .padding(start = 15.dp)
                .fillMaxWidth()
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ){

            LazyColumn() {
                item{
                    NewRecipe()
                }
            }
            Button(onClick = { }){
                Text("prova")
            }
        }
        Divider(
            modifier = Modifier.padding(top = 5.dp, start = 15.dp, end = 15.dp, bottom = 5.dp)
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Top,
            modifier = Modifier.weight(3f)
        ){
            val description = remember{ mutableStateOf(TextFieldValue()) }
            OutlinedTextField(
                value = description.value,
                onValueChange = {
                    if(it.text.length <= 200) description.value = it
                },
                placeholder = {Text(text = "Inserisci la descrizione")},
                label = {Text(stringResource(R.string.descrizione))},
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                maxLines = 10,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = MaterialTheme.colors.primary,
                    unfocusedLabelColor = MaterialTheme.colors.primary
                )

            )
        }
        Box(
            modifier = Modifier.fillMaxWidth()
        )

    }
}


@Composable
fun NewRecipe(){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Row(){
            //menù ingredienti a tendina
        }
        Row(
            modifier = Modifier.weight(1f)
        ){
            Unit()
        }
        Box(){
            //quantità ingredienti
        }
        IconButton(onClick =  {}){
            Icon(Icons.Rounded.Delete, contentDescription = "")
        }
    }
}

@Composable
fun Ingrediente(){

}

@Composable
fun Unit(){
    //menù gr ml a tendina
    val expanded = remember { mutableStateOf(false)}
    val unit = remember{ mutableStateOf("") }
    OutlinedTextField(
        value = unit.value,
        onValueChange = {
            unit.value = it
        },
        //label = {Text(stringResource(R.string.unità))},
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        readOnly = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = MaterialTheme.colors.primary,
            //unfocusedLabelColor = MaterialTheme.colors.primary,
        ),
        trailingIcon = {
            Icon(
                Icons.Rounded.ArrowDropDown,
                contentDescription = "",
                Modifier.clickable { expanded.value = !expanded.value }
            )
        }
    )
    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false },
        modifier = Modifier
            .wrapContentSize()
            .background(MaterialTheme.colors.background)
    ) {
        val g = stringResource(R.string.grammi)
        val ml = stringResource(R.string.ml)
        DropdownMenuItem(
            onClick = {
                unit.value = g
                expanded.value = false
            },
            content = {Text(text = g)}
        )
        DropdownMenuItem(
            onClick = {
                unit.value = ml
                expanded.value = false
            },
            content = {Text(text = ml)}
        )
    }
}