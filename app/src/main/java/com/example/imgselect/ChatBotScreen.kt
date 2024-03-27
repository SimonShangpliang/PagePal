package com.example.imgselect

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.VectorProperty
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.imgselect.animations.LoadingAnimation
import com.example.imgselect.data.Chat
import com.example.imgselect.model.ChatViewModel
import com.example.imgselect.model.ChatViewModelWithImage
import com.example.imgselect.model.DiscussUiState
import com.example.imgselect.model.ModeViewModel
import com.example.imgselect.ui.theme.Purple80
import com.example.imgselect.ui.theme.lighterYellow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(chatViewModel: ChatViewModel  , chatViewModelWithImage: ChatViewModelWithImage , viewModel: TypewriterViewModel,modeViewModel: ModeViewModel ) {
    //val messages = remember { mutableStateListOf<ChatQueryResponse>() }
    var sendButtonEnabled = remember { mutableStateOf(false)}
    val messageQuery = remember { mutableStateListOf<ChatQuery>()}
    val response by chatViewModel.response.observeAsState()
    val responseForImageQuery by chatViewModelWithImage.response.observeAsState()
    val message by chatViewModel.messages.observeAsState(mutableListOf())
    val messageFromImageQuery by chatViewModelWithImage.messages.observeAsState(mutableListOf())
    var query  = remember { mutableStateOf("") }
    val isImageMode=modeViewModel.isImageMode.collectAsState()
    val combinedMessage = message + messageFromImageQuery
    var imageDialog by remember{ mutableStateOf(false)
    }
    val sortedCombinedMessages = combinedMessage.sortedBy { it.timestamp }

    if(chatViewModel.query == "" && chatViewModelWithImage.query == "") {
        sendButtonEnabled.value = false
    } else {
        sendButtonEnabled.value = true


    }
//    LaunchedEffect(message,messageFromImageQuery)
//    {
//        viewModel.stateInitialSet()
//    }






        Column(modifier = Modifier
            .fillMaxSize()
            ) {

            AnimatedVisibility(imageDialog)
            {

                ImageDialog(setShowDialog = {imageDialog = it
                    Log.d("main","jere herere")

                }, chatViewModelWithImage,{
                    //setZeroOffset=it

                })


            }
//            Row() {
//                IconButton(
//                    onClick = {
//                        val content = Chat(0,message = combinedMessage)
//                        chatViewModel.saveChat(content)
//                    },
//                    modifier = Modifier.background(Color.Transparent,CircleShape)
//                ) {
//                    Icon(painter = painterResource(id = R.drawable.save_alt), contentDescription =null )
//                }
//            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(20.dp))
            MessagesList(messages = sortedCombinedMessages, viewModel = viewModel)
            // Input field and send button

            Row(modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
            )
            {
                val infiniteTransition= rememberInfiniteTransition(label = "circular motion")
                val rotatedAnimation=infiniteTransition.animateFloat(label = "circular motion",
                    initialValue = 0f,
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(tween(1000, easing = LinearEasing))
                )
                val rainbowColors = listOf(
                    Color.Red,
                    Color(0xFFFF7F00), // Orange
                    Color.Yellow,
                    Color.Green,
                    Color.Blue,
                    Color(0xFF4B0082), // Indigo
                    Color(0xFF8B00FF)  // Violet
                )

                val rainbowColorsBrush = Brush.linearGradient(
                    colors = rainbowColors,
                    start = Offset.Zero,
                    end = Offset(100f, 0f)
                )

                AnimatedVisibility(visible = isImageMode.value,modifier=Modifier.fillMaxHeight()) {
                    IconButton(onClick = {imageDialog=true},modifier=Modifier.align(Alignment.CenterVertically)
                        )
                    {
Icon(painterResource(id = R.drawable.baseline_camera_24),contentDescription = "more photos",modifier= Modifier
    .size(30.dp)
    .align(Alignment.CenterVertically)
    .drawBehind {
        rotate(rotatedAnimation.value) {
            drawCircle(
                rainbowColorsBrush,
                style = Stroke(2f)
            )
        }
    })
                    }
                }

                OutlinedTextField(
                    value = if(!isImageMode.value) {chatViewModel.query} else { chatViewModelWithImage.query},
                    onValueChange = { if(!isImageMode.value) {chatViewModel.query = it} else {chatViewModelWithImage.query = it} },
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 40.dp, max = 200.dp)
                        .verticalScroll(rememberScrollState())
                        .wrapContentSize()
                        .align(Alignment.CenterVertically)

                        // .border(1.dp, Color.White, RoundedCornerShape(20.dp))
                        .width(328.dp),
                    colors= TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.White),
                    shape = MaterialTheme.shapes.extraLarge,

                    placeholder = { Text(
                        text = "Type in your question",
                        modifier = Modifier.fillMaxHeight(),
                        fontSize = 20.sp,
                    )} ,
                    singleLine = false,
                    trailingIcon = {


                    },
                    maxLines = Int.MAX_VALUE

                )
Box(modifier=Modifier.fillMaxHeight()) {
    Switch(checked=isImageMode.value, onCheckedChange = {modeViewModel.setMode(!isImageMode.value)
                                                        chatViewModel.query=""
        chatViewModelWithImage.query=""
                                                        },modifier= Modifier
        .scale(0.6f)
        .align(Alignment.TopCenter),colors= SwitchDefaults.colors(checkedTrackColor = Color.White, checkedBorderColor = Color.White, checkedIconColor = Color.White, checkedThumbColor = Color.Black)
    )

    IconButton(
        onClick = {
            // Add message to list and clear input field
            if (!isImageMode.value) {
                chatViewModel.getResponseFromChatBot({
                    Log.d("main", it)
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
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(top = 5.dp),
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            modifier=Modifier.size(35.dp)
        )
    }

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

    }


    Column(modifier = Modifier
        .fillMaxWidth()
        .animateContentSize()
        .padding(start = 25.dp, end = 25.dp, bottom = 20.dp)
        .background(Color.Black, RoundedCornerShape(15.dp))

    ) {

            Text(
                text = textToDisplay,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 14.sp
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
    LazyColumn(contentPadding = PaddingValues(bottom = 80.dp) , modifier = Modifier
        .fillMaxHeight(0.85f)
        .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        items(messages) { message ->
            if (message.fromUser == true) {

Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
    Text(
        text = message.message ?: "",
        modifier = Modifier
            .fillMaxWidth(0.80f)
            .align(Alignment.CenterVertically)
            .padding(start = 25.dp, bottom = 8.dp),
        textAlign = TextAlign.Start,
        color = Color.White,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )
    Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){

        IconButton(onClick = { /*TODO*/

                             },modifier=Modifier.size(30.dp).padding(end=5.dp)) {
            Icon(painter = painterResource(id = R.drawable.baseline_volume_up_24),"deete")
        }
    IconButton(onClick = { /*TODO*/ },modifier=Modifier.padding(end=20.dp).size(30.dp)) {
        Icon(painter = painterResource(id = R.drawable.save_alt),"deete")
    }}
}
                // Display user message normally
                //val appUiState=viewModel.uiState.collectAsState().value

            } else {

                TypewriterTextSingle(message = message, viewModel = viewModel)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySearchBar(
    placeHolder : String,
    onQueryChanged: (String) -> Unit,
    cornerRadius: Float = 30f
){

    Box(
        modifier = Modifier
            .fillMaxWidth(),
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
                .padding(horizontal = 20.dp)
                ,
            placeholder = {
                Text(
                    text = placeHolder,
                    color = Color.White.copy(alpha = 0.5f)
                )
            },
            colors =TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.White),

            trailingIcon = {
                IconButton(onClick = { }) {
                    Icon(painter = painterResource(id = R.drawable.search), contentDescription ="Search" )
                }
            },
            shape = MaterialTheme.shapes.extraLarge
        )
    }
}
@Composable
fun SavedChatsScreen(chatList: LiveData<List<Chat>>, chatViewModel: ChatViewModel , goToFullChat: (Chat) -> Unit , ) {
    val chats by chatList.observeAsState(initial = emptyList())
    val searchQuery = remember { mutableStateOf("")}

    val filteredChats = if(searchQuery.value.isEmpty()) {
        chats
    } else {
        chats.filter { chat->
            chat.message?.any { chatQueryResponse ->
                chatQueryResponse.message?.contains(searchQuery.value, ignoreCase = true)?: false
            } ?: false
        }
    }


    Column(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize()
    ) {
        Text(
            text = "Chats",
            fontSize = 35.sp,
            color = Color.White,
            modifier = Modifier.padding(24.dp)
        )
        MySearchBar(placeHolder = "Search", onQueryChanged = {query -> searchQuery.value = query})

        LazyColumn(
            //modifier = Modifier.border(2.dp, Color.Red),
        ) {
            items(filteredChats) {chat->
                ChatRow(chat = chat , {goToFullChat(chat)} , deleteChat = {chatViewModel.deleteChat(chat)})
                if(chats.indexOf(chat) == chats.size-1) {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }

}
fun Modifier.coloredShadow(
    color: Color,
    alpha: Float = 0.2f,
    borderRadius: Dp = 0.dp,
    shadowRadius: Dp = 20.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp
) = composed {

    val shadowColor = color.copy(alpha = alpha).toArgb()
    val transparent = color.copy(alpha= 0f).toArgb()

    this.drawBehind {

        this.drawIntoCanvas {
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            frameworkPaint.color = transparent

            frameworkPaint.setShadowLayer(
                shadowRadius.toPx(),
                offsetX.toPx(),
                offsetY.toPx(),
                shadowColor
            )
            it.drawRoundRect(
                0f,
                0f,
                this.size.width,
                this.size.height,
                borderRadius.toPx(),
                borderRadius.toPx(),
                paint
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRow(chat: Chat , goToFullChat: (Chat) -> Unit , deleteChat: () -> Unit) {

        Card(
            onClick = { goToFullChat(chat) },
            colors = CardDefaults.cardColors(lighterYellow),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column() {
                if(chat.message?.isNotEmpty() == true) {
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


//                    Icon(
//                        painter = painterResource(id = R.drawable.baseline_delete_24),
//                        contentDescription = null,
//                        modifier = Modifier
//                            .clickable { deleteChat() }
//                            .padding(16.dp),
//                        tint = Color.Black
//                    )

                    }
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
    private val _uiState: MutableStateFlow<AnimationState> = MutableStateFlow(AnimationState.Initial)
    val uiState=_uiState.asStateFlow()
    fun getMessageState(timestamp: Long): TypewriterState {
      //  _uiState.value=AnimationState.Initial
        return messagesState.getOrPut(timestamp) { TypewriterState() }
    }
   fun stateInitialSet()
   {
       _uiState.value=AnimationState.Initial
   }

    suspend fun startTypewriterEffect(timestamp: Long, text: String) {
        _uiState.value= AnimationState.InProgress

        val state = getMessageState(timestamp)
        if (!state.animationCompleted.value) {
            while (state.charIndex.value < text.length) {
                delay(20) // Adjust delay time for the typewriter effect
                state.charIndex.value++
            }
            state.animationCompleted.value = true
            _uiState.value= AnimationState.Completed
        }
    }


    class TypewriterState {
        var charIndex = mutableStateOf(0)
        var animationCompleted = mutableStateOf(false)
    }
}
sealed class AnimationState {
    object Initial : AnimationState()
    object InProgress : AnimationState()
    object Completed : AnimationState()
}