package com.example.szefapp

import android.content.Context
import com.example.szefapp.persistence.AppDatabase
import com.example.szefapp.persistence.task.TaskDao

object AppProvider {

    private fun provideTaskDataSource(context: Context): TaskDao {
        val database = AppDatabase.getInstance(context)
        return database.taskDao()
    }

    fun provideViewModelFactory(context: Context): ViewModelFactory {
        val dataSource = provideTaskDataSource(context)
        return ViewModelFactory(dataSource)
    }
}