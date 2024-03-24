package com.example.mytestapp

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
import com.example.imgselect.ui.theme.lightBar
import com.example.imgselect.ui.theme.lighterPurple
import com.example.imgselect.ui.theme.lighterTeal
import com.example.imgselect.ui.theme.lighterYellow


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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShelfItem(name: String , goToOneDeck: (String)-> Unit){
    Box(modifier=Modifier.clickable { goToOneDeck(name) }, contentAlignment = Alignment.TopStart){
        Card(
            colors = CardDefaults.cardColors(
                containerColor = lighterYellow,
            ),
            modifier = Modifier
                .size(width = 130.dp, height = 170.dp)
                .graphicsLayer(
                    scaleX = 1f,
                    scaleY = 1f,
                    rotationZ = 18f,
                    translationX = 20f,
                ),
        ) {}
        Card(
            colors = CardDefaults.cardColors(
                containerColor = lighterTeal,
            ),
            modifier = Modifier
                .size(width = 130.dp, height = 170.dp)
                .graphicsLayer(
                    scaleX = 1f,
                    scaleY = 1f,
                    rotationZ = 13f,
                    translationX = 6f,
                )
        ) {}

        Card(
            colors = CardDefaults.cardColors(
                containerColor = lighterPurple,
            ),
            modifier = Modifier
                .size(width = 150.dp, height = 180.dp)
                .graphicsLayer(
                    scaleX = 1f,
                    scaleY = 1f,
                ),


            ) {
            Text(
                text = name,
                modifier = Modifier
                    .padding(16.dp),
                textAlign = TextAlign.Center,
                color = darkBar
            )
        }

    }
}

@Preview
@Composable
fun FlashcardLibraryPreview() {
    //flashCardLibrary()
}