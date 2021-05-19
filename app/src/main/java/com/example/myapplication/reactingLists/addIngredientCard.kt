package com.example.myapplication.reactingLists

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.myapplication.screens.Unit


@Composable
fun addIngredientCard(ingredientList: SnapshotStateList<String>) {

    //val ingredientList = remember { mutableStateListOf<String>() }

    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(items = ingredientList,
            itemContent = { _, ingredient ->
                NewIngredient(ingredientList = ingredientList, ingredient = ingredient)
            })
    }
}

@Composable
fun NewIngredient(ingredientList: SnapshotStateList<String>, ingredient: String) {
    var str = "empty ingredient"
    val openDialog = remember { mutableStateOf(false)  }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp)
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
        IconButton(onClick =  {ingredientList.remove(ingredient)}){
            Icon(Icons.Rounded.Delete, contentDescription = "")
        }
    }
    if (openDialog.value) {

        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text(text = "") },
            text = { dialogIngredient(str) },
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
fun dialogIngredient(str: String) {
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