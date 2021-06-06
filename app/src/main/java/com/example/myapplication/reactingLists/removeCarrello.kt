package com.example.myapplication.reactingLists

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.List
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.ShowEmpty
import com.example.myapplication.database.RicetteViewModel
import com.example.myapplication.database.Ingrediente

@Composable
fun RemoveCarrello(model: RicetteViewModel) {

    val ingredientList = model.listaCarrello.observeAsState()

    if(ingredientList.value != null) {
        if(ingredientList.value!!.isEmpty())
            ShowEmpty(stringResource(R.string.carrello))
        else
            ShowList(model, ingredientList)
    }

}

@Composable
fun ShowList(model: RicetteViewModel, ingredientList: State<List<Ingrediente>?>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(items = ingredientList.value!!,
            itemContent = { _, ingredient ->

                Card(
                    elevation = 5.dp,
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(start = 10.dp, top = 5.dp, bottom = 5.dp, end = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillParentMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            ingredient.ingrediente,
                            style = TextStyle(
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier.padding(16.dp)
                        )
                        IconButton(
                            onClick = {
                                model.updateCarrello(ingredient.ingrediente, false)
                            }
                        ) {
                            Icon(Icons.Rounded.Delete, "")
                        }
                    }
                }
            })
        item {
            Box(
                modifier = Modifier
                    .background(Color.Transparent)
                    .height(100.dp)
                    .fillMaxWidth()
            ) {}
        }
    }
}

