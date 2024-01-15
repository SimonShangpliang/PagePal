package com.example.dictionary.model


import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imgselect.DictionaryNetwork.DictionaryApi
import com.example.imgselect.DictionaryNetwork.WordData
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException
import kotlin.reflect.KProperty

class DictionaryViewModel : ViewModel() {
    var response : List<WordData>? by mutableStateOf(null)
    var word: String by mutableStateOf("")
    var wrongWord: Boolean by mutableStateOf(false)


    fun getMeaning() {
        viewModelScope.launch {
            try {
                wrongWord = false
                response = DictionaryApi.retrofitService.getMeaning(word)
                Log.d(TAG , "Raw Json Response: $response")


            } catch(e: IOException) {
                Log.d(TAG , "its an error")

            } catch(e: HttpException) {
                Log.d(TAG , "Word doesn't exist")

                wrongWord = true
            }

        }
    }
}


