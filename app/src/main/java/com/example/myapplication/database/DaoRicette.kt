package com.example.myapplication.database

import androidx.lifecycle.LiveData
import androidx.room.*

// LiveData gestisce autonomamente le operazioni in background:
// non servono coroutine/suspend function

@Dao
interface DaoRicette {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngrediente(ingrediente: Ingrediente)
    //sia per aggiungere che per eliminare

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRicettaPreview(ricettePreview: RicettePreview)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRicettaCompleta(ricettaCompleta: RicettaCompleta)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategoria(categoria: Categoria)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRicetteCategoria(rc: RicettaCategorie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredienteRicetta(ingrRic: IngredienteRIcetta)

    // Update del campo preferiti
    @Update
    suspend fun updateRicettaPreview(ric: RicettePreview)

    @Update
    suspend fun updateRicettaCompleta(ric: RicettaCompleta)

    @Update
    suspend fun updateCarrello(ingrediente: Ingrediente)

    @Delete
    suspend fun deleteIngredienteRicetta(ingrediente: Ingrediente)

    @Delete
    suspend fun deleteIngr(ingrediente: Ingrediente)

    @Delete
    suspend fun deleteRicetta(ric:RicettePreview)

    @Delete
    suspend fun eliminaIngredienteRicetta(ricettaIng: IngredienteRIcetta)

    @Query("UPDATE RicettePreview SET preferito=:pref WHERE titolo=:ric")
    suspend fun updatePref(pref:Boolean,ric:String)

    @Query("select count(*) from Categoria")
    suspend fun countCategoria():Int

    @Query("Select count(*) from Ingrediente")
    suspend fun countIngredienti():Int

    @Query("Select *from Ingrediente where inCarrello=1")
    fun getAllIngrIncarrello():LiveData<List<Ingrediente>?>

    @Query("select * from RicettaCompleta where titolo=:titolo")
    suspend fun showRicettaCompleta(titolo:String) : RicettaCompleta

    //bisogna passare al metodo la frase inserita dall'utente + il suffisso '%'
    //es utente inserisce carb
    //al metodo passiamo carb%
    @Query ("SELECT * from RicettePreview where titolo LIKE :nome ")
    fun getRicByName(nome:String):LiveData<List<RicettePreview>>

    @Query ("SELECT * from RicettePreview where titolo LIKE :nome and preferito=1")
    fun getRicPreferitiByName(nome:String):LiveData<List<RicettePreview>>

    @Query ("SELECT inCarrello from Ingrediente where ingrediente=:ingrediente")
    suspend fun inCarrello(ingrediente: String): Boolean

    //lista ingredienti di una ricetta
    @Query("Select Distinct * from IngredienteRIcetta where titolo=:ricetta")
    suspend fun IngrOfRecipe(ricetta:String):List<IngredienteRIcetta>

    @Query("Select Distinct Categoria.* from Categoria where Categoria.categoria in(Select RicettaCategorie.categoria from RicettePreview Inner Join RicettaCategorie on RicettaCategorie.titolo=RicettePreview.titolo where RicettePreview.titolo=:ricetta)")
    suspend fun allCatFromRecipe(ricetta: String):List<Categoria>

    @Query ("SELECT Distinct RicettePreview.*  from RicettePreview Inner Join RicettaCategorie on RicettePreview.titolo=RicettaCategorie.titolo where RicettaCategorie.categoria in (:lista)")
    fun getFilterRic(lista:List<String>):LiveData<List<RicettePreview>>

    @Query ("SELECT Distinct RicettePreview.* from RicettePreview Inner Join RicettaCategorie on RicettePreview.titolo=RicettaCategorie.titolo where RicettaCategorie.categoria in (:lista) and preferito=1")
    fun getFilterRicPref(lista:List<String>):LiveData<List<RicettePreview>>

    @Query("SELECT * from RicettePreview where preferito =1")
    fun getPreferiti():LiveData<List<RicettePreview>>

    @Query("SELECT * From RicettePreview")
    fun getAllPreview():LiveData<List<RicettePreview>>

    @Query ("Select count(*) from IngredienteRIcetta where ingrediente=:ingrediente")
    suspend fun numberIngInIngreRicetta(ingrediente: String):Int

    @Query("Delete from Ingrediente where ingrediente=:ingrediente")
    suspend fun deleteIngrFromName(ingrediente: String)

    @Query("select uri from RicettaCompleta where titolo=:titolo")
    suspend fun getUri(titolo:String):String

    @Query("select uri from RicettePreview ")
    suspend  fun getUriPreview():List<String?>

    @Query("Select count(*) from Ricettepreview where titolo=:titolo")
    suspend fun titoloExist(titolo: String):Int
}