package com.example.imgselect.model

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonNull.content


class ChatViewModel(application: Application): AndroidViewModel(application) {
    private val _uiState: MutableStateFlow<ChatUiState> = MutableStateFlow(ChatUiState.Initial)
    val uiState=_uiState.asStateFlow()

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
            modelName = "gemini-1.5-flash-latest",
            apiKey = "AIzaSyC-PzCAvCDvJeHCZMx9YY4mSjq9U6OAYKo",
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
            val existingChat = repositoryForChats.getChat(chat.id)
            if(existingChat != null) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(getApplication<Application>().applicationContext , "The Chat is already saved", Toast.LENGTH_SHORT).show()
                }
            } else {
                repositoryForChats.addChat(chat)
                withContext(Dispatchers.Main) {
                    Toast.makeText(getApplication<Application>().applicationContext , "Chat saved", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
//
    fun getChatList() : LiveData<List<Chat>> {
        val chatList = repositoryForChats.readAllChat
        return chatList
    }

    fun deleteChat(chat:Chat) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryForChats.deleteChat(chat)
        }
    }

    suspend fun getChat(chatId: Int): Chat {
        return repositoryForChats.getChat(chatId)
    }



    fun getResponseFromChatBot(onText:(String)->Unit) {
        _uiState.value= ChatUiState.Loading

        viewModelScope.launch {
            try {
                //Log.d("finalQuery" , imageText)
                val updatedMessages = _messages.value.orEmpty() + ChatQueryResponse(query, true , System.currentTimeMillis())
                _messages.value = updatedMessages
               val responseText = generativeModel.generateContent(query).text.toString()
                _uiState.value = ChatUiState.Success(responseText)

                val updatedResponse = _messages.value.orEmpty() + ChatQueryResponse(responseText , false , System.currentTimeMillis())
                _messages.value = updatedResponse
                _response.postValue(responseText)
                Log.d("ChatViewModel", "Response updated to: $response")
            } catch(e: Exception) {
                _uiState.value= ChatUiState.Error(e.localizedMessage ?: "Error in Generating content")
                val errorMessage = "Error in Generating content: ${e.localizedMessage ?: "Unknown error"}"
                val errorResponse = _messages.value.orEmpty() + ChatQueryResponse(errorMessage, false, System.currentTimeMillis())
                _messages.value = errorResponse
                _response.postValue(errorMessage)


                Log.e("getResponseFromChatBot", "Error generating content: ${e.message}", e)
            }

        }
    }


}
sealed interface ChatUiState{
    object Initial: ChatUiState
    object Loading: ChatUiState
    data class Success(val outputText:String): ChatUiState
    data class Error(val error:String): ChatUiState

}