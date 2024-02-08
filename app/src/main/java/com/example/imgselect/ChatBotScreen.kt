package com.example.imgselect

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.imgselect.model.ChatViewModel
import java.nio.file.WatchEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(chatViewModel: ChatViewModel = viewModel()) {
    val messages = remember { mutableStateListOf<ChatQueryResponse>() }
    val response by chatViewModel.response.observeAsState()
    var query  = remember { mutableStateOf("") }


    LaunchedEffect(response) {
        response?.let {
            messages.add(ChatQueryResponse(query = query.value , response = it))
        }
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Chat With PagePal" , modifier = Modifier.background(Color.Yellow)) } , colors = TopAppBarDefaults.largeTopAppBarColors(Color.Yellow)) }) { padding ->
        Column(modifier = Modifier
            .padding(padding)
            .padding(8.dp)) {
            // Messages list
            Column(modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())) {
                messages.forEach { message ->
                    Text(text = message.query.toString(), modifier = Modifier
                        .padding(4.dp)
                        .align(Alignment.End) , color = Color.White)
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = message.response.toString() , modifier = Modifier
                        .padding(4.dp)
                        .align(Alignment.Start) , color = Color.White)

                }
            }

            // Input field and send button
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) {
                TextField(
                    value = chatViewModel.query,
                    onValueChange = { chatViewModel.query = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("What's on your mind") }
                )

                Spacer(modifier = Modifier.width(10.dp))
                Button(onClick = {
                    // Add message to list and clear input field
                    chatViewModel.getResponseFromChatBot()
                    query.value = chatViewModel.query
                    chatViewModel.query = ""

                }) {
                    Text("Send")
                }
            }
        }
    }
}

@Preview
@Composable
fun ChatScreenPreview() {
    ChatScreen()
}

data class ChatQueryResponse(
    val query: String? = null,
    val response: String? = null
)