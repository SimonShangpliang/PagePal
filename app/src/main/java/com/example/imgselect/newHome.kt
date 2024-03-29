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
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.imgselect.DictionaryNetwork.WebsiteCount
import com.example.imgselect.model.WebHistoryViewModel
import com.example.imgselect.ui.theme.OpenSans
import com.example.imgselect.ui.theme.aliceBlue
import com.example.imgselect.ui.theme.interestcolour
import com.example.imgselect.ui.theme.interestcolour2
import com.example.imgselect.ui.theme.tinyTeal


@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showBackground = true)
@Composable
fun Home(navController: NavController) {
    val webHistoryViewModel=viewModel<WebHistoryViewModel>()
    val websiteCounts by webHistoryViewModel.websiteCounts.observeAsState(emptyList())


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
            title="Website",
            selectedIcon = painterResource(id = R.drawable.globe),
            unselectedIcon = painterResource(id = R.drawable.globe_grey),
            route=Screen.WebViewScreen.route
        ),
        BottomNavigationItem(
            title = "Camera",
            selectedIcon = painterResource(id = R.drawable.photo_camera),
            unselectedIcon = painterResource(id = R.drawable.photo_camera_grey),
            route=Screen.CameraScreen.route

        ),
        BottomNavigationItem(
            title="PDF",
            selectedIcon = painterResource(R.drawable.picture_as_pdf),
            unselectedIcon= painterResource(R.drawable.picture_as_pdf),
            route=Screen.PdfScreen.route

        )

    )

    Scaffold(

        topBar = {
            TopAppBar(
                colors = smallTopAppBarColors(
                    containerColor = darkBar,
                    titleContentColor = aliceBlue
                ),
                title = {
                    Row(modifier=Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){
                        Text(text="Welcome",modifier=Modifier.padding(start=10.dp,top=10.dp), textAlign= TextAlign.Start, fontFamily = OpenSans ,fontWeight=FontWeight.SemiBold)
                        IconButton(
modifier=Modifier.padding(end=10.dp,top=10.dp),
                         onClick = { navController.navigate(Screen.ProfileScreen.route) }
                         ){
                            Icon(painter= painterResource(id =R.drawable.profile_icon ) , contentDescription ="profile",)
                        }


                    }
                }
            )
        }, bottomBar = {
                       NavigationBar(
                           containerColor = darkBar, contentColor = darkBar, modifier= Modifier

                           .height(85.dp)) {

                           Box(modifier=Modifier.padding(bottom = 20.dp, start = 20.dp, end = 20.dp,top=8.dp).height(50.dp) .coloredShadow(
                               Color.Black, 0.8f, 16.dp, 30.dp,10.dp, 0.dp
                           )
                               .background(interestcolour1, RoundedCornerShape(12.dp)),

                            //   .shadow(12.dp, shape = CircleShape, ambientColor = Color.Black, clip = true)
                           ){ Row(
                               modifier = Modifier,Arrangement.Center, Alignment.CenterVertically
                           ) {


                               items.forEachIndexed { index, item ->
                                   this@NavigationBar.NavigationBarItem(
                                       selected = false,
                                       onClick = {
                                           // selectedItemIndex==index

                                           navController.navigate(item.route)
                                       },
                                       icon = {
                                           Icon(
                                               painter =
                                               if (index == selectedItemIndex) {
                                                   item.selectedIcon
                                               } else item.unselectedIcon,
                                               contentDescription = item.title, tint = Color.White,modifier=Modifier
                                           )
                                       }
                                   )

                               }
                           }
                       }
                       }


       }
,

        containerColor = darkBar


    ) { innerPadding ->
        val scroll= rememberScrollState()
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(scroll),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Spacer(modifier=Modifier.height(38.dp))
            FlashcardsHome(navController)

            MakeRows(title = "PDF", recentList = RecentList )
            websiteCounts?.let { counts ->
                // Access counts here when it's not null
                MakeRows2(title = "Websites", recentList = counts)
            }

        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FlashcardsHome(navController: NavController){
    val cardsPage = remember{
        mutableStateListOf(
            homeCard("FLASHCARDS", lighterPurple, outlinePurple,Screen.MeaningScreen.route),
            homeCard("SUMMARY SHELF", lighterTeal, medTeal,Screen.SummaryScreen.route),
            homeCard("CHATS SAVED", lighterYellow, outlineYellow,Screen.ChatListScreen.route)
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
                .padding(start = 10.dp, end = 34.dp,top=4.dp),

            contentAlignment = Alignment.BottomCenter,
        ) {
            Card(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(androidx.compose.material.MaterialTheme.colors.background)
                    .fillMaxWidth()
                    .aspectRatio(1.94f)
                    .padding(horizontal = 0.dp),
                shape = RoundedCornerShape(12.dp),
                backgroundColor = cardReverse[page].color,
                elevation = 6.dp,
                border = BorderStroke( 2.dp,cardReverse[page].color1
                ),
            ) {
                Box(modifier = Modifier
                    .clickable {
                        navController.navigate(cardReverse[page].route)
                    }
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                    Alignment.Center

                ) {
                    Text(text=cardReverse[page].title,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.SemiBold,
                        color= interestcolour1,
                        fontFamily = OpenSans
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
            Text(text="Recent $title",modifier=Modifier.padding(start=19.dp), textAlign= TextAlign.Start,color= aliceBlue, fontWeight = FontWeight.SemiBold,)
            Row(modifier=Modifier.fillMaxWidth().padding(top=3.dp), horizontalArrangement = Arrangement.End){
                Text(text="Show All",color= tinyTeal,textAlign=TextAlign.End,fontSize = 11.5.sp,)
                Image(painter= painterResource(id =R.drawable.fa_solid_chevron_circle_right ),
                    contentDescription = null,
                    modifier= Modifier
                        .padding(end = 13.dp,start=2.dp,top=1.1.dp),
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
fun MakeRows2(title:String, recentList:List<WebsiteCount>){
    var index=0
    Column(modifier=Modifier) {
        Row(modifier=Modifier,){
            Text(text="Frequently Visited $title",modifier=Modifier.padding(start=19.dp), textAlign= TextAlign.Start,color= aliceBlue, fontWeight = FontWeight.SemiBold,)
            Row(modifier=Modifier.fillMaxWidth().padding(top=3.dp), horizontalArrangement = Arrangement.End){
                Text(text="Show All",color= tinyTeal,textAlign=TextAlign.End,fontSize = 11.5.sp,)
                Image(painter= painterResource(id =R.drawable.fa_solid_chevron_circle_right ),
                    contentDescription = null,
                    modifier= Modifier
                        .padding(end = 13.dp,start=2.dp,top=1.1.dp),
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
                    singleTitle =recentUI.websiteName,
                    subSingleTitle ="",
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