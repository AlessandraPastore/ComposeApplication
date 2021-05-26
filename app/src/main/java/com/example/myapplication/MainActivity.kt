package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.database.RicetteViewModel
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val model: RicetteViewModel = ViewModelProvider(this).get(RicetteViewModel::class.java)

            GeneralManager(model)
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun GeneralManager(model: RicetteViewModel)
{
    val actual = isSystemInDarkTheme()
    val enableDarkMode = remember { mutableStateOf(actual) }

    //val enableDarkMode: Boolean by model.enableDarkMode.observeAsState(false)

    MyApplicationTheme (enableDarkMode){
        MainScreen(model,enableDarkMode)
    }
}

