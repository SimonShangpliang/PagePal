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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch

class ChatViewModelWithImage: ViewModel() {

    private var generativeModel: GenerativeModel
    var query: String by mutableStateOf("")
    private val _imageList = MutableStateFlow<List<Bitmap?>>(emptyList())
    val imageList: StateFlow<List<Bitmap?>> = _imageList
    private val _response = MutableLiveData<String>()
    val response: LiveData<String> = _response
    var isImageSelected: Boolean by mutableStateOf(false)
    private val _messages = MutableLiveData<List<ChatQueryResponse>>(listOf())
    val messages: LiveData<List<ChatQueryResponse>> = _messages

    init {
        val config = generationConfig { temperature = 0.70f }
        generativeModel = GenerativeModel(
            modelName = "gemini-pro-vision",
            apiKey = "AIzaSyAOgrCj3x9WE8JoSDB5yuoGqH7m4Rn0IWI",
            generationConfig = config
        )
        Log.d("gemini" , "gemini-pro-vision is initialized")
    }
    fun clearImageList() {
        _imageList.value = emptyList()
        isImageSelected = false // Update the state to indicate that no image is selected
    }

    fun getResponseFromChatBot() {
        viewModelScope.launch {
            Log.d("imageList" , "There are ${imageList.value.size} images")
            Log.d("imageList" , query)
            val updatedMessages = _messages.value.orEmpty() + ChatQueryResponse(query, true , System.currentTimeMillis())
            _messages.value = updatedMessages
            val inputContent = content {
                imageList.value.forEach {
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
    fun addBitmapToList(bitmap: Bitmap?) {
        bitmap?.let {
            val currentList = _imageList.value.toMutableList()
            currentList.add(bitmap)
            _imageList.value = currentList
            isImageSelected = true // Update the state to indicate that an image is selected
        }
    }

    fun removeBitmapFromList(bitmap: Bitmap) {
        val currentList = _imageList.value.toMutableList()
        currentList.remove(bitmap)
        _imageList.value = currentList
        isImageSelected = currentList.isNotEmpty() // Update the state based on whether images are present
    }
}