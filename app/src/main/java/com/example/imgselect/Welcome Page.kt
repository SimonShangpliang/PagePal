package com.example.imgselect

import android.Manifest
import android.content.Context
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.navigation.NavController
import com.example.imgselect.ui.theme.Purple80
import com.example.imgselect.ui.theme.aliceBlue
import com.example.imgselect.ui.theme.backgroundcolor
import com.example.imgselect.ui.theme.lighterPurple
import com.example.imgselect.ui.theme.profileborder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun welcomeScreen(navController: NavController){
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
        Text(text = "Welcome,", fontSize = 30.sp, color = Color.White)
        Spacer(modifier = Modifier.size(5.dp))
Row(modifier=Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.Bottom
) {
        Text(
            text = "PagePal",
            modifier = Modifier.alignByBaseline(),
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
            color = lighterPurple
        )
        Text(
            text = "User",
            modifier = Modifier
                .alignByBaseline()
                .padding(start = 10.dp),
            fontSize = 30.sp,
            color = Color.White
        )

}
        Spacer(modifier = Modifier.size(28.dp))
                            val transition = rememberInfiniteTransition()

        val borderColor by transition.animateColor(label="border",
                        initialValue = lighterPurple,
                        targetValue = Purple80,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1000, easing = LinearEasing),
                            repeatMode = RepeatMode.Reverse
                        ))
        OutlinedTextField(value = usernameState.value,modifier=Modifier.border(2.dp,borderColor,MaterialTheme.shapes.extraLarge), onValueChange ={usernameState.value=it}
            , label = { Text(text ="Username" ,color= Color.White.copy(alpha = 0.5f)) },colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                textColor = Color.White

            ),
            shape = MaterialTheme.shapes.extraLarge)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
           Button(onClick = { Sharedpreferences.name=usernameState.value
                                     navController.navigate(Screen.HomeScreen.route)
                                     }, colors=ButtonDefaults.buttonColors(containerColor = lighterPurple)) {
                Text(text = "Save Info",color=Color.Black)
            }
        }
    }
}

