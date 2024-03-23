package com.example.imgselect.data

import android.app.appsearch.Migrator
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Meaning::class , Summary::class] , version = 4 , exportSchema = false)
abstract class LocalStorageDatabase: RoomDatabase() {


    abstract fun userDao() : LocalStorageDao

    companion object {


        @Volatile
        private var INSTANCE: LocalStorageDatabase? = null

        fun getDatabase(context: Context) : LocalStorageDatabase {
            val tempInstant = INSTANCE
            if(tempInstant != null) {
                return tempInstant
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalStorageDatabase::class.java,
                    "content_table"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

@Database(entities = [Chat::class] , version = 1 , exportSchema = false)
@TypeConverters(Converters::class)
abstract class LocalStorageDatabaseForChat: RoomDatabase() {
    abstract fun userDaoForChats() : LocalStorageDaoForChats

    companion object {


        @Volatile
        private var INSTANCE: LocalStorageDatabaseForChat? = null

        fun getDatabase(context: Context) : LocalStorageDatabaseForChat {
            val tempInstant = INSTANCE
            if(tempInstant != null) {
                return tempInstant
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalStorageDatabaseForChat::class.java,
                    "content_table_for_chats"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

