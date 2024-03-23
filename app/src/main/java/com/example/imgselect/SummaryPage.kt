package com.example.imgselect

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import com.example.imgselect.ui.theme.lightblue
import com.example.imgselect.ui.theme.lighterPurple
import com.example.imgselect.ui.theme.lighterYellow
import kotlin.random.Random

@Composable
fun SummaryScreen(summaryList: LiveData<List<Summary>>, navController: NavController , summaryViewModel: SummaryViewModel){

    val summarylist by summaryList.observeAsState(initial  = emptyList())
    Log.d("SummaryList" , "${summaryList}")
    Surface(

        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                //.verticalScroll(rememberScrollState())
        ){
            HeadlineText()
            Spacer(modifier = Modifier.size(14.dp))
            MySearchBar( placeHolder ="Search date,title" )
            Spacer(modifier = Modifier.size(20.dp))
            SortAndSearch()
            Spacer(modifier = Modifier.size(20.dp))
//            summaryCard(cardColor = Purple80)
//            Spacer(modifier = Modifier.size(16.81.dp))
//            summaryCard(cardColor = lightblue)
//            Spacer(modifier = Modifier.size(16.81.dp))
//            summaryCard(cardColor = Purple80)
//            Spacer(modifier = Modifier.size(16.81.dp))
            LazyColumn {
                items(summarylist) {summary ->
                    summary.summary?.let {
                        summaryCard(
                            title = summary.title ,
                            date = summary.time ,
                            summary = it,
                            delete = {summaryViewModel.deleteSummary(summary)}
                        )
                        if(summarylist.indexOf(summary) == summarylist.size-1) {
                            Spacer(modifier = Modifier.size(80.dp))
                        }
                        Spacer(modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}
@Composable
fun HeadlineText(){
    Column(modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)) {
        Text(text = "Summary",
            color= Color.White,
            fontSize = 40.sp,
            fontStyle = FontStyle.Normal,
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySearchBar(
    modifier : Modifier = Modifier,
    placeHolder : String,
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
            }, modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color.Black, RoundedCornerShape(24.dp))
                .padding(horizontal = 20.dp)
                .border(1.dp, Color.White, RoundedCornerShape(20.dp)),
            placeholder = {
                Text(
                    text = placeHolder
                )
            },
            colors = TextFieldDefaults.textFieldColors(
               textColor = Color.White
            ),

            trailingIcon = {
                IconButton(onClick = { }) {
                    Icon(painter = painterResource(id = R.drawable.search), contentDescription ="Search" )
                }
            },
        )
    }
}
@Composable
fun SortAndSearch(){
    var isDialog by remember {
        mutableStateOf(value = false)
    }
    var OnDialog by remember {
        mutableStateOf(value = false)
    }
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween) {
        Button(onClick = { isDialog=true },
            modifier = Modifier
                .height(32.dp)) {
            Text(text = " Sort By ")
        }
        Button(onClick = {OnDialog = true },
                        modifier = Modifier
                .height(32.dp)) {
            Text(text = "Search By")
        }
    }
    if (isDialog){
        AlertDialog(onDismissRequest = { isDialog=false}, confirmButton = { Button(
            onClick = {isDialog=false },
            modifier = Modifier
                .height(32.dp)
        ) {
            Row {
                Icon(painter = painterResource(id = R.drawable.vector__1_), contentDescription = "Book")
                Spacer(modifier = Modifier.size(4.dp))
                Text(text = "Ascending")
            }

        } }
            ,dismissButton = { Button(
                onClick = { isDialog=false},
                modifier = Modifier
                    .height(32.dp)
            ) {
                Row {
                    Icon(painter = painterResource(id = R.drawable.vector__1_), contentDescription = "Book")
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(text = "Descending")
                }

            }}, title = {Text(text = "Sort By")},
            containerColor = Color.Black,
            titleContentColor = Color.White,
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            ),
            modifier = Modifier.size(320.dp,140.dp)
        )
    }
    if (OnDialog){
        AlertDialog(onDismissRequest = { OnDialog=false}, confirmButton = { Button(
            onClick = {OnDialog=false },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .height(32.dp)

        ) {
            Row {
                Icon(painter = painterResource(id = R.drawable.vector__1_), contentDescription = "Book")
                Spacer(modifier = Modifier.size(4.dp))
                Text(text = "Date ")
            }

        } }
            , dismissButton = { Button(
                onClick = { OnDialog=false},
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .height(32.dp)
            ) {
                Row {
                    Icon(painter = painterResource(id = R.drawable.vector__1_), contentDescription = "Book")
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(text = "Topic")
                }

            }}, title = {Text(text = "Search By")},
            containerColor = Color.Black,
            titleContentColor = Color.White,
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            ),
            modifier = Modifier.size(320.dp,140.dp)
        )

    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun summaryCard(title: String, date: String, summary: String, delete:() -> Unit){
    val colors = listOf(lightblue , lighterPurple , lighterYellow)
    val randomColor = colors[Random.nextInt(colors.size)]
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
            containerColor = randomColor),
        onClick = {!isSelected.value}
    ) {
        Column (modifier = Modifier.padding(horizontal = 27.dp, vertical = 24.dp)){
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Absolute.SpaceBetween) {
                Text(text = title, fontSize = 24.sp, fontWeight = FontWeight.Medium , color = Color.Black)
                Row(horizontalArrangement = Arrangement.Absolute.SpaceBetween , modifier = Modifier.width(70.dp)) {
                    Icon(painter = painterResource(id = R.drawable.generic), contentDescription = "Summary type" , tint = Color.Black , modifier = Modifier.size(30.dp))
                    Icon(painter = painterResource(id = R.drawable.baseline_delete_24), contentDescription = "delete" , tint = Color.Black , modifier = Modifier
                        .clickable { delete() }
                        .size(30.dp))
                }
            }

            Spacer(modifier = Modifier.height(39.dp))
            if(isSelected.value) {
                Text(
                    text = summary,
                    color = Color.Black
                )
            } else {
                Text(
                    text = summary,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(28.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End){
                Text(text = date, fontSize = 12.sp, fontWeight = FontWeight.Medium , color = Color.Black)
            }
        }

    }
}




