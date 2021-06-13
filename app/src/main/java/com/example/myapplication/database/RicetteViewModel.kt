package com.example.myapplication.database

import android.app.Application
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Filtro
import com.example.myapplication.MainActivity
import com.example.myapplication.RicettaSample
import com.example.myapplication.getFilters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

// AndroidViewModel è sottoclasse di ViewModel
class RicetteViewModel(application: Application):AndroidViewModel(application) {

    // Recupero della reference al Dao, non accessibile all'esterno di RicetteViewModel
    private val ricDao :DaoRicette
    init {
        ricDao = RicetteDataBase.getDataBase(application,viewModelScope).dao()
    }

    // Lista delle ricette da mostrare
    var ricette: LiveData<List<RicettePreview>> = ricDao.getAllPreview()

    // Viene premuto il cuore -> Ricetta aggiunta o eliminata dai preferiti
    fun onPreferitoChange(ric: RicettePreview)=viewModelScope.launch (Dispatchers.IO){
        ricDao.updateRicettaPreview(ric)
    }

    //eliminazione di una ricetta
    fun onRicettaDelete() =viewModelScope.launch (Dispatchers.IO) {
        val listing=ricDao.IngrOfRecipe(_ricettaSelezionata.value!!.titolo)

        ricDao.deleteRicetta(_ricettaSelezionata.value!!)

        listing.forEach{
            if(ricDao.numberIngInIngreRicetta(it.ingrediente)==0)
                ricDao.deleteIngrFromName(it.ingrediente)
        }
    }

    private val lock= Mutex()
    //aggiunge la ricetta salvata al database
    @Synchronized
    fun onRicettaAdd()=viewModelScope.launch (Dispatchers.IO) {
        lock.withLock {

            if(_ricettaVuota.value!!.uri.equals("null")) {
                ricDao.insertRicettaPreview(RicettePreview(_ricettaVuota.value!!.titolo, false))
                ricDao.insertRicettaCompleta(
                    RicettaCompleta(
                        _ricettaVuota.value!!.titolo,
                        _ricettaVuota.value!!.descrizione
                    )
                )
            }
            else {
                ricDao.insertRicettaPreview(
                    RicettePreview(
                        _ricettaVuota.value!!.titolo,
                        false,
                        _ricettaVuota.value!!.uri
                    )
                )
                ricDao.insertRicettaCompleta(
                    RicettaCompleta(
                        _ricettaVuota.value!!.titolo,
                        _ricettaVuota.value!!.descrizione,
                        _ricettaVuota.value!!.uri
                    )
                )
            }

            _ricettaVuota.value!!.filtri.forEach { filtro ->
                ricDao.insertRicetteCategoria(
                        RicettaCategorie(
                            _ricettaVuota.value!!.titolo,
                            filtro.name
                        )
                )
            }

            _ricettaVuota.value!!.ingredienti.forEach { ingrediente ->

                ricDao.insertIngrediente(Ingrediente(ingrediente.ingrediente, false))
                ricDao.insertIngredienteRicetta(ingrediente)

            }

            val main=MainActivity.get()
            main?.resetUri()
        }
    }

