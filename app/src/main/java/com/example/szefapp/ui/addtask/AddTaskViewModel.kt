package com.example.szefapp.ui.addtask

import com.example.szefapp.persistence.task.TaskDao
import com.example.szefapp.persistence.task.TaskEntity
import com.example.szefapp.ui.core.BaseViewModel

class AddTaskViewModel(private val dataSource: TaskDao) : BaseViewModel() {

    private var newTask = TaskEntity(isDone = false, refreshTimer = 0, text = "")

    fun updateTask(task: TaskEntity) {
        load { dataSource.insertTask(task) }
    }

    fun addNewTask() {
        updateTask(newTask)
    }

    fun changeDescriptionOfNewTask(text: String) {
        newTask =
            TaskEntity(isDone = newTask.isDone, refreshTimer = newTask.refreshTimer, text = text)
    }
}