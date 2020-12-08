package com.example.szefapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.szefapp.persistence.task.TaskDao
import com.example.szefapp.ui.addtask.AddTaskViewModel
import com.example.szefapp.ui.tasklist.TaskListViewModel

class ViewModelFactory(private val dataSource: TaskDao): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TaskListViewModel::class.java)){
            return TaskListViewModel(dataSource) as T
        }
        if(modelClass.isAssignableFrom(AddTaskViewModel::class.java)){
            return AddTaskViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown VieModel class")
    }
}