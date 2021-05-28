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
import kotlinx.coroutines.Job
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

    //aggiunge la ricetta salvata in _ricettaVuota
    fun onRicettaAdd()=viewModelScope.launch (Dispatchers.IO){

        // Si deve aggiungere anche la categoria


        ricDao.insertRicettaPreview(RicettePreview( _ricettaVuota.value!!.titolo , false))
        ricDao.insertRicettaCompleta(RicettaCompleta( _ricettaVuota.value!!.titolo , _ricettaVuota.value!!.descrizione))

        _ricettaVuota.value!!.filtri.forEach{ filtro ->
            ricDao.insertRicetteCategoria(RicettaCategorie(_ricettaVuota.value!!.titolo, filtro.name))
        }



        _ricettaVuota.value!!.ingredienti.forEach { ingrediente ->
            //ingrediente.titolo = _ricettaVuota.value!!.titolo

            ricDao.insertIngrediente(Ingrediente(ingrediente.ingrediente, false))
            ricDao.insertIngredienteRicetta(ingrediente)
        }
    }

    fun onRicettaAddVerify():Boolean{

        if(_ricettaVuota.value!!.titolo.isBlank()) return false
        if(_ricettaVuota.value!!.descrizione.isBlank()) return false
        if(_ricettaVuota.value!!.filtri.isEmpty()) return false
        if(_ricettaVuota.value!!.ingredienti.isEmpty()) return false

        _ricettaVuota.value!!.ingredienti.forEach { ingrediente ->

            if(ingrediente.ingrediente.isBlank() || ingrediente.qta.isBlank()) return false
            ingrediente.titolo = _ricettaVuota.value!!.titolo

        }

        return true
    }

    // Quando viene schiacciato il tasto Home, carica la lista di tutte le ricette
    fun onHomeClick(){
        ricette = ricDao.getAllPreview()

        val lista: MutableList<String> = mutableListOf()

        for(filtro in _filtri.value!!){
            if(filtro.checked)
                lista.add(filtro.name)
        }

        if(!lista.isEmpty())
            ricette = ricDao.getFilterRic(lista)
    }

    // Quando viene schiacciato il tasto Preferiti, carica la lista delle ricette classificate come tali
    fun onPreferitiClick(){
        ricette = ricDao.getPreferiti()

        val lista: MutableList<String> = mutableListOf()

        for(filtro in _filtri.value!!){
            if(filtro.checked)
                lista.add(filtro.name)
        }

        if(!lista.isEmpty())
            ricette = ricDao.getFilterRic(lista)

    }

    private var _ricettaVuota = MutableLiveData(RicettaSample("","", mutableListOf(), mutableListOf()))
    //val ricettaVuota: LiveData<RicettaSample> = _ricettaVuota

    fun onTitoloInsert(titolo: String){
        _ricettaVuota.value!!.titolo = titolo
        Log.d("doge", _ricettaVuota.value!!.titolo )
    }

    fun onDescrizioneInsert(descrizione: String){
        _ricettaVuota.value!!.descrizione = descrizione
    }

    fun onIngredientsInsert(ingList : MutableList<IngredienteRIcetta>){
        _ricettaVuota.value!!.ingredienti = ingList
    }

    fun onFilterInsert(filterList : MutableList<Filtro>){
        _ricettaVuota.value!!.filtri = filterList
    }

    fun restartFilters(){
        for(filtro in _filtri.value!!){
            filtro.checked = false
        }
    }


    private val _ricettaCompleta = MutableLiveData(RicettaSample("","", mutableListOf(), mutableListOf()))
    val ricettaCompleta: LiveData<RicettaSample> = _ricettaCompleta

    fun getRicetta(titolo:String)=viewModelScope.launch (Dispatchers.IO) {

        _ricettaCompleta.value!!.titolo = titolo
        _ricettaCompleta.value!!.descrizione = ricDao.showRicettaCompleta(titolo).descrizione
        _ricettaCompleta.value!!.ingredienti = ricDao.IngrOfRecipe(titolo) as MutableList<IngredienteRIcetta>
        //ricettaCompleta.filtri = manca getFiltriRic

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

        Log.d("Test", ricette.value!!.size.toString())

        for(filtro in _filtri.value!!){
            if(filtro.checked)
                lista.add(filtro.name)


        }

        Log.d("Test",lista.toString())

        val tmp: List<String> = lista
        Log.d("Test",tmp.toString())

        //if(!lista.isEmpty())
            //ricette = ricDao.getFilterRic()
        ricette = ricDao.getFilterRic(listOf("Secondo piatto"))
        Log.d("Test", ricette.value!!.size.toString())

        //val xx = ricDao.getFilterRic()

        //if(xx.value != null){
          //  Log.d("Test", xx.value!!.size.toString())
        //}


        //ricette = ricDao.getFilterRic(listOf("Secondo piatto"))
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
