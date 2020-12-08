package com.example.szefapp.ui.tasklist

import com.example.szefapp.persistence.task.TaskDao
import com.example.szefapp.persistence.task.TaskEntity
import com.example.szefapp.ui.core.BaseViewModel
import io.reactivex.Single

class TaskListViewModel(private val dataSource: TaskDao) : BaseViewModel() {

    fun getTasks(): Single<Array<TaskEntity>> = dataSource.getTasks().firstOrError()

    fun updateTask(task: TaskEntity) {
        load { dataSource.insertTask(task) }
    }

    fun deleteTask(id: String) {
        load { dataSource.deleteTask(id) }
    }

}
