package com.example.szefapp.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.szefapp.persistence.task.TaskConverter
import com.example.szefapp.persistence.task.TaskDao
import com.example.szefapp.persistence.task.TaskEntity


@Database(entities = [TaskEntity::class], version = 1)
@TypeConverters(TaskConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase = INSTANCE ?: synchronized(this){
            INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
        }
        private fun buildDatabase(context: Context) = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "App.db").build()
    }
}