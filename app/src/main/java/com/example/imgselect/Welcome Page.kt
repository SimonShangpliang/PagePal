package com.example.imgselect

import android.Manifest
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.imgselect.ui.theme.backgroundcolor
import com.example.imgselect.ui.theme.profileborder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun welcomeScreen(){
    val context: Context = LocalContext.current
    val Sharedpreferences = remember {
        ProfileData(context)
    }
    var usernameState = remember {
        mutableStateOf("")
    }
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundcolor)
            .padding(horizontal = 55.dp, vertical = 60.dp),
    ){
        Text(text = "Welcome,", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text(text = "PagePal User", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Spacer(modifier = Modifier.size(8.dp))
        Column {
            RoundImage(
                image = painterResource(id = R.drawable.ic_launcher_background), modifier = Modifier
                    .height(82.dp)
                    .width(82.dp)
                    .clickable {  },
                color = profileborder,
                borderWidth = 1.4f
            )
            Spacer(modifier = Modifier.size(6.dp))
            Text(text = "Profile Photo", color = Color.White , fontSize = 18.sp)
        }
        OutlinedTextField(value = usernameState.value, onValueChange ={usernameState.value=it}
            , label = { Text(text ="Username" ) })
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 104.dp), verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedButton(onClick = { Sharedpreferences.name=usernameState.value }, Modifier.background(Color.Transparent)) {
                Text(text = "Save Info")
            }
        }
    }
}

