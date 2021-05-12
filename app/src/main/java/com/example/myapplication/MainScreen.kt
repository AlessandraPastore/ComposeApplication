package com.example.myapplication


import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.KEY_ROUTE
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController


@Composable
fun MainScreen( page : String){
    val navController = rememberNavController()
    NavConfig(navController)
    Scaffold(
        topBar = { TopBar(page) },

        floatingActionButton = {
            FloatingActionButton(onClick = {  }) {
                Icon( Icons.Filled.Add, "")
            }
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = { BottomBar(navController, page) },
        content = {

            when(page){
                stringResource(R.string.home) -> Home()
                stringResource(R.string.preferiti) -> Preferiti()
                stringResource(R.string.carrello) -> Carrello()
                stringResource(R.string.impostazioni) -> Impostazioni()
            }



        }
    )


}

@Preview
@Composable
fun Prova(){
    Text("ciao")
    MainScreen(stringResource(R.string.home))
}



@Composable
private fun TopBar(page : String){
    TopAppBar(
        title = {
            Text(text = page)
        }
    )
}




@Composable
private fun BottomBar(navController: NavHostController, page : String){
    var selectedItem by remember { mutableStateOf(page)}
    val home = stringResource(R.string.home)
    val preferiti = stringResource(R.string.preferiti)
    val carrello = stringResource(R.string.carrello)
    val impostazioni = stringResource(R.string.impostazioni)

    BottomAppBar(
        cutoutShape = CircleShape,
        content = {
            BottomNavigation {
                BottomNavigationItem(
                    icon = {
                        Icon(Icons.Filled.Home , "")
                    },
                    label = {
                        Text(
                            text = home,
                            softWrap = false,
                            overflow = TextOverflow.Visible,
                            textAlign = TextAlign.Justify
                        )
                    },
                    selected = selectedItem == home,
                    onClick = {
                        navController.navigate(home)
                        selectedItem = home

                    },
                    alwaysShowLabel = false
                )

                BottomNavigationItem(
                    icon = {
                        Icon(Icons.Filled.Favorite , "")
                    },
                    label = {
                        Text(
                            text = preferiti,
                            softWrap = false,
                            overflow = TextOverflow.Visible,
                            textAlign = TextAlign.Justify
                        )
                    },
                    selected = selectedItem == preferiti,
                    onClick = {
                        Log.d("TAG", "cliccato preferiti")
                        navController.navigate(preferiti)
                        if(selectedItem != preferiti){

                            selectedItem = preferiti
                        }

                    },
                    alwaysShowLabel = false
                )
                Box(modifier = Modifier.weight(1f))

                BottomNavigationItem(
                    icon = {
                        Icon(Icons.Filled.ShoppingCart , "")
                    },
                    label = {
                        Text(
                            text = carrello,
                            softWrap = false,
                            overflow = TextOverflow.Visible,
                            textAlign = TextAlign.Justify
                        )
                    },
                    selected = selectedItem == carrello,
                    onClick = {
                        selectedItem = carrello
                    },
                    alwaysShowLabel = false,

                )

                BottomNavigationItem(
                    icon = {
                        Icon(Icons.Filled.Settings ,  "")
                    },
                    label = {
                        Text(
                            text = impostazioni,
                            softWrap = false,
                            overflow = TextOverflow.Visible,
                            textAlign = TextAlign.Justify,
                            modifier = Modifier.offset(x = (-15).dp)  //non mi piace
                        )
                    },
                    selected =  selectedItem == impostazioni,
                    onClick = {
                        selectedItem = impostazioni
                    },
                    alwaysShowLabel = false
                )
            }

        }

    )
}










