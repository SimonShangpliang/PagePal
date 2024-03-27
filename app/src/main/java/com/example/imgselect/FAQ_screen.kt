package com.example.imgselect

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
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.smallTopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.imgselect.ui.theme.OpenSans
import com.example.imgselect.ui.theme.aliceBlue
import com.example.imgselect.ui.theme.darkBar
import com.example.imgselect.ui.theme.interestcolour1
import com.example.imgselect.ui.theme.interestcolour2
import com.example.imgselect.ui.theme.lighterTeal

@Composable
fun Expandable(
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
           // .padding(vertical = 12.dp)
            .background(color = darkBar)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(shape = RoundedCornerShape(25.dp))
                .background(interestcolour1)
                .coloredShadow(color= interestcolour2,alpha=0.5f),

            ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = question,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = OpenSans

                    ),
                    fontWeight = FontWeight.SemiBold,
                    color = lighterTeal,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(
                            start = 15.dp,
                            top = 30.dp,
                            bottom = if (!expanded) 30.dp else 10.dp
                        )

                )
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable(onClick = { expanded = !expanded })
                        .fillMaxWidth(0.2f)
                        .padding(end=5.dp)
                   ,
                    tint = lighterTeal,

                )
            }
            if (expanded) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                )
                {
                    Text(
                        text = answer,
                        fontSize = 16.sp,
                        fontFamily = OpenSans,
                        color = aliceBlue,
                        modifier = Modifier.padding(
                            start = 15.dp,
                            end = 15.dp,
                            top = 10.dp,
                            bottom = 10.dp
                        )
                    )
                }

            }

        }
    }
//    Spacer(modifier = Modifier.height(0.5.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun FAQscreen(){
    Scaffold(
        topBar = {
            TopAppBar(
                colors = smallTopAppBarColors(
                    containerColor = darkBar,
                    titleContentColor = aliceBlue,
                ),
                title = {
                    Text("F.A.Q.", textAlign = TextAlign.Center,modifier=Modifier.fillMaxWidth(),color= lighterTeal,fontFamily = OpenSans)
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
             Expandable(
                "Who is Thor the god of?",
                "In Norse mythology, he is a hammer-wielding god associated with lightning, thunder, storms, sacred groves and trees, strength, the protection of humankind, hallowing, and fertility."
            )
            Expandable(
                "Who is Thor the god of?",
                "In Norse mythology, he is a hammer-wielding god associated with lightning, thunder, storms, sacred groves and trees, strength, the protection of humankind, hallowing, and fertility."
            )
            Expandable(
                "Who is Thor the god of?",
                "In Norse mythology, he is a hammer-wielding god associated with lightning, thunder, storms, sacred groves and trees, strength, the protection of humankind, hallowing, and fertility."
            )
        }
    }
}