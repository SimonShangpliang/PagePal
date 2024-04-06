package com.example.imgselect

import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.dictionary.model.DictionaryViewModel
import com.example.dictionary.model.WordMeaningUiState
import com.example.imgselect.DictionaryNetwork.Definition
import com.example.imgselect.DictionaryNetwork.Meaning
import com.example.imgselect.DictionaryNetwork.WordData
import com.example.imgselect.animations.LoadingAnimation
import com.example.imgselect.model.DiscussUiState
import com.example.imgselect.model.SummaryViewModel
import kotlinx.coroutines.delay
import kotlin.streams.toList
import com.example.imgselect.data.Summary
import com.example.imgselect.data.Web
import com.example.imgselect.model.ChatViewModelWithImage
import com.example.imgselect.model.InterpretUiState
import com.example.imgselect.model.WebHistoryViewModel
import com.example.imgselect.ui.theme.GhostWhite
import com.example.imgselect.ui.theme.OpenSans
import com.example.imgselect.ui.theme.aliceBlue
import com.example.imgselect.ui.theme.backgroundcolor
import com.example.imgselect.ui.theme.interestcolour1
import com.example.imgselect.ui.theme.interestcolour2
import com.example.imgselect.ui.theme.lightWhite
import com.example.imgselect.ui.theme.lighterPurple
import com.example.imgselect.ui.theme.lighterTeal
import com.example.imgselect.ui.theme.midnightBlue
import com.kamatiaakash.text_to_speech_using_jetpack_compose.AudioViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalTextApi::class)
@Composable
fun WordMeaningDialog(
    setShowDialog: (Boolean) -> Unit,
    onResponse: (String) -> Unit,

    onButton: (Boolean) -> Unit,
    dictionaryViewModel: DictionaryViewModel,
    audioViewModel: AudioViewModel
) {
val context= LocalContext.current
    var listMeaning: List<WordData>? by remember { mutableStateOf(null) }
    LaunchedEffect(dictionaryViewModel.uiState.value)
    {
        if(dictionaryViewModel.uiState.value is WordMeaningUiState.Success)
        {
            listMeaning=dictionaryViewModel.processResponse()
        }
    }


    Dialog(onDismissRequest = { setShowDialog(false)
        dictionaryViewModel.uiState.value=WordMeaningUiState.Initial
    }) {
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
                            fontFamily = OpenSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp),
                        color = lighterTeal,

                        modifier = Modifier
                            .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 5.dp)
                            .align(Alignment.CenterHorizontally)
                    )

                    dictionaryViewModel.uiState.value.let { uiState ->



                        when (uiState) {
                            is WordMeaningUiState.Loading -> {
                                // Show loading indicator if needed
                                LoadingAnimation(Modifier.align(Alignment.CenterHorizontally))
                            }
                            is WordMeaningUiState.Success -> {
                                Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                                    Text(

                                        text = listMeaning?.getOrNull(0)?.word ?: "",
                                        style = TextStyle(
                                            fontFamily = MaterialTheme.typography.titleSmall.fontFamily,
                                            fontSize = 23.sp
                                        ),
                                        fontStyle = FontStyle.Italic,
                                        color = lightWhite,

                                        modifier = Modifier
                                            .padding(20.dp)
                                            .align(Alignment.CenterVertically)

                                    )
                                    IconButton(modifier= Modifier
                                        .background(Color.Black, CircleShape)
                                        .align(Alignment.CenterVertically),onClick = { /*TODO*/
audioViewModel.justSpeech(listMeaning?.getOrNull(0)?.word ?: "", context = context)
                                    }) {
                                        Icon(painter= painterResource(id = R.drawable.baseline_volume_up_24),"speak")

                                    }
                                }
                                listMeaning?.forEach { wordData ->
                                    Column(modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp)) {

                                        wordData.meanings.groupBy { it.partOfSpeech }.forEach {
                                            (partOfSpeech, meanings) ->
                                            Column(modifier= Modifier
                                                .padding(10.dp)
                                                .background(Color.Black, RoundedCornerShape(10.dp))){
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
                                                      Row(
                                                          modifier = Modifier.padding(
                                                              start = 16.dp,
                                                              end = 16.dp,
                                                              top = 8.dp,
                                                              bottom = if (definition.example == null) 6.dp else 0.dp
                                                          )
                                                      ) {
                                                          Text(
                                                              text = ":${definition.definition}",
                                                              style = MaterialTheme.typography.bodySmall,
                                                              color = Color.White,
                                                              modifier = Modifier
                                                                  .weight(1f)
                                                                  .align(Alignment.CenterVertically)
                                                          )
                                                          RadioButton(
                                                              selected = definition.isSelected,
                                                              onClick = { /* Handle click event */
                                                                  listMeaning =
                                                                      listMeaning?.map { wordData ->
                                                                          val updatedMeanings =
                                                                              wordData.meanings.map { meaning ->
                                                                                  val updatedDefinitions =
                                                                                      meaning.definitions.map { def ->
                                                                                          if (def == definition) {
                                                                                              def.copy(
                                                                                                  isSelected = !def.isSelected
                                                                                              )
                                                                                          } else {
                                                                                              def
                                                                                          }
                                                                                      }
                                                                                  meaning.copy(
                                                                                      definitions = updatedDefinitions
                                                                                  )
                                                                              }
                                                                          wordData.copy(meanings = updatedMeanings)

                                                                      }
                                                                  dictionaryViewModel.listMeaning =
                                                                      listMeaning
                                                              }
                                                          )


                                                      }
                                                      definition.example?.let {
                                                          Text(
                                                              text = "Example: $it",
                                                              style = MaterialTheme.typography.bodySmall,
                                                              color = lightWhite,
                                                              modifier = Modifier.padding(
                                                                  start = 16.dp,
                                                                  end = 16.dp,
                                                                  top = 0.dp,
                                                                  bottom = 8.dp
                                                              ),
                                                              fontSize = 10.5.sp,
                                                              fontStyle = FontStyle.Italic
                                                          )
                                                      }
                                                      if (!definition.synonyms.isNullOrEmpty()) {
                                                          Text(
                                                              text = "Synonyms: ${definition.synonyms.joinToString()}",
                                                              style = MaterialTheme.typography.bodySmall,
                                                              color = aliceBlue,
                                                              modifier = Modifier.padding(
                                                                  start = 16.dp,
                                                                  end = 16.dp,
                                                                  top = 2.dp,
                                                                  bottom = 8.dp
                                                              )
                                                          )
                                                      }
                                                      if (!definition.antonyms.isNullOrEmpty()) {
                                                          Text(
                                                              text = "Antonyms: ${definition.antonyms.joinToString()}",
                                                              style = MaterialTheme.typography.bodySmall,
                                                              color = aliceBlue,
                                                              modifier = Modifier.padding(
                                                                  start = 16.dp,
                                                                  end = 16.dp,
                                                                  top = 2.dp,
                                                                  bottom = 8.dp
                                                              )
                                                          )
                                                      }
                                                  }
                                              }}

                            }
                        }


                    }

                }
                            is WordMeaningUiState.Error -> {
                                Text(
                                    text = "Error: " + uiState.error,
                                    style = TextStyle(
                                        fontFamily = MaterialTheme.typography.titleSmall.fontFamily,
                                        fontSize = 23.sp
                                    ),
                                    color = Color.White,
                                    modifier = Modifier
                                        .padding(20.dp)
                                        .align(Alignment.CenterHorizontally)
                                )

                                // Handle error state if needed
                            }
                            else -> {
                                // Handle other states if needed
                            }
                        }


                    }

                }



                Box(
                    modifier = Modifier
                        .background(midnightBlue)
                        .fillMaxSize()
                ) {
                    Row(modifier= Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically){
                        Button(
                            onClick = {
// here you will give the response back from user selected meanings using listMeaning State
                                dictionaryViewModel.listMeaning?.forEach { worddata->
                                    worddata.meanings.forEach { meanings->
                                        meanings.definitions.forEach { definitions->
                                            Log.d("DefinitionsNew" , "${definitions.definition}")
                                            Log.d("DefinitionsNew" , "${definitions.isSelected}")
                                        }
                                    }
                                }
// here you will give the response back from user selected meanings using listMeaning State
                                setShowDialog(false)
                                dictionaryViewModel.dialogVisible = true


                        },
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            // Change the background color here
                            contentColor = Color.White,
                            containerColor = lighterTeal// Change the text color here
                        ),
                        modifier = Modifier
                            .height(50.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp, pressedElevation =5.dp )


                    ) {
                        Text(
                            text = "Save", color = Color.Black,  fontFamily = OpenSans, fontWeight = FontWeight.SemiBold
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
                                dictionaryViewModel.uiState.value=WordMeaningUiState.Initial

                                onResponse("done")
                            },
                            shape = RoundedCornerShape(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                // Change the background color here
                                contentColor = Color.White,
                                containerColor = lighterTeal// Change the text color here
                            ),
                            modifier = Modifier
                                .height(50.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp, pressedElevation =5.dp )

                        ) {
                            Text(
                                text = "Done", color = Color.Black, fontFamily = OpenSans,fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ImageList(bitmap:Bitmap?,bitmapList: List<Bitmap?>,chatViewModelWithImage: ChatViewModelWithImage) {
    val scroll= rememberScrollState()

    if(bitmapList.size==0){
        Box(modifier=Modifier.fillMaxWidth()){
        Text("Add Images to send",modifier=Modifier.align(Alignment.Center))}
    }else{
    Column (modifier=Modifier.verticalScroll(scroll)){
//        bitmapList.forEach { bitmap ->
            if (bitmap != null) {
                Row(modifier= Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(5.dp)
                    , horizontalArrangement = Arrangement.SpaceBetween) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .clip(RoundedCornerShape(4.dp))
                            .padding(5.dp),
                        contentScale = ContentScale.FillWidth
                    )
                    IconButton(onClick = { /*TODO*/
                    chatViewModelWithImage.removeBitmapFromList(bitmap)
                    },modifier= Modifier
                        .size(35.dp)
                        .padding(end = 5.dp)) {
                        Icon(Icons.Default.Delete,"delete_item",tint=Color.White)
                    }
                }
            } else {
                Text(
                    text = "Image not available",
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
//}
}

@Composable
fun HorizontalImageList(chatViewModelWithImage: ChatViewModelWithImage) {

    val bitmapList=chatViewModelWithImage.imageList.collectAsState().value
    var imageDialog by remember{ mutableStateOf(false)
    }
    var currBitmap :Bitmap? by remember {
        mutableStateOf(null)
    }
    AnimatedVisibility(imageDialog)
    {
        if(currBitmap!=null){
        ImageDialog(currBitmap ,setShowDialog = {imageDialog = it
            Log.d("main","jere herere")

        }, chatViewModelWithImage,{
        })}
    }
    val scroll= rememberScrollState()
    if(bitmapList.size==0){
//        Box(modifier=Modifier.fillMaxWidth()){
//            Text("Add Images to send",modifier=Modifier.align(Alignment.Center))}
    }else{
      Row(modifier= Modifier
          .horizontalScroll(scroll)
          .animateContentSize()
          .padding(horizontal = 5.dp)){
            bitmapList.forEach { bitmap ->
                if (bitmap != null) {
                    Box(modifier= Modifier
                        .fillMaxWidth()
                        .background(
                            Color.Black, RoundedCornerShape(
                                topStart = if (bitmap == bitmapList[0]) 10.dp else 0.dp,
                                bottomStart = if (bitmap == bitmapList[0]) 10.dp else 0.dp,
                                topEnd = if (bitmap == bitmapList[bitmapList.size - 1]) 10.dp else 0.dp,
                                bottomEnd = if (bitmap == bitmapList[bitmapList.size - 1]) 10.dp else 0.dp
                            )
                        )
                        .padding(5.dp)
                        ) {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .clip(RoundedCornerShape(4.dp))
                                .padding(5.dp)
                                .clickable { currBitmap=bitmap
                                           imageDialog=true},
                            contentScale = ContentScale.FillHeight
                        )
                        IconButton(onClick = { /*TODO*/
                            chatViewModelWithImage.removeBitmapFromList(bitmap)
                        },modifier= Modifier
                            .size(35.dp)
                            .align(Alignment.TopEnd)
                            .padding(end = 5.dp)) {
                            Icon(Icons.Default.Delete,"delete_item",tint=Color.Gray)
                        }
                    }
                } else {
                    Text(
                        text = "Image not available",
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }}
}
@OptIn(ExperimentalTextApi::class)
@Composable
fun SummaryDialog(
    setShowDialog: (Boolean) -> Unit,
    summaryViewModel: SummaryViewModel,
    audioViewModel: AudioViewModel,
    setOffset: (Int)->Unit
) {

    val appUiState=summaryViewModel.uiState.collectAsState().value
val context= LocalContext.current
    Dialog(onDismissRequest = {
        setOffset(1)
        setShowDialog(false)

    }
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.height(700.dp),
            color = Color.Black
        ) {
            DisposableEffect(Unit){
                onDispose {
                    audioViewModel.stopTextToSpeech()
                }
            }
            Column(modifier = Modifier.fillMaxSize()) {
                val scroll = rememberScrollState()
                Column(
                    modifier = Modifier
                        .background(Color(0xff1E1E1E))
                        .fillMaxHeight(0.88f)
                        .fillMaxWidth()
                        .verticalScroll(scroll)
                ) {
                    Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                    Text(
                        text = "Summary",
                        style = TextStyle(
                            fontFamily = MaterialTheme.typography.titleSmall.fontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp),
                        color = Color.White,

                        modifier = Modifier
                            .padding(20.dp)
                    )
                    AnimatedContent(targetState = appUiState,modifier=Modifier ,label="appUiState") {appUiState->
                        when(appUiState) {
                            is DiscussUiState.Success->{ IconButton(modifier= Modifier
                                .background(Color.Black, CircleShape)
                                .align(Alignment.CenterVertically),onClick = { /*TODO*/
                                    audioViewModel.justSpeech(appUiState.outputText,context)
                            }) {
                                Icon(painter= painterResource(id = R.drawable.baseline_volume_up_24),"speak")

                            }}
                            else-> {}
                        }
                    }
                    }

                    when(appUiState) {
                        is DiscussUiState.Success->TypewriterText(texts = listOf(appUiState.outputText))
                        is DiscussUiState.Loading-> LoadingAnimation(modifier=Modifier.align(Alignment.CenterHorizontally))
                        else-> TypewriterText(texts = listOf("Error in summarizing please try again") )
                    }






                }



                Row(
                    modifier = Modifier
                        .background(interestcolour1)
                        .fillMaxSize()
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
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
                        //.align(Alignment.Start)

                    ) {
                        Text(
                            text = "Done", color = Color.Black,
                        )
                    }

                    Button(
                        onClick = {
                            setOffset(1)
                            summaryViewModel.dialogVisible = true

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
                        //.align(Alignment.Start)

                    ) {
                        Text(
                            text = "Save", color = Color.Black,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun InterpretDialog(
    setShowDialog: (Boolean) -> Unit,
    chatViewModelWithImage: ChatViewModelWithImage,
    audioViewModel: AudioViewModel,
    setOffset: (Int)->Unit
) {

    val interpretUiState=chatViewModelWithImage.interpretUiState.collectAsState().value
val context= LocalContext.current
    Dialog(onDismissRequest = {
        setShowDialog(false)

    }
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
                    Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
DisposableEffect(Unit){
    onDispose {
        audioViewModel.stopTextToSpeech()
    }
}
                    Text(
                        text = "Interpretation",
                        style = TextStyle(
                            fontFamily = MaterialTheme.typography.titleSmall.fontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp),
                        color = Color.White,

                        modifier = Modifier
                            .padding(20.dp)
                    )
                        AnimatedContent(targetState = interpretUiState,modifier=Modifier ,label="interpretUiState") {interpretUiState->
                            when(interpretUiState) {
                                is InterpretUiState.Success->{ IconButton(modifier= Modifier
                                    .background(Color.Black, CircleShape)
                                    .align(Alignment.CenterVertically),onClick = { /*TODO*/
                                    audioViewModel.justSpeech(interpretUiState.responseText,context)
                                }) {
                                    Icon(painter= painterResource(id = R.drawable.baseline_volume_up_24),"speak")

                                }}
                                else-> {}
                            }
                        }}

                    when(interpretUiState) {
                        is InterpretUiState.Success->TypewriterText(texts = listOf(interpretUiState.responseText))
                        is InterpretUiState.Loading-> LoadingAnimation(modifier=Modifier.align(Alignment.CenterHorizontally))
                        else-> TypewriterText(texts = listOf("Error in summarizing please try again") )
                    }






                }



                Row(
                    modifier = Modifier
                        .background(Color(0xff141414))
                        .fillMaxSize()
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
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
                        //.align(Alignment.Start)

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
fun ImageDialog(currBitmap: Bitmap?,
    setShowDialog: (Boolean) -> Unit,
    chatViewModelWithImage: ChatViewModelWithImage,
    setOffset: (Int)->Unit
) {

    val imageList=chatViewModelWithImage.imageList.collectAsState()

    Dialog(onDismissRequest = {
        setOffset(1)
        setShowDialog(false)

    }
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.height(700.dp),
            color = Color.White
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .background(Color(0xff1E1E1E))
                        .fillMaxHeight(0.88f)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Image",
                        style = TextStyle(
                            fontFamily = MaterialTheme.typography.titleSmall.fontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp),
                        color = Color.White,

                        modifier = Modifier
                            .padding(top = 20.dp, bottom = 10.dp, start = 20.dp, end = 20.dp)
                            .align(Alignment.CenterHorizontally)
                    )

                    ImageList(currBitmap,imageList.value,chatViewModelWithImage)







                }



                Row(
                    modifier = Modifier
                        .background(Color(0xff141414))
                        .fillMaxSize()
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
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
                        //.align(Alignment.Start)

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

@Composable
fun TypewriterText(texts: List<String>, durationMillis: Int = 2000) {
    var currentText by remember { mutableStateOf("") }
    var textIndex by remember { mutableStateOf(0) }
    var charIndex by remember { mutableStateOf(0) }

    LaunchedEffect(texts) {
        while (textIndex < texts.size) {
            if (charIndex < texts[textIndex].length) {
                currentText += texts[textIndex][charIndex]
                charIndex++
                delay(10)
            } else {
                delay(durationMillis.toLong())
                textIndex++
                charIndex = 0
                if (textIndex < texts.size) {
                    currentText = ""
                }
            }
        }
    }

    Text(
        text = currentText,
        modifier = Modifier.padding(16.dp),
        color = Color.White
    )
}



@RequiresApi(Build.VERSION_CODES.N)
fun String.splitToCodePoints(): List<String> {
    return codePoints()
        .toList()
        .map {
            String(Character.toChars(it))
        }
}







@OptIn(ExperimentalTextApi::class)
@Composable
fun WebHistoryDialog(
    setShowDialog: (Boolean) -> Unit,
    history :List<Web>,
    webHistoryViewModel: WebHistoryViewModel,
    setUrl:(String)->Unit
) {

    val searchQuery = remember { mutableStateOf("")}
    val updatedHistory = if(searchQuery.value.isEmpty()) {
        history
    } else {
        history.filter { website->
            website.website?.contains(searchQuery.value , ignoreCase = true) ?: false
        }
    }



    Dialog(onDismissRequest = { setShowDialog(false) }
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.height(700.dp),
            color = Color(0xff1E1E1E)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier
                    .height(10.dp)
                    .background(Color(0xff1E1E1E)))
                MySearchBar1(
                    placeHolder = "Search",
                    onQueryChanged = {query -> searchQuery.value = query}
                )
                Column(
                    modifier = Modifier
                        .background(Color(0xff1E1E1E))
                        .fillMaxHeight(0.88f)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "History",
                        style = TextStyle(
                            fontFamily = MaterialTheme.typography.titleSmall.fontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp),
                        color = Color.White,

                        modifier = Modifier
                            .padding(20.dp)
                            .align(Alignment.CenterHorizontally)
                    )

                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        modifier = Modifier.fillMaxSize(), // Ensure the LazyColumn takes up all available space

                    ) {
                        items(updatedHistory.sortedByDescending { it.date }){ web ->
                            Box(
                                modifier = Modifier
                                    .background(Color.Black)
                                    .clickable {
                                        setUrl(web.website ?: "www.google.com")
                                    }
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row() {
                                    Text(text = web.website ?: "Unknown Website", color = Color.White, maxLines = 1,modifier=Modifier.fillMaxWidth(0.8f))
                                    IconButton(onClick = {
                                        CoroutineScope(Dispatchers.IO).launch{
                                            webHistoryViewModel.deleteWeb(web)

                                        }
                                    }) {
                                        Icon(Icons.Default.Delete,"delete",tint=Color.White)
                                    }
                                }
                            }                        }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySearchBar1(
    placeHolder : String,
    onQueryChanged: (String) -> Unit,
    cornerRadius: Float = 30f
){

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray),
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
                onQueryChanged(it)
            }, modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(1.dp))
                .padding(horizontal = 20.dp)
                .border(0.5.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(10.dp)),
            placeholder = {
                Text(
                    text = placeHolder,
                    color = Color.White.copy(alpha = 0.5f)
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.White,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                placeholderColor = Color.White.copy(alpha = 0.5f)
            ),

            trailingIcon = {
                IconButton(onClick = { }) {
                    Icon(painter = painterResource(id = R.drawable.search), contentDescription ="Search" )
                }
            },
            shape = RoundedCornerShape(10.dp)
        )
    }
}






