package com.example.mytestapp

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.smallTopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.example.dictionary.model.DictionaryViewModel
import com.example.imgselect.DictionaryNetwork.WordData
import com.example.imgselect.MySearchBar
import com.example.imgselect.R
import com.example.imgselect.Screen
import com.example.imgselect.data.Meaning
import com.example.imgselect.deckUI
import com.example.imgselect.shelf
import com.example.imgselect.ui.theme.OpenSans
import com.example.imgselect.ui.theme.aliceBlue
import com.example.imgselect.ui.theme.darkBar
import com.example.imgselect.ui.theme.interestcolour
import com.example.imgselect.ui.theme.interestcolour1
import com.example.imgselect.ui.theme.interestcolour2
import com.example.imgselect.ui.theme.interestcolour3
import com.example.imgselect.ui.theme.lightBar
import com.example.imgselect.ui.theme.lighterPurple
import com.example.imgselect.ui.theme.lighterTeal
import com.example.imgselect.ui.theme.lighterYellow
import com.example.imgselect.ui.theme.medTeal
import com.example.imgselect.ui.theme.outlinePurple
import com.example.imgselect.ui.theme.outlineYellow


val headingSize = 6.dp
val shelfRowList = mutableListOf<shelf>(
    shelf(
        title = "College",
        cardDeck = mutableListOf(
            deckUI(
                name="Design",
                subtitle="ME 101",
            ),
            deckUI(
                name="Design",
                subtitle="ME 101",
            ),
            deckUI(
                name="Design",
                subtitle="ME 101",
            ),deckUI(
                name="Design",
                subtitle="ME 101",
            )
        )
    )
)





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun flashCardLibrary(dictionaryViewModel: DictionaryViewModel , navController: NavController) {
    var presses by remember { mutableIntStateOf(0) }
    val isDark = true // it gonna be passed to the function


    Scaffold(
        topBar = {
            TopAppBar(
                colors = smallTopAppBarColors(
                    containerColor = interestcolour3,
                    titleContentColor = aliceBlue
                ),
                title = {
                    Text(text="Flashcard Library", fontSize =30.sp , modifier=Modifier,textAlign= TextAlign.Start,fontWeight=FontWeight.SemiBold)
                }

            )
        },
        containerColor = interestcolour3
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
           //  Divider(thickness = 1.dp, color = interestcolour2)
            shelfRow(shelfRowList = dictionaryViewModel.getMeaningList() , dictionaryViewModel = dictionaryViewModel , navController = navController)

        }
    }
}
@Composable
fun shelfRow(shelfRowList: LiveData<List<Meaning>> , dictionaryViewModel: DictionaryViewModel , navController: NavController){

    val meaningList by shelfRowList.observeAsState(initial  = emptyList())
    val state = rememberScrollState()
    meaningList.forEach { meaning->
        dictionaryViewModel.setOfTitle.add(meaning.title)
        dictionaryViewModel.setOfDates.add(meaning.date)
        meaning.wordDetails?.forEach { word ->
            dictionaryViewModel.setOfWords.add(word.word)
        }
    }
    Column(
        modifier = Modifier.verticalScroll(
            state =  state,
            enabled = true
        )
    ) { EachShelfRow("Title",navController,dictionaryViewModel,0,dictionaryViewModel.setOfTitle)
        EachShelfRow("Date",navController,dictionaryViewModel,1,dictionaryViewModel.setOfDates)
        EachShelfRow("Word",navController,dictionaryViewModel,2,dictionaryViewModel.setOfWords)

    }


}

@Composable
fun ShelfItem(name: String,  number: String,textAlign: TextAlign , goToOneDeck: (String)-> Unit ,){
    Column(modifier=Modifier){
        Box(modifier=Modifier.clickable { goToOneDeck(name) }, contentAlignment = Alignment.Center){
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = lighterYellow,
                ),
                border = BorderStroke(1.dp,color= outlineYellow),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                modifier = Modifier
                    .size(width = 130.dp, height = 155.dp)
                    .graphicsLayer(
                        scaleX = 1f,
                        scaleY = 1f,
                        rotationZ = 7f,
                        translationX = 105f,
                        translationY = 0f,
                    )
            ) {}
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = lighterTeal,
                ),
                border = BorderStroke(1.dp,color= medTeal),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                modifier = Modifier
                    .size(width = 130.dp, height = 168.dp)
                    .graphicsLayer(
                        scaleX = 1f,
                        scaleY = 1f,
                        rotationZ = 4f,
                        translationX = 70f,
                        translationY = 0f,

                        )
            ) {}

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = lighterPurple,
                ),
                border = BorderStroke(1.dp,color= outlinePurple),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                ),
                modifier = Modifier
                    .size(width = 150.dp, height = 180.dp)
                    .align(
                        Alignment.Center
                    )
                    .graphicsLayer(
                        scaleX = 1f,
                        scaleY = 1f,
                    ),


                ) {
                Spacer(modifier=Modifier.height(6.dp))
                Row(modifier=Modifier,){
                    Text(text =number+".",modifier=Modifier.padding(start=3.dp), textAlign= TextAlign.Start,color= interestcolour1, fontWeight = FontWeight.Bold,)
                    Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End){
                        Image(painter= painterResource(id =R.drawable.generic ),
                            contentDescription = null,
                            modifier= Modifier
                                .padding(end = 3.dp),
                            Alignment.TopEnd)
                    }
                }
            }
            Text(text=name, textAlign = TextAlign.Center,modifier=Modifier.fillMaxWidth() , color = Color.Black)

        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(text=name,modifier = Modifier
            .padding(1.dp)
            .fillMaxWidth(),
            textAlign = textAlign,
            fontWeight = FontWeight.SemiBold,
            fontSize=14.sp,
            color = lightBar)

        Spacer(modifier = Modifier.height(20.dp))

    }
}

