package com.example.imgselect.model

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imgselect.ChatQueryResponse
import com.example.imgselect.data.Chat
import com.example.imgselect.data.LocalStorageDatabase
import com.example.imgselect.data.LocalStorageDatabaseForChat
import com.example.imgselect.data.LocalStorageRepositoryForChats
import com.example.imgselect.data.Summary
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonNull.content

class ChatViewModel(application: Application): AndroidViewModel(application) {


    private var generativeModel: GenerativeModel
    var query: String by mutableStateOf("")
    var responseGenerationFinished: Boolean by mutableStateOf(true)
    private val _response = MutableLiveData<String>()
    val response: LiveData<String> = _response
    private val _messages = MutableLiveData<List<ChatQueryResponse>>(listOf())
    val messages: LiveData<List<ChatQueryResponse>> = _messages
    //var bitmap: ImageBitmap? by mutableStateOf(null)
//    var imageText: String by mutableStateOf("")

    //var finalQuery: String by mutableStateOf(imageText)

    private val repositoryForChats: LocalStorageRepositoryForChats
    private val readAllChat: LiveData<List<Chat>>





    init {
        val config = generationConfig { temperature = 0.70f }
        generativeModel = GenerativeModel(
            modelName = "gemini-pro",
            apiKey = "AIzaSyAOgrCj3x9WE8JoSDB5yuoGqH7m4Rn0IWI",
            generationConfig = config
        )
//        val localStorageDao = LocalStorageDatabase.getDatabase(application).userDao()
//        repository = LocalStorageRepository()
//        readAllChat = repository.readAllChat
        val localStorageDaoForChats = LocalStorageDatabaseForChat.getDatabase(application).userDaoForChats()
        repositoryForChats = LocalStorageRepositoryForChats(localStorageDaoForChats)
        readAllChat = repositoryForChats.readAllChat

    }

    fun saveChat(chat: Chat) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryForChats.addChat(chat)
        }
    }
//
    fun getChatList() : LiveData<List<Chat>> {
        val chatList = repositoryForChats.readAllChat
        return chatList
    }



    fun getResponseFromChatBot() {
        viewModelScope.launch {
            try {
                //Log.d("finalQuery" , imageText)
                val updatedMessages = _messages.value.orEmpty() + ChatQueryResponse(query, true , System.currentTimeMillis())
                _messages.value = updatedMessages
                val responseText = generativeModel.generateContent(query).text.toString()
                val updatedResponse = _messages.value.orEmpty() + ChatQueryResponse(responseText , false , System.currentTimeMillis())
                _messages.value = updatedResponse
                _response.postValue(responseText)
                Log.d("ChatViewModel", "Response updated to: $response")
            } catch(e: Exception) {
                Log.e("getResponseFromChatBot", "Error generating content: ${e.message}", e)
            }

        }
    }


}