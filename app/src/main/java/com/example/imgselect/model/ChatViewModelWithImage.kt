package com.example.imgselect.model

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imgselect.ChatQueryResponse
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.launch

class ChatViewModelWithImage: ViewModel() {

    private var generativeModel: GenerativeModel
    var query: String by mutableStateOf("")
    var imageList = mutableListOf<Bitmap?>()
    private val _response = MutableLiveData<String>()
    val response: LiveData<String> = _response
    var isImageSelected: Boolean by mutableStateOf(false)
    private val _messages = MutableLiveData<List<ChatQueryResponse>>(listOf())
    val messages: LiveData<List<ChatQueryResponse>> = _messages

    init {
        val config = generationConfig { temperature = 0.70f }
        generativeModel = GenerativeModel(
            modelName = "gemini-pro-vision",
            apiKey = "AIzaSyAcjltECY8ja_imWzStrDcULxdN5gihbyY",
            generationConfig = config
        )
        Log.d("gemini" , "gemini-pro-vision is initialized")
    }

    fun getResponseFromChatBot() {
        viewModelScope.launch {
            Log.d("imageList" , "There are ${imageList.size} images")
            Log.d("imageList" , query)
            val updatedMessages = _messages.value.orEmpty() + ChatQueryResponse(query, true , System.currentTimeMillis())
            _messages.value = updatedMessages
            val inputContent = content {
                imageList.forEach {
                    if (it != null) {
                        image(it)
                    }
                }

                text(query)
            }

            try {
                val responseText = generativeModel.generateContent(inputContent).text.toString()
                Log.d("responseTextforImage", responseText)
                val updatedResponse = _messages.value.orEmpty() + ChatQueryResponse(responseText , false , System.currentTimeMillis())
                _messages.value = updatedResponse
                _response.postValue(responseText)
            } catch (e: Exception) {
                Log.e("getResponseFromChatBot", "Error generating content: ${e.message}", e)
            }



        }
    }
}