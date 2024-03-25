package com.example.imgselect.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imgselect.DictionaryNetwork.WebsiteCount
import com.example.imgselect.data.LocalStorageDatabase
import com.example.imgselect.data.LocalStorageDatabaseForChat
import com.example.imgselect.data.LocalStorageRepositoryForChats
import com.example.imgselect.data.LocalStorageRepositoryForWeb
import com.example.imgselect.data.Web
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class WebHistoryViewModel(application: Application): AndroidViewModel(application) {
    private val repository: LocalStorageRepositoryForWeb
     val webHistory: LiveData<List<Web>>
    private val _websiteCounts = MutableLiveData<List<WebsiteCount>>()
    val websiteCounts: LiveData<List<WebsiteCount>> get() = _websiteCounts


    init {
        val localStorageDaoForWeb = LocalStorageDatabase.getDatabase(application).webDao()
        repository = LocalStorageRepositoryForWeb(localStorageDaoForWeb)
        webHistory   = repository.readAllWeb
        viewModelScope.launch {
            _websiteCounts.value = repository.getWebsiteCounts()
        }

    }



    // Function to add a web history entry
    fun addWeb(web: Web) {
        CoroutineScope(Dispatchers.IO).launch {
        repository.addWeb(web)}
    }

    // Function to delete a web history entry
    fun deleteWeb(web: Web) {
        CoroutineScope(Dispatchers.IO).launch {

        repository.deleteWeb(web)}
    }
}
