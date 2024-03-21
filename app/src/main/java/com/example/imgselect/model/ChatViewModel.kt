package com.example.imgselect.model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imgselect.ChatQueryResponse
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonNull.content

class ChatViewModel: ViewModel() {


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



    init {
        val config = generationConfig { temperature = 0.70f }
        generativeModel = GenerativeModel(
            modelName = "gemini-pro",
            apiKey = "AIzaSyAcjltECY8ja_imWzStrDcULxdN5gihbyY",
            generationConfig = config
        )
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