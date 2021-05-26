package com.example.myapplication


import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.KEY_ROUTE
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.database.RicetteViewModel


@ExperimentalAnimationApi
@Composable
fun MainScreen(model: RicetteViewModel,enableDarkMode: MutableState<Boolean>) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)

    val ricettaVuota by model.ricettaVuota.observeAsState(RicettaSample("","", mutableListOf()))

    Scaffold(
        floatingActionButton = {
            if(currentRoute != "${Screen.RicettaDetail.route}/{ricetta}")
                FAB(navController, currentRoute)
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            if(currentRoute != Screen.NuovaRicetta.route && currentRoute != "${Screen.RicettaDetail.route}/{ricetta}")
                BottomBar(model,navController, currentRoute)  //la bottom bar non si mostra su NuovaRicetta e su RicettaDetail
        },
    ){
        NavConfig(navController, enableDarkMode, model, ricettaVuota)
    }
}

// Funzione che gestisce il bottone centrale
@Composable
private fun FAB(navController: NavHostController, currentRoute: String?) {

    FloatingActionButton(
        onClick = {

            if(currentRoute != Screen.NuovaRicetta.route) {
                navController.navigate(Screen.NuovaRicetta.route){

                    popUpTo = navController.graph.startDestination
                    launchSingleTop = true

                }

            }
            else{
                navController.navigate(Screen.Home.route){

                    popUpTo = navController.graph.startDestination
                    launchSingleTop = true

                }
            }

        },
    )
    {
        if(currentRoute != Screen.NuovaRicetta.route)   Icon( Icons.Rounded.Add, "")
        else Icon( Icons.Rounded.Check, "")
    }
}

// Funzione che gestisce la BottomBar e la navigazione delle icone
@Composable
private fun BottomBar(model: RicetteViewModel, navController: NavHostController, currentRoute: String?){

    val home = Screen.Home.route
    val preferiti = Screen.Preferiti.route
    val carrello = Screen.Carrello.route
    val impostazioni = Screen.Impostazioni.route

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

                        model.onHomeClick()

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
                        model.onPreferitiClick()

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










