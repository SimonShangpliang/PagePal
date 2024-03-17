package com.example.imgselect


import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.imgselect.ui.theme.lighterTeal
import com.example.imgselect.ui.theme.medTeal

import kotlin.math.abs



@OptIn(ExperimentalFoundationApi::class)
fun PagerState.offsetForPage(page: Int): Float{
    return (currentPage - page) + currentPageOffsetFraction
}




@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen2(){
    val cardsPage = remember{
        mutableStateListOf(
            flashcard(
                term="nostalgia",
                definition ="a sentimental longing or wistful affection for a period in the past",
            ),
            flashcard(
                term="fierce",
                definition ="having or displaying an intense or ferocious aggressiveness",
            ),
            flashcard(
                term="reign",
                definition ="hold royal office; rule as monarch",
            ),

            )
        //// pass the list instead of this
    }
    Log.d( "Lilac haze", "Your message 1")
    val cardReverse = cardsPage.reversed()
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val state = rememberPagerState {3} /// change this accordingly (change 4)




}


@Composable
fun singleCard(term:String,definition:String,modifier: Modifier) {

    Log.d( "Lilac haze", "Your message 2")

    var showDefinition = remember{ mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center){
        Card(
            modifier = modifier
                .fillMaxWidth(0.72f),
            shape = RoundedCornerShape(6.dp),
            border = BorderStroke(1.dp, medTeal ),
            colors =  CardDefaults
                .cardColors(containerColor = lighterTeal)

        ){
            Column(
                Modifier
                    .clickable { showDefinition.value = !showDefinition.value }
                    .height(460.dp)
                    .width(320.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,


                ){
                if(!showDefinition.value){
                    Text(text = term)
                    Log.d( "Lilac haze", "Your message 3")

                } else {
                    Text(text = definition)
                }
            }


        }
    }
}
