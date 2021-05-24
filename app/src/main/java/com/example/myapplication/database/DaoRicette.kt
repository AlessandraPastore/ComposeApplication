package com.example.myapplication.database

import androidx.lifecycle.LiveData
import androidx.room.*

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

    @Update
    suspend fun updateRicettaPreview(ric: RicettePreview)

    @Update
    suspend fun updateRicettaCompleta(ric: RicettaCompleta)

    @Update
    suspend fun updateCarrello(ingrediente: Ingrediente)

    @Delete
    suspend fun deleteIngr(ingrediente: Ingrediente)

    @Delete
    suspend fun eliminaRicetta(ric:RicettePreview)

    @Delete
    suspend fun eliminaIngredienteRicetta(ricettaIng: IngredienteRIcetta)

    @Query("UPDATE RicettePreview SET preferito=:pref WHERE titolo=:ric")
    suspend fun updatePref(pref:Boolean,ric:String)

    @Query("select count(*) from Categoria")
    suspend fun countCategoria():Int

    @Query("Select count(*) from Ingrediente")
    suspend fun countIngredienti():Int

    @Query("Select *from Ingrediente where inCarrello=:inCarrello")
    fun getAllIngrIncarrello(inCarrello :Boolean=true):LiveData<List<Ingrediente>?>

    @Query("select * from RicettaCompleta where titolo=:titolo")
    suspend fun showRicettaCompleta(titolo:String) : RicettaCompleta

    //bisogna passare al metodo la frase inserita dall'utente + il suffisso '%'
    //es utente inserisce carb
    //al metodo passiamo carb%
    @Query ("SELECT * from RicettePreview where titolo LIKE :nome ")
    fun getRicByName(nome:String):LiveData<List<RicettePreview>?>

    //lista ingredienti di una ricetta
    @Query("Select Distinct * from IngredienteRIcetta where ricetta=:ricetta")
    suspend fun IngrOfRecipe(ricetta:String):List<IngredienteRIcetta>

    @Query ("SELECT Distinct RicettePreview.* from RicettePreview Inner Join RicettaCategorie on RicettePreview.titolo=RicettaCategorie.ricetta where RicettaCategorie.categoria in (:lista)")
    fun getFilterRic(lista:List<String>):LiveData<List<RicettePreview>>

    @Query("SELECT * from RicettePreview where preferito =:liked")
    fun getPreferiti(liked:Boolean=true):LiveData<List<RicettePreview>>

    @Query("SELECT * From RicettePreview")
    fun getAllPreview():LiveData<List<RicettePreview>>
}