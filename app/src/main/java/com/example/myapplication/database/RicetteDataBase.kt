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
                    "AGAR AGAR [EUCHEUMA spp], secco",
                    "AGLIO [ALLIUM SATIVUM], fresco",
                    "AGLIO, in polvere",
                    "AGNELLO [OVIS AGNUS]",
                    "AGNELLO, CARNE GRASSA",

                    "AGNELLO, COSTOLETTE",

                    "ALBICOCCHE (PRUNUS ARMENIACA)",
                    "ALBICOCCHE, polpa secca",
                    "ALCOOL PURO",

                    "AMARENE [PRUNUS CERASUS]",
                    "AMARI A BASSA GRADAZIONE",
                    "AMARI AD ALTA GRADAZIONE",
                    "AMIDO",
                    "ANANAS [ANANAS SATIVUS]",
                    "ANANAS SCIROPPATA",
                    "ANATRA [ANAS BOSCHAS]",
                    "ANGUILLA DI FIUME [ANGUILLA ANGUILLA]",
                    "ANGUILLA DI MARE [ANGUILLA ANGUILLA]",
                    "ANGUILLA MARINATA",

                    "ARACHIDI [ARACHIS HYPOGEA] CRUDE",
                    "ARACHIDI TOSTATE",

                    "ARAGOSTA [PALINURUS ELEPHAS]",
                    "ARANCE [CITRUS AURANTIUM]",
                    "ARANCE, SCORZA",
                    "ARANCIATA IN LATTINA",
                    "ARINGA [CLUPEA HARENGUS]",
                    "ARINGA AFFUMICATA",
                    "ARINGA MARINATA",
                    "ARINGA SALATA",
                    "ASIAGO",
                    "ASPARAGI DI BOSCO [ASPARAGUS OFFICINALIS]",
                    "ASPARAGI DI CAMPO",
                    "ASPARAGI DI SERRA",
                    "ASPARAGI IN SCATOLA",
                    "ASTICE [HOMARUS VULGARIS]",
                    "AVENA [AVENA SATIVA]",
                    "AVOCADO [PERSEA GRATISSIMA]",
                    "BABA' AL RHUM",
                    "BABACO [CARICA PENTAGONA]",
                    "BACCALA' [MERLUCCIUS MERLUCCIUS], ammollato",
                    "BACCALA', secco",
                    "BANANA [MUSA SAPIENTIUM]",
                    "BARBABIETOLE ROSSE [BETA VULGARIS, CV RAPA, FORMA RUBRA]",
                    "BASILICO [OCIMUM BASILICUM], fresco",
                    "BASILICO, secco macinato",
                    "BEVANDE GASSATE DIETETICHE",
                    "BIETA [BETA VULGARIS, CV CICLA]",
                    "BIETA IN SCATOLA",
                    "BIGNE'",
                    "BIRRA CHIARA",
                    "BIRRA SCURA",
                    "BISCOTTI AL LATTE",
                    "BISCOTTI FROLLINI",
                    "BISCOTTI INTEGRALI",
                    "BISCOTTI PER L'INFANZIA",
                    "BISCOTTI 'PETIT BEURRE'",
                    "BISCOTTI RICOPERTI AL CIOCCOLATO",
                    "BISCOTTI SAVOIARDO",
                    "BISCOTTI WAFERS",
                    "BISCOTTO PER LA PRIMA COLAZIONE",
                    "BOVINO, CERVELLO",
                    "BOVINO, CUORE",
                    "BOVINO, FEGATO",
                    "BOVINO, LESSATO IN GELATINA, IN SCATOLA",
                    "BOVINO, LINGUA",



                    "BOVINO, TRIPPA",

                    "BRANDY",
                    "BRESAOLA",
                    "BRIE",
                    "BRIOCHES",
                    "BRIOCHES CON CREMA",
                    "BRIOCHES CON MARMELLATA",
                    "BROCCOLETTI DI RAPE [BRASSICA RAPA, CV ESCULENTA]",
                    "BROCCOLO A TESTA [BRASSICA OLERACEA, CV BOTRYTIS CAPUT]",
                    "BRODO DI CARNE E VERDURA",
                    "BRODO DI CARNE VARIA",
                    "BRODO DI DADO",
                    "BRODO DI GALLINA",
                    "BRODO VEGETALE",

                    "BUDINO AL CIOCCOLATO",
                    "BUDINO ALLA VANIGLIA",
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
                    "CALAMARO [LOLIGO VULGARIS]",
                    "CALAMARO SURGELATO",
                    "CAMEMBERT",
                    "CANDITI",
                    "CANNELLA [CINNAMOMUM VERUM e AROMATICUM]",
                    "CANNOLI ALLA CREMA",
                    "CAPOCOLLO",
                    "CAPPERI SOTT'ACETO",
                    "CAPRETTO [CAPRA HIREUS]",
                    "CAPRIOLO [CERVUS spp], COSCIA",
                    "CAPRIOLO [CERVUS spp], SCHIENA",
                    "CARAMELLE ALLA FRUTTA",
                    "CARAMELLE ALLA MENTA",
                    "CARAMELLE DURE",
                    "CARAMELLE MORBIDE, MOU",
                    "CARCIOFI [CYNARA SCOLYMUS]",
                    "CARCIOFI SURGELATI",
                    "CARCIOFINI SOTT'ACETO",
                    "CARCIOFINI SOTT'OLIO",
                    "CARDI [CYNARA CARDUNCULUS]",
                    "CAROTE [DAUCUS CAROTA]",
                    "CARPA [CYPRINUS CARPIO]",
                    "CASTAGNE [CASTANEA SATIVA]",
                    "CASTAGNE, secche",
                    "CASTRATO [OVIS AGNUS], CARNE SEMIGRASSA",
                    "CASTRATO, CARNE GRASSA",
                    "CASTRATO, CARNE MAGRA",
                    "CAVALLO [EQUUS CABALUS]",
                    "CAVALLO, CUORE",
                    "CAVALLO, FEGATO",
                    "CAVIALE",
                    "CAVOLFIORE [BRASSICA OLERACEA, CV BOTRYTIS CAULIFLAURA]",
                    "CAVOLI DI BRUXELLES [BRASSICA OLERACEA, CV GEMMIFERA]",
                    "CAVOLO BROCCOLO VERDE RAMOSO [BRASSICA OLERACEA CV BOTRITYS CIMOSA]",
                    "CAVOLO CAPPUCCIO ROSSO [BRASSICA OLERACEA, CV CAPITATA RUBRA]",
                    "CAVOLO CAPPUCCIO VERDE [BRASSICA OLERACEA, CV CAPITATA ALBA]",
                    "CECI [CICER ARIETINUM], secchi",
                    "CECI IN SCATOLA",
                    "CEDRO CANDITO",
                    "CEFALO MUGGINE [MUGIL CEPHALUS]",
                    "CEREALI AGGREGATI",
                    "CERNIA [EPINEPHELUS GUAZA]",
                    "CERNIA DI FONDO [POLYPRION CERNIUM]",
                    "CERNIA SURGELATA",
                    "CERTOSINO",
                    "CETRIOLI [CUCUMIS SATIVUS]",

                    "CHAMPAGNE",
                    "CHEDDAR",
                    "CHERRY BRANDY",
                    "CICCIOLI",
                    "CICORIA [CICHORIUM INTYBUS]",

                    "CILIEGE [PRUNUS AVIUM]",
                    "CIOCCOLATO AL LATTE",
                    "CIOCCOLATO BIANCO",
                    "CIOCCOLATO CON NOCCIOLE",
                    "CIOCCOLATO FONDENTE",
                    "CIOCCOLATO GIANDUIA",
                    "CIPOLLE [ALLIUM CEPA]",
                    "CIPOLLE, secche",
                    "CIPOLLINE [ALLIUM CEPA, CV PRAECOX]",
                    "CIPOLLINE SOTT'ACETO",
                    "COCA COLA",
                    "COCOMERO [CITRULLUS VULGARIS]",
                    "COGNAC",
                    "CONFETTI",
                    "CONIGLIO [ORYCTOLAGUS CUNICULUS], CARNE SEMIGRASSA",
                    "CONIGLIO, CARNE GRASSA",

                    "CONO PER GELATO",
                    "COPPA",
                    "COPPA PARMA",
                    "CORNED BEEF IN SCATOLA",
                    "COTECHINO",

                    "COZZA O MITILO [MYTILUS EDULIS]",

                    "CRACOTTE",
                    "CRAUTI",
                    "CREMA DI CACAO E NOCCIOLE",
                    "CREMA PER PASTICCERIA",
                    "CREME CARAMEL",
                    "CRESCIONE [NASTURTIUM OFFICINALIS]",
                    "CROSTATA CON MARMELLATA",
                    "CRUSCA DI GRANO",
                    "CUMINO [CUMINUM CYMINUM], SEMI",
                    "DADI PER BRODO",
                    "DADI PER BRODO VEGETALE",
                    "DAIKON [RAPHUS SATIVUS]",
                    "DATTERI [PHOENIX DACTYLIFERA], freschi",
                    "DATTERI, secchi",

                    "DENTICE SURGELATO",
                    "DOLCE VERDE",
                    "DOLCIFICANTE A BASE DI SACCARINA",
                    "EMMENTHAL",
                    "ERBA CIPOLLINA [ALLIUM SCHOENOPRASUM], fresca",
                    "ESTRATTO DI CARNE BOVINA",
                    "FAGIANO ",
                    "FAGIOLI ",
                    "FAGIOLI secchi",
                    "FAGIOLI IN SCATOLA",

                    "FAGIOLI, secchi",
                    "FAGIOLINI [PHASEOLUS VULGARIS]",
                    "FAGIOLINI IN SCATOLA",
                    "FARAONA ",

                    "FARINA DI AVENA",
                    "FARINA DI CASTAGNE",
                    "FARINA DI CECI",
                    "FARINA DI COCCO",
                    "FARINA DI FRUMENTO, INTEGRALE",
                    "FARINA DI FRUMENTO, TIPO ",
                    "FARINA DI FRUMENTO, TIPO ",
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
                    "FICHI D'INDIA [OPUNTIA FICUS INDICA]",

                    "FICHI SECCHI",
                    "FINOCCHIO [PHOENICULUM VULGARE]",

                    "FIOCCHI DI AVENA",
                    "FIOCCHI DI CRUSCA DI GRANO",
                    "FIOCCHI DI LATTE MAGRO",
                    "FIOCCHI DI MAIS (CORNFLAKES)",
                    "FIOCCHI DI RISO",
                    "FIOCCHI D'ORZO",
                    "FIOR DI LATTE",
                    "FIORI DI ZUCCA [CUCURBITA PEPO]",
                    "FOCACCIA",
                    "FOGLIE DI RAPA [BRASSICA RAPA]",
                    "FONTINA",
                    "FORMAGGINO",
                    "FORMAGGINO, a basso tenore di lipidi",
                    "FORMAGGIO AL GORGONZOLA E AL MASCARPONE",
                    "FORMAGGIO SPALMABILE (tipo Philadelphia)",
                    "FRAGOLE [FRAGARIA VESCA]",
                    "FRUMENTO DURO [TRITICUM DURUM]",
                    "FRUMENTO TENERO [TRITICUM AESTIVUM]",
                    "FUNGHI CHIODINI [ARMILLARIA MELLEA]",
                    "FUNGHI GALLINACCI [CANTHARELLUS CIBARIUS FRIES]",
                    "FUNGHI GALLINACCI, secchi",
                    "FUNGHI IN SCATOLA",

                    "FUNGHI PORCINELLI [LECINUM SCABRUM]",
                    "FUNGHI PORCINI [BOLETUS EDULIS]",

                    "FUNGHI SECCHI",
                    "FUNGHI SHIITAKE [LENTINUS EDODES], secchi",
                    "FUNGHI SOTT'OLIO",
                    "FUNGHI SPUGNOLI [MORCHELLA ESCULENTA]",
                    "GALLINA [GALLUS GALLUS]",

                    "GAMBERETTI ",

                    "GELATINA ALIMENTARE, secca",
                    "GELATO AL CIOCCOLATO",
                    "GELATO ALLA FRUTTA",
                    "GELATO FIOR DI LATTE",
                    "GERME DI GRANO",
                    "GERMOGLI DI ERBA MEDICA [MEDICAGO SATIVA]",
                    "GHIACCIOLO ALL'ARANCIO",

                    "GIRASOLE, SEMI",
                    "GOMME DA MASTICARE",
                    "GORGONZOLA",
                    "GORGONZOLA CON LE NOCI",
                    "GOUDA FRESCO",
                    "GOUDA STAGIONATO",
                    "GRANA",

                    "GRANCHIO, POLPA IN SCATOLA",
                    "GRANITA DI LIMONE",
                    "GRANITO DI TENERO",
                    "GRAPPA",
                    "GRISSINI",
                    "GRISSINI INTEGRALI",
                    "GROVIERA",

                    "INSALATA DI RISO",



                    "KIWI [ACTINIDIA DELICIOSA]",

                    "LAMPONI [RUBUS IDAEUS]",
                    "LARDO",
                    "LATTE DI BUFALA",
                    "LATTE DI CAPRA",
                    "LATTE DI MANDORLE",
                    "LATTE DI PECORA",
                    "LATTE DI SOIA",

                    "LATTE DI VACCA, INTERO",



                    "LATTE DI VACCA, PARZIALMENTE SCREMATO",


                    "LATTE DI VACCA, SCREMATO",
                    "LATTE DI VACCA, SCREMATO IN POLVERE",
                    "LATTE DI VACCA, SCREMATO UHT",

                    "LATTERIA",
                    "LATTERINI [ATHERINA BOYERI]",
                    "LATTUGA [LACTUCA SATIVA]",

                    "LENTICCHIE secche",

                    "LEPRE ",
                    "LIEVITO DI BIRRA, COMPRESSO",

                    "LIMONE [CITRUS LIMONUM]",
                    "LIMONE, SCORZA",
                    "LINO, SEMI",
                    "LIQUIRIZIA DOLCE",

                    "LUCCIO ",
                    "LUMACA]",

                    "LUPINI ",
                    "MACEDONIA DI FRUTTA IN SCATOLA",

                    "MAIONESE",
                    "MAIS ",


                    "MANDARINI ",
                    "MANDORLE DOLCI ",
                    "MANGO ",
                    "MARGARINA",
                    "MARGARINA VEGETALE",
                    "MARMELLATA (ALBIC.,FICHI,MELE COT.,PESCHE, PRUGNE]",
                    "MARMELLATA (AMARENE,CILIEGE,UVA,MARASCHE)",
                    "MARMELLATA (NORMALI E TIPO FRUTTA VIVA)",
                    "MARMELLATA A RIDOTTO TENORE DI ZUCCHERO",
                    "MARMELLATA DI ALBICOCCHE",
                    "MARMELLATA DI ARANCE",
                    "MARMELLATA DI PRUGNE",
                    "MARSALA ALL'UOVO",
                    "MARSALA TIPICO",
                    "MASCARPONE",
                    "MELA ",


                    "MELANZANE ",

                    "MELONE D'ESTATE ",
                    "MELONE D'INVERNO ",
                    "MELU' O PESCE MOLO [GADUS POTASSOU]",
                    "MENTA  fresca",
                    "MENTA, secca",


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
                    "NESPOLE [ERIOBOTRYA JAPONICA]",
                    "NOCCIOLE [CORYLUS AVELLANA]",
                    "NOCE DI COCCO [COCOS NUCIFERA]",
                    "NOCE MOSCATA [MYRISTICA FRAGRANS]",
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
                    "PASTA ALL'UOVO, fresca",
                    "PASTA ALL'UOVO, secca",

                    "PASTA DI SEMOLA",
                    "PASTA DI SEMOLA INTEGRALE",
                    "PASTA FROLLA",
                    "PASTA SFOGLIA",

                    "PASTICCINI AL COCCO",
                    "PASTORELLA",
                    "PATATE ",


                    "PECORINO",

                    "PEPE NERO ",
                    "PEPERONCINI PICCANTI [CAPSICUM ANNUUM]",
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

                    "POMODOR",

                    "POMODORI SECCHI",
                    "POMODORI SOTT'OLIO",
                    "POMODORI, PELATI, IN SCATOLA CON LIQUIDO",
                    "POMODORO, CONCENTRATO",

                    "POMODORO, CONSERVA (sostanza secca 30%)",
                    "POMPELMO ",
                    "POMPELMO ROSA [CITRUS PARADISI]",
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
                    "RANA PESCATRICE [",
                    "RAPE [BRASSICA RAPA]",
                    "RAVANELLI ",
                    "RAZZA ",

                    "RIBES NERO ",
                    "RIBES ROSSO ",
                    "RICOTTA DI PECORA",
                    "RICOTTA DI VACCA",

                    "RISO",

                    "RISO, TIPO PARBOILED",
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

                    "SARDA [",
                    "SARDINE SALATE",
                    "SARDINE SOTT'OLIO",
                    "SCALOGNO ",
                    "SCAMORZA",
                    "SCAMORZA AFFUMICATA",
                    "SCAROLA [LACTUCA SCAROLA]",
                    "SCIROPPO DI MALTO",
                    "SCIROPPO PER BIBITE",

                    "SEDANO ",
                    "SEDANO RAPA ",
                    "SEGO DI BUE",
                    "SEMOLA",
                    "SEPPIA ",

                    "SGOMBRO",

                    "SGOMBRO, FILETTI SOTT'OLIO",

                    "SOFFICINI AL FORMAGGIO SURGELATI",
                    "SOGLIOLA [SOLEA SOLEA]",
                    "SOGLIOLA SURGELATA",
                    "SOIA ",
                    "SOTTILETTE",
                    "SPECK",
                    "SPIGOLA [MORONE LABRAX]",
                    "SPINACI [SPINACIA OLERACEA]",
                    "SPINACI IN SCATOLA",
                    "SPREMUTA DI ARANCIA",
                    "SPREMUTA DI POMPELMO",
                    "STARNA ",
                    "STOCCAFISSO [GADUS POUTASSOU], ammollato",
                    "STOCCAFISSO, secco",
                    "STRACCHINO",
                    "STRUTTO O SUGNA",
                    "SUCCO DI ALBICOCCA, CONSERVATO",
                    "SUCCO DI ANANAS, CONSERVATO",
                    "SUCCO DI ARANCIA E POMPELMO non zuccherato",
                    "SUCCO DI ARANCIA, CONCENTRATO",
                    "SUCCO DI ARANCIA, CONSERVATO, non zuccherato",
                    "SUCCO DI BARBABIETOLA, FRESCO",
                    "SUCCO DI CAROTE, CONSERVATO",
                    "SUCCO DI FRUTTA, CONSERVATO",
                    "SUCCO DI LIMONE, CONSERVATO",
                    "SUCCO DI LIMONE, FRESCO",
                    "SUCCO DI MANDARINO, CONSERVATO, non zuccherat",
                    "SUCCO DI MANDARINO, FRESCO",
                    "SUCCO DI MELA, CONSERVATO, non zuccherato",
                    "SUCCO DI MELAGRANA, FRESCO",
                    "SUCCO DI PERA, CONSERVATO",
                    "SUCCO DI PESCA, CONSERVATO",
                    "SUCCO DI POMPELMO, CONSERVATO, non zuccherato",
                    "SUCCO DI POMPELMO, CONSERVATO, zuccherato",
                    "SUCCO DI UVA, CONSERVATO, non zuccherato",
                    "SUCCO TROPICALE, CONSERVATO",
                    "SUINO [SUS SCROFA], CARNE SEMIGRASSA",
                    "SUINO, BISTECCA",
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
                    "TORTELLINI, freschi",
                    "TRIGLIA [MULLUS SURMULETUS]",
                    "TROTA [SALMO TRUTTA]",
                    "UOVO ",
                    "UVA ",
                    "UVA SULTANINA/UVETTA, UVA SECCA",
                    "VALERIANA",
                    "VANIGLIA",
                    "VENTAGLIO O PETTINE [PECTEN JACOBEAUS]",
                    "VERDURA E LEGUMI, SURGELATI",
                    "VERMOUTH DOLCE",
                    "VERMOUTH SECCO",
                    "VINO BIANCO",
                    "VINO ROSATO",
                    "VINO ROSSO",
                    "VITELLO ",
                    "VODKA",
                    "VONGOLA ",
                    "VONGOLE IN SCATOLA, AL NATURALE",
                    "WHISKY",
                    "WURSTEL",
                    "YOGURT DI LATTE INTERO",
                    "YOGURT DI LATTE MAGRO ALLA FRUTTA",
                    "ZAFFERANO ",
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
                dao.insertIngredienteRicetta(ingrRic = IngredienteRIcetta("pietanza 1","UOVO DI OCA",2))
                dao.insertIngredienteRicetta(ingrRic = IngredienteRIcetta("pietanza 1","VINO ROSATO",200))
                dao.insertRicetteCategoria(rc = RicettaCategorie("pietanza 1","secondo"))
                dao.insertRicetteCategoria(rc = RicettaCategorie("pietanza 1","vegano"))

                dao.insertRicettaPreview(RicettePreview("pietanza 2",false))
                dao.insertRicettaCompleta(RicettaCompleta("pietanza 2","la si cucina quando si è ad una festa"))
                dao.insertIngredienteRicetta(ingrRic = IngredienteRIcetta("pietanza 2","WHISKY",200))
                dao.insertIngredienteRicetta(ingrRic = IngredienteRIcetta("pietanza 2","VODKA",200))
                dao.insertRicetteCategoria(rc = RicettaCategorie("pietanza 2","antipasto"))
                dao.insertRicetteCategoria(rc = RicettaCategorie("pietanza 2","vegano"))
            }
        }
    }
}