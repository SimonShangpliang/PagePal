package com.example.imgselect


import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.imgselect.ui.theme.darkBar
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
fun MainScreen2(){
    var index =0
    val cardsPageTitle ="Personal Notes"
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
            flashcard(
                term="reign1",
                definition ="hold royal office; rule as monarch",
            ),
            flashcard(
                term="reign2",
                definition ="hold royal office; rule as monarch",
            ),
            flashcard(
                term="reign3",
                definition ="hold royal office; rule as monarch",
            ),
            flashcard(
                term="reign4",
                definition ="hold royal office; rule as monarch",
            ),
            flashcard(
                term="reign5",
                definition ="hold royal office; rule as monarch",
            ),flashcard(
                term="reign6",
                definition ="hold royal office; rule as monarch",
            ),flashcard(
                term="reign7",
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
                    term=cardsPage[page_index].term,
                    definition=cardsPage[page_index].definition,
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

                        }
                )
                index++
            }


        }
    }
}








@Composable
fun singleCard(term:String,definition:String,modifier: Modifier,colorCard: Color,colorBorder:Color) {

    Log.d( "Lilac haze", "Your message 2")

    var showDefinition = remember{ mutableStateOf(false) }

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

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun SingleDeckScreen(){

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
                        modifier= Modifier.padding(start=13.dp,top=5.dp).size(15.dp,20.dp)
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
            MainScreen2()

        }
    }
}


