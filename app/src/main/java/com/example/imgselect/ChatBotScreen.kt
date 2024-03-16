package com.example.imgselect

import android.util.Log
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.imgselect.model.ChatViewModel
import com.example.imgselect.model.ChatViewModelWithImage
import com.example.imgselect.model.TextRecognitionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.nio.file.WatchEvent


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(chatViewModel: ChatViewModel , navController: NavController , chatViewModelWithImage: ChatViewModelWithImage , viewModel: TypewriterViewModel) {
    //val messages = remember { mutableStateListOf<ChatQueryResponse>() }
    var sendButtonEnabled = remember { mutableStateOf(false)}
    val messageQuery = remember { mutableStateListOf<ChatQuery>()}
    val response by chatViewModel.response.observeAsState()
    val responseForImageQuery by chatViewModelWithImage.response.observeAsState()
    val message by chatViewModel.messages.observeAsState(mutableListOf())
    val messageFromImageQuery by chatViewModelWithImage.messages.observeAsState(mutableListOf())
    var query  = remember { mutableStateOf("") }

    val combinedMessage = message + messageFromImageQuery

    val sortedCombinedMessages = combinedMessage.sortedBy { it.timestamp }

    if(chatViewModel.query == "" && chatViewModelWithImage.query == "") {
        sendButtonEnabled.value = false
    } else {
        sendButtonEnabled.value = true

    }



    LaunchedEffect(response , responseForImageQuery) {
        sortedCombinedMessages.forEach {
            Log.d("sorted" , it.message.toString())
        }
//        if(chatViewModelWithImage.imageList.isEmpty()) {
//            response?.let {
//                message.
//            }
//        } else {
//            responseForImageQuery?.let {
//                messages.add(ChatQueryResponse(message = it , fromUser = false))
//            }
//        }
        if(message.isNotEmpty()) {
            message.forEach { message->
                Log.d("message" , message.message.toString())
            }
        }
        if(messageFromImageQuery.isNotEmpty()) {
            messageFromImageQuery.forEach { message->
                Log.d("messageFromImage" , message.message.toString())
            }
        }
    }

    LaunchedEffect(chatViewModelWithImage.isImageSelected) {
        Log.d("isImageSelected" , "${chatViewModelWithImage.isImageSelected}")
    }

        Column(modifier = Modifier
            .fillMaxSize()
            ) {

            MessagesList(messages = sortedCombinedMessages, viewModel = viewModel)


            // Input field and send button
            Row(modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
            )
            {
                TextField(
                    value = if(chatViewModelWithImage.imageList.isEmpty()) {chatViewModel.query} else { chatViewModelWithImage.query},
                    onValueChange = { if(chatViewModelWithImage.imageList.isEmpty()) {chatViewModel.query = it} else {chatViewModelWithImage.query = it} },
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 56.dp, max = 200.dp)
                        .verticalScroll(rememberScrollState()).wrapContentSize(),
                    placeholder = { Text(
                        text = "What's on your mind",
                        modifier = Modifier.fillMaxHeight(),
                        fontSize = 20.sp,

                    )} ,
                    singleLine = false,
                    leadingIcon = {
                        Column() {

                                if(chatViewModelWithImage.isImageSelected) {
//                                    chatViewModel.bitmap?.let {
//                                        Image(
//                                            bitmap = it,
//                                            contentDescription = null,
//                                            modifier = Modifier.size(50.dp)
//                                        )
//                                    }
//                                    chatViewModelWithImage.imageList.forEach {
//                                        if (it != null) {
//                                            Image(
//                                                bitmap = it.asImageBitmap(),
//                                                contentDescription = null,
//                                                modifier = Modifier.size(50.dp)
//                                            )
//                                        }
//                                    }

                                    LazyRow {
                                        items(chatViewModelWithImage.imageList) {image->
                                            if (image != null) {
                                                Image(
                                                    bitmap = image.asImageBitmap(),
                                                    contentDescription = null,
                                                    modifier = Modifier.size(30.dp),
                                                    contentScale = ContentScale.Fit
                                                )
                                            }
                                        }
                                    }

                                }

                            Icon(
                                painter = painterResource(id = R.drawable.baseline_attach_file_24),
                                contentDescription = null,
                                modifier = Modifier
                                    .clickable { navController.navigate(Screen.SummaryScreen.route) }
                                    .size(30.dp)
                            )



                            }

                    },
                    trailingIcon = {
                        if(chatViewModelWithImage.imageList.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = null,
                                modifier = Modifier.clickable {
                                    chatViewModelWithImage.isImageSelected = false
                                    chatViewModel.query = ""
                                    chatViewModelWithImage.query = ""
                                    chatViewModelWithImage.imageList.clear()
                                }
                            )
                        }

                    },
                    maxLines = Int.MAX_VALUE

                )

                Spacer(modifier = Modifier.width(10.dp))
                Button(onClick = {
                    // Add message to list and clear input field
                    if(chatViewModelWithImage.imageList.isEmpty()) {
                        chatViewModel.getResponseFromChatBot()
                        query.value = chatViewModel.query
                        messageQuery.add(ChatQuery(query = chatViewModel.query))
                        chatViewModel.query = ""
                    } else {
                        chatViewModelWithImage.getResponseFromChatBot()
                        query.value = chatViewModelWithImage.query
                        messageQuery.add(ChatQuery(query = chatViewModelWithImage.query))
                        chatViewModelWithImage.query = ""
                        //chatViewModelWithImage.imageList.clear()
                    }

                    //chatViewModel.imageText = ""
                    //chatViewModel.isImageSelected = false

                },
                    enabled = sendButtonEnabled.value,
                    modifier = Modifier.background(Color.Transparent , CircleShape),
                    colors = ButtonDefaults.buttonColors(Color.White)) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                    )
                }
            }
        }

}


