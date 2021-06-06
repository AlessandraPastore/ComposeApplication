package com.example.myapplication.reactingLists

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.database.IngredienteRIcetta
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun addIngredientCard(ingredientList: MutableList<IngredienteRIcetta>) {


    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        itemsIndexed(items = ingredientList,
            itemContent = { _, ingredient ->
                NewIngredient(ingredientList = ingredientList, ingredient = ingredient)
            }
        )
    }
}

@Composable
fun NewIngredient(
    ingredientList: MutableList<IngredienteRIcetta>,
    ingredient: IngredienteRIcetta,
) {

    // Stato interno, gestisce il pop up dell'ingrediente per l'inserimento dei dati da parte dell'utente
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
            onDismissRequest = {
                openDialog.value = false
            },
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
fun dialogIngredient(ingrediente: IngredienteRIcetta) {

    val ingVal = remember { mutableStateOf(ingrediente.ingrediente) }
    val quantity = remember { mutableStateOf(ingrediente.qta) }
    Column(
        verticalArrangement = Arrangement.Center
    )
    {
        OutlinedTextField(
            value = ingVal.value,
            onValueChange = {
                if (it.length <= 20) ingVal.value = it
                ingrediente.ingrediente = ingVal.value
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
                if (it.length <= 15) quantity.value = it
                ingrediente.qta = quantity.value
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