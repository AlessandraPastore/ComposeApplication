package com.example.myapplication.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.example.myapplication.Screen
import com.example.myapplication.reactingLists.addIngredientCard



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
            )
            MyTextField(stringResource(R.string.titolo), 20, true)
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

        val ingredientList = remember { mutableStateListOf<String>() }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ){
            addIngredientCard(ingredientList)
        }
        IconButton(
            // Aggiunge un elemento a ingredientList
            onClick = {
                ingredientList.add("empty ingredient")
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
            MyTextField(stringResource(R.string.descrizione), 200, false)
        }
        Box(
            modifier = Modifier.fillMaxWidth()
        )

    }
}

/*
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
*/


@Composable
fun MyTextField(str: String, max: Int, singleLine : Boolean){
    val title = remember{ mutableStateOf(TextFieldValue()) }
    OutlinedTextField(
        value = title.value,
        onValueChange = {
            if(it.text.length <= max) title.value = it
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