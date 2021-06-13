package com.example.myapplication.reactingLists

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.database.IngredienteRIcetta
import com.example.myapplication.database.RicetteViewModel

/*
Composable che gestisce la lista degli ingredienti in NuovaRicetta
 */
@Composable
fun NewIngredient(
    ingredientList: MutableList<IngredienteRIcetta>,
    ingredient: IngredienteRIcetta,
    model: RicetteViewModel,
) {

    // Stato interno, gestisce il pop up dell'ingrediente per l'inserimento dei dati da parte dell'utente
    val openDialog = remember { mutableStateOf(false)  }

    //Pulsante contenente il nome dell'ingrediente e icona di elimina
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
            if(ingredient.ingrediente == "") Text(stringResource(R.string.empty))
            else Text(ingredient.ingrediente)
        }
        IconButton(onClick =  {
            ingredientList.remove(ingredient)
            model.onIngredientsInsert(ingredientList)
        }){
            Icon(Icons.Rounded.Delete, contentDescription = "")
        }
    }

    //AlertDialog che si mostra per modificare gli ingredienti inseriti
    if (openDialog.value) {

        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = { Text(text = "") },
            text = { DialogIngredient(ingredient) },    //contenuto dell'alertDialog
            confirmButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                    }) {
                    Text(stringResource(R.string.fatto))
                }
            },
            backgroundColor = MaterialTheme.colors.background
        )
    }
}

//AlertDialog per la modifica dell'ingrediente
@Composable
fun DialogIngredient(ingrediente: IngredienteRIcetta) {

    val ingVal = remember { mutableStateOf(ingrediente.ingrediente) }
    val quantity = remember { mutableStateOf(ingrediente.qta) }


    Column(
        verticalArrangement = Arrangement.Center
    )
    {
        //Nome dell'ingrediente
        OutlinedTextField(
            value = ingVal.value,
            onValueChange = {
                if (it.length <= 20) ingVal.value = it
                ingrediente.ingrediente = ingVal.value
            },
            placeholder = { Text(stringResource(R.string.inserireIngrediente)) },
            label = { Text(stringResource(R.string.ingrediente)) },
            modifier = Modifier
                .padding(start = 10.dp)
                .fillMaxWidth(),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = MaterialTheme.colors.primary,
                unfocusedLabelColor = MaterialTheme.colors.primary
            )
        )

        //Quantità dell'ingrediente
        OutlinedTextField(
            value = quantity.value,
            onValueChange = {
                if (it.length <= 15) quantity.value = it
                ingrediente.qta = quantity.value
            },
            placeholder = { Text(stringResource(R.string.inserireQuantity)) },
            label = { Text(stringResource(R.string.quantity)) },
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