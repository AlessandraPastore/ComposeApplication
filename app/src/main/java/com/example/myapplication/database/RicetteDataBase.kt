package com.example.myapplication.database

import android.content.Context

import android.util.Log
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(
    entities =
    [
        Categoria::class,
        Ingrediente::class,
        IngredienteRIcetta::class,
        RicettaCategorie::class,
        RicettaCompleta::class,
        RicettePreview::class
    ],
    version = 4,
    exportSchema = true
)
abstract class RicetteDataBase : RoomDatabase() {
    abstract fun dao(): DaoRicette

    companion object {
        private var INSTANCE: RicetteDataBase? = null

        fun getDataBase(context: Context,
                        scope:CoroutineScope) :RicetteDataBase {
            return INSTANCE?: synchronized(this)
            {
                val instance=Room.databaseBuilder(context.applicationContext,RicetteDataBase::class.java,
                "ricette_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(RicetteDataBaseCallback(scope))
                    .build()
                INSTANCE=instance
                instance
            }


        }

        private class RicetteDataBaseCallback (private val  scope: CoroutineScope):RoomDatabase.Callback()
        {
            override fun onOpen(db: SupportSQLiteDatabase)
            {
                super.onOpen(db)



                INSTANCE?.let { database->

                    scope.launch {
                        prepopulateDatabase(database.dao())
                    }
                }

            }
        }
        suspend fun prepopulateDatabase(dao:DaoRicette)
        {
           if(dao.countIngredienti()==0) {
                dao.insertCategoria(Categoria("Antipasto"))
                dao.insertCategoria(Categoria("Primo piatto"))
                dao.insertCategoria(Categoria("Secondo piatto"))
                dao.insertCategoria(Categoria("Dolce"))
                dao.insertCategoria(Categoria("Vegetariano"))
                dao.insertCategoria(Categoria("Vegano"))
                dao.insertCategoria(Categoria("Gluten free"))

                dao.insertIngrediente(Ingrediente("VONGOLA",false))
                dao.insertIngrediente(Ingrediente("WURSTEL",false))
                dao.insertIngrediente(Ingrediente("ZAFFERANO",false))
                dao.insertIngrediente(Ingrediente("ZUCCHINE",false))

                dao.insertRicettaPreview(RicettePreview("pietanza 1",false))
                dao.insertRicettaCompleta(RicettaCompleta("pietanza 1","la si cucina quando si è a casa da soli"))
                dao.insertIngredienteRicetta(ingrRic = IngredienteRIcetta("pietanza 1","VONGOLA","2"))
                dao.insertIngredienteRicetta(ingrRic = IngredienteRIcetta("pietanza 1","WURSTEL","200"))
                dao.insertRicetteCategoria(rc = RicettaCategorie("pietanza 1","Secondo piatto"))
                dao.insertRicetteCategoria(rc = RicettaCategorie("pietanza 1","Vegano"))

                dao.insertRicettaPreview(RicettePreview("pietanza 2",false))
                dao.insertRicettaCompleta(RicettaCompleta("pietanza 2","la si cucina quando si è ad una festa"))
                dao.insertIngredienteRicetta(ingrRic = IngredienteRIcetta("pietanza 2","ZAFFERANO","200"))
                dao.insertIngredienteRicetta(ingrRic = IngredienteRIcetta("pietanza 2","ZUCCHINE","200"))
                dao.insertRicetteCategoria(rc = RicettaCategorie("pietanza 2","Antipasto"))
                dao.insertRicetteCategoria(rc = RicettaCategorie("pietanza 2","Vegano"))
            }
        }
    }
}