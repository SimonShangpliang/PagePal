package com.example.imgselect


import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import com.example.dictionary.model.DictionaryViewModel
import com.example.imgselect.DictionaryNetwork.Definition
import com.example.imgselect.DictionaryNetwork.Meaning
import com.example.imgselect.DictionaryNetwork.WordData
import com.example.imgselect.ui.theme.OpenSans
import com.example.imgselect.ui.theme.darkBar
import com.example.imgselect.ui.theme.interestcolour1
import com.example.imgselect.ui.theme.lightBar
import com.example.imgselect.ui.theme.lighterPurple
import com.example.imgselect.ui.theme.lighterTeal
import com.example.imgselect.ui.theme.lighterYellow
import com.example.imgselect.ui.theme.medTeal
import com.example.imgselect.ui.theme.outlinePurple
import com.example.imgselect.ui.theme.outlineYellow

import kotlin.math.abs



@OptIn(ExperimentalFoundationApi::class)
fun PagerState.offsetForPage(page: Int): Float{
    return (currentPage - page) + currentPageOffsetFraction
}




@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen2(mainList: List<WordData> , dictionaryViewModel: DictionaryViewModel){

    var index =0
    val cardsPageTitle ="Personal Notes"
    val cardsPage  = mainList
    Log.d("cardPage" , "${cardsPage}")
//    var groupByIdentifier: Map<String , List<com.example.imgselect.data.Meaning>> = emptyMap()
//    if(identifier == 1) {
//        groupByIdentifier= cardsPage.filter { it.title != null }.groupBy { it.title!! }
//    }
//    else if(identifier == 1) {
//        groupByIdentifier = cardsPage.filter { it.date != null }.groupBy { it.date!! }
//    }

//    var finalList: List<com.example.imgselect.data.Meaning>? = groupByIdentifier[basis]

        //// pass the list instead of this
    Log.d( "Lilac haze", "Your message 1")
    val cardReverse = cardsPage.reversed()
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val state = rememberPagerState (pageCount = { cardReverse.size}, initialPage = cardReverse.size-1)

    Column(modifier = Modifier,) {
        Spacer(modifier=Modifier.height(8.dp))
        Text(text=cardsPageTitle, color = lightBar, fontWeight = FontWeight.Bold, fontSize = 20.sp,modifier=Modifier.padding(start=20.dp), textAlign = TextAlign.Start)
        Box(modifier = Modifier
            .padding(vertical = 40.dp)
            .fillMaxWidth(),
            contentAlignment = Alignment.Center)
        {
            HorizontalPager(state = state, beyondBoundsPageCount = 1, pageSpacing = -((screenWidth)/2), reverseLayout = true,
            ){ page_index->
                singleCard(
                    term=cardsPage[page_index].word,
                    //definition=cardsPage[page_index].definition,
                    colorCard = if(index%3==0){
                        lighterTeal}else if(index%3==1){
                        lighterPurple}else{
                        lighterYellow},
                    colorBorder =if(index%3==0){
                        medTeal}else if(index%3==1){
                        outlinePurple}else{
                        outlineYellow},
                    modifier = Modifier

                        .graphicsLayer {
                            rotationZ =
                                if (state.offsetForPage(page_index) > 0) state.offsetForPage(
                                    page_index
                                ) * 2 else 0f
                            translationX =
                                if (state.offsetForPage(page_index) > 0) (state.offsetForPage(
                                    page_index
                                )) * -(screenWidth.toPx() / 2 - 50) - (screenWidth.toPx() / 20) else (state.offsetForPage(
                                    page_index
                                )) * (screenWidth.toPx() / 2) - (screenWidth.toPx() / 20)
                            translationY = if (state.offsetForPage(page_index) > 0) abs(
                                state.offsetForPage(page_index)
                            ) * 20 else 0f
                            // shadowElevation = state.pageCount - abs(state.offsetForPage(page_index))
                            alpha = 1f

                        },
                    data = cardsPage[page_index].meanings,
                    dictionaryViewModel = dictionaryViewModel
                )
                index++
            }


        }
    }
}








