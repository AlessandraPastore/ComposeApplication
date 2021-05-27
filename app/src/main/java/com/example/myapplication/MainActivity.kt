package com.example.myapplication

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.database.RicetteViewModel
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {


companion object  {
    var inst: MainActivity? = null
        fun get(): MainActivity? {
            return inst
        }
    }

    fun ImageSelection() {
        intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        try {
            startActivityForResult(intent, 100)
        } catch (e: ActivityNotFoundException) {


        }


    }

    lateinit var enableDarkMode: MutableState<Boolean>

    @ExperimentalAnimationApi
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
    private var imageUriState =  mutableStateOf<Uri?>(null)
    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        imageUriState.value = uri
    }
    private val pickImgCode = 100
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK)
        {
            when(requestCode)
            {
                pickImgCode -> {
                    imageUriState.value = data?.data

                }
            }
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

@ExperimentalAnimationApi
@Composable
fun GeneralManager(model: RicetteViewModel, enableDarkMode: MutableState<Boolean>)
{
    MyApplicationTheme (enableDarkMode){
        MainScreen(model,enableDarkMode)
    }
}