    //verifica i pre requisiti per inserire la ricetta nel database
    fun onRicettaAddVerify():String{

        if(_ricettaVuota.value!!.titolo.isBlank()) return "(inserisci il titolo)"

        var titoloExist = -1

        viewModelScope.launch {
            if(_modify.value == true)
                titoloExist = 0

            else
            {
                runBlocking {
                    titoloExist = ricDao.titoloExist(_ricettaVuota.value!!.titolo)
                }
            }
        }

        if(titoloExist == 1) return "(il titolo è già esistente!!)"

        if(_ricettaVuota.value!!.titolo.contains("%")) return "(non puoi usare il carattere % per il titolo)"
        if(_ricettaVuota.value!!.descrizione.isBlank()) return "(inserisci una descrizione)"

        if(_ricettaVuota.value!!.filtri.isEmpty()) return "(inserisci uno o più filtri)"

        var count = 0

        _ricettaVuota.value!!.filtri.forEach{ filtro ->

            when(filtro.name){
                Filtro.Antipasto.name -> count++
                Filtro.Primo.name -> count++
                Filtro.Secondo.name -> count++
                Filtro.Dessert.name -> count++
            }
        }

        if(count != 1) return "(inserisci almeno un filtro)"

        if(_ricettaVuota.value!!.ingredienti.isEmpty()) return "(inserisci degli ingredienti)"

        _ricettaVuota.value!!.ingredienti.forEach { ingrediente ->

            if(ingrediente.ingrediente.isBlank() || ingrediente.qta.isBlank()) return "(alcuni campi degli ingredienti sono vuoti)"
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

    //ricettaVuota contiene la nuova ricetta da inserire nel database
    private var _ricettaVuota = MutableLiveData(RicettaSample("","", mutableListOf(), mutableListOf(),""))

    //restituisce la ricettaVuota
    fun getRicettaVuota(): RicettaSample {
        return _ricettaVuota.value!!
    }

    //resetta la ricettaVuota
    fun resetRicettaVuota(){
        _ricettaVuota.value = RicettaSample("","", mutableListOf(), mutableListOf(),"")
    }

    //inserisce l'immagine
    fun onImageInsert(uri:String?)
    {
        _ricettaVuota.value!!.uri=uri
    }

    //inserisce il titolo
    fun onTitoloInsert(titolo: String){
        _ricettaVuota.value!!.titolo = titolo
    }

    //inserisce la descrizione
    fun onDescrizioneInsert(descrizione: String){
        _ricettaVuota.value!!.descrizione = descrizione
    }

    //inserisce gli ingredienti
    fun onIngredientsInsert(ingList : MutableList<IngredienteRIcetta>){
        _ricettaVuota.value!!.ingredienti = ingList
    }

    //inserisce i filtri
    fun onFilterInsert(filterList : MutableList<Filtro>){
        _ricettaVuota.value!!.filtri = filterList
    }

    //resetta i filtri a false
    @Synchronized
    fun restartFilters(){
        for(filtro in _filtri.value!!){
            filtro.checked = false
        }
    }

    //resituisce la categoria di una data ricetta
    fun getCategoria(titolo: String): String {

        var category : List<Categoria>
        runBlocking {
            category = ricDao.allCatFromRecipe(titolo)
        }

        val list = listOf(Filtro.Antipasto,Filtro.Primo,Filtro.Secondo,Filtro.Dessert)
        var ftl = ""
        for (filter in list){
            for (cat in category) {
                if (cat.categoria == filter.name)  ftl = filter.name
            }
        }

        return ftl

    }

    //ricettaCompleta contiene le informazioni di una ricetta nel dettaglio
    private val _ricettaCompleta = MutableLiveData(RicettaSample("","", mutableListOf(), mutableListOf(),""))


    //inserisce in ricetta completa le informazioni della ricetta passata
    fun getRicetta(titolo:String)=viewModelScope.launch (Dispatchers.IO) {
        _ricettaCompleta.value!!.titolo = titolo
        _ricettaCompleta.value!!.descrizione = ricDao.showRicettaCompleta(titolo).descrizione
        _ricettaCompleta.value!!.ingredienti =
            ricDao.IngrOfRecipe(titolo) as MutableList<IngredienteRIcetta>
        _ricettaCompleta.value!!.uri = ricDao.getUri(titolo)

        //conversione categoria -> filtro
        val categoriaList = ricDao.allCatFromRecipe(titolo)
        val filterList = getFilters()
        val tmp = mutableListOf<Filtro>()

        for (filter in filterList) {
            for (cat in categoriaList) {
                if (cat.categoria == filter.name) tmp.add(filter)
            }
        }

        _ricettaCompleta.value!!.filtri = tmp
    }

    //restituisce la ricettaCompleta
    fun getRicettaCompleta():RicettaSample{
        return _ricettaCompleta.value!!
    }

    //resetta la ricettaCompleta
    fun resetComplete(){
        _ricettaCompleta.value = RicettaSample("","", mutableListOf(), mutableListOf(),"")
    }

    /*
    Statoper gestire i dati della ricetta selezionata
    nella scrollable list di Home e Preferiti
     */
    private val _ricettaSelezionata = MutableLiveData(RicettePreview("",false))
    val ricettaSelezionata: LiveData<RicettePreview> = _ricettaSelezionata

    //seleziona la nuova ricetta
    fun selectRicetta(ricetta : RicettePreview){
        resetSelection()
        _ricettaSelezionata.value = ricetta
    }

    //resetta selezione
    fun resetSelection(){
        _ricettaSelezionata.value = RicettePreview("",false)
    }


    /*
    Stato per la gestione dei filtri in Home e Preferiti
     */
    private val _filtri = MutableLiveData(getFilters())
    val filtri: LiveData<List<Filtro>> = _filtri

    //mostra solo le ricette con dei dati filtri
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
     Stato per la gestione della App Bar in schermate Home e Preferiti
     e relative funzioni per la gestione degli eventi
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

        if(tipologia == "Home")
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

    // Funzione che gestisce la ricerca delle ricette
    fun onDisplaySearch(tipologia: String) {

        if (tipologia == "Home")
            ricette = ricDao.getRicByName("%" + _ricerca.value!! + "%")
        else
            ricette = ricDao.getRicPreferitiByName("%" + _ricerca.value!! + "%")
    }

    // Funzione che gestisce l'uscita dalla ricerca
    fun onBackFromSearch(tipologia: String){
        onSearch(false)

        resetRicerca()

        if(tipologia == "Home")
            onHomeClick()
        else
            onPreferitiClick()
    }

    // Variabile che gestisce le modifiche ad una data ricetta
    private val _modify = MutableLiveData(false)

    fun modifyRecipe(){
        resetModify()
        getRicetta(_ricettaSelezionata.value!!.titolo)
        if(_fromDetails.value != true) onInvertPress()
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

    // Variabile che memorizza la tipologia di schermata (Home o Preferiti)
    private val _isPreferiti = MutableLiveData(false)
    val isPreferiti: LiveData<Boolean> = _isPreferiti

    fun getTipologia(): Boolean?
    {
        return _isPreferiti.value
    }

    fun updateTipologia(status: Boolean){
        _isPreferiti.value = status
    }

    // Variabile che memorizza la lista di ingredienti da mostrare nel carrello
    val listaCarrello: LiveData<List<Ingrediente>?> = ricDao.getAllIngrIncarrello()

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

    // Stato della barra della ricerca in Home e Preferiti
    private val _ricerca = MutableLiveData("")
    val ricerca: LiveData<String> = _ricerca

    fun resetRicerca(){
        _ricerca.value = ""
    }

    fun onClickRicerca(testo: String){
        _ricerca.value = testo
    }

    // Variabile di stato per aiutare la navigazione
    private val _fromDetails = MutableLiveData(false)

    fun isFromDetails(){
        _fromDetails.value = !_fromDetails.value!!
    }

    fun getFromDetails(): Boolean? {
        return _fromDetails.value
    }

    fun setBitmap()=viewModelScope.launch (Dispatchers.IO){
        val m= MainActivity.get()?.map
        ricDao.getUriPreview().forEach {
            if(it!=null)
                try{
            m?.set(Uri.parse(it),  MediaStore.Images.Media.getBitmap(MainActivity.get()?.contentResolver,Uri.parse(it)))}
                catch (e: java.lang.Exception) {
                    m?.set(Uri.parse(it),  null)
                }
        }
    }


}
