package com.example.myapplication

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun Home() {
    val expanded = remember { mutableStateOf(false)}
    val items = getFilters()
    Scaffold(
        topBar = { TopAppBar(
            title = {
                Text(text = stringResource(R.string.home))
            },
            actions = {
                IconButton(onClick = {  }) {
                    Icon(Icons.Rounded.Search, contentDescription = "")
                }
                IconButton(onClick = { expanded.value = true }) {
                    Icon(Icons.Rounded.FilterAlt, contentDescription = "")
                }
                DropdownMenu(
                    expanded = expanded.value,
                    onDismissRequest = { expanded.value = false },
                    modifier = Modifier.wrapContentSize()

                ) {
                    items.forEach{
                        val checked = remember { mutableStateOf(it.checked) }

                        DropdownMenuItem(onClick = {
                            checked.value = !checked.value
                            it.checked = checked.value
                            //chiama viewmodel per una query?
                        }) {
                            Row(){
                                Checkbox(
                                    checked = checked.value,
                                    onCheckedChange = { checked.value = it }
                                )
                                Text(it.name)
                            }
                        }


                        Divider()
                    }

                }
            }
            )
        }
    )
    {

        LazyColumn() {
            items(50) {
                val checked = rememberSaveable { mutableStateOf(false) }    //devo farlo per ogni index qualcosa
                Row(modifier = Modifier.fillParentMaxWidth()) {
                    Card(
                        backgroundColor = MaterialTheme.colors.surface,
                        elevation = 5.dp,
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .padding(10.dp)
                            .clickable { }
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .background(MaterialTheme.colors.surface)

                        ) {

                            Surface(
                                color = MaterialTheme.colors.onSurface,
                                elevation = 19.dp,
                                border = BorderStroke(1.dp, Color.Gray),
                                modifier = Modifier.size(130.dp),
                            ) {

                            }
                            Column(
                                modifier = Modifier
                                    .padding(start = 12.dp)
                                    .align(Alignment.CenterVertically)
                            ) {
                                Text(
                                    text = "item",
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(15.dp)
                                )
                                Text(
                                    text = "item",
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(15.dp)
                                )

                            }
                            Column(
                                modifier = Modifier
                                    .padding(end = 12.dp)
                                    .align(Alignment.CenterVertically)
                            ) {
                                IconToggleButton(
                                    checked = checked.value,
                                    onCheckedChange = { checked.value = it }) {
                                    if (!checked.value) Icon(Icons.Rounded.FavoriteBorder, "")
                                    else Icon(Icons.Rounded.Favorite, "")


                                }
                            }

                        }


                    }
                }
            }
        }
    }
}