@Composable
fun singleCard(term:String,modifier: Modifier,colorCard: Color,colorBorder:Color , data: List<Meaning> , dictionaryViewModel: DictionaryViewModel) {

    Log.d( "dataList", "${data}")

    var showDefinition = remember{ mutableStateOf(false) }
    var whichMeaning = remember { mutableStateOf(0)}
    val groupedByPartOfSpeech: Map<String, List<Meaning>> = data.filter { it.partOfSpeech != null }.groupBy { it.partOfSpeech!! }
    var nouns: List<Meaning>? = groupedByPartOfSpeech["noun"]
    var verbs: List<Meaning>? = groupedByPartOfSpeech["verb"]
    var adjective: List<Meaning>? = groupedByPartOfSpeech["adjective"]

    var finalNouns: MutableList<Definition> = mutableListOf()
    if (nouns != null) {
        nouns.forEach { noun->
            noun.definitions.forEach { definition->
                if(definition.isSelected) {
                    finalNouns.add(definition)
                }
            }
        }
    }

    var finalVerbs: MutableList<Definition> = mutableListOf()
    if (verbs != null) {
        verbs.forEach { verb->
            verb.definitions.forEach { definition->
                if(definition.isSelected) {
                    finalVerbs.add(definition)
                }
            }
        }
    }



    var finalAdjectives: MutableList<Definition> = mutableListOf()
    if (adjective != null) {
        adjective.forEach { adjectives->
            adjectives.definitions.forEach { definition->
                if(definition.isSelected) {
                    finalAdjectives.add(definition)
                }
            }
        }
    }

    Log.d("Noun" , "${nouns}")



    Box(modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center){
        Card(
            modifier = modifier
                .fillMaxWidth(.8f),
            shape = RoundedCornerShape(6.dp),
            border = BorderStroke(1.dp, colorBorder ),
            colors =  CardDefaults
                .cardColors(containerColor = colorCard)

        ){
            Column(
                Modifier
                    .clickable {
                        whichMeaning.value = ((whichMeaning.value + 1) % 4)
                        Log.d("whichMeaning", "${whichMeaning.value}")
                    }
                    .height(460.dp)
                    .width(320.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,


                ){
                if(whichMeaning.value == 0){
                    Text(
                        text = term,
                        color = interestcolour1,
                        fontWeight = FontWeight.Bold,
                        fontFamily= OpenSans,
                        fontSize=36.sp
                    )
                    Log.d( "Lilac haze", "Your message 3")

                }
                else if(whichMeaning.value == 1) {
                    if (finalNouns != null && finalNouns.isNotEmpty()) {
                        Text(
                            text = "Noun",
                            color = interestcolour1,
                            fontFamily = OpenSans,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier=Modifier.height(4.dp))
                        finalNouns.forEach { noun->
                                    Column(modifier=Modifier.fillMaxWidth(0.8f), Arrangement.SpaceAround,
                                         ) {
                                        Text(
                                            text = noun.definition,
                                            color = interestcolour1,
                                            modifier = Modifier,
                                            fontStyle = FontStyle.Normal,
                                            fontFamily = OpenSans,
                                            fontSize = 15.sp


                                        )

                                        noun.example?.let {
                                            Text(
                                                text = "e.g.",
                                                color = interestcolour1,
                                                modifier = Modifier,
                                                fontFamily = OpenSans,
                                                fontSize = 14.sp

                                            )
                                            Text(
                                                text = it,
                                                color = interestcolour1,
                                                modifier = Modifier,
                                                fontStyle = FontStyle.Italic,
                                                fontSize = 14.sp

                                            )
                                        }
                                    }


                        }
                    } else {
                        whichMeaning.value = 2
                    }


                }
                else if(whichMeaning.value == 2) {
                    if(finalVerbs != null && finalVerbs.isNotEmpty()) {
                        Log.d("Verbs" , "${finalVerbs}")
                        Column {
                            Text(
                                text = "Verb",
                                color = interestcolour1,
                                fontFamily = OpenSans,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier=Modifier.height(4.dp))

                            if (finalVerbs != null) {
                                finalVerbs.forEach { verb->
                                    Column(modifier=Modifier.fillMaxWidth(0.8f), Arrangement.SpaceAround,){
                                                Text(
                                                    text = verb.definition,
                                                    color = interestcolour1,
                                                    modifier = Modifier,
                                                    fontStyle = FontStyle.Normal,
                                                    fontFamily = OpenSans,
                                                    fontSize = 15.sp
                                                )

                                                verb.example?.let {
                                                    Text(
                                                        text = "e.g.",
                                                        color = interestcolour1,
                                                        modifier = Modifier,
                                                        fontFamily = OpenSans,
                                                        fontSize = 14.sp
                                                    )
                                                    Text(
                                                        text = it,
                                                        color = interestcolour1,
                                                        modifier = Modifier,
                                                        fontStyle = FontStyle.Italic,
                                                        fontSize = 14.sp
                                                    )
                                                }
                                            }
                                        }


                            }
                        }
                    } else {
                        whichMeaning.value = 3
                    }

                }

                else if(whichMeaning.value == 3)
                    if(finalAdjectives != null && finalAdjectives.isNotEmpty()) {
                        Log.d("Adjective" , "${finalAdjectives}")
                        Column {
                            Text(
                                text = "Adjectives",
                                color = interestcolour1,
                                fontFamily = OpenSans,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier=Modifier.height(4.dp))

                            if (finalAdjectives != null) {
                                finalAdjectives.forEach { adjective->
                                    Column(modifier=Modifier.fillMaxWidth(0.8f), Arrangement.SpaceAround){
                                                Text(
                                                    text = adjective.definition,
                                                    color = interestcolour1,
                                                    modifier = Modifier,
                                                    fontStyle = FontStyle.Normal,
                                                    fontFamily = OpenSans,
                                                    fontSize = 15.sp
                                                )

                                                adjective.example?.let {
                                                    Text(
                                                        text = "e.g.",
                                                        color = interestcolour1,
                                                        modifier = Modifier,
                                                        fontFamily = OpenSans,
                                                        fontSize = 14.sp
                                                    )
                                                    Text(
                                                        text = it,
                                                        color = interestcolour1,
                                                        modifier = Modifier,
                                                        fontStyle = FontStyle.Italic,
                                                        fontSize = 14.sp
                                                    )
                                                }
                                            }
                                        }


                            }
                        }
                    } else {
                        whichMeaning.value = 0
                    }

                }
            }


        }
    }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleDeckScreen(basis: String , identifier: Int , dictionaryViewModel: DictionaryViewModel , list: LiveData<List<com.example.imgselect.data.Meaning>>){
    val usableList by list.observeAsState(initial = emptyList())
    val listOfWords : MutableList<WordData> = mutableListOf()
    usableList.forEach { meaning->
        meaning.wordDetails?.forEach { word->
            listOfWords.add(word)
        }
    }



    var groupByIdentifier: Map<String , List<com.example.imgselect.data.Meaning>> = emptyMap()
    var groupByIdentifier1: Map<String , List<WordData>> = emptyMap()
    if(identifier == 0) {
        groupByIdentifier= usableList.filter { it.title != null }.groupBy { it.title!! }
    }
    else if(identifier == 1) {
        groupByIdentifier = usableList.filter { it.date != null }.groupBy { it.date!! }
    }
    else if(identifier == 2) {
        groupByIdentifier1 =listOfWords.toList().filter { it.word != null }.groupBy { it.word!! }
    }

    var finalList: List<com.example.imgselect.data.Meaning>? = groupByIdentifier[basis]
    Log.d("FinalList" , "${finalList}")

    var finalUsableList: MutableList<WordData> = mutableListOf()

    if (finalList != null) {
        finalList.forEach { item->
            item.wordDetails?.forEach { words->
                finalUsableList.add(words)
            }
        }
    }

    if(identifier == 2) {
        val wordList = groupByIdentifier1[basis]
        if (wordList != null && wordList.isNotEmpty()) {
            wordList.sortedBy { it.time } // replace 'timestamp' with your actual date or timestamp field
            val mostRecentDefinition = wordList.last()
            finalUsableList = mutableListOf(mostRecentDefinition)
        }
    }





    Scaffold(

        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = darkBar,
                    titleContentColor = lightBar
                ),
                navigationIcon = {
                    Image(painter= painterResource(id =R.drawable.bac ),
                        contentDescription = null,
                        modifier= Modifier
                            .padding(start = 13.dp, top = 5.dp)
                            .size(15.dp, 20.dp)
                            ,
                        Alignment.TopStart
                    )

                },
                title = {
                    Row(modifier=Modifier){


                        Text(text="Back",modifier=Modifier, color = lightBar, fontSize = 15.sp, textAlign = TextAlign.Start)

                    }
                }
            )
        },


        containerColor = darkBar

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            MainScreen2( mainList =  finalUsableList.toList() , dictionaryViewModel = dictionaryViewModel)
            Log.d("finalUsableList" , "${finalUsableList.toList()}")

        }
    }
}


