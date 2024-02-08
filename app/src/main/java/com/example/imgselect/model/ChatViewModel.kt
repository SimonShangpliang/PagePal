package com.example.imgselect.model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.launch

class ChatViewModel: ViewModel() {


    private var generativeModel: GenerativeModel
    var query: String by mutableStateOf("")
    private val _response = MutableLiveData<String>()
    val response: LiveData<String> = _response



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
            val responseText = generativeModel.generateContent(query).text.toString()
            _response.postValue(responseText)
            Log.d("ChatViewModel", "Response updated to: $response")
        }
    }


}