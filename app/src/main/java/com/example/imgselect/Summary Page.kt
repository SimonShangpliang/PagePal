package com.example.imgselect

import android.service.quicksettings.Tile
import android.widget.Button
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.material3.AlertDialogDefaults as AlertDialogDefaults

@Composable
fun SummaryScreen(){
    Surface(

        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ){
            HeadlineText()
            MySearchBar( placeHolder ="Search..." )
            SortAndSearch()
            summaryCard(cardColor = Color.Cyan)
            summaryCard(cardColor = Color.Magenta)
            summaryCard(cardColor = Color.Blue)
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
            .fillMaxWidth()
            .clip(RoundedCornerShape(cornerRadius.dp))
            .border(
                BorderStroke(
                    0.1.dp,
                    SolidColor(MaterialTheme.colorScheme.onSurface)
                ),
                RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ){


        var text= remember {
            mutableStateOf("")
        }
    OutlinedTextField(
        value = text.value,
        onValueChange = {
           text.value= it
        },
        modifier = Modifier.width(320.dp),
        placeholder = {
            Text(
                text = placeHolder
            )
        },

        trailingIcon = {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    modifier = modifier.size(22.dp)
                )
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
        .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween) {
        Button(onClick = { isDialog=true },
            modifier = Modifier
                .padding(20.dp)) {
            Text(text = " Sort By ")
        }
        Button(onClick = {OnDialog = true },
            modifier = Modifier
                .padding(20.dp)) {
            Text(text = "Search By")
        }
    }
    if (isDialog){
        AlertDialog(onDismissRequest = { isDialog=false}, confirmButton = { Button(
            onClick = {isDialog=false },
            modifier = Modifier
                .padding(6.dp)

            ) {
            Text(text = " Ascending")
        } }
            ,dismissButton = { Button(
                onClick = { isDialog=false},
                modifier = Modifier
                    .padding(6.dp)
            ) {
                Text(text = "Descending")
            }}, title = {Text(text = "Sort By")},
            containerColor = Color.Black,
            titleContentColor = Color.White
            )
    }
    if (OnDialog){
        AlertDialog(onDismissRequest = { OnDialog=false}, confirmButton = { Button(
            onClick = {OnDialog=false },
            modifier = Modifier
                .padding(20.dp)
        ) {
            Text(text = " Date")
        } }
            , dismissButton = { Button(
                onClick = { OnDialog=false},
                        modifier = Modifier
                        .padding(20.dp)
            ) {
                Text(text = "Topic")
            }}, title = {Text(text = "Search By")},
           containerColor = Color.Black,
            titleContentColor = Color.White
                   )

    }
}
@Composable
fun summaryCard(cardColor: Color
){
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(200.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor)
    ) {
        Column {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Stop digging", fontSize = 40.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "If you find you have dug yourself into o hole stap digging. If you find you have dug yourself into a hole....stop....", fontSize = 20.sp)
            }
            Row(modifier = Modifier.padding(end = 10.dp, start = 180.dp)) {
                Text(text = "Date-14-03-2024", fontSize = 20.sp)
            }
        }
    }
}




