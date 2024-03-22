package com.example.imgselect

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.imgselect.DictionaryNetwork.WordData

@OptIn(ExperimentalTextApi::class)
@Composable
fun WordMeaningDialog(
    setShowDialog: (Boolean) -> Unit,
    listMeaning:List<WordData>?,
    onResponse: (String) -> Unit,

    onButton: (Boolean) -> Unit
) {
    val gradientBrush = Brush.linearGradient(
        colors = listOf(Color.Green, Color.Cyan)

    )

    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.height(700.dp),
            color = Color.White
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                val scroll = rememberScrollState()
                Column(
                    modifier = Modifier
                        .background(Color(0xff1E1E1E))
                        .fillMaxHeight(0.88f)
                        .fillMaxWidth()
                        .verticalScroll(scroll)
                ) {
                    Text(
                        text = "Word Meaning",
                        style = TextStyle(
                            fontFamily = MaterialTheme.typography.titleSmall.fontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp),
                        color = Color.White,

                        modifier = Modifier
                            .padding(20.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Text(
                        text = listMeaning?.getOrNull(0)?.word ?: "No Word",
                        style = TextStyle(
                            fontFamily = MaterialTheme.typography.titleSmall.fontFamily,
                            fontSize = 23.sp
                        ),
                        color = Color.White,
                        modifier = Modifier
                            .padding(20.dp)
                            .align(Alignment.CenterHorizontally)
                    )

                    listMeaning?.forEach { wordData ->
                        Column(modifier = Modifier.fillMaxWidth()) {
                            wordData.meanings.groupBy { it.partOfSpeech }?.forEach { (partOfSpeech, meanings) ->
                                Text(
                                    text = partOfSpeech ?: "Unknown",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = Color.White,
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                        .background(Color.DarkGray)
                                )
                                meanings.forEach { meaning ->
                                    meaning.definitions.forEach { definition ->
                                        Text(
                                            text = "Definition: ${definition.definition}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.White,
                                            modifier = Modifier.padding(16.dp)
                                        )
                                        definition.example?.let {
                                            Text(
                                                text = "Example: $it",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color.White,
                                                modifier = Modifier.padding(horizontal = 16.dp)
                                            )
                                        }
                                        if (!definition.synonyms.isNullOrEmpty()) {
                                            Text(
                                                text = "Synonyms: ${definition.synonyms.joinToString()}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color.White,
                                                modifier = Modifier.padding(horizontal = 16.dp)
                                            )
                                        }
                                        if (!definition.antonyms.isNullOrEmpty()) {
                                            Text(
                                                text = "Antonyms: ${definition.antonyms.joinToString()}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color.White,
                                                modifier = Modifier.padding(horizontal = 16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                }



                Box(
                    modifier = Modifier
                        .background(Color(0xff141414))
                        .fillMaxSize()
                ) {
                    Button(
                        onClick = {

                            setShowDialog(false)
//                            sharedPreferences.edit()
//                                .putString("OpenStatus", "done")
//                                .apply()
                            onResponse("done")
                        },
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            // Change the background color here
                            contentColor = Color.White,
                            containerColor = Color.Green// Change the text color here
                        ),
                        modifier = Modifier
                            .height(50.dp)
                            .align(Alignment.Center)

                    ) {
                        Text(
                            text = "Done", color = Color.Black,
                        )
                    }
                }
            }
        }
    }
}



@OptIn(ExperimentalTextApi::class)
@Composable
fun SummaryDialog(
    setShowDialog: (Boolean) -> Unit,
    Summary:String) {
    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.height(700.dp),
            color = Color.White
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                val scroll = rememberScrollState()
                Column(
                    modifier = Modifier
                        .background(Color(0xff1E1E1E))
                        .fillMaxHeight(0.88f)
                        .fillMaxWidth()
                        .verticalScroll(scroll)
                ) {
                    Text(
                        text = "Summary",
                        style = TextStyle(
                            fontFamily = MaterialTheme.typography.titleSmall.fontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp),
                        color = Color.White,

                        modifier = Modifier
                            .padding(20.dp)
                            .align(Alignment.CenterHorizontally)
                    )

                    Text(
                        text = Summary,
                        style = TextStyle(
                            fontFamily = MaterialTheme.typography.titleSmall.fontFamily,
                            fontSize = 18.sp),
                        color = Color.White,

                        modifier = Modifier
                            .padding(20.dp)
                            .align(Alignment.CenterHorizontally)
                    )



                }



                Box(
                    modifier = Modifier
                        .background(Color(0xff141414))
                        .fillMaxSize()
                ) {
                    Button(
                        onClick = {

                            setShowDialog(false)

                        },
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            // Change the background color here
                            contentColor = Color.White,
                            containerColor = Color.Green// Change the text color here
                        ),
                        modifier = Modifier
                            .height(50.dp)
                            .align(Alignment.Center)

                    ) {
                        Text(
                            text = "Done", color = Color.Black,
                        )
                    }
                }
            }
        }
    }
}




