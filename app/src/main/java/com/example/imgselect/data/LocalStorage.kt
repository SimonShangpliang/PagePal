package com.example.imgselect.data

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "content_table_wordMeaning")
data class Meaning(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val word: String?,
    val meaning: String?,

    )

@Entity(tableName = "content_table_summary")
data class Summary(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val summary: String?,
    val image: ByteArray?
)