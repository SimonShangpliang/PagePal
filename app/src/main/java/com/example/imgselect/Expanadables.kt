package com.example.imgselect

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ExpandableItem2(
    question: String,

    answer: String,
) {
    var expanded by remember { mutableStateOf(false) }
    val icon = if (expanded) {
        Icons.Filled.KeyboardArrowUp
    } else {
        Icons.Filled.KeyboardArrowDown
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { expanded = !expanded }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = question,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Default

                    ),
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(0.9f)

                )
                Icon(icon,contentDescription = null,modifier= Modifier.size(20.dp),tint= Color.White)
            }
            if (expanded) {
                Box(modifier= Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Black)){
                    Text(
                        text = answer,
                        style = MaterialTheme.typography.titleSmall.copy(color = Color.White, fontSize = 16.sp),

                        color = Color.White,
                        modifier= Modifier.padding(start=15.dp,end=15.dp,top=10.dp, bottom = 10.dp)
                    )
                }
            }
        }
    }
    Divider(modifier= Modifier
        //padding(start = 20.dp, end = 20.dp, top = 15.dp, bottom = 15.dp)
        .height(2.dp),color= Color.Green)
}


@Composable
fun ExpandableItem1(
    question: String,
    words:List<String>,
    answer: String,
    imagePainter: Painter,
    imageContentDescription: String
) {
    var expanded by remember { mutableStateOf(false) }
    val icon = if (expanded) {
        Icons.Filled.KeyboardArrowUp
    } else {
        Icons.Filled.KeyboardArrowDown
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { expanded = !expanded }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                //    contentPadding = PaddingValues(16.dp)
            ) {
                Text(
                    text = question,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Default

                    ),
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(0.9f)
                )
                Icon(icon,contentDescription = null,modifier= Modifier.size(20.dp),tint= Color.White)

            }
            if (expanded) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(modifier= Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Black)){
                    Column(horizontalAlignment = Alignment.CenterHorizontally){
                        Spacer(modifier = Modifier.height(8.dp))

                        Image(
                            painter = imagePainter,
                            contentDescription = imageContentDescription,
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .clip(RoundedCornerShape(10.dp))
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = answer,
                            style = MaterialTheme.typography.titleSmall.copy(color = Color.White, fontSize = 16.sp),

                            color = Color.White,
                            modifier= Modifier.padding(start=10.dp,end=10.dp,top=10.dp, bottom = 10.dp)
                        )
                    }
                }
            }
        }
    }
    Divider(modifier= Modifier
        .padding(start = 20.dp, end = 20.dp, top = 15.dp, bottom = 15.dp)
        .height(2.dp),color= Color.Green)


}

