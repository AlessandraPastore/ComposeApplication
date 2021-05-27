package com.example.myapplication.database

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Filtro
import com.example.myapplication.RicettaSample
import com.example.myapplication.getFilters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// AndroidViewModel è sottoclasse di ViewModel
class RicetteViewModel(application: Application):AndroidViewModel(application) {

    // Recupero della reference al Dao, non accessibile all'esterno di RicetteViewModel
    private val ricDao :DaoRicette
    init {
        ricDao = RicetteDataBase.getDataBase(application,viewModelScope).dao()
    }

    var ricette: LiveData<List<RicettePreview>> = ricDao.getAllPreview()

    // Viene premuto il cuore -> Ricetta aggiunta o eliminata dai preferiti
    fun onPreferitoChange(ric: RicettePreview)=viewModelScope.launch (Dispatchers.IO){
        ricDao.updateRicettaPreview(ric)
    }

    fun onRicettaDelete(ric: RicettePreview)=viewModelScope.launch (Dispatchers.IO){
        ricDao.deleteRicetta(ric)
    }

    fun onRicettaAdd(ric: RicettaSample)=viewModelScope.launch (Dispatchers.IO){

        // Si deve aggiungere anche la categoria

        ricDao.insertRicettaPreview(RicettePreview( _ricettaVuota.value!!.titolo , false))
        ricDao.insertRicettaCompleta(RicettaCompleta( _ricettaVuota.value!!.titolo , ric.descrizione))

        val lista = ric.ingredienti

        lista.forEach{ ingrediente ->
            ingrediente.titolo = ric.titolo

            ricDao.insertIngrediente(Ingrediente(ingrediente.ingrediente, false))
            ricDao.insertIngredienteRicetta(ingrediente)
        }

        Log.d("test",ric.titolo)
    }



    // Quando viene schiacciato il tasto Home, carica la lista di tutte le ricette
    fun onHomeClick(){
        ricette = ricDao.getAllPreview()
    }

    // Quando viene schiacciato il tasto Preferiti, carica la lista delle ricette classificate come tali
    fun onPreferitiClick(){
        //ricette = ricDao.getPreferiti()

        ricette = ricDao.getFilterRic(listOf("Secondo piatto"))
    }
    /*
    fun onFiltroChecked(filtro: Filtro){
        ricette = ricDao.getFilterRic(listOf(filtro.name))
    }

     */

    private var _ricettaVuota = MutableLiveData(RicettaSample("","", mutableListOf()))
    val ricettaVuota: LiveData<RicettaSample> = _ricettaVuota

    fun onTitoloInsert(titolo: String){
        _ricettaVuota.value!!.titolo = titolo
        Log.d("doge", _ricettaVuota.value!!.titolo )
    }

    fun onDescrizioneInsert(descrizione: String){
        _ricettaVuota.value!!.descrizione = descrizione
    }


    /*
    private val _titolo = MutableLiveData("")
    val titolo: LiveData<String> = _titolo

    private val _ingrediente = MutableLiveData("")
    val ingrediente: LiveData<String> = _ingrediente

    private val _quantità = MutableLiveData("")
    val quantità: LiveData<String> = _quantità
     */

    private val _filtri = MutableLiveData(getFilters())
    val filtri: LiveData<List<Filtro>> = _filtri

    fun onFiltroChecked(){
        val lista: MutableList<String> = mutableListOf()

        for(filtro in _filtri.value!!){
            if(filtro.checked)
                lista.add(filtro.name)
                Log.d("Test",filtro.checked.toString() + filtro.name)
        }

        ricette = ricDao.getFilterRic(lista)
    }

    /*
     Instance state per la gestione della App Bar in schermate Home e Preferiti
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
        _longPressed.value = _longPressed.value != true
    }

    fun insert(ingrediente: Ingrediente)=viewModelScope.launch (Dispatchers.IO){
        ricDao.insertIngrediente(ingrediente)
    }

    fun delete(ingrediente: Ingrediente)=viewModelScope.launch (Dispatchers.IO){
        ricDao.deleteIngr(ingrediente)
    }

}
