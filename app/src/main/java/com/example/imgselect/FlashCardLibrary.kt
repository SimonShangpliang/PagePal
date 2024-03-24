package com.example.mytestapp

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.smallTopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.imgselect.R
import com.example.imgselect.Screen
import com.example.imgselect.data.Meaning
import com.example.imgselect.deckUI
import com.example.imgselect.shelf
import com.example.imgselect.ui.theme.darkBar
import com.example.imgselect.ui.theme.interestcolour1
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
                    containerColor = darkBar,
                    titleContentColor = lightBar
                ),
                title = {
                    Text(text="FlashCard Library")
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
            Image(painter=painterResource(id = R.drawable.searchbar3), contentDescription = "searchBar",modifier=Modifier.fillMaxWidth())
            // gonna be replaced by mayank"s searchBar Ui

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
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp).height(50.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text ="Title",
                color= lightBar,
                fontWeight = FontWeight.Bold,
                modifier= Modifier.padding(start=20.dp),
                fontSize = 25.sp
            )

            Image(
                painter = painterResource(id = R.drawable.frame_30),
                contentDescription = null,
                modifier = Modifier.size(100.dp).clickable {  }
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        LazyRow(modifier = Modifier,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            items(dictionaryViewModel.setOfTitle.toList()) {title->
                Log.d("Title" , title)
                ShelfItem(name = title) {word ->
                    navController.navigate("${Screen.SingleDeckScreen.route}/${title}/${0}")


                }

            }

        }


        Spacer(modifier = Modifier.height(50.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp).height(50.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text ="Date",
                color= lightBar,
                fontWeight = FontWeight.Bold,
                modifier= Modifier.padding(start=20.dp),
                fontSize = 25.sp
            )

            Image(
                painter = painterResource(id = R.drawable.frame_30),
                contentDescription = null,
                modifier = Modifier.size(100.dp).clickable {  }
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        LazyRow(modifier = Modifier,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            items(dictionaryViewModel.setOfDates.toList()) {date->
                Log.d("Title" , date)
                ShelfItem(name = date) {time ->
                    Log.d("DateNavigation" , "${date}")
                    navController.navigate("${Screen.SingleDeckScreen.route}/${date}/${1}")

                }

            }

        }



        Spacer(modifier = Modifier.height(50.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp).height(50.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text ="Words",
                color= lightBar,
                fontWeight = FontWeight.Bold,
                modifier= Modifier.padding(start=20.dp),
                fontSize = 25.sp
            )

            Image(
                painter = painterResource(id = R.drawable.frame_30),
                contentDescription = null,
                modifier = Modifier.size(100.dp).clickable {  }
            )
        }


        Spacer(modifier = Modifier.height(25.dp))
        LazyRow(modifier = Modifier,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            items(dictionaryViewModel.setOfWords.toList()) {word->
                Log.d("Title" , word)
                ShelfItem(name = word) {words ->
                    navController.navigate("${Screen.SingleDeckScreen.route}/${word}/${2}")

                }

            }

        }

        Spacer(modifier = Modifier.height(100.dp))
    }




}

@Composable
fun ShelfItem(name: String, goToOneDeck: (String)-> Unit){
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
                    Text(text ="1.",modifier=Modifier.padding(start=3.dp), textAlign= TextAlign.Start,color= interestcolour1, fontWeight = FontWeight.Bold,)
                    Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End){
                        Image(painter= painterResource(id =R.drawable.generic ),
                            contentDescription = null,
                            modifier= Modifier
                                .padding(end = 3.dp),
                            Alignment.TopEnd)
                    }
                }
            }
            Text(text=name, textAlign = TextAlign.Center,modifier=Modifier.fillMaxWidth())

        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(text=name,modifier = Modifier
            .padding(1.dp)
            .fillMaxWidth(),
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.SemiBold,
            fontSize=14.sp,
            color = lightBar)

    }
}

