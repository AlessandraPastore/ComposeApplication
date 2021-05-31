package com.example.myapplication


import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
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
import com.example.myapplication.reactingLists.dialogIngredient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@ExperimentalAnimationApi
@Composable
fun MainScreen(model: RicetteViewModel,enableDarkMode: MutableState<Boolean>) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)



    Scaffold(
        floatingActionButton = {
            if(currentRoute != "${Screen.RicettaDetail.route}/{ricetta}")
                FAB(model, navController, currentRoute)
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            if(currentRoute != Screen.NuovaRicetta.route && currentRoute != "${Screen.RicettaDetail.route}/{ricetta}")
                BottomBar(model,navController, currentRoute)  //la bottom bar non si mostra su NuovaRicetta e su RicettaDetail
        },
    ){
        NavConfig(navController, enableDarkMode, model)
    }
}

// Funzione che gestisce il bottone centrale
@Composable
private fun FAB(model: RicetteViewModel, navController: NavHostController, currentRoute: String?) {

    val openDialog = remember { mutableStateOf(false)  }
    val scope = rememberCoroutineScope()
    var error:String

    FloatingActionButton(
        onClick = {

            if(currentRoute != Screen.NuovaRicetta.route) {
                navController.navigate(Screen.NuovaRicetta.route){

                    popUpTo = navController.graph.startDestination
                    launchSingleTop = true

                }

            }
            else{

                error = model.onRicettaAddVerify()
                Log.d("test", error)

                if(!error.equals("")) openDialog.value = true
                else {

                    //se era una modifica bisogna resettare le variabili e cancellarla prima
                    if(model.getModify()){
                        model.addModify()
                        Log.d("test","cancellata")
                    }

                    scope.launch {
                        delay(1000)  //aspetto che faccia il delete, non so ancora un modo migliore
                        model.onRicettaAdd()                //ERRORE su dispositivi lenti
                    }

                    model.restartFilters()
                    navController.navigate(Screen.Home.route){

                        popUpTo = navController.graph.startDestination
                        launchSingleTop = true

                    }
                }



            }


        },
    )
    {
        if(currentRoute != Screen.NuovaRicetta.route)   Icon( Icons.Rounded.Add, "")
        else Icon( Icons.Rounded.Check, "")
    }

    if(openDialog.value) AlertError(openDialog)
}

@Composable
fun AlertError(openDialog: MutableState<Boolean>) {
    AlertDialog(
        onDismissRequest = { openDialog.value = false },
        title = { Text(text = "Errore nei dati inseriti") },
        text = { Text(text="Uno o più campi risultano vuoti") },
        confirmButton = {
            Button(
                onClick = {
                    openDialog.value = false
                }) {
                Text("Capito")
            }
        },
        backgroundColor = MaterialTheme.colors.background
    )
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

                        model.onHomeClick()     // Carico tutte le ricette

                        model.updateTipologia(false)

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

                        model.onPreferitiClick()    // Carico solo le ricette dei preferiti

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










