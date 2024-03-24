package com.example.imgselect

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.imgselect.data.Chat
import com.example.imgselect.model.ChatViewModel
import com.example.imgselect.model.ChatViewModelWithImage
import com.example.imgselect.ui.theme.Purple80
import com.example.imgselect.ui.theme.lighterYellow
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(chatViewModel: ChatViewModel  , chatViewModelWithImage: ChatViewModelWithImage , viewModel: TypewriterViewModel ) {
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
            Row() {
                IconButton(
                    onClick = {
                        val content = Chat(0,message = combinedMessage)
                        chatViewModel.saveChat(content)
                    },
                    modifier = Modifier.background(Color.Transparent,CircleShape)
                ) {
                    Icon(painter = painterResource(id = R.drawable.save_alt), contentDescription =null )
                }
            }
            MessagesList(messages = sortedCombinedMessages, viewModel = viewModel)
            // Input field and send button
            Row(modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
            )
            {
                OutlinedTextField(
                    value = if(chatViewModelWithImage.imageList.isEmpty()) {chatViewModel.query} else { chatViewModelWithImage.query},
                    onValueChange = { if(chatViewModelWithImage.imageList.isEmpty()) {chatViewModel.query = it} else {chatViewModelWithImage.query = it} },
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 56.dp, max = 200.dp)
                        .verticalScroll(rememberScrollState())
                        .wrapContentSize()
                        .border(1.dp, Color.White, RoundedCornerShape(20.dp))
                        .width(328.dp),
                    placeholder = { Text(
                        text = "Type in your question",
                        modifier = Modifier.fillMaxHeight(),
                        fontSize = 20.sp,
                    )} ,
                    singleLine = false,
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
                        chatViewModel.getResponseFromChatBot({
                            Log.d("main",it)
                        })

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

@Composable
fun SavedChatsScreen(chatList: LiveData<List<Chat>>, chatViewModel: ChatViewModel , goToFullChat: (Chat) -> Unit , ) {
    val chats by chatList.observeAsState(initial = emptyList())
    LazyColumn {
        items(chats) {chat->
            ChatRow(chat = chat , {goToFullChat(chat)} , deleteChat = {chatViewModel.deleteChat(chat)})
            if(chats.indexOf(chat) == chats.size-1) {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRow(chat: Chat , goToFullChat: (Chat) -> Unit , deleteChat: () -> Unit) {
    Column() {
        Card(
            onClick = { goToFullChat(chat) },
            colors = CardDefaults.cardColors(lighterYellow),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            chat.message?.get(0)?.message?.let {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = it,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(16.dp)
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.baseline_delete_24),
                        contentDescription = null,
                        modifier = Modifier
                            .clickable { deleteChat() }
                            .padding(16.dp),
                        tint = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            chat.message?.get(1)?.message?.let {
                Text(
                    text = it,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullChatScreen(chat: Chat) {
    //val convertedList: List<String> = chat.message?.map { it.toString() } ?: emptyList()
    val messages = chat.message ?: listOf()
    Card (
        colors = CardDefaults.cardColors(Purple80),
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp)
            .fillMaxWidth()
    ){
        LazyColumn {
            items(messages) {message->
                message.message?.let {
                    if(message.fromUser == true) {
                        Text(
                            text = it,
                            color = Color.Black,
                            modifier = Modifier.padding(16.dp),
                            fontStyle = FontStyle.Normal,
                            fontSize = 20.sp
                        )
                    } else {
                        Text(
                            text = it,
                            color = Color.DarkGray,
                            modifier = Modifier.padding(16.dp),
                            fontSize = 16.sp
                        )
                    }

                }
                if(messages.indexOf(message) == messages.size - 1) {
                    Spacer(modifier = Modifier.height(100.dp))
                }

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