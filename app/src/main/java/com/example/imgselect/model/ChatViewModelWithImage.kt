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
    private val interpretImage = MutableStateFlow<Bitmap?>(null)

    val interpretImageState: StateFlow<Bitmap?> = interpretImage


    val response: LiveData<String> = _response
    private val _messages = MutableLiveData<List<ChatQueryResponse>>(listOf())
    val messages: LiveData<List<ChatQueryResponse>> = _messages

    private val _interpretUiState:MutableStateFlow<InterpretUiState> = MutableStateFlow(InterpretUiState.Initial)
    val interpretUiState: StateFlow<InterpretUiState> = _interpretUiState
    init {
        val config = generationConfig { temperature = 0.70f }
        generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash-latest",
            apiKey = "AIzaSyC-PzCAvCDvJeHCZMx9YY4mSjq9U6OAYKo",
            generationConfig = config
        )
        Log.d("gemini" , "gemini-pro-vision is initialized")
    }
    override fun onCleared() {
        // Perform cleanup tasks here before the ViewModel is destroyed
        // For example, recycle bitmaps
        _imageList.value.forEach { bitmap ->
            bitmap?.recycle()
        }

        // Call super.onCleared() at the end
        super.onCleared()
    }
    fun setInterpretImage(bitmap: Bitmap?) {
        interpretImage.value = bitmap
    }
    fun clearInterpretImage() {
        interpretImage.value = null
    }
    fun clearImageList() {
        _imageList.value = emptyList()
    }
    fun getInterpretResponseFromChatBot() {
        viewModelScope.launch {
            _interpretUiState.value = InterpretUiState.Loading

            Log.d("imageList", "There is 1 image")
            Log.d("imageList", "Interpret this image")


            val inputContent = content {
                val imageBitmap = interpretImage.value
                if (imageBitmap != null) {
                    image(imageBitmap)
                }

                text("Interpret this image")
            }

            try {
                val responseText = generativeModel.generateContent(inputContent).text.toString()
                Log.d("responseTextforImage", responseText)

                _interpretUiState.value = InterpretUiState.Success(responseText)

            } catch (e: Exception) {
                _interpretUiState.value = InterpretUiState.Error(e.localizedMessage ?: "Error interpreting image")

                Log.e("getResponseFromChatBot", "Error generating content: ${e.message}", e)
            }
        }
    }

    fun getResponseFromChatBot() {
        _interpretUiState.value = InterpretUiState.Loading

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
                _interpretUiState.value = InterpretUiState.Success(responseText)

                val updatedResponse = _messages.value.orEmpty() + ChatQueryResponse(responseText , false , System.currentTimeMillis())
                _messages.value = updatedResponse
                _response.postValue(responseText)
            } catch (e: Exception) {
                _interpretUiState.value = InterpretUiState.Error(e.localizedMessage ?: "Error interpreting image")
                val errorMessage = "Error in Generating content: ${e.localizedMessage ?: "Unknown error"}"
                val errorResponse = _messages.value.orEmpty() + ChatQueryResponse(errorMessage, false, System.currentTimeMillis())
                _messages.value = errorResponse
                _response.postValue(errorMessage)
                Log.e("getResponseFromChatBot", "Error generating content: ${e.message}", e)
            }



        }
    }
    fun addBitmapToList(bitmap: Bitmap?) {
        bitmap?.let {
            val currentList = _imageList.value.toMutableList()
            currentList.add(bitmap)
            _imageList.value = currentList
        }
    }

    fun removeBitmapFromList(bitmap: Bitmap) {
        val currentList = _imageList.value.toMutableList()
        currentList.remove(bitmap)
        _imageList.value = currentList
    }
}
sealed class InterpretUiState {
    object Initial : InterpretUiState()
    object Loading : InterpretUiState()
    data class Success(val responseText: String) : InterpretUiState()
    data class Error(val errorMessage: String) : InterpretUiState()
}