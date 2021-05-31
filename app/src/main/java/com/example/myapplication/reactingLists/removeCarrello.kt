package com.example.myapplication.reactingLists

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.database.RicetteViewModel
import androidx.compose.runtime.livedata.observeAsState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RemoveCarrello(model: RicetteViewModel) {

    val ingredientList = model.listaCarrello.observeAsState()    //me la dovrà dare il viewModel

    if(ingredientList.value != null) {
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
            item() {
                Box(
                    modifier = Modifier
                        .background(Color.Transparent)
                        .height(100.dp)
                        .fillMaxWidth()
                ) {}
            }
        }
    }
}