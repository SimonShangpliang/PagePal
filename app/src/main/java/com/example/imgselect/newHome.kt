package com.example.imgselect


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.smallTopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import android.util.Log
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable


@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showBackground = true)
@Composable
fun Home() {
    var presses by remember { mutableIntStateOf(0) }
    val RecentList= mutableListOf<recent>(
        recent("Design","ME101"),
        recent("Design","ME101"),
        recent("Design","ME101"),
        recent("Design","ME101"),
        recent("Design","ME101"),
        recent("Design","ME101"),
        recent("Design","ME101"),

        )
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }
    val items = listOf(
        BottomNavigationItem(
            title="ChatBot",
            selectedIcon = painterResource(id = R.drawable.globe),
            unselectedIcon = painterResource(id = R.drawable.globe_grey)
        ),
        BottomNavigationItem(
            title = "Camera",
            selectedIcon = painterResource(id = R.drawable.photo_camera),
            unselectedIcon = painterResource(id = R.drawable.photo_camera_grey)
        ),
        BottomNavigationItem(
            title="PDF",
            selectedIcon = painterResource(R.drawable.picture_as_pdf),
            unselectedIcon= painterResource(id = R.drawable.picture_as_pdf_grey)
        )

    )

    Scaffold(

        topBar = {
            TopAppBar(
                colors = smallTopAppBarColors(
                    containerColor = darkBar,
                    titleContentColor = lightBar
                ),
                title = {
                    Row(modifier=Modifier){
                        Text(text="Welcome",modifier=Modifier, textAlign= TextAlign.Start)
                        Image(painter= painterResource(id =R.drawable.profile_icon ),
                            contentDescription = null,
                            modifier= Modifier
                                .fillMaxWidth()
                                .padding(end = 13.dp),
                            Alignment.TopEnd)

                    }
                }
            )
        }, bottomBar = {
                       NavigationBar(containerColor = darkBar, contentColor = Color.White){
                           items.forEachIndexed{ index,item ->
                               NavigationBarItem(
                                   selected = false,
                                   onClick = { selectedItemIndex==index},
                                   icon = {
                                           Icon(painter =  item.selectedIcon,
//                                           if(index==selectedItemIndex){
//                                               item.selectedIcon}else item.unselectedIcon,
                                          contentDescription = item.title
                                           )
                                   }
                               )

                           }
                       }


       }
,

        containerColor = darkBar

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Spacer(modifier=Modifier.height(38.dp))
            FlashcardsHome()
            MakeRows(title = "PDF", recentList = RecentList )

        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FlashcardsHome(){
    val cardsPage = remember{
        mutableStateListOf(
            homeCard("Flashcards", lighterPurple, outlinePurple),
            homeCard("Summary Shelf", lighterTeal, medTeal),
            homeCard("Chat Bot", lighterYellow, outlineYellow)
           )}
    val configuration = LocalConfiguration.current
    val cardReverse = cardsPage.reversed()
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val pagerState = rememberPagerState(
        pageCount = { cardReverse.size}, initialPage = cardReverse.size-1
    )
    HorizontalPager(
        modifier = Modifier
            .fillMaxWidth(),
        state = pagerState,
        beyondBoundsPageCount = 4,
        pageSpacing = (-screenWidth / 2),
        reverseLayout = true

    ) { page ->
        Box(
            modifier = Modifier
                .graphicsLayer {
                    translationX =
                        if (pagerState.offsetForPage(page) > 0) (pagerState.offsetForPage(page)) * -(screenWidth.toPx() / 2 - 50) else (pagerState.offsetForPage(
                            page
                        )) * (screenWidth.toPx() / 2)
                    translationY = if (pagerState.offsetForPage(page) > 0) abs(
                        pagerState.offsetForPage(page)
                    ) * -50 else 0f
                }
                .padding(start = 10.dp, end = 34.dp),

            contentAlignment = Alignment.BottomCenter,
        ) {
            Card(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(androidx.compose.material.MaterialTheme.colors.background)
                    .fillMaxWidth()
                    .aspectRatio(1.94f)
                    .padding(horizontal = 0.dp),
                shape = RoundedCornerShape(8.dp),
                backgroundColor = cardReverse[page].color,
                elevation = 6.dp,
                border = BorderStroke( 2.dp,cardReverse[page].color1
                ),
            ) {
                Box(modifier = Modifier
                    .clickable {
                        /// write to navigate
                    }
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                    Alignment.Center

                ) {
                    Text(text=cardReverse[page].title,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.SemiBold,
                        color= interestcolour1,
                        )
                }
            }
        }
    }
}

@Composable
fun MakeRows(title:String, recentList:MutableList<recent>){
    var index=0
    Column(modifier=Modifier) {
        Row(modifier=Modifier,){
            Text(text="Recent $title",modifier=Modifier.padding(start=19.dp), textAlign= TextAlign.Start,color= lightBar, fontWeight = FontWeight.Bold,)
            Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End){
                Text(text="Show All",color= lightBar,textAlign=TextAlign.End,fontSize = 11.5.sp,)
                Image(painter= painterResource(id =R.drawable.fa_solid_chevron_circle_right ),
                    contentDescription = null,
                    modifier= Modifier
                        .padding(end = 13.dp,top=2.5.dp,start=2.dp),
                    Alignment.TopEnd)
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
        LazyRow(modifier = Modifier,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            this.items(recentList) { recentUI  ->
                SingleRecent(
                    singleTitle =recentUI.singleTitle,
                    subSingleTitle =recentUI.subSingleTitle,
                    backColor = if(index%3==0){
                        lighterTeal}else if(index%3==1){
                        lighterPurple}else{
                        lighterYellow},
                    outColor= if(index%3==0){
                        medTeal}else if(index%3==1){
                        outlinePurple}else{
                        outlineYellow},
                )
                index++
            }

        }
    }
}

@Composable
fun SingleRecent(singleTitle:String,subSingleTitle:String,backColor:Color,outColor:Color){
    Column(){
        Log.d("backColor",backColor.toString())
        Card(
            backgroundColor = backColor,
            border = BorderStroke(1.dp, color = outColor),
            shape = RoundedCornerShape(6.dp),
            elevation =  6.dp,
            modifier = Modifier
                .size(width = 148.dp, height = 180.dp)
            ,) {
        }
        Text(text=singleTitle,modifier = Modifier
            .padding(1.dp)
            .fillMaxWidth(),
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.SemiBold,
            fontSize=14.sp,
            color = lightBar)
        Text(text=subSingleTitle,modifier = Modifier
            .padding(1.dp)
            .fillMaxWidth(),
            fontWeight = FontWeight.Light,
            fontSize=10.sp,
            textAlign = TextAlign.Start,
            color = lightBar)

    }
}