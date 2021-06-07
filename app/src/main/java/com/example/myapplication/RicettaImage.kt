package com.example.myapplication

import android.net.Uri
import androidx.appcompat.content.res.AppCompatResources
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources.getDrawable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.example.myapplication.*
import com.example.myapplication.R
import com.example.myapplication.database.RicettePreview
import com.example.myapplication.database.RicetteViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import androidx.compose.ui.viewinterop.AndroidView
import java.io.IOException
import java.io.InputStream


@Composable
fun  RicettaImage (urStr:Uri?){

    var urinew= urStr

    if(urinew == null){
        Log.d("null","lo vedo")
    }

    if(urinew!=null)
        //Log.d("Image",urinew.toString())

    if (urinew != null) {
        Log.d("image2", File(urinew.encodedPath!!).canRead().toString())
    }

    Log.d("image22", "entrato")
    //Log.d("Image2", File(urinew!!.encodedPath).toString())

    var img:ImageView

    val scope = rememberCoroutineScope()

        AndroidView(
            { context ->
                ImageView(context).apply {
                    try {
                        scope.launch {
                            setImageURI(urinew)
                        }
                            Log.d("image22", this.contentDescription.toString())

                    } catch (e: java.lang.Exception) {
                        Log.d("image", "bene")

                        setImageDrawable(
                            (AppCompatResources.getDrawable(
                                MainActivity.get()?.applicationContext!!,
                                R.drawable.foto
                            )
                                    )
                        )
                    }

                    this.scaleType = ImageView.ScaleType.CENTER_CROP

                }
            },

            modifier = Modifier.fillMaxSize(),


            update = { imageView ->
                try {
                    scope.launch {
                        imageView.setImageURI(urinew)
                    }
                } catch (e: java.lang.Exception) {
                    imageView.setImageDrawable(
                        AppCompatResources.getDrawable(
                            MainActivity.get()?.applicationContext!!,
                            R.drawable.foto
                        )
                    )
                }
            })



}
