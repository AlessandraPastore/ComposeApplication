package com.example.myapplication

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
    //enable dovrà esser posto dentro il view model in modo tale da verificare se l'utente ha dato i permessi
    //in caso contrario mostriamo le cose di default
    var enable=
        mutableStateOf(false)

    private fun requestStoragePermission() {
       if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE))
            requestResult.launch( android.Manifest.permission.READ_EXTERNAL_STORAGE)
    }
    private val requestResult=registerForActivityResult(ActivityResultContracts.RequestPermission())
    {
            permission ->
        enable.value = permission
    }
    fun loadImage()
    {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        try {
            startActivityForResult(intent, pickImgCode)
        } catch(e: ActivityNotFoundException) {
            Toast.makeText(applicationContext, "no!", Toast.LENGTH_SHORT).show()
        }
    }
    private val pickImgCode = 100
    var imageUri:MutableState<Uri?> = mutableStateOf(null)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                pickImgCode -> {
                     imageUri.value = data?.data
                    val contentResolver = applicationContext.contentResolver

                    val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION
// Check for the freshest data.
                    imageUri.value?.let { contentResolver.takePersistableUriPermission(it, takeFlags)
                    Log.d("PermissionTest",it.toString())
                    }
                }
            }
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


    lateinit var enableDarkMode: MutableState<Boolean>


    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED)
        {
            requestStoragePermission()
        }
        else
            enable.value=true
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

