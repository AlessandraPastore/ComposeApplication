package com.example.myapplication

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.database.RicetteViewModel
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private var imageUri:MutableState<Uri?> = mutableStateOf(null)

    val map:MutableMap<Uri?,Bitmap?> =mutableMapOf()

    // Gestione del salvataggio dell'immagine selezionata dall'utente
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            if (intent != null) {
                imageUri.value = intent.data
                map[imageUri.value] =
                    MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri.value)
                val contentResolver = applicationContext.contentResolver
                val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION
                imageUri.value?.let {
                    contentResolver.takePersistableUriPermission(it, takeFlags)
                }
            }
        }
    }

    // Funzione chiamata per aprire la finestra di dialogo che permette all'utente di selezionare
    // un'immagine dalla galleria
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

    // Oggetto che ritorna una reference a MainActivity: utilizzato quando serviva usare
    // il contesto dell'activity in determinate funzioni
    companion object {
        var inst: MainActivity? = null
          fun get(): MainActivity? {
                return inst
          }

    }

    fun resetUri()
    {
        imageUri.value=null
    }

    // Variabili per lo stato persistente
    private lateinit var enableDarkMode: MutableState<Boolean>
    private lateinit var listView: MutableState<Boolean>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        map[null] = null
        inst=this
        setContent {
            val model: RicetteViewModel = ViewModelProvider(this).get(RicetteViewModel::class.java)
            model.setBitmap()
            val preferences = getPreferences(MODE_PRIVATE)

            val starState = preferences.getBoolean(stringResource(R.string.darkMode_key),false)
            val listState = preferences.getBoolean(stringResource(R.string.listView_key),false)

            enableDarkMode = remember { mutableStateOf(starState) }
            listView = remember { mutableStateOf(listState)}

            GeneralManager(model, enableDarkMode, listView)
        }
    }

    // Gestione dell'immagine quando l'applicazione passa in background
    override fun onPostResume() {
        super.onPostResume()
        ViewModelProvider(this).get(RicetteViewModel::class.java).setBitmap()
    }

    // override di onPause() per salvare lo stato persistente
    override fun onPause() {
        super.onPause()

        val preferences = getPreferences(MODE_PRIVATE)
        val editor = preferences.edit()

        editor.putBoolean(getString(R.string.darkMode_key), enableDarkMode.value)
        editor.putBoolean(getString(R.string.listView_key), listView.value)

        editor.apply()
    }
}


@Composable
fun GeneralManager(
    model: RicetteViewModel,
    enableDarkMode: MutableState<Boolean>,
    listView: MutableState<Boolean>
)
{
    // Applico il tema definito in Colors.kt all'applicazione
    MyApplicationTheme (enableDarkMode){
        MainScreen(model,enableDarkMode, listView)
    }
}

