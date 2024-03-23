package com.example.imgselect

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
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
import com.example.imgselect.DictionaryNetwork.Definition
import com.example.imgselect.DictionaryNetwork.Meaning
import com.example.imgselect.DictionaryNetwork.WordData
import com.example.imgselect.model.SummaryViewModel

@OptIn(ExperimentalTextApi::class)
@Composable
fun WordMeaningDialog(
    setShowDialog: (Boolean) -> Unit,
    initialListMeaning: List<WordData>?,
    onResponse: (String) -> Unit,

    onButton: (Boolean) -> Unit
) {

    var listMeaning by remember { mutableStateOf(initialListMeaning) }


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
                                        Row(modifier = Modifier.padding(16.dp)) {
                                            Text(
                                                text = "Definition: ${definition.definition}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color.White,
                                                modifier = Modifier.weight(1f)
                                            )
                                            RadioButton(
                                                selected = definition.isSelected,
                                                onClick = { /* Handle click event */
                                                    listMeaning = listMeaning?.map { wordData ->
                                                        val updatedMeanings = wordData.meanings.map { meaning ->
                                                            val updatedDefinitions = meaning.definitions.map { def ->
                                                                if (def == definition) {
                                                                    def.copy(isSelected = !def.isSelected)
                                                                } else {
                                                                    def
                                                                }
                                                            }
                                                            meaning.copy(definitions = updatedDefinitions)
                                                        }
                                                        wordData.copy(meanings = updatedMeanings)
                                                    }
                                                }
                                            )



                                        }
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
                    Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically){
                    Button(
                        onClick = {
// here you will give the response back from user selected meanings using listMeaning State


                        },
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            // Change the background color here
                            contentColor = Color.White,
                            containerColor = Color.Green// Change the text color here
                        ),
                        modifier = Modifier
                            .height(50.dp)

                    ) {
                        Text(
                            text = "Save", color = Color.Black,
                        )
                    }
                        Button(
                            onClick = {

                                setShowDialog(false)
//                            sharedPreferences.edit()
//                                .putString("OpenStatus", "done")
//                                .apply()
//                            val selectedDefs = selectedDefinitions.values.flatten()
//                            //onResponse(selectedDefs)
//                            Log.d("main",selectedDefs.toString())
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
}



@OptIn(ExperimentalTextApi::class)
@Composable
fun SummaryDialog(
    setShowDialog: (Boolean) -> Unit,
    Summary:String,
) {

    Dialog(onDismissRequest = { setShowDialog(false) }
    ) {
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




