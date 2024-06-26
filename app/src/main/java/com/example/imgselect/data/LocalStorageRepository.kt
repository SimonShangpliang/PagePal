package com.example.imgselect.data

import androidx.lifecycle.LiveData
import com.example.imgselect.DictionaryNetwork.WebsiteCount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalStorageRepository(private val localStorageDao: LocalStorageDao) {

    val readAllMeaning: LiveData<List<Meaning>> = localStorageDao.readAllMeaning()

    suspend fun addMeaning(meaning: Meaning) {
        localStorageDao.addMeaning(meaning)
    }

    val readAllSummary: LiveData<List<Summary>> = localStorageDao.readAllSummary()


    suspend fun addSummary(summary: Summary) {
        localStorageDao.addSummary(summary)
    }

    fun deleteSummary(summary: Summary) {
        localStorageDao.deleteSummary(summary)
    }

    suspend fun getSummary(summaryId: Int): Summary {
        return  withContext(Dispatchers.IO) {
            localStorageDao.getSummary(summaryId)
        }

    }



}

class LocalStorageRepositoryForChats(private val localStorageDaoForChats: LocalStorageDaoForChats) {
    fun addChat(chat: Chat) {
        localStorageDaoForChats.addChat(chat)
    }

    val readAllChat: LiveData<List<Chat>> = localStorageDaoForChats.readAllChat()

    suspend fun getChat(chatId: Int): Chat {
        return  withContext(Dispatchers.IO) {
            localStorageDaoForChats.getChat(chatId)
        }

    }
    fun deleteChat(chat: Chat) {
        localStorageDaoForChats.deleteChat(chat)
    }


}

class LocalStorageRepositoryForWeb(private val webDao: WebDao) {
    fun addWeb(web: Web) {
        webDao.addWeb(web)
    }
    fun addWebBookMarked(web: WebBookMarked) {
        webDao.addBookMarkedWeb(web)
    }

    fun deleteWebBookMarked(web: WebBookMarked) {
        webDao.deleteBookMarkedWeb(web)
    }



    val readAllWeb: LiveData<List<Web>> = webDao.readAllWeb()
    val readBookmarkedWeb: LiveData<List<WebBookMarked>> = webDao.readBookmarkedWeb()

    fun deleteWeb(web: Web) {
        webDao.deleteWeb(web)
    }



    suspend fun getWebsiteCounts(): List<WebsiteCount> {

        return webDao.getWebsiteCounts()
    }
}