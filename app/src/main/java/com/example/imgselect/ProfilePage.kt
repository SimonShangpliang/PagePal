package com.example.imgselect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.imgselect.ui.theme.Purple80
import com.example.imgselect.ui.theme.PurpleGrey80
import com.example.imgselect.ui.theme.interestcolour
import com.example.imgselect.ui.theme.profileborder


@Composable
fun ProfileScreen(navController: NavController) {
    Surface(modifier = Modifier.fillMaxSize(),
        color = Color.Black) {
        Column {
            HeadlineText(text = "Profile")
            Spacer(modifier = Modifier.size(28.dp))
            RoundImage(
                image = painterResource(id = R.drawable.ic_launcher_background), modifier = Modifier
                    .height(82.dp)
                    .width(82.dp)
            )
            Spacer(modifier = Modifier.size(12.dp))
            Introdution()
            Spacer(modifier = Modifier.size(72.dp))
            Interests()

        }
    }}







@Composable
fun HeadlineText(text: String) {
    Column(modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 32.sp,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun RoundImage(
    image: Painter,
    modifier: Modifier
) {

    Row(modifier = Modifier.padding(horizontal = 20.dp)) {
        Box {
            Image(
                painter = image, contentDescription = null,
                modifier = modifier
                    .aspectRatio(1f, matchHeightConstraintsFirst = true)
                    .border(1.4.dp, color = profileborder, shape = CircleShape)
                    .padding(4.dp)
                    .clip(CircleShape)
            )
        }
    }

}



@Composable
fun Introdution() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .height(41.dp)
            .padding(horizontal = 20.dp), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.height(41.dp)) {
            Text(
                text = "Mayank Choudhary",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "#interests",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }
        OutlinedButton(
            onClick = { /*TODO*/ }, modifier = Modifier
                .height(42.dp)
                .background(Color.Black)
        ) {
            Text(
                text = "Edit Profile",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }
    }
}

@Composable
fun Interests() {
    var interests = mutableListOf<String>()


    Card(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .height(418.dp),
        colors = CardDefaults.cardColors(
            containerColor = interestcolour)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 21.dp, vertical = 21.dp),

            ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp),
                    horizontalArrangement = Arrangement.Absolute.Center,
                ) {
                    InterestButton(name = "Fiction", selected = interests.contains("Fiction"))
                    InterestButton(name = "Novel", selected = interests.contains("Novel"))
                    InterestButton(name = "Narrative", selected = interests.contains("Narrative"))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp),
                    horizontalArrangement = Arrangement.Absolute.Center,
                ) {
                    InterestButton(
                        name = "Historical Fiction",
                        selected = interests.contains("Historical Fiction")
                    )
                    InterestButton(
                        name = "Non Fiction",
                        selected = interests.contains("Non Fiction")
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp),
                    horizontalArrangement = Arrangement.Absolute.Center,
                ) {
                    InterestButton(name = "Romantic", selected = interests.contains("Romantic"))
                    InterestButton(name = "Biography", selected = interests.contains("Biography"))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp),
                    horizontalArrangement = Arrangement.Absolute.Center,
                ) {
                    InterestButton(
                        name = "Childern's literature",
                        selected = interests.contains("Childern's literature")
                    )
                    InterestButton(name = "Thriller", selected = interests.contains("Thriller"))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp),
                    horizontalArrangement = Arrangement.Absolute.Center,
                ) {
                    InterestButton(name = "Sci-Fi", selected = interests.contains("Sci-Fi"))
                    InterestButton(name = "Mystery", selected = interests.contains("Mystery"))
                    InterestButton(name = "History", selected = interests.contains("History"))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp),
                    horizontalArrangement = Arrangement.Absolute.Center,
                ) {
                    InterestButton(name = "Poetry", selected = interests.contains("Poetry"))
                    InterestButton(name = "Horror", selected = interests.contains("Horror"))
                    InterestButton(name = "Crime", selected = interests.contains("Crime"))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp),
                    horizontalArrangement = Arrangement.Absolute.Center,
                ) {
                    InterestButton(
                        name = "AutoBiography",
                        selected = interests.contains("AutoBiography")
                    )
                    InterestButton(name = "Cookbook", selected = interests.contains("Cookbook"))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier
                .fillMaxWidth()
                .height(18.dp)) {
                Text(
                    text = "Show More",
                    modifier = Modifier.clickable(onClick = {}),
                    color = PurpleGrey80,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InterestButton(name: String, selected: Boolean) {
    var _selected by remember {
        mutableStateOf(selected)
    }

    var ran = (1..2).random()
    val bb = if (_selected) {
        if (ran == 1) PurpleGrey80 else PurpleGrey80
    } else Color.Transparent

    Box(
        modifier = Modifier
            .padding(horizontal = 1.dp)
            .border(1.dp, Color.Black, shape = CircleShape)
            .background(
                bb,
                RoundedCornerShape(20.dp)
            )
            .clickable {
                if (_selected) {
                    _selected = false

                } else {
                    _selected = true
                }
            }
            .padding(vertical = 5.dp)
    ) {
        Row(modifier = Modifier.padding(horizontal = 2.dp), horizontalArrangement = Arrangement.Center) {
            Text(
                text = "   $name   ",
                fontSize = 18.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .basicMarquee(
                        iterations = 100
                    ),
                color = Color.White
            )
            if (_selected){
                Icon(
                    painter = painterResource(id = R.drawable.checked),
                    contentDescription = null,)
            }
            else{
                Icon(
                    painter = painterResource(id = R.drawable.uil_plus_circle),
                    contentDescription = null,)
            }

        }
    }
}





