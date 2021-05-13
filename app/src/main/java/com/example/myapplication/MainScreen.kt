package com.example.myapplication


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.KEY_ROUTE
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController


@Composable
fun MainScreen(enableDarkMode: MutableState<Boolean>) {
    val navController = rememberNavController()
    Scaffold(
        //topBar = { TopBar(page) },
        floatingActionButton = {
            FloatingActionButton(onClick = {  }) {
                Icon( Icons.Rounded.Add, "")
            }
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = { BottomBar(navController) },

    ){
        NavConfig(navController,enableDarkMode)
    }


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
private fun BottomBar(navController: NavHostController){

    val home = stringResource(R.string.home)
    val preferiti = stringResource(R.string.preferiti)
    val carrello = stringResource(R.string.carrello)
    val impostazioni = stringResource(R.string.impostazioni)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)

    BottomAppBar(
        cutoutShape = CircleShape,
        content = {
            BottomNavigation {
                BottomNavigationItem(
                    icon = {
                        Icon(Icons.Rounded.Home , "")
                    },
                    label = {
                        Text(
                            text = home,
                            softWrap = false,
                            overflow = TextOverflow.Visible,
                            textAlign = TextAlign.Justify
                        )
                    },
                    selected = currentRoute == home,
                    onClick = {
                        navController.navigate(home) {
                            popUpTo = navController.graph.startDestination
                            launchSingleTop = true
                        }
                    },
                    alwaysShowLabel = false
                )

                BottomNavigationItem(
                    icon = {
                        Icon(Icons.Rounded.Favorite , "")
                    },
                    label = {
                        Text(
                            text = preferiti,
                            softWrap = false,
                            overflow = TextOverflow.Visible,
                            textAlign = TextAlign.Justify
                        )
                    },
                    selected = currentRoute == preferiti,
                    onClick = {
                            navController.navigate(preferiti) {
                                popUpTo = navController.graph.startDestination
                                launchSingleTop = true
                            }
                    },
                    alwaysShowLabel = false
                )

                Box(modifier = Modifier.weight(1f))

                BottomNavigationItem(
                    icon = {
                        Icon(Icons.Rounded.ShoppingCart , "")
                    },
                    label = {
                        Text(
                            text = carrello,
                            softWrap = false,
                            overflow = TextOverflow.Visible,
                            textAlign = TextAlign.Justify
                        )
                    },
                    selected = currentRoute == carrello,
                    onClick = {
                        navController.navigate(carrello) {
                            popUpTo = navController.graph.startDestination
                            launchSingleTop = true
                        }

                    },
                    alwaysShowLabel = false,
                )

                BottomNavigationItem(
                    icon = {
                        Icon(Icons.Rounded.Settings ,  "")
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
                    selected =  currentRoute == impostazioni,
                    onClick = {
                        navController.navigate(impostazioni) {
                            popUpTo = navController.graph.startDestination
                            launchSingleTop = true
                        }
                    },
                    alwaysShowLabel = false
                )
            }

        }

    )
}










