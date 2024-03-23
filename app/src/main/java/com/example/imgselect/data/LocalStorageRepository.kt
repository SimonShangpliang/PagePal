package com.example.imgselect.data

import androidx.lifecycle.LiveData

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



}

class LocalStorageRepositoryForChats(private val localStorageDaoForChats: LocalStorageDaoForChats) {
    fun addChat(chat: Chat) {
        localStorageDaoForChats.addChat(chat)
    }

    val readAllChat: LiveData<List<Chat>> = localStorageDaoForChats.readAllChat()


}