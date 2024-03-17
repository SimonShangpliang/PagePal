package com.example.mytestapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.smallTopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.imgselect.R
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
@Preview(showBackground = true)
@Composable
fun flashCardLibrary() {
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

            shelfRow(shelfRowList = shelfRowList)

        }
    }
}

@Composable
fun shelfRow(shelfRowList:MutableList<shelf>){

    for (shelf in shelfRowList) {
        val cardDeck = shelf.cardDeck
        Column(){
            Text(text =shelf.title,
                color= lightBar,
                fontWeight = FontWeight.Bold,
                modifier= Modifier.padding(start=20.dp)
            )
            Spacer(modifier = Modifier.height(15.dp))
            LazyRow(modifier = Modifier,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(28.dp)
            ) {
                this.items(cardDeck) { deckUI  ->
                    ShelfItem(
                        name=deckUI.name,
                        subname =deckUI.subtitle,
                        term=deckUI.name,
                    )
                }
            }
        }


    }

}

@Composable
fun ShelfItem(name: String,subname:String,term:String ){
    Box(modifier=Modifier, contentAlignment = Alignment.TopStart){
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
                )
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