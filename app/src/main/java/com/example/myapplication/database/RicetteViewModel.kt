package com.example.myapplication.database

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Filtro
import com.example.myapplication.R
import com.example.myapplication.RicettaSample
import com.example.myapplication.getFilters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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

    fun onRicettaDelete() =viewModelScope.launch (Dispatchers.IO) {
        val listing=ricDao.IngrOfRecipe(_ricettaSelezionata.value!!.titolo)
        Log.d("test", "delete" + _ricettaSelezionata.value!!)

        ricDao.deleteRicetta(_ricettaSelezionata.value!!)

        listing.forEach{
            if(ricDao.numberIngInIngreRicetta(it.ingrediente)==0)
                ricDao.deleteIngrFromName(it.ingrediente)
        }

    }

    //aggiunge la ricetta salvata in _ricettaVuota
    fun onRicettaAdd()=viewModelScope.launch (Dispatchers.IO){


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

    fun onRicettaAddVerify():String{

        if(_ricettaVuota.value!!.titolo.isBlank()) return "titolo"
        if(_ricettaVuota.value!!.titolo.contains("%")) return "titolo"
        if(_ricettaVuota.value!!.descrizione.isBlank()) return "descrizione"
        if(_ricettaVuota.value!!.filtri.isEmpty()) return "filtri"
        if(_ricettaVuota.value!!.ingredienti.isEmpty()) return "ingredienti"

        _ricettaVuota.value!!.ingredienti.forEach { ingrediente ->

            if(ingrediente.ingrediente.isBlank() || ingrediente.qta.isBlank()) return "ingredientE"
            ingrediente.titolo = _ricettaVuota.value!!.titolo

        }

        return ""
    }

    // Quando viene schiacciato il tasto Home, carica la lista di tutte le ricette
    fun onHomeClick(){
        ricette = ricDao.getAllPreview()

        onFiltroChecked(false)
    }

    // Quando viene schiacciato il tasto Preferiti, carica la lista delle ricette classificate come tali
    fun onPreferitiClick(){
        ricette = ricDao.getPreferiti()

        onFiltroChecked(true)
    }

    private var _ricettaVuota = MutableLiveData(RicettaSample("","", mutableListOf(), mutableListOf()))
    //val ricettaVuota: LiveData<RicettaSample> = _ricettaVuota

    fun onTitoloInsert(titolo: String){
        _ricettaVuota.value!!.titolo = titolo
        Log.d("doge", _ricettaVuota.value!!.titolo )
    }

    fun onDescrizioneInsert(descrizione: String){
        _ricettaVuota.value!!.descrizione = descrizione
        Log.d("doge", _ricettaVuota.value!!.descrizione )
    }

    fun onIngredientsInsert(ingList : MutableList<IngredienteRIcetta>){
        _ricettaVuota.value!!.ingredienti = ingList
        Log.d("doge", _ricettaVuota.value!!.ingredienti.toString() )
    }

    fun onFilterInsert(filterList : MutableList<Filtro>){
        _ricettaVuota.value!!.filtri = filterList
        Log.d("doge", _ricettaVuota.value!!.filtri.toString() )
    }

    fun restartFilters(){
        for(filtro in _filtri.value!!){
            filtro.checked = false
        }
    }


    private val _ricettaCompleta = MutableLiveData(RicettaSample("","", mutableListOf(), mutableListOf()))
    val ricettaCompleta: LiveData<RicettaSample> = _ricettaCompleta

    //inserisce in ricetta completa le informazioni della ricetta passata
    fun getRicetta(titolo:String)=viewModelScope.launch (Dispatchers.IO) {

        _ricettaCompleta.value!!.titolo = titolo
        _ricettaCompleta.value!!.descrizione = ricDao.showRicettaCompleta(titolo).descrizione
        _ricettaCompleta.value!!.ingredienti = ricDao.IngrOfRecipe(titolo) as MutableList<IngredienteRIcetta>

        //conversione categoria -> filtro
        val categoriaList = ricDao.allCatFromRecipe(titolo)
        val filterList = getFilters()
        val tmp = mutableListOf<Filtro>()

        for(filter in filterList) {
            for (cat in categoriaList) {
                if (cat.categoria.equals(filter.name)) tmp.add(filter)
            }
        }

        _ricettaCompleta.value!!.filtri = tmp

    }

    fun getRicettaCompleta():RicettaSample{
        return _ricettaCompleta.value!!
    }

    fun resetComplete(){
        _ricettaCompleta.value = RicettaSample("","", mutableListOf(), mutableListOf())
    }

    /*
    Instance state per gestire i dati della ricetta selezionata
    nella scrollable list di Home e Preferiti
     */
    private val _ricettaSelezionata = MutableLiveData(RicettePreview("",false))
    val ricettaSelezionata: LiveData<RicettePreview> = _ricettaSelezionata

    fun selectRicetta(ricetta : RicettePreview){
        resetSelection()
        _ricettaSelezionata.value = ricetta
        Log.d("test", _ricettaSelezionata.value.toString() )
    }

    fun resetSelection(){
        _ricettaSelezionata.value = RicettePreview("",false)
        Log.d("test", "reset"+_ricettaSelezionata.value.toString())
    }


    /*
    Instance state per la gestione dei filtri in Home e Preferiti
     */
    private val _filtri = MutableLiveData(getFilters())
    val filtri: LiveData<List<Filtro>> = _filtri

    fun onFiltroChecked(preferiti: Boolean){
        val lista: MutableList<String> = mutableListOf()

        for(filtro in _filtri.value!!){
            if(filtro.checked)
                lista.add(filtro.name)
        }

        if(!lista.isEmpty()){
            if(!preferiti)
                ricette = ricDao.getFilterRic(lista)
            else
                ricette = ricDao.getFilterRicPref(lista)
        }


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

    fun onInvertPress() {
        _longPressed.value = _longPressed.value != true
    }

    fun getLongPressed(): Boolean {
        return _longPressed.value!!
    }

    // Chiamata al click del bottone Applica nel menù dei filtri
    fun onApplicaClick(tipologia: String){

        if(tipologia.equals("home"))
            onHomeClick()
        else
            onPreferitiClick()

        onExpand(false)
    }

    // Funzione chiamata al click del cestino in LongPress
    fun onBinClick(){
        onRicettaDelete()
        onInvertPress()
    }

    fun onDisplaySearch(tipologia: String, titolo: String) {

        if (tipologia.equals("Home"))
            ricette = ricDao.getRicByName(titolo)
        else
            ricette = ricDao.getRicPreferitiByName(titolo)
    }

    fun onBackFromSearch(tipologia: String){
        onSearch(false)

        if(tipologia.equals("Home"))
            onHomeClick()
        else
            onPreferitiClick()
    }




    private val _modify = MutableLiveData(false)
    val modify: LiveData<Boolean> = _modify

    fun modifyRecipe(){
        resetModify()
        getRicetta(_ricettaSelezionata.value!!.titolo)
        onInvertPress()
    }

    fun resetModify(){
        _modify.value = !_modify.value!!
    }

    fun getModify(): Boolean {
        return _modify.value!!
    }

    fun addModify(){
        modifyRecipe()
        resetComplete()
        onRicettaDelete()
        onInvertPress()
    }

    fun insert(ingrediente: Ingrediente)=viewModelScope.launch (Dispatchers.IO){
        ricDao.insertIngrediente(ingrediente)
    }

    fun delete(ingrediente: Ingrediente)=viewModelScope.launch (Dispatchers.IO){
        ricDao.deleteIngr(ingrediente)
    }

    private var _isPreferiti = MutableLiveData(false)
    val isPreferiti: LiveData<Boolean> = _isPreferiti

    fun getTipologia(): Boolean?
    {
        return _isPreferiti.value
    }

    fun updateTipologia(status: Boolean){
        _isPreferiti.value = status
    }

    val listaCarrello: LiveData<List<Ingrediente>?> = ricDao.getAllIngrIncarrello()

    fun getCarrello(): List<Ingrediente>? {
        return listaCarrello.value!!
    }

    fun isInCarrello(ingrediente:String): Boolean {
        var valore = false

        runBlocking {
            valore = ricDao.inCarrello(ingrediente)
        }

        return valore
    }

    fun updateCarrello(ingrediente: String, status: Boolean)=viewModelScope.launch (Dispatchers.IO) {
        ricDao.updateCarrello(Ingrediente(ingrediente, status))
    }

    fun addCarrello(ingrediente: String)=viewModelScope.launch (Dispatchers.IO){
        ricDao.updateCarrello(Ingrediente(ingrediente, true))
    }

    fun removeCarrello(ingrediente: String)=viewModelScope.launch (Dispatchers.IO){
        ricDao.updateCarrello(Ingrediente(ingrediente, false))
    }

}
