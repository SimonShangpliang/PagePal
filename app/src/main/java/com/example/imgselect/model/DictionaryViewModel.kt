package com.example.dictionary.model


import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imgselect.DictionaryNetwork.DictionaryApi
import com.example.imgselect.DictionaryNetwork.WordData
import com.example.imgselect.data.LocalStorageDatabase
import com.example.imgselect.data.LocalStorageRepository
import com.example.imgselect.data.Meaning
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNull.content
import retrofit2.HttpException
import java.io.IOException
import kotlin.reflect.KProperty

class DictionaryViewModel(application: Application) : AndroidViewModel(application) {

    private val readAllMeaning: LiveData<List<Meaning>>
    private val repository: LocalStorageRepository

    var response : List<WordData>? by mutableStateOf(null)
    var word: String by mutableStateOf("")
    var wrongWord: Boolean by mutableStateOf(false)

    init {
        val localStorageDao = LocalStorageDatabase.getDatabase(application).userDao()
        repository = LocalStorageRepository(localStorageDao)
        readAllMeaning = repository.readAllMeaning
    }


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

    fun saveMeaning() {
        viewModelScope.launch(Dispatchers.IO) {
            val content = Meaning(0,word , response.toString())
            Log.d("word" , "${word}")
            Log.d("meaning" , "${response}")
            repository.addMeaning(content)
        }
    }

    fun getMeaningList(): LiveData<List<Meaning>> {
        val meaningList = repository.readAllMeaning
        return meaningList
    }
}


