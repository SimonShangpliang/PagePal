package com.example.imgselect

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.example.imgselect.model.PhotoTakenViewModel
import com.example.imgselect.ui.theme.Purple80
import com.example.imgselect.ui.theme.PurpleGrey80
import com.example.imgselect.ui.theme.backgroundcolor
import com.example.imgselect.ui.theme.interestcolour
import com.example.imgselect.ui.theme.profileborder


@Composable
fun ProfileScreen(navController: NavController) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = backgroundcolor)
        ) {
            HeadlineText(text = "Profile")
            Spacer(modifier = Modifier.size(28.dp))

            Introdution()
            Spacer(modifier = Modifier.size(72.dp))
            Interests()
        }
    }







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
    modifier: Modifier,
    color: Color,
    borderWidth: Float

) {


    Row(modifier = Modifier.padding(horizontal = 20.dp)) {
        Box {
            Image(
                painter = image, contentDescription = null,
                modifier = modifier
                    .aspectRatio(1f, matchHeightConstraintsFirst = true)
                    .border(borderWidth.dp, color = color, shape = CircleShape)
                    .padding(4.dp)
                    .clip(CircleShape))
                    }}

        }




@Composable
fun Introdution() {
    var showDialog by remember { mutableStateOf(false) }
    val context: Context = LocalContext.current
    val Sharedpreferences = remember {
        ProfileData(context)
    }
    var name= remember {
        mutableStateOf(Sharedpreferences.name)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundcolor)
            .height(41.dp)
            .padding(horizontal = 20.dp), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.height(41.dp)) {
            Text(
                text =name.value ,
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
            onClick = { showDialog=true }, modifier = Modifier
                .height(42.dp)
                .background(backgroundcolor)
        ) {
            Text(
                text = "Edit Username",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            if (showDialog){
                Dialog(onDismissRequest = { showDialog = false }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .background(
                                backgroundcolor,
                                RoundedCornerShape(4.dp)
                            )
                            .border(
                                1.dp,
                                MaterialTheme.colors.onBackground,
                                RoundedCornerShape(8.dp)
                            )
                            .padding(20.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        Column {
                            androidx.compose.material.Text(
                                text = "Username:",
                                color = Color.White,
                                fontSize = 18.sp,

                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            OutlinedTextField(
                                value =name.value ,
                                onValueChange = { name.value = it },
                                modifier = Modifier
                                    .border(1.dp, Color.White)
                                    .fillMaxWidth()
                                    .height(48.dp),
                                textStyle = TextStyle(
                                    color = Color.White
                                )
                            )
                            Spacer(modifier = Modifier.height(15.dp))

                            androidx.compose.material.Button(onClick = {
                               Sharedpreferences.name=name.value
                                showDialog = false
                            }) {
                                androidx.compose.material.Text(text = "Save")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Interests() {
    val context= LocalContext.current
    var interest =remember{ProfileData(context)}
   var interests=interest.getInterests()
    var count by remember{ mutableStateOf(0) }
    LaunchedEffect(count)
    {
        interests=interest.getInterests()
    }
    Card(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .height(440.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0x70424242))
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
                        .height(50.dp),
                    horizontalArrangement = Arrangement.Absolute.Center,
                ) {
                    InterestButton({action,name->
                        if(action=="add"){
                                   interest.addInterest(name)
                        count++}
                        else
                        {
                            count++
                            interest.removeInterest(name)
                        }
                    },name = "Fiction", _selected = interests.contains("Fiction"))
                    InterestButton({action,name->
                        if(action=="add"){
                            interest.addInterest(name)
                            count++}
                        else
                        {
                            count++
                            interest.removeInterest(name)
                        }
                    },name = "Novel", _selected = interests.contains("Novel"))
                    InterestButton({action,name->
                        if(action=="add"){
                            interest.addInterest(name)
                            count++}
                        else
                        {
                            count++
                            interest.removeInterest(name)
                        }
                    },name = "Narrative", _selected = interests.contains("Narrative"))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    horizontalArrangement = Arrangement.Absolute.Center,
                ) {
                    InterestButton({action,name->
                        if(action=="add"){
                            interest.addInterest(name)
                            count++}
                        else
                        {
                            count++
                            interest.removeInterest(name)
                        }
                    },
                        name = "Historical Fiction",
                        _selected = interests.contains("Historical Fiction")
                    )
                    InterestButton({action,name->
                        if(action=="add"){
                            interest.addInterest(name)
                            count++}
                        else
                        {
                            count++
                            interest.removeInterest(name)
                        }
                    },
                        name = "Non Fiction",
                        _selected = interests.contains("Non Fiction")
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    horizontalArrangement = Arrangement.Absolute.Center,
                ) {
                    InterestButton({action,name->
                        if(action=="add"){
                            interest.addInterest(name)
                            count++}
                        else
                        {
                            count++
                            interest.removeInterest(name)
                        }
                    },name = "Romantic", _selected = interests.contains("Romantic"))
                    InterestButton({action,name->
                        if(action=="add"){
                            interest.addInterest(name)
                            count++}
                        else
                        {
                            count++
                            interest.removeInterest(name)
                        }
                    },name = "Biography", _selected = interests.contains("Biography"))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    horizontalArrangement = Arrangement.Absolute.Center,
                ) {
                    InterestButton({action,name->
                        if(action=="add"){
                            interest.addInterest(name)
                            count++}
                        else
                        {
                            count++
                            interest.removeInterest(name)
                        }
                    },
                        name = "Childern's literature",
                        _selected = interests.contains("Childern's literature")
                    )
                    InterestButton({action,name->
                        if(action=="add"){
                            interest.addInterest(name)
                            count++}
                        else
                        {
                            count++
                            interest.removeInterest(name)
                        }
                    },name = "Thriller", _selected = interests.contains("Thriller"))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    horizontalArrangement = Arrangement.Absolute.Center,
                ) {
                    InterestButton({action,name->
                        if(action=="add"){
                            interest.addInterest(name)
                            count++}
                        else
                        {
                            count++
                            interest.removeInterest(name)
                        }
                    },name = "Sci-Fi", _selected = interests.contains("Sci-Fi"))
                    InterestButton({action,name->
                        if(action=="add"){
                            interest.addInterest(name)
                            count++}
                        else
                        {
                            count++
                            interest.removeInterest(name)
                        }
                    },name = "Mystery", _selected = interests.contains("Mystery"))
                    InterestButton({action,name->
                        if(action=="add"){
                            interest.addInterest(name)
                            count++}
                        else
                        {
                            count++
                            interest.removeInterest(name)
                        }
                    },name = "History", _selected = interests.contains("History"))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    horizontalArrangement = Arrangement.Absolute.Center,
                ) {
                    InterestButton({action,name->
                        if(action=="add"){
                            interest.addInterest(name)
                            count++}
                        else
                        {
                            count++
                            interest.removeInterest(name)
                        }
                    },name = "Poetry", _selected = interests.contains("Poetry"))
                    InterestButton({action,name->
                        if(action=="add"){
                            interest.addInterest(name)
                            count++}
                        else
                        {
                            count++
                            interest.removeInterest(name)
                        }
                    },name = "Horror", _selected = interests.contains("Horror"))
                    InterestButton({action,name->
                        if(action=="add"){
                            interest.addInterest(name)
                            count++}
                        else
                        {
                            count++
                            interest.removeInterest(name)
                        }
                    },name = "Crime", _selected = interests.contains("Crime"))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    horizontalArrangement = Arrangement.Absolute.Center,
                ) {
                    InterestButton({action,name->
                        if(action=="add"){
                            interest.addInterest(name)
                            count++}
                        else
                        {
                            count++
                            interest.removeInterest(name)
                        }
                    },
                        name = "AutoBiography",
                        _selected = interests.contains("AutoBiography")
                    )
                    InterestButton({action,name->
                        if(action=="add"){
                            interest.addInterest(name)
                            count++}
                        else
                        {
                            count++
                            interest.removeInterest(name)
                        }
                    },name = "Cookbook", _selected = interests.contains("Cookbook"))
                }
            }


        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InterestButton(onClick:(String,String)->Unit,name: String, _selected: Boolean) {

    var ran = (1..2).random()
    val bb = if (_selected) {
        if (ran == 1) PurpleGrey80 else PurpleGrey80
    } else Color.Transparent

    Box(
        modifier = Modifier
            .padding(horizontal = 1.dp)
            .background(
                bb,
                RoundedCornerShape(20.dp)
            )
            .border(1.dp, Color.Black, shape = RoundedCornerShape(20.dp))

            .clickable {
                if (_selected) {
                    onClick("remove", name)
                } else {
                    onClick("add", name)
                }
            }
            .padding(vertical = 5.dp)
    ) {
        Row(modifier = Modifier.padding(horizontal = 0.dp).fillMaxHeight(), horizontalArrangement =Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
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
                color = if(!_selected) Color.White else Color.Black
            )
            if (_selected){
                Icon(
                    painter = painterResource(id = R.drawable.checked),
                    contentDescription = null,tint=Color.Black)
            }
            else{
                Icon(
                    painter = painterResource(id = R.drawable.uil_plus_circle),
                    contentDescription = null)
            }

        }
    }
}





