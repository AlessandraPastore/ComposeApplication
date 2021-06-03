package com.example.myapplication

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.database.RicetteViewModel
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch
import java.net.URI

class MainActivity : ComponentActivity() {

    var enable=
        mutableStateOf(false)
    var imageUri:MutableState<Uri?> = mutableStateOf(null)
    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            if (intent != null) {
                imageUri.value = intent.data
                val contentResolver = applicationContext.contentResolver
                val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION
                imageUri.value?.let {
                    contentResolver.takePersistableUriPermission(it, takeFlags)
                }
            }
        }
    }

        private val pickImgCode = 100
    fun loadImage()
    {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        try {

            startForResult.launch(intent)
        } catch(e: ActivityNotFoundException) {
            Toast.makeText(applicationContext, "no!", Toast.LENGTH_SHORT).show()
        }

    }

fun getUri():Uri?
{
    return imageUri.value
}

companion object  {
    var inst: MainActivity? = null
        fun get(): MainActivity? {
            return inst
        }

    }
fun ResetUri()
{
    imageUri.value=null
}

    lateinit var enableDarkMode: MutableState<Boolean>


    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inst=this
        setContent {
            val model: RicetteViewModel = ViewModelProvider(this).get(RicetteViewModel::class.java)

            val preferences = getPreferences(MODE_PRIVATE)
            val starState = preferences.getBoolean(stringResource(R.string.darkMode_key),false)

            enableDarkMode = remember { mutableStateOf(starState) }

            GeneralManager(model, enableDarkMode)
        }
    }


    override fun onPause() {
        super.onPause()

        val preferences = getPreferences(MODE_PRIVATE)
        val editor = preferences.edit()

        editor.putBoolean(getString(R.string.darkMode_key), enableDarkMode.value)

        editor.apply()
    }
}


@ExperimentalFoundationApi
@Composable
fun GeneralManager(model: RicetteViewModel, enableDarkMode: MutableState<Boolean>)
{
    MyApplicationTheme (enableDarkMode){
        MainScreen(model,enableDarkMode)
    }
}

