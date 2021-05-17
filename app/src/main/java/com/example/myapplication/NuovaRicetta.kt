package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
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
            myTextField(stringResource(R.string.titolo), 20, true)
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
                    NewIngredient()
                }
            }
            IconButton(onClick =  {/*aggiunge ingrediente() a una lista */}){
                Icon(Icons.Rounded.Add, contentDescription = "", tint = MaterialTheme.colors.primary)
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
            myTextField(stringResource(R.string.descrizione), 200, false)
        }
        Box(
            modifier = Modifier.fillMaxWidth()
        )

    }
}

@Composable
fun NewIngredient(){
    var str = "empty ingredient"
    val openDialog = remember { mutableStateOf(false)  }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth(),
    ){
        Button(
            onClick = { openDialog.value = true },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text(str)
        }
        IconButton(onClick =  {}){
            Icon(Icons.Rounded.Delete, contentDescription = "")
        }
    }
    if (openDialog.value) {

        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text(text = "") },
            text = { AddIngredient(str) },
            confirmButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                    }) {
                    Text("Fatto")
                }
            },
            backgroundColor = MaterialTheme.colors.background
        )
    }
}


@Composable
fun AddIngredient(str: String) {
    val isGr = remember { mutableStateOf(true) }
    var quantity : Int
    //al click di fatto invii al viewModel un nuovo ingrediente fatto con str, isGr e quantity
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        //verticalArrangement = Arrangement.Bottom,
    ){
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Unit(isGr)  //menù ingredienti a tendina, preferisco farlo quando abbiamo la view così gli passo anche la str da mostrare
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(
                modifier = Modifier.weight(1f)
            ){
                Unit(isGr)
            }
            Box(
                modifier = Modifier.weight(1f),
            ){
                val quantity = remember{ mutableStateOf(TextFieldValue()) }
                OutlinedTextField(
                    value = quantity.value,
                    onValueChange = {
                        if(it.text.length <= 20) quantity.value = it
                    },
                    label = {Text("Quantità")},
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = MaterialTheme.colors.primary,
                        unfocusedLabelColor = MaterialTheme.colors.primary
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)

                )
            }
        }
    }

}

@Composable
fun Ingrediente(){

}

@Composable
fun Unit(isGr: MutableState<Boolean>) {
    //menù gr ml a tendina
    val expanded = remember { mutableStateOf(false)}
    val unit = remember{ mutableStateOf("") }
    OutlinedTextField(
        value = unit.value,
        onValueChange = {
            unit.value = it
        },
        label = {Text(stringResource(R.string.unità))},
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        readOnly = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = MaterialTheme.colors.primary,
            unfocusedLabelColor = MaterialTheme.colors.primary,
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
        val gr = stringResource(R.string.grammi)
        val ml = stringResource(R.string.ml)
        DropdownMenuItem(
            onClick = {
                isGr.value = true
                unit.value = gr
                expanded.value = false
            },
            content = {Text(text = gr)}
        )
        DropdownMenuItem(
            onClick = {
                isGr.value = false
                unit.value = ml
                expanded.value = false
            },
            content = {Text(text = ml)}
        )
    }
}

@Composable
fun myTextField(str: String, max: Int, singleLine : Boolean){
    val title = remember{ mutableStateOf(TextFieldValue()) }
    OutlinedTextField(
        value = title.value,
        onValueChange = {
            if(it.text.length <= max) title.value = it
        },
        placeholder = {Text(text = "Inserire $str")},
        label = {Text(str)},
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        singleLine = singleLine,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = MaterialTheme.colors.primary,
            unfocusedLabelColor = MaterialTheme.colors.primary
        )

    )
}