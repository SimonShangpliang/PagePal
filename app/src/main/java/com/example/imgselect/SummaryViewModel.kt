package com.example.imgselect

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonNull.content
import java.lang.Exception

class SummaryViewModel: ViewModel() {

    private val _uiState: MutableStateFlow<DiscussUiState> = MutableStateFlow(DiscussUiState.Initial)
    val uiState=_uiState.asStateFlow()
    private var generativeModel: GenerativeModel
    init{
        val config= generationConfig {temperature=0.70f  }
        generativeModel= GenerativeModel(
            modelName = "gemini-pro",
            apiKey = "AIzaSyDt-sDiRFzo203g38_safthogeiXCZFTqM",
            generationConfig = config
        )
    }
    fun questioning(userInput:String){
        _uiState.value=DiscussUiState.Loading
        val prompt=userInput
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val content= content{

                    text(prompt)
                }
                var output=""
                output+= generativeModel.generateContent(content).text
                _uiState.value =DiscussUiState.Success(output)
                Log.d("MainActivity","Response done")

//                generativeModel.generateContentStream(content).collect{
//output+=it.text
//                    _uiState.value =HomeUiState.Success(output)
//                }
                Log.d("MainActivity",output)

            }catch (e: Exception){
                _uiState.value=DiscussUiState.Error(e.localizedMessage?:"Error in Generating content")
                Log.d("MainActivity",e.localizedMessage)
            }

        }

    }
}
sealed interface DiscussUiState{
    object Initial: DiscussUiState
    object Loading: DiscussUiState
    data class Success(val outputText:String): DiscussUiState
    data class Error(val error:String): DiscussUiState

}