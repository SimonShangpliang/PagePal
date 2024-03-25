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
import com.example.imgselect.model.DiscussUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNull.content
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.net.SocketException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.reflect.KProperty

class DictionaryViewModel(application: Application) : AndroidViewModel(application) {

    private val readAllMeaning: LiveData<List<Meaning>>
    private val repository: LocalStorageRepository

    var response : List<WordData>? by mutableStateOf(null)
    var word: String by mutableStateOf("")
    var wrongWord: Boolean by mutableStateOf(false)
    var listMeaning: List<WordData>? by mutableStateOf(listOf())
    var title: String by mutableStateOf("Others")
    var dialogVisible: Boolean by mutableStateOf(false)
    var setOfTitle: MutableSet<String> = mutableSetOf()
    var setOfDates: MutableSet<String> = mutableSetOf()
    var setOfWords: MutableSet<String> = mutableSetOf()
    var whichMeaning: Int by mutableStateOf(0)
    var uiState: MutableState<WordMeaningUiState> = mutableStateOf(WordMeaningUiState.Initial)

    init {
        val localStorageDao = LocalStorageDatabase.getDatabase(application).userDao()
        repository = LocalStorageRepository(localStorageDao)
        readAllMeaning = repository.readAllMeaning
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun processResponse(): List<WordData>? {
        return suspendCoroutine { continuation ->
            val result = response
//                ?.flatMap { response ->
//                response.meanings.flatMap { meaning ->
//                    meaning.definitions.map { definition ->
//                        definition.definition
//                    }
//                }
//            } ?: emptyList()

            // Resuming coroutine with the result
            continuation.resume(result)

        }
    }

    suspend fun getMeaning(): List<WordData> {
        uiState.value = WordMeaningUiState.Loading

        val sanitizedWord = word.filter { it.isLetter() || it.isWhitespace() }

        return suspendCoroutine { continuation ->
            val job = viewModelScope.launch {
                try {
                    wrongWord = false
                    response = DictionaryApi.retrofitService.getMeaning(sanitizedWord)
                    Log.d(TAG , "Raw Json Response: $response")
                    uiState.value = WordMeaningUiState.Success("Success")

                    continuation.resume(response ?: emptyList())
                } catch(e: IOException) {
                    Log.d(TAG , "its an error")
                    uiState.value = WordMeaningUiState.Error(e.message ?: "Unknown error occurred")

                    continuation.resumeWithException(e)
                } catch(e: HttpException) {
                    Log.d(TAG , "Word doesn't exist")
                    wrongWord = true
                    uiState.value = WordMeaningUiState.Error("Word meaning doesn't exist")

                    continuation.resume(emptyList()) // or handle differently based on your requirement
                }catch(e: SocketException) {
                    Log.d(TAG , "its an error")
                    uiState.value = WordMeaningUiState.Error(e.message ?: "Unknown error occurred")

                    continuation.resumeWithException(e)
                }
            }

            // Register a cancellation callback

        }
    }


    fun saveMeaning() {
        viewModelScope.launch(Dispatchers.IO) {
            val current = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy/MM/dd")
            val date = dateFormat.format(current.time)
            val content = Meaning(0, listMeaning , date , title)
            Log.d("MeaningOnSuccess" , "Content: ${content}")
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


sealed interface WordMeaningUiState {
    object Initial : WordMeaningUiState
    object Loading : WordMeaningUiState
    data class Success(val outputText: String) : WordMeaningUiState
    data class Error(val error: String) : WordMeaningUiState
}

