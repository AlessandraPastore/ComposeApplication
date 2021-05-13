package com.example.myapplication

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun Home() {
    Scaffold(
        topBar = { TopAppBar(
            title = {
                Text(text = stringResource(R.string.home))
            })
        }
    )
    {

        LazyColumn() {
            items(50) {
                var checked by rememberSaveable { mutableStateOf(false) }    //devo farlo per ogni index qualcosa
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
                                    checked = checked,
                                    onCheckedChange = { checked = it }) {
                                    if (!checked) Icon(Icons.Filled.FavoriteBorder, "")
                                    else Icon(Icons.Filled.Favorite, "")


                                }
                            }

                        }


                    }
                }
            }
        }
    }
}