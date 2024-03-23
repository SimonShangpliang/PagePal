package com.example.imgselect.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LocalStorageDao {

    @Insert
    fun addMeaning(meaning: Meaning)

    @Query("SELECT * FROM content_table_wordMeaning")
    fun readAllMeaning(): LiveData<List<Meaning>>

    @Insert
    fun addSummary(summary: Summary)

    @Query("SELECT * FROM content_table_summary")
    fun readAllSummary(): LiveData<List<Summary>>

    @Delete
    fun deleteSummary(summary: Summary)

    @Query("SELECT * FROM content_table_summary WHERE id = :summaryId")
    fun getSummary(summaryId: Int): Summary




}

@Dao
interface LocalStorageDaoForChats {
    @Insert
    fun addChat(chat: Chat)

    @Query("SELECT * FROM content_table_chatbot")
    fun readAllChat(): LiveData<List<Chat>>

    @Query("SELECT * FROM content_table_chatbot WHERE id = :chatId")
    fun getChat(chatId: Int): Chat

    @Delete
    fun deleteChat(chat: Chat)
}

@Dao
interface WebDao {
    @Insert
    fun addWeb(web: Web)

    @Query("SELECT * FROM content_table_webHistory")
    fun readAllWeb(): LiveData<List<Web>>

    @Delete
    fun deleteWeb(web: Web)
}