@Composable
fun TypewriterTextSingle(
    message: ChatQueryResponse,
    viewModel: TypewriterViewModel,
    modifier: Modifier = Modifier,
) {
    val text = message.message ?: ""
    Log.d("text" , text)
    val messageState = viewModel.getMessageState(message.timestamp)
    val textToDisplay by remember {
        derivedStateOf {
            if (messageState.charIndex.value <= text.length)
                text.substring(0, messageState.charIndex.value)
            else text
        }
    }


    LaunchedEffect(key1 = message.timestamp, key2 = messageState.animationCompleted) {
        viewModel.startTypewriterEffect(message.timestamp, text)
        Log.d("Inside" , "Inside Launched Effect")
        Log.d("Inside" , textToDisplay + "no Text")
    }
    SideEffect {
        Log.d("Recomposition", "Current charIndex: ${messageState.charIndex}, Displaying Text: $textToDisplay")
    }

    Card(
        elevation = CardDefaults.cardElevation(2.dp) ,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp, start = 16.dp),
        shape = RoundedCornerShape(2.dp),
        colors = CardDefaults.cardColors(Color.Black),
        border = BorderStroke(2.dp , Color.DarkGray)
    ) {
        Text(
            text = textToDisplay,
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp),
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }



}



@Preview
@Composable
fun ChatScreenPreview() {
    //ChatScreen()
}

@Composable
fun MessagesList(messages: List<ChatQueryResponse>, viewModel: TypewriterViewModel) {
    LazyColumn(contentPadding = PaddingValues(bottom = 80.dp) , modifier = Modifier.fillMaxSize(0.85f)) {
        items(messages) { message ->
            if (message.fromUser == true) {


                    Text(
                        text = message.message ?: "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, bottom = 8.dp),
                        textAlign = TextAlign.Start,
                        color = Color.White,
                        fontSize = 24.sp
                    )

                // Display user message normally

            } else {
                TypewriterTextSingle(message = message, viewModel = viewModel)
            }
        }
    }
}


data class ChatQueryResponse(
    val message: String? = null,
    val fromUser: Boolean? = null,
    val timestamp: Long = System.currentTimeMillis()
)

data class ChatQuery(
    val query: String? = null
)

class TypewriterViewModel : ViewModel() {
    private val messagesState = mutableMapOf<Long, TypewriterState>()

    fun getMessageState(timestamp: Long): TypewriterState {
        return messagesState.getOrPut(timestamp) { TypewriterState() }
    }

    suspend fun startTypewriterEffect(timestamp: Long, text: String) {
        val state = getMessageState(timestamp)
        if (!state.animationCompleted.value) {
            while (state.charIndex.value < text.length) {
                delay(50) // Adjust delay time for the typewriter effect
                state.charIndex.value++
            }
            state.animationCompleted.value = true
        }
    }


    class TypewriterState {
        var charIndex = mutableStateOf(0)
        var animationCompleted = mutableStateOf(false)
    }
}