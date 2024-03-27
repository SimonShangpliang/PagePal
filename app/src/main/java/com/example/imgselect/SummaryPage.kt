package com.example.imgselect

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.example.imgselect.data.Summary
import com.example.imgselect.model.SummaryViewModel
import com.example.imgselect.ui.theme.backgroundcolor
import com.example.imgselect.ui.theme.lightblue
import com.example.imgselect.ui.theme.lighterPurple
import com.example.imgselect.ui.theme.lighterYellow
import java.nio.file.Files.delete
import kotlin.random.Random

enum class SortOrder { Ascending, Descending }
enum class SearchBy { Date, Topic , All }

@Composable
fun SummaryScreen(summaryList: LiveData<List<Summary>>, navController: NavController , summaryViewModel: SummaryViewModel , goToSummaryListPage: (Summary) -> Unit){

    val summarylist by summaryList.observeAsState(initial  = emptyList())
    Log.d("SummaryList" , "${summaryList}")
    val colors = listOf(lightblue, lighterPurple, lighterYellow)
    val searchQuery = remember { mutableStateOf("")}
    val sortOrder = remember { mutableStateOf(SortOrder.Ascending) }
    val searchBy = remember { mutableStateOf(SearchBy.All) }
    val sortedAndFilteredSummaries = summarylist
        // First, apply the search filter
        .filter { summary ->
            if (searchQuery.value.isEmpty()) true
            else {
                when (searchBy.value) {
                    SearchBy.Date -> summary.time.contains(searchQuery.value, ignoreCase = true)
                    SearchBy.Topic -> summary.title.contains(searchQuery.value, ignoreCase = true)
                    else -> summary.summary?.contains(searchQuery.value , ignoreCase = true)?: false || summary.title.contains(searchQuery.value, ignoreCase = true)||summary.time.contains(searchQuery.value, ignoreCase = true)
                }
            }
        }
        // Then, apply the sorting
        .let { list ->
            when (sortOrder.value) {
                SortOrder.Ascending -> list.sortedBy { it.time }
                SortOrder.Descending -> list.sortedByDescending { it.time }
            }
        }

    Surface(

        modifier = Modifier.fillMaxSize(),
        color = Color(0xff1E1E1E)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                //.verticalScroll(rememberScrollState())
        ){
            HeadlineText()
            Spacer(modifier = Modifier.size(14.dp))
            MySearchBar(
                placeHolder ="Search date,title",
                onQueryChanged = {query-> searchQuery.value = query}
            )
            Spacer(modifier = Modifier.size(20.dp))
            SortAndSearch(
                onSortSelected = { selectedSortOrder ->
                    sortOrder.value = selectedSortOrder
                },
                onSearchBySelected = { selectedSearchBy ->
                    searchBy.value = selectedSearchBy
                }
            )
            Spacer(modifier = Modifier.size(20.dp))
//            summaryCard(cardColor = Purple80)
//            Spacer(modifier = Modifier.size(16.81.dp))
//            summaryCard(cardColor = lightblue)
//            Spacer(modifier = Modifier.size(16.81.dp))
//            summaryCard(cardColor = Purple80)
//            Spacer(modifier = Modifier.size(16.81.dp))
            LazyColumn {
//                items(summarylist) {summary ->
//                    summary?.let {
//                        summaryCard(
//                            summary = it,
//                            delete = {summaryViewModel.deleteSummary(summary)},
//                            goToSummaryListPage = {goToSummaryListPage(summary)},
//                            color =
//                        )
//                        if(summarylist.indexOf(summary) == summarylist.size-1) {
//                            Spacer(modifier = Modifier.size(80.dp))
//                        }
//                        Spacer(modifier = Modifier.size(20.dp))
//                    }
//                }

                itemsIndexed(sortedAndFilteredSummaries) {index,summary ->
                    summaryCard(
                            summary = summary,
                            delete = {summaryViewModel.deleteSummary(summary)},
                            goToSummaryListPage = {goToSummaryListPage(summary)},
                            color = colors[index % colors.size]
                        )
                    if(sortedAndFilteredSummaries.indexOf(summary) == summarylist.size-1) {
                            Spacer(modifier = Modifier.size(80.dp))
                        }
                        Spacer(modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}
@Composable
fun HeadlineText(){
    Column(modifier = Modifier.padding(vertical = 15.dp, horizontal = 24.dp)) {
        Text(text = "Summary",
            color= Color.White,
            fontSize = 34.sp,

            fontStyle = FontStyle.Normal,
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySearchBar(
    modifier : Modifier = Modifier,
    placeHolder : String,
    onQueryChanged: (String) -> Unit,
    cornerRadius: Float = 30f
){

    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    )
    {


        var text= remember {
            mutableStateOf("")
        }
        OutlinedTextField(
            value = text.value,
            onValueChange = {
                text.value= it
                onQueryChanged(it)
            }, modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                .padding(horizontal = 20.dp)
                .border(0.5.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(10.dp)),
            placeholder = {
                Text(
                    text = placeHolder,
                    color = Color.White.copy(alpha = 0.5f)
                )
            },
            colors = TextFieldDefaults.textFieldColors(
               textColor = Color.White,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                placeholderColor = Color.White.copy(alpha = 0.5f)
            ),

            trailingIcon = {
                IconButton(onClick = { }) {
                    Icon(painter = painterResource(id = R.drawable.search), contentDescription ="Search" )
                }
            },
            shape = RoundedCornerShape(10.dp)
        )
    }
}
@Composable
fun SortAndSearch(onSortSelected: (SortOrder) -> Unit , onSearchBySelected:(SearchBy) -> Unit){
    var isSortDialogOpen by remember { mutableStateOf(false) }
    var isSearchByDialogOpen by remember { mutableStateOf(false) }
    var isAscending by remember { mutableStateOf(false) }
    var isDecending by remember { mutableStateOf(false) }
    var isDate by remember { mutableStateOf(false) }
    var isTopic by remember { mutableStateOf(false) }
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween) {
        Button(onClick = { isSortDialogOpen = true  },
            modifier = Modifier
                .height(40.dp),colors=ButtonDefaults.buttonColors(containerColor = Color.Black, contentColor = Color.White)) {
            Text(text = " Sort By ")
        }
        Button(onClick = {isSearchByDialogOpen = true },colors=ButtonDefaults.buttonColors(containerColor = Color.Black, contentColor = Color.White),
                        modifier = Modifier
                .height(40.dp)) {
            Text(text = "Search By")
        }
    }
    if (isSortDialogOpen){
        AlertDialog(onDismissRequest = {isSortDialogOpen = false}, confirmButton = { Button(
            onClick = {
                isAscending = !isAscending
                isDecending = false // Make sure to reset the other flag
                onSortSelected(if (isAscending) SortOrder.Ascending else SortOrder.Descending)
                isSortDialogOpen = false },
            modifier = Modifier
                .height(32.dp),
            colors = if(isAscending) ButtonDefaults.buttonColors(Color.Green) else ButtonDefaults.buttonColors()
        ) {
            Row {
                Icon(painter = painterResource(id = R.drawable.vector__1_), contentDescription = "Book")
                Spacer(modifier = Modifier.size(4.dp))
                Text(text = "Ascending")
            }

        } }
            ,dismissButton = { Button(
                onClick = {
                    isDecending = !isDecending
                    isAscending = false // Reset the other flag
                    onSortSelected(if (isDecending) SortOrder.Descending else SortOrder.Ascending)
                    isSortDialogOpen = false},
                modifier = Modifier
                    .height(32.dp),
                colors = if(isDecending) ButtonDefaults.buttonColors(Color.Green) else ButtonDefaults.buttonColors()
            ) {
                Row {
                    Icon(painter = painterResource(id = R.drawable.vector__1_), contentDescription = "Book")
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(text = "Descending")
                }

            }}, title = {Text(text = "Sort By")},
            containerColor =Color(0xff1E1E1E),
            titleContentColor = Color.White,
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            ),
            modifier = Modifier.height(IntrinsicSize.Max).width(IntrinsicSize.Max)
        )
    }
    if (isSearchByDialogOpen){
        AlertDialog(onDismissRequest = { isSearchByDialogOpen = false},
            confirmButton = { Button(
            onClick = {
                isDate = !isDate
                isTopic = false // Reset the other flag
                onSearchBySelected(if (isDate) SearchBy.Date else SearchBy.All)
                isSearchByDialogOpen = false},
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .height(32.dp),
                colors = if(isDate) ButtonDefaults.buttonColors(Color.Green) else ButtonDefaults.buttonColors()

        ) {
            Row {
                Icon(painter = painterResource(id = R.drawable.vector__1_), contentDescription = "Book")
                Spacer(modifier = Modifier.size(4.dp))
                Text(text = "Date ")
            }

        } }
            , dismissButton = { Button(
                onClick = {
                    isTopic = !isTopic
                    isDate = false // Reset the other flag
                    onSearchBySelected(if (isTopic) SearchBy.Topic else SearchBy.All)
                    isSearchByDialogOpen = false},
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .height(32.dp),
                colors = if(isTopic) ButtonDefaults.buttonColors(Color.Green) else ButtonDefaults.buttonColors()
            ) {
                Row {
                    Icon(painter = painterResource(id = R.drawable.vector__1_), contentDescription = "Book")
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(text = "Topic")
                }

            }}, title = {Text(text = "Search By")},
            containerColor = Color(0xff1E1E1E),
            titleContentColor = Color.White,
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            ),
            modifier = Modifier.height(IntrinsicSize.Max).width(IntrinsicSize.Max)
        )

    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun summaryCard(summary: Summary, delete:() -> Unit , goToSummaryListPage: (Summary)-> Unit , color: Color){
//    val colors = listOf(lightblue , lighterPurple , lighterYellow)
//    val randomColor = colors[Random.nextInt(colors.size)]
    var isSelected  = remember {
        mutableStateOf(false)
    }
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            //.height(197.dp)
        ,
        colors = CardDefaults.cardColors(
            containerColor = color),
        onClick = {
            !isSelected.value
            goToSummaryListPage(summary)
        }
    ) {
        Column (modifier = Modifier.padding(horizontal = 27.dp, vertical = 24.dp)){
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Absolute.SpaceBetween) {
                Text(text = summary.title, fontSize = 24.sp, fontWeight = FontWeight.Medium , color = Color.Black)
                Row(horizontalArrangement = Arrangement.Absolute.SpaceBetween , modifier = Modifier.width(70.dp)) {
                    Icon(painter = painterResource(id = R.drawable.generic), contentDescription = "Summary type" , tint = Color.Black , modifier = Modifier.size(30.dp))
                    Icon(painter = painterResource(id = R.drawable.baseline_delete_24), contentDescription = "delete" , tint = Color.Black , modifier = Modifier
                        .clickable { delete() }
                        .size(30.dp))
                }
            }

            Spacer(modifier = Modifier.height(39.dp))
            if(isSelected.value) {
                summary.summary?.let {
                    Text(
                        text = it,
                        color = Color.Black
                    )
                }
            } else {
                summary.summary?.let {
                    Text(
                        text = it,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End){
                Text(text = summary.time, fontSize = 12.sp, fontWeight = FontWeight.Medium , color = Color.Black)
            }
        }

    }
}

@Composable
fun SummaryListPage(summary: Summary) {

    Log.d("SummaryIsWhat" , "${summary}")

    Card(
        colors = CardDefaults.cardColors(lighterYellow),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = summary.title,
                modifier = Modifier.padding(16.dp),
                color = Color.Black,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = summary.time,
                modifier = Modifier.padding(16.dp),
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        summary?.summary?.let {
            Text(
                text = it,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                color = Color.DarkGray,
                fontSize = 16.sp
            )
        }
    }

}




