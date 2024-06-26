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
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
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
import androidx.compose.ui.input.key.Key.Companion.F
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.dictionary.model.DictionaryViewModel
import com.example.imgselect.DictionaryNetwork.WebsiteCount
import com.example.imgselect.data.WebBookMarked
import com.example.imgselect.model.WebHistoryViewModel
import com.example.imgselect.ui.theme.OpenSans
import com.example.imgselect.ui.theme.aliceBlue
import com.example.imgselect.ui.theme.interestcolour
import com.example.imgselect.ui.theme.interestcolour2
import com.example.imgselect.ui.theme.tinyTeal
import com.example.mytestapp.ShelfItem
import java.net.URLEncoder


@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showBackground = true)
@Composable
fun Home(navController: NavController) {
    val webHistoryViewModel=viewModel<WebHistoryViewModel>()
    val bookmarkedWebs by webHistoryViewModel.bookmarkedWebs.observeAsState(emptyList())
    val context= LocalContext.current
    var recommendedWebsites =remember{ProfileData(context).getWebsitesByInterests()}


    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }
    val googleUrl = URLEncoder.encode("https://www.google.com", "UTF-8")

    val items = listOf(
        BottomNavigationItem(
            title="Website",
            selectedIcon = painterResource(id = R.drawable.globe),
            unselectedIcon = painterResource(id = R.drawable.globe_grey),
            route="${Screen.WebViewScreen.route}/${googleUrl}"
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
                       Text(text="Welcome", fontSize =30.sp , modifier=Modifier.padding(start=10.dp).align(Alignment.CenterVertically),textAlign= TextAlign.Start,fontWeight=FontWeight.Bold)

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

                           Box(modifier= Modifier
                               .padding(bottom = 20.dp, start = 20.dp, end = 20.dp, top = 8.dp)
                               .height(50.dp)
                               .coloredShadow(
                                   Color.Black, 0.8f, 16.dp, 30.dp, 10.dp, 0.dp
                               )
                               .background(Color(0x70424242), RoundedCornerShape(12.dp)),
//                           Color(0xff424242
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

//            MakeRows(title = "PDF", recentList = RecentList ) {
//                navController.navigate(Screen.GridOfRecentPDF.route)
//            }

            bookmarkedWebs?.let { counts ->
                // Access counts here when it's not null
                FreqVisited(title = "Websites", recentList = bookmarkedWebs,navController=navController){
                    navController.navigate(Screen.GridOfRecentWebsites.route)
                }
            }


           recommendedWebsites?.let { counts ->
                // Access counts here when it's not null
                RecommendedWebsites(title = "Hand Picked For You", recentList = recommendedWebsites,navController=navController){
                    navController.navigate(Screen.GridOfRecentWebsites.route)
                }
            }

        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FlashcardsHome(navController: NavController){
    val cardsPage = remember{
        mutableStateListOf(
            homeCard("FLASHCARDS", lighterPurple, outlinePurple,Screen.MeaningScreen.route,"Flashcards are the pocket-sized keys to unlocking vast realms of knowledge, turning learning into a game and mastery into a habit.\""),
            homeCard("SUMMARY SHELF", lighterTeal, medTeal,Screen.SummaryScreen.route,"Summaries distill oceans of information into droplets of understanding, guiding us through the labyrinth of knowledge with clarity and conciseness"),
            homeCard("CHATS SAVED", lighterYellow, outlineYellow,Screen.ChatListScreen.route,"Conversations with a chatbot: where curiosity meets technology, weaving a tapestry of questions, answers, and endless possibilities.")
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
                .padding(start = 10.dp, end = 34.dp, top = 4.dp),

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
                 //   Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                    Text(text=cardReverse[page].title,
                        fontSize = 30.sp,


                                color= interestcolour1

                        )
//                        Text(
//                            text = cardReverse[page].description,
//                            color = Color(0X001F1F1F).copy(alpha = 0.5f),
//
//                            style = TextStyle(fontStyle = FontStyle.Italic, fontSize = 18.sp),
//                            modifier = Modifier.padding(20.dp).align(Alignment.CenterHorizontally),
//                            overflow = TextOverflow.Ellipsis,
//                            textAlign = TextAlign.Center,
//                            fontWeight = FontWeight.SemiBold
//                        )

              //      }
                }
            }
        }
    }
}

