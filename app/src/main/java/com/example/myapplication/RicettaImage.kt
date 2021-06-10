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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import java.io.IOException
import java.io.InputStream
import java.lang.NullPointerException


@Composable
fun  RicettaImage (uri:Uri?,Detail:Boolean,categoria: String){

    var urinew= uri
    if(!Detail)
        urinew=null

    AndroidView(
        { context -> ImageView(context).apply{
            try
            {
                //setImageURI(urinew)
                    //setImageBitmap(MainActivity.get()?.map?.get(urinew))
                Log.d("imageapply", this.contentDescription.toString())
            }
            catch (e: java.lang.Exception) {
                Log.d("image","bene")

                setImageDrawable(
                    (AppCompatResources.getDrawable(
                        MainActivity.get()?.applicationContext!!,
                        R.drawable.foto)
                            )
                )
            }

            this.scaleType = ImageView.ScaleType.CENTER_CROP

        }
        },

        modifier = Modifier.fillMaxSize(),


        update={  imageView ->
            GlobalScope.launch(Dispatchers.IO) {
            try{
                //imageView.setImageURI(urinew)
                val bit=MainActivity.get()!!.map[urinew]
                Log.d("prova aggiornamento",(bit==null).toString())
                Log.d("imageUpdate",bit.toString())
                if(bit!=null)
                imageView.setImageBitmap(MainActivity.get()!!.map[urinew])
                else
                    throw NullPointerException()
               }
            catch (e: java.lang.Exception){
                Log.d("imageUpdatecatch","null")
                when (categoria) {
                    "Antipasto" -> imageView.setImageDrawable(
                        AppCompatResources.getDrawable(
                            MainActivity.get()?.applicationContext!!,
                            R.drawable.antipasto
                        )
                    )
                    "Primo piatto" -> imageView.setImageDrawable(
                        AppCompatResources.getDrawable(
                            MainActivity.get()?.applicationContext!!,
                            R.drawable.primo
                        )
                    )
                    "Secondo piatto" -> imageView.setImageDrawable(
                        AppCompatResources.getDrawable(
                            MainActivity.get()?.applicationContext!!,
                            R.drawable.secondo
                        )
                    )
                    "Dolce" -> imageView.setImageDrawable(
                        AppCompatResources.getDrawable(
                            MainActivity.get()?.applicationContext!!,
                            R.drawable.dessert
                        )
                    )
                    else ->
                        imageView.setImageDrawable(
                            AppCompatResources.getDrawable(
                                MainActivity.get()?.applicationContext!!,
                                R.drawable.foto
                            )
                        )
                }
            }
        } })


}
