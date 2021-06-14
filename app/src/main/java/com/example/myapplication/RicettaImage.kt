package com.example.myapplication

import android.net.Uri
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

// Funzione che gestisce l'immagine da visualizzare: si è utilizzato AndroidView al fine di
// mostrare l'interoperabilità tra Compose e AndroidView
@Composable
fun  RicettaImage (uri:Uri?,Detail:Boolean,categoria: String){

    var urinew= uri
    if(!Detail)
        urinew=null

    AndroidView(
        { context -> ImageView(context).apply{
            try
            {

            }
            catch (e: java.lang.Exception) {

                setImageDrawable(
                    (getDrawable(
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

            try{

                val bit=MainActivity.get()!!.map[urinew]
                if(bit!=null)
                imageView.setImageBitmap(MainActivity.get()!!.map[urinew])
                else
                    throw NullPointerException()

               }
            catch (e: java.lang.Exception){

                //Decide l'immagine appartenente alla categoria adeguata
                when (categoria) {
                    Filtro.Antipasto.name -> imageView.setImageDrawable(
                        getDrawable(
                            MainActivity.get()?.applicationContext!!,
                            R.drawable.antipasto
                        )
                    )
                    Filtro.Primo.name -> imageView.setImageDrawable(
                        getDrawable(
                            MainActivity.get()?.applicationContext!!,
                            R.drawable.primo
                        )
                    )
                    Filtro.Secondo.name -> imageView.setImageDrawable(
                        getDrawable(
                            MainActivity.get()?.applicationContext!!,
                            R.drawable.secondo
                        )
                    )
                    Filtro.Dessert.name -> imageView.setImageDrawable(
                        getDrawable(
                            MainActivity.get()?.applicationContext!!,
                            R.drawable.dessert
                        )
                    )

                    //immagine di default
                    else ->
                        imageView.setImageDrawable(
                            getDrawable(
                                MainActivity.get()?.applicationContext!!,
                                R.drawable.foto
                            )
                        )
                }
            }
        })


}