@Composable
fun MakeRows(title:String, recentList:MutableList<recent> , goToGridView:()->Unit){
    var index=0
    Column(modifier=Modifier) {
        Row(modifier=Modifier,){
            Text(text="Recent $title",modifier=Modifier.padding(start=19.dp), textAlign= TextAlign.Start,color= aliceBlue, fontWeight = FontWeight.SemiBold,)
            Row(
                modifier= Modifier
                    .fillMaxWidth()
                    .padding(top = 3.dp)
                    .clickable { goToGridView() },
                horizontalArrangement = Arrangement.End
            ){
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
                SingleRecent(modifier=Modifier.clickable {  },
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
fun FreqVisited(title:String, recentList:List<WebBookMarked> ,navController: NavController,goToSingleGridScreen: () -> Unit){
    var index=0
    Column(modifier=Modifier) {
        if(recentList.size>0){
        Row(modifier=Modifier,) {
            Text(
                text = "Bookmarked $title",
                modifier = Modifier.padding(start = 19.dp),
                textAlign = TextAlign.Start,
                color = aliceBlue,
                fontWeight = FontWeight.SemiBold,
            )
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 3.dp)
//                    .clickable {
//                        goToSingleGridScreen()
//                    },
//                horizontalArrangement = Arrangement.End
//            ) {
//                Text(
//                    text = "Show All",
//                    color = tinyTeal,
//                    textAlign = TextAlign.End,
//                    fontSize = 11.5.sp,
//                )
//                Image(
//                    painter = painterResource(id = R.drawable.fa_solid_chevron_circle_right),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .padding(end = 13.dp, start = 2.dp, top = 1.1.dp),
//                    Alignment.TopEnd
//                )
//           }
            Spacer(modifier = Modifier.height(20.dp))
        }
        }
        LazyRow(modifier = Modifier,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            this.items(recentList) { recentUI  ->
                val encodedUrl = URLEncoder.encode(recentUI.website, "UTF-8")

                SingleRecent(modifier=Modifier.clickable { navController.navigate("${Screen.WebViewScreen.route}/$encodedUrl") },
                    singleTitle =recentUI.title!!,
                    subSingleTitle =recentUI.website!!,
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
fun RecommendedWebsites(title:String, recentList:List<Website> ,navController: NavController,goToSingleGridScreen: () -> Unit){
    var index=0
    Column(modifier=Modifier) {
        if  (recentList.size>0){
        Row(modifier=Modifier,){
       Text(text="$title",modifier=Modifier.padding(start=19.dp), fontSize = 20.sp, textAlign= TextAlign.Start,color= aliceBlue, fontWeight = FontWeight.SemiBold)
//            Row(
//                modifier= Modifier
//                    .fillMaxWidth()
//                    .padding(top = 3.dp)
//                    .clickable {
//                        goToSingleGridScreen()
//                    }
//                ,
//                horizontalArrangement = Arrangement.End
//            ){
//                Text(text="Show All",color= tinyTeal,textAlign=TextAlign.End,fontSize = 11.5.sp,)
//                Image(painter= painterResource(id =R.drawable.fa_solid_chevron_circle_right ),
//                    contentDescription = null,
//                    modifier= Modifier
//                        .padding(end = 13.dp,start=2.dp,top=1.1.dp),
//                    Alignment.TopEnd)
//            }
            Spacer(modifier = Modifier.height(20.dp))}

        }else
        {
            Column() {
                Text(
                    text = "$title",
                    modifier = Modifier.padding(start = 19.dp),
                    textAlign = TextAlign.Start,
                    color = aliceBlue,
                    fontSize = 20.sp,

                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Pick your interests on Profile Page...",
                    color = Color.Gray,
                    style = TextStyle(fontStyle = FontStyle.Italic, fontSize = 10.sp),
                    modifier = Modifier.padding(start = 19.dp),
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        LazyRow(modifier = Modifier,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            this.items(recentList) { recentUI  ->
                val encodedUrl = URLEncoder.encode(recentUI.Link, "UTF-8")

                SingleRecent(modifier=Modifier.clickable { navController.navigate("${Screen.WebViewScreen.route}/$encodedUrl") },
                    singleTitle =recentUI.Title,
                    subSingleTitle =recentUI.genreName,
                    about=recentUI.Description,
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
fun removeUrlPrefixAndSuffix(url: String): String {
    return url.replace("https://", "")
        .replace("http://", "")
        .replace("/","")

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SingleRecent(singleTitle:String,subSingleTitle:String,about:String="",backColor:Color,outColor:Color,modifier:Modifier){
    Column(modifier=modifier){
        Log.d("backColor",backColor.toString())
        Card(
            backgroundColor = backColor,
            border = BorderStroke(1.dp, color = outColor),
            shape = RoundedCornerShape(6.dp),
            elevation =  6.dp,
            modifier = Modifier
                .size(width = 148.dp, height = 180.dp)
            ,) {
            if(about!=""){

                Text(
                    text = about
                    ,
                    color = Color.Gray,
                    style = TextStyle(fontStyle = FontStyle.Italic),
                    textAlign = TextAlign.Center,
                    fontSize = 10.sp,
                    maxLines = 12,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(0.8f).padding(horizontal = 10.dp, vertical = 20.dp).align(Alignment.CenterHorizontally)
                )

            }
        }
        Text(text=singleTitle,modifier = Modifier
            .padding(1.dp)
            .width(148.dp)
            .basicMarquee(iterations = 100),
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.SemiBold,
            fontSize=14.sp,
            color = lightBar)
        Text(
            text = subSingleTitle,
            color = Color.Gray,
            style = TextStyle(fontStyle = FontStyle.Italic),
            fontSize = 10.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.width (148.dp).padding(1.dp)
        )

    }
}
@Composable
fun GridOfRowOfRecentPDF() {
    val RecentList= mutableListOf<recent>(
        recent("Design","ME101"),
        recent("Design","ME101"),
        recent("Design","ME101"),
        recent("Design","ME101"),
        recent("Design","ME101"),
        recent("Design","ME101"),
        recent("Design","ME101"),
        )

    Column(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Recent PDF",
            modifier = Modifier
                .padding(horizontal = 30.dp, vertical = 16.dp)
                .fillMaxWidth(),
            color = Color.White,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold
        )
//        MySearchBar(
//            placeHolder = "Search",
//            onQueryChanged = { query -> searchQuery.value = query }
//        )
        Spacer(modifier = Modifier.height(20.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .background(Color.Black)
                .padding(top = 12.dp, end = 12.dp, bottom = 12.dp, start = 50.dp)
                .fillMaxHeight()
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            itemsIndexed(RecentList) {index,recentUI->
                SingleRecent(modifier=Modifier,
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
            }
        }
    }

}

@Composable
fun GridOfRowOfRecentWebsites(webHistoryViewModel: WebHistoryViewModel) {
    val websiteCounts by webHistoryViewModel.websiteCounts.observeAsState(emptyList())
    var index = 0

    Column(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Recent Websites",
            modifier = Modifier
                .padding(horizontal = 30.dp, vertical = 16.dp)
                .fillMaxWidth(),
            color = Color.White,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold
        )
//        MySearchBar(
//            placeHolder = "Search",
//            onQueryChanged = { query -> searchQuery.value = query }
//        )
        Spacer(modifier = Modifier.height(20.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .background(Color.Black)
                .padding(top = 12.dp, end = 12.dp, bottom = 12.dp, start = 50.dp)
                .fillMaxHeight()
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            this.items(websiteCounts) { recentUI  ->
                SingleRecent(modifier=Modifier,
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