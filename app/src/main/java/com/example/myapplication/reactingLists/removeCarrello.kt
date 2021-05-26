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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@ExperimentalAnimationApi
@Composable
fun RemoveCarrello() {

    var ingredientList = mutableListOf<String>("a","b","c","d","e","f","g","h","i","l")    //me la dovrà dare il viewModel
    val deletedIngredientList = remember { mutableStateListOf<String>() }

    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(items = ingredientList,
            itemContent = { _, ingredient ->

                AnimatedVisibility(
                    visible = !deletedIngredientList.contains(ingredient),
                    exit = shrinkVertically()
                ) {
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
                                ingredient,
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Center
                                ),
                                modifier = Modifier.padding(16.dp)
                            )
                            IconButton(
                                onClick = {
                                    deletedIngredientList.add(ingredient)
                                    //ingredientList.remove(ingredient)   //crash, ma tanto dovrà farlo il viewmodel questo
                                }
                            ) {
                                Icon(Icons.Rounded.Delete,"")
                            }
                        }
                    }
                }
            })
        item(){
            Box(modifier = Modifier
                .background(Color.Transparent)
                .height(100.dp)
                .fillMaxWidth()
            ){}
        }
    }
}