@Composable
fun GridOfARowOfFlashLib(elements: List<String> , dictionaryViewModel: DictionaryViewModel , navController: NavController) {
    val text = when (elements) {
        dictionaryViewModel.setOfTitle.toList() -> "Title"
        dictionaryViewModel.setOfDates.toList() -> "Date"
        else -> "Words"
    }
    val searchQuery = remember { mutableStateOf("") }
    val filteredList = if (searchQuery.value.isEmpty()) {
        elements
    } else {
        elements.filter { element ->
            element.contains(searchQuery.value, ignoreCase = true)
        }
    }
    Column(
        modifier = Modifier.background(Color.Black)
    ) {
        Text(
            text = text,
            modifier = Modifier
                .padding(horizontal = 30.dp, vertical = 16.dp)
                .fillMaxWidth(),
            color = Color.White,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold
        )
        MySearchBar(
            placeHolder = "Search",
            onQueryChanged = { query -> searchQuery.value = query }
        )
        Spacer(modifier = Modifier.height(20.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .background(Color.Black)
                .padding(16.dp)
                .fillMaxHeight()
        ) {
            itemsIndexed(filteredList) { index, element ->
                if (elements == dictionaryViewModel.setOfTitle.toList()) {
                    ShelfItem(
                        name = element,
                        number = (index + 1).toString(),
                        textAlign = TextAlign.Center
                    ) { time ->
                        Log.d("DateNavigation", "${element}")
                        navController.navigate("${Screen.SingleDeckScreen.route}/${element}/${0}")

                    }
                } else if (elements == dictionaryViewModel.setOfDates.toList()) {
                    ShelfItem(
                        name = element,
                        number = (index + 1).toString(),
                        textAlign = TextAlign.Center
                    ) { time ->
                        Log.d("DateNavigation", "${element}")
                        navController.navigate("${Screen.SingleDeckScreen.route}/${element}/${1}")

                    }
                } else {
                    ShelfItem(
                        name = element,
                        number = (index + 1).toString(),
                        textAlign = TextAlign.Center
                    ) { time ->
                        Log.d("DateNavigation", "${element}")
                        navController.navigate("${Screen.SingleDeckScreen.route}/${element}/${2}")

                    }
                }
            }
        }
    }

}
@Composable
fun EachShelfRow(heading:String,navController: NavController,dictionaryViewModel: DictionaryViewModel,index1:Int,mutableSetLIST:MutableSet<String>){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp,end=16.dp,top=16.dp)
            .height(50.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text =heading,
            color= aliceBlue,
            fontWeight = FontWeight.SemiBold,
            modifier= Modifier.padding(start=1.dp),
            fontSize = 20.sp
        )

        Image(
            painter = painterResource(id = com.example.imgselect.R.drawable.frame_30),
            contentDescription = null,
            modifier = Modifier
                .padding(bottom = 20.dp)
                .size(80.dp)
                .clickable {
                    navController.navigate("${Screen.SingleRowOfFlashLib.route}/${index1}")
                }
        )
    }
    //  Spacer(modifier = Modifier.height(10.dp))
    LazyRow(modifier = Modifier,
        contentPadding = PaddingValues(start=20.dp,end=40.dp),
        horizontalArrangement = Arrangement.spacedBy(58.dp)
    ) {
        itemsIndexed(mutableSetLIST.toList()) {index,heading->
           // Log.d("Title" , title)
            ShelfItem(name = heading , number = (index+1).toString() , textAlign = TextAlign.Start) {word ->
                navController.navigate("${Screen.SingleDeckScreen.route}/${heading}/${index1}")

            }


        }

    }
}
