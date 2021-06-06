package com.example.myapplication


import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavHostController
import androidx.navigation.compose.KEY_ROUTE
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.database.RicetteViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch



@ExperimentalFoundationApi
@Composable
fun MainScreen(
    model: RicetteViewModel,
    enableDarkMode: MutableState<Boolean>,
    listView: MutableState<Boolean>
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)

    Scaffold(
        floatingActionButton = {

            // Negli schermi che mostrano i dettagli delle singole ricette non si mostra il FAB
            if(currentRoute != "${Screen.RicettaDetail.route}/{ricetta}")
                FAB(model, navController, currentRoute)
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {

            // La bottom bar non si mostra su NuovaRicetta e su RicettaDetail
            if(currentRoute != Screen.NuovaRicetta.route && currentRoute != "${Screen.RicettaDetail.route}/{ricetta}")
                BottomBar(model,navController, currentRoute)
        },
    ){
        NavConfig(model, navController, enableDarkMode,  listView)
    }
}

// Funzione che gestisce il bottone centrale "fluttuante"
@Composable
private fun FAB(model: RicetteViewModel, navController: NavHostController, currentRoute: String?) {

    val openDialog = remember { mutableStateOf(false)  }
    val scope = rememberCoroutineScope()
    val error = remember { mutableStateOf("")  }

    FloatingActionButton(
        onClick = {

            if(currentRoute != Screen.NuovaRicetta.route) {
                model.resetRicettaVuota()
                navController.navigate(Screen.NuovaRicetta.route){

                    popUpTo = navController.graph.startDestination
                    launchSingleTop = true

                }

            }
            else{

                error.value = model.onRicettaAddVerify()
                Log.d("error", error.value)

                //se error è qualcosa di diverso da "" si è presentato un errore
                if(error.value != "") openDialog.value = true
                else {

                    //se era una modifica bisogna resettare le variabili e cancellarla prima
                    if(model.getModify()){
                        model.addModify()   //viene cancellata qua dentro
                        Log.d("test","cancellata")
                    }


                    scope.launch {
                        delay(1000)  //aspetto che faccia il delete, non so ancora un modo migliore
                        model.onRicettaAdd()
                    }


                    //resetta le variabili
                    if(model.getFromDetails() == true) {
                        model.isFromDetails()
                        model.onInvertPress()
                    }

                    model.restartFilters()
                    model.updateTipologia(false)
                    model.onHomeClick()

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

    if(openDialog.value) AlertError(openDialog, error.value)
}

@Composable
fun AlertError(openDialog: MutableState<Boolean>, error: String) {
    AlertDialog(
        onDismissRequest = { openDialog.value = false },
        title = { Text(text = "Errore nei dati inseriti") },
        text = {
            Column(){
                Text(text="Uno o più campi risultano vuoti")
                Text(text=error)
            }

               },
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

// Funzione che gestisce la BottomBar e la navigazione delle relative icone
@Composable
private fun BottomBar(model: RicetteViewModel, navController: NavHostController, currentRoute: String?){

    val home = Screen.Home.route
    val preferiti = Screen.Preferiti.route
    val carrello = Screen.Carrello.route
    val impostazioni = Screen.Modalità.route

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










