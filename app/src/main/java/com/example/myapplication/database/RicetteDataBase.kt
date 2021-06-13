package com.example.myapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myapplication.Filtro
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


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
    version = 5,
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
                        runBlocking {
                            prepopulateDatabase(database.dao())
                        }
                    }
                }

            }
        }
        suspend fun prepopulateDatabase(dao:DaoRicette)
        {
            if(dao.countIngredienti()==0) {
                dao.insertCategoria(Categoria(Filtro.Antipasto.name))
                dao.insertCategoria(Categoria(Filtro.Primo.name))
                dao.insertCategoria(Categoria(Filtro.Secondo.name))
                dao.insertCategoria(Categoria(Filtro.Dessert.name))
                dao.insertCategoria(Categoria(Filtro.Vegetariano.name))
                dao.insertCategoria(Categoria(Filtro.Vegano.name))
                dao.insertCategoria(Categoria(Filtro.GlutenFree.name))

                dao.insertIngrediente(Ingrediente("Pasta",false))
                dao.insertIngrediente(Ingrediente("Sugo al Pomodoro",false))
                dao.insertIngrediente(Ingrediente("Pollo",false))
                dao.insertIngrediente(Ingrediente("succo di limone",false))

                dao.insertRicettaPreview(RicettePreview("Pasta al Pomodoro",false))
                dao.insertRicettaCompleta(RicettaCompleta("Pasta al Pomodoro","Cuocere la pasta per 10 minuti, alla fine aggiungerci il sugo al pomodoro."))
                dao.insertIngredienteRicetta(ingrRic = IngredienteRIcetta("Pasta al Pomodoro","Pasta","200g"))
                dao.insertIngredienteRicetta(ingrRic = IngredienteRIcetta("Pasta al Pomodoro","Sugo al Pomodoro","200g"))
                dao.insertRicetteCategoria(rc = RicettaCategorie("Pasta al Pomodoro",Filtro.Primo.name))
                dao.insertRicetteCategoria(rc = RicettaCategorie("Pasta al Pomodoro",Filtro.Vegano.name))

                dao.insertRicettaPreview(RicettePreview("Pollo al limone",false))
                dao.insertRicettaCompleta(RicettaCompleta("Pollo al limone","Cuocere il pollo in una padella per 10 munuti, successivamente aggiungerci il succo di limone."))
                dao.insertIngredienteRicetta(ingrRic = IngredienteRIcetta("Pollo al limone","Pollo","200g"))
                dao.insertIngredienteRicetta(ingrRic = IngredienteRIcetta("Pollo al limone","succo di limone","200ml"))
                dao.insertRicetteCategoria(rc = RicettaCategorie("Pollo al limone",Filtro.Secondo.name))
                dao.insertRicetteCategoria(rc = RicettaCategorie("Pollo al limone",Filtro.GlutenFree.name))
            }
        }
    }
}