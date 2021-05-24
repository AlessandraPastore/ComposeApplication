package com.example.myapplication.database

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// AndroidViewModel è sottoclasse di ViewModel
class RicetteViewModel(application: Application):AndroidViewModel(application) {

    // Recupero della reference al Dao
    val ricDao :DaoRicette
    init {
        ricDao = RicetteDataBase.getDataBase(application,viewModelScope).dao()
    }

    val ricette: LiveData<List<RicettePreview>> = ricDao.getAllPreview()

    fun onPreferitoChange(ric: RicettePreview)=viewModelScope.launch (Dispatchers.IO){
        ricDao.updateRicettaPreview(ric)
    }

    /*
     Instance state per la gestione della App Bar in schermata Home (Home.kt)
     e relative funzioni per la gestione degli eventi (non richiedono uso di coroutine)
     */
    private val _expanded = MutableLiveData(false)
    val expanded: LiveData<Boolean> = _expanded

    private val _searching = MutableLiveData(false)
    val searching: LiveData<Boolean> = _searching

    private val _longPressed = MutableLiveData(false)
    val longPressed: LiveData<Boolean> = _longPressed

    fun onExpand(status: Boolean){
        _expanded.value = status
    }

    fun onSearch(status: Boolean){
        _searching.value = status
    }

    fun onLongPress(status: Boolean){
        _longPressed.value = status
    }

    fun onInvertPress(){
        if(_longPressed.value == true)
            _longPressed.value = false
        else
            _longPressed.value = true
    }

    // Instance state per gestire la DarkMode (Forse dovrebbe essere Persistent?)
    /*
    private val _enableDarkMode = MutableLiveData(false)
    val enableDarkMode: LiveData<Boolean> = _enableDarkMode
    fun onDarkModeChange(mode: Boolean)
    {
        _enableDarkMode.value = !mode
    }
    */

    /*
    init {
        viewModelScope.launch(Dispatchers.IO){
            val _ricette = ricDao.getAllPreview()
        }
    }
    */


    fun insert(ingrediente: Ingrediente)=viewModelScope.launch (Dispatchers.IO){
        ricDao.insertIngrediente(ingrediente)
    }

    fun delete(ingrediente: Ingrediente)=viewModelScope.launch (Dispatchers.IO){
        ricDao.deleteIngr(ingrediente)
    }

}
