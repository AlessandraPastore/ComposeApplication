package com.example.myapplication.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RicetteViewModel (application: Application):AndroidViewModel(application) {

     val ricDao :DaoRicette

    val ingr:LiveData<List<Ingrediente>?>
    init {
        ricDao = RicetteDataBase.getDataBase(application,viewModelScope).dao()

        ingr=ricDao.getAllIngr()
    }
    fun insert(ingrediente: Ingrediente)=viewModelScope.launch (Dispatchers.IO){
    ricDao.insertIngrediente(ingrediente)
   }
    fun delete(ingrediente: Ingrediente)=viewModelScope.launch (Dispatchers.IO){
        ricDao.deleteIngr(ingrediente)
    }

}