package com.example.imgselect.data

import android.app.appsearch.Migrator
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Meaning::class , Summary::class] , version = 3 , exportSchema = false)
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
