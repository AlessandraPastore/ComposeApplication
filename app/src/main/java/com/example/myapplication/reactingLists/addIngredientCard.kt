package com.example.myapplication.reactingLists

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.myapplication.database.IngredienteRIcetta


@Composable
fun addIngredientCard(ingredientList: MutableList<IngredienteRIcetta>) {


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
fun NewIngredient(ingredientList: MutableList<IngredienteRIcetta>, ingredient: IngredienteRIcetta) {
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
            Text(ingredient.ingrediente)
        }
        IconButton(onClick =  {ingredientList.remove(ingredient)}){
            Icon(Icons.Rounded.Delete, contentDescription = "")
        }
    }
    if (openDialog.value) {

        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text(text = "") },
            text = { dialogIngredient(ingredient) },
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

//al posto di str prob ci andrà un oggetto di tipo ingrediente o la lista stessa degli ingredienti? una ref
@Composable
fun dialogIngredient(str: IngredienteRIcetta) {

    val ingredient = remember { mutableStateOf(TextFieldValue()) }
    val quantity = remember { mutableStateOf(TextFieldValue()) }
    Column(
        verticalArrangement = Arrangement.Center
    )
    {

        OutlinedTextField(
            value = ingredient.value,
            onValueChange = {
                if (it.text.length <= 15) ingredient.value = it
                //str.ingrediente = ingredient.value.text
            },
            placeholder = { Text(text = "Inserire ingrediente") },
            label = { Text("Ingrediente") },
            modifier = Modifier
                .padding(start = 10.dp)
                .fillMaxWidth(),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = MaterialTheme.colors.primary,
                unfocusedLabelColor = MaterialTheme.colors.primary
            )
        )

        OutlinedTextField(
            value = quantity.value,
            onValueChange = {
                if (it.text.length <= 8) quantity.value = it
                //str.quantity = quantity.value.text
            },
            placeholder = { Text(text = "Inserire quantità") },
            label = { Text("Quantità") },
            modifier = Modifier
                .padding(start = 10.dp)
                .fillMaxWidth(),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = MaterialTheme.colors.primary,
                unfocusedLabelColor = MaterialTheme.colors.primary
            )
        )
    }
}