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




}

@Dao
interface LocalStorageDaoForChats {
    @Insert
    fun addChat(chat: Chat)

    @Query("SELECT * FROM content_table_chatbot")
    fun readAllChat(): LiveData<List<Chat>>
}