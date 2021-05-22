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
    version = 2,
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
                Log.d("test","open")


                INSTANCE?.let { database->
                    Log.d("test","let")
                    scope.launch (Dispatchers.IO){


                        prepopulateDatabase(database.dao())

                    }
                }
               if(INSTANCE==null)
                Log.d("test","null")
            }
        }
        suspend fun prepopulateDatabase(dao:DaoRicette)
        { Log.d("test","prepopulate")
            if(dao.countIngredienti()==0) {
                 dao.insertCategoria(Categoria("antipasto"))
                dao.insertCategoria(Categoria("primo"))
                dao.insertCategoria(Categoria("secondo"))
                dao.insertCategoria(Categoria("dolce"))
                dao.insertCategoria(Categoria("vegetariano"))
                dao.insertCategoria(Categoria("vegano"))
                dao.insertCategoria(Categoria("gluten free"))
                val li = listOf<String>(
                    "ACCIUGHE o ALICI [ENGRAULIS ENCHRASICHOL]",
                    "ACETO",
                    "ACQUA",
                    "ACQUA TONICA",
                    "AGLIO",
                    "AGNELLO",
                    "ALBICOCCHE ",
                    "ALCOOL PURO",
                    "AMARENE ",
                    "AMIDO",
                    "ANANAS ",
                    "ANATRA [ANAS BOSCHAS]",
                    "ANGUILLA DI FIUME ",
                    "ANGUILLA DI MARE ",
                    "ANGUILLA MARINATA",
                    "ARACHIDI",
                    "ARACHIDI TOSTATE",
                    "ARAGOSTA ",
                    "ARANCE ",
                    "ARINGA ",
                    "ASIAGO",
                    "ASPARAGI",
                    "ASTICE ",
                    "AVENA ",
                    "AVOCADO ",
                    "BABACO",

                    "BACCALA",
                    "BANANA ",
                    "BARBABIETOLE ROSSE ",
                    "BASILICO",
                    "BIETA ",
                    "BIRRA CHIARA",
                    "BIRRA SCURA",
                    "BISCOTTI AL LATTE",
                    "BISCOTTI FROLLINI",
                    "BISCOTTI INTEGRALI",
                    "BISCOTTI RICOPERTI AL CIOCCOLATO",
                    "BISCOTTI SAVOIARDO",
                    "BISCOTTI WAFERS",
                    "BOVINO",
                    "BRESAOLA",
                    "BRIE",
                    "BRODO DI CARNE E VERDURA",
                    "BRODO DI CARNE VARIA",
                    "BRODO DI DADO",
                    "BRODO DI GALLINA",
                    "BRODO VEGETALE",
                    "BURRATA",
                    "BURRINI",
                    "BURRO",
                    "BURRO DI ARACHIDI",
                    "CACAO AMARO, IN POLVERE",
                    "CACAO DOLCE, IN POLVERE, SOLUBILE",
                    "CACIOCAVALLO",
                    "CACIOCAVALLO AFFUMICATO",
                    "CACIOTTA AFFUMICATA",
                    "CACIOTTA ROMANA DI PECORA",
                    "CACIOTTA TOSCANA",
                    "CACIOTTINA FRESCA",
                    "CAFFE' TOSTATO, MACINATO",
                    "CALAMARO",
                    "CALAMARO SURGELATO",
                    "CAMEMBERT",
                    "CANDITI",
                    "CANNELLA ",
                    "CANNOLI ALLA CREMA",
                    "CAPOCOLLO",
                    "CAPPERI SOTT'ACETO",
                    "CAPRETTO ",
                    "CAPRIOLO",
                    "CAPRIOLO",

                    "CARCIOFI",
                    "CARDI",
                    "CAROTE",
                    "CARPA",
                    "CASTAGNE ",


                    "CAVALLO",
                    "CAVIALE",
                    "CAVOLFIORE ",
                    "CAVOLI DI BRUXELLES ",
                    "CAVOLO BROCCOLO VERDE RAMOSO ",
                    "CAVOLO CAPPUCCIO ROSSO ",
                    "CAVOLO CAPPUCCIO VERDE ",

                    "CECI",
                    "CEDRO CANDITO",
                    "CEFALO MUGGINE",
                    "CEREALI AGGREGATI",
                    "CERNIA ",
                    "CERNIA SURGELATA",
                    "CERTOSINO",
                    "CETRIOLI ",
                    "CHAMPAGNE",
                    "CHEDDAR",
                    "CHERRY BRANDY",
                    "CICCIOLI",
                    "CICORIA ",
                    "CILIEGE ",
                    "CIOCCOLATO AL LATTE",
                    "CIOCCOLATO BIANCO",
                    "CIOCCOLATO CON NOCCIOLE",
                    "CIOCCOLATO FONDENTE",
                    "CIOCCOLATO GIANDUIA",
                    "CIPOLLE",
                    "CIPOLLINE SOTT'ACETO",
                    "COCA COLA",
                    "COCOMERO ",
                    "COGNAC",
                    "CONFETTI",
                    "CONIGLIO ",
                    "CONO PER GELATO",
                    "COPPA",
                    "COPPA PARMA",
                    "CORNED BEEF IN SCATOLA",
                    "COTECHINO",
                    "COZZA ",
                    "CRACOTTE",
                    "CRAUTI",
                    "CREMA DI CACAO E NOCCIOLE",
                    "CREMA PER PASTICCERIA",
                    "CRESCIONE ",
                    "CROSTATA CON MARMELLATA",
                    "CRUSCA DI GRANO",
                    "CUMINO SEMI",
                    "DADI PER BRODO",
                    "DATTERI",
                    "DENTICE ",
                    "DOLCE VERDE",
                    "DOLCIFICANTE A BASE DI SACCARINA",
                    "EMMENTHAL",
                    "ERBA CIPOLLINA ",
                    "FAGIANO ",
                    "FAGIOLI ",
                    "FAGIOLINI ",
                    "FARAONA ",
                    "FARINA DI AVENA",
                    "FARINA DI CASTAGNE",
                    "FARINA DI CECI",
                    "FARINA DI COCCO",
                    "FARINA DI FRUMENTO, INTEGRALE",
                    "FARINA DI GRANO DURO",
                    "FARINA DI GRANO SARACENO",
                    "FARINA DI MAIS",
                    "FARINA DI ORZO",
                    "FARINA DI RISO",
                    "FARINA DI SEGALE, integrale",
                    "FARINA DI SEGALE, semi0integrale",
                    "FARINA DI SOIA, a basso contenuto lipidico",
                    "FARINA DI SOIA, intera",
                    "FARINA LATTEA",
                    "FAVE [VICIA FABA]",
                    "FAVE SECCHE",
                    "FETA",
                    "FETTE BISCOTTATE",
                    "FETTE BISCOTTATE, INTEGRALI",
                    "FETTE DI SEGALE INTEGRALI",
                    "FICHI [FICUS CARICA]",
                    "FICHI D'INDIA",
                    "FICHI SECCHI",
                    "FINOCCHIO ",
                    "FIOCCHI DI AVENA",
                    "FIOCCHI DI CRUSCA DI GRANO",
                    "FIOCCHI DI LATTE MAGRO",
                    "FIOCCHI DI MAIS (CORNFLAKES)",
                    "FIOCCHI DI RISO",
                    "FIOCCHI D'ORZO",
                    "FIOR DI LATTE",
                    "FIORI DI ZUCCA",
                    "FOCACCIA",
                    "FOGLIE DI RAPA ",
                    "FONTINA",
                    "FORMAGGINO",
                    "FORMAGGINO, a basso tenore di lipidi",
                    "FORMAGGIO AL GORGONZOLA E AL MASCARPONE",
                    "FORMAGGIO SPALMABILE (tipo Philadelphia)",
                    "FRAGOLE ",
                    "FUNGHI CHIODINI ",
                    "FUNGHI IN SCATOLA",
                    "FUNGHI PORCINELLI ",
                    "FUNGHI PORCINI ",
                    "FUNGHI SECCHI",
                    "FUNGHI SOTT'OLIO",
                    "FUNGHI SPUGNOLI ",
                    "GALLINA ",
                    "GAMBERETTI ",
                    "GELATINA ALIMENTARE, secca",
                    "GELATO AL CIOCCOLATO",
                    "GELATO ALLA FRUTTA",
                    "GELATO FIOR DI LATTE",
                    "GERME DI GRANO",
                    "GIRASOLE, SEMI",
                    "GORGONZOLA",
                    "GRANA",
                    "GRANCHIO, POLPA IN SCATOLA",
                    "GRANITA DI LIMONE",
                    "GRAPPA",
                    "GRISSINI",
                    "GRISSINI INTEGRALI",
                    "GROVIERA",
                    "INSALATA DI RISO",
                    "KIWI",
                    "LAMPONI",
                    "LARDO",
                    "LATTE DI BUFALA",
                    "LATTE DI CAPRA",
                    "LATTE DI MANDORLE",
                    "LATTE DI PECORA",
                    "LATTE DI SOIA",
                    "LATTE DI VACCA, ",
                    "LATTERIA",
                    "LATTERINI ",
                    "LATTUGA ",
                    "LENTICCHIE secche",
                    "LEPRE ",
                    "LIEVITO DI BIRRA",
                    "LIMONE",
                    "LINO, SEMI",
                    "LIQUIRIZIA DOLCE",
                    "LUCCIO ",
                    "LUMACA",
                    "LUPINI ",
                    "MACEDONIA DI FRUTTA IN SCATOLA",
                    "MAIONESE",
                    "MAIS ",
                    "MANDARINI ",
                    "MANDORLE DOLCI ",
                    "MANGO ",
                    "MARGARINA",
                    "MARGARINA VEGETALE",
                    "MARMELLATA DI ALBICOCCHE",
                    "MARMELLATA DI ARANCE",
                    "MARMELLATA DI PRUGNE",
                    "MARSALA ALL'UOVO",
                    "MARSALA TIPICO",
                    "MASCARPONE",
                    "MELA ",
                    "MELANZANE ",
                    "MELONE",
                    "MENTA ",
                    "MERINGA",
                    "MERLUZZO ",
                    "MIELE",
                    "MIRTILLO ",
                    "MORA",
                    "MORTADELLA DI SUINO",
                    "MOZZARELLA",
                    "MOZZARELLA DI BUFALA",
                    "MUESLI",
                    "NATTO",
                    "NESPOLE ",
                    "NOCCIOLE ",
                    "NOCE DI COCCO ",
                    "NOCE MOSCATA ",
                    "NOCI",
                    "OCA ",
                    "OLIO DI COCCO",
                    "OLIO DI COLZA",
                    "OLIO DI GERME DI GRANO",
                    "OLIO DI MAIS",
                    "OLIO DI OLIVA",
                    "OLIO DI PALMA",
                    "OLIO DI SEMI DI ARACHIDI",
                    "OLIO DI SEMI DI GIRASOLE",
                    "OLIVE ",
                    "ORATA ",
                    "ORIGANO ",
                    "ORTICA ",
                    "ORZO  PERLATO",
                    "OSTRICA",
                    "PAN DI SPAGNA",
                    "PANCARRE' AMERICANO COMUNE",
                    "PANCETTA AFFUMICATA O BACON",
                    "PANCETTA COPPATA",
                    "PANCETTA DI MAIALE",
                    "PANCETTA TESA",
                    "PANE AL LATTE",
                    "PANE DI GRANO DURO",
                    "PANE DI GRANO E SEGALE",
                    "PANE DI SEGALE",
                    "PANE DI SOIA",
                    "PANE GRATTUGIATO",
                    "PANE INTEGRALE",
                    "PANETTONE",
                    "PANNA(da cucina)",
                    "PAPAYA ",
                    "PAPRIKA  in polvere",
                    "PARMIGIANO",
                    "PASTA ALL'UOVO",
                    "PASTA DI SEMOLA",
                    "PASTA DI SEMOLA INTEGRALE",
                    "PASTA FROLLA",
                    "PASTA SFOGLIA",
                    "PASTORELLA",
                    "PATATE ",
                    "PECORINO",
                    "PEPE NERO ",
                    "PEPERONCINI PICCANTI",
                    "PEPERONI",
                    "PEPERONI SOTT'ACETO",
                    "PERA ",
                    "PESCA ",
                    "PESCA SCIROPPATA",
                    "PESCE SPADA",
                    "PINOLI ",
                    "PISELLI, freschi",
                    "PISELLI IN SCATOLA",
                    "PISTACCHI",
                    "POLENTA",
                    "POLLO ",
                    "POLLO, ALI",
                    "POLLO, COSCIA",
                    "POLLO, PETTO",
                    "POLLO, REGAGLIE",
                    "POLPO ",
                    "POMODORO",
                    "POMODORI SECCHI",
                    "POMODORI SOTT'OLIO",
                    "POMODORI, PELATI, IN SCATOLA CON LIQUIDO",
                    "POMODORO, CONSERVA",
                    "POMPELMO ",
                    "POMPELMO ROSA ",
                    "POP CORN",
                    "PORRI ",
                    "PREZZEMOLO ",
                    "PROSCIUTTO COTTO",
                    "PROSCIUTTO CRUDO",
                    "PROVOLA AFFUMICATA",
                    "PROVOLONCINO DOLCE",
                    "PROVOLONE",
                    "PROVOLONE PICCANTE",
                    "PRUGNE ",
                    "QUAGLIA",
                    "RADICCHIO ROSSO",
                    "RANA ",
                    "RANA ",
                    "RAPE ",
                    "RAVANELLI ",
                    "RAZZA ",
                    "RIBES NERO ",
                    "RIBES ROSSO ",
                    "RICOTTA DI PECORA",
                    "RICOTTA DI VACCA",
                    "RISO",
                    "ROBIOLA",
                    "ROSMARINO",
                    "RUCOLA ",
                    "SALAME DI SUINO",
                    "SALAME DI SUINO E BOVINO",
                    "SALAME FABRIANO",
                    "SALAME UNGHERESE",
                    "SALATINI",
                    "SALE ",
                    "SALMONE ",
                    "SALMONE AFFUMICATO",
                    "SALSICCIA DI SUINO E BOVINO, fresca",
                    "SALSICCIA DI SUINO",
                    "SALVIA ",
                    "SARDA ",
                    "SARDINE SALATE",
                    "SARDINE SOTT'OLIO",
                    "SCALOGNO ",
                    "SCAMORZA",
                    "SCAMORZA AFFUMICATA",
                    "SCAROLA ",
                    "SCIROPPO DI MALTO",
                    "SEDANO ",
                    "SEDANO RAPA ",
                    "SEGO DI BUE",
                    "SEMOLA",
                    "SEPPIA ",
                    "SGOMBRO",
                    "SGOMBRO, FILETTI SOTT'OLIO",
                    "SOFFICINI AL FORMAGGIO SURGELATI",
                    "SOGLIOLA ",
                    "SOGLIOLA ",
                    "SOIA ",
                    "SOTTILETTE",
                    "SPECK",
                    "SPIGOLA ",
                    "SPINACI ",
                    "SPINACI IN SCATOLA",
                    "SPREMUTA DI ARANCIA",
                    "SPREMUTA DI POMPELMO",
                    "STARNA ",
                    "STOCCAFISSO ",

                    "STRACCHINO",
                    "STRUTTO O SUGNA",
                    "SUCCO DI ALBICOCCA",
                    "SUCCO DI ANANAS",
                    "SUCCO DI ARANCIA",


                    "SUCCO DI CAROTE",
                    "SUCCO DI LIMONE",
                    "SUCCO DI MELA",
                    "SUINO",
                    "SURIMI",
                    "TACCHINA ",
                    "TALEGGIO",
                    "TARTUFO NERO ",
                    "TE' DETEINATO, in foglie",
                    "TE', in foglie",
                    "TIMO ",
                    "TOFU",
                    "TONNO",
                    "TONNO SOTT'OLIO, SGOCCIOLATO",
                    "TORRONE CON MANDORLE",
                    "TORTELLINI",
                    "TRIGLIA ",
                    "TROTA",
                    "UOVO ",
                    "UVA ",

                    "VALERIANA",
                    "VANIGLIA",

                    "VERDURA E LEGUMI, SURGELATI",
                    "VERMOUTH ",

                    "VINO BIANCO",

                    "VINO ROSSO",
                    "VITELLO ",

                    "VONGOLA",

                    "WHISKY",
                    "WURSTEL",
                    "YOGURT DI LATTE INTERO",
                    "YOGURT DI LATTE MAGRO ALLA FRUTTA",
                    "ZAFFERANO",
                    "ZAMPONE",
                    "ZUCCA GIALLA",
                    "ZUCCHERO ",
                    "ZUCCHERO DI CANNA, GREZZO",
                    "ZUCCHINE"
                )
               li.forEach {
                   dao.insertIngrediente(Ingrediente(it, false))
                }
                dao.insertRicettaPreview(RicettePreview("pietanza 1",false))
                dao.insertRicettaCompleta(RicettaCompleta("pietanza 1","la si cucina quando si è a casa da soli"))
                dao.insertIngredienteRicetta(ingrRic = IngredienteRIcetta("pietanza 1","VONGOLA",2))
                dao.insertIngredienteRicetta(ingrRic = IngredienteRIcetta("pietanza 1","WURSTEL",200))
                dao.insertRicetteCategoria(rc = RicettaCategorie("pietanza 1","secondo"))
                dao.insertRicetteCategoria(rc = RicettaCategorie("pietanza 1","vegano"))

                dao.insertRicettaPreview(RicettePreview("pietanza 2",false))
                dao.insertRicettaCompleta(RicettaCompleta("pietanza 2","la si cucina quando si è ad una festa"))
                dao.insertIngredienteRicetta(ingrRic = IngredienteRIcetta("pietanza 2","ZAFFERANO",200))
                dao.insertIngredienteRicetta(ingrRic = IngredienteRIcetta("pietanza 2","ZUCCHINE",200))
                dao.insertRicetteCategoria(rc = RicettaCategorie("pietanza 2","antipasto"))
                dao.insertRicetteCategoria(rc = RicettaCategorie("pietanza 2","vegano"))
            }
        }
    }
}