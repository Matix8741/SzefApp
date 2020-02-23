package com.example.szefapp.ui.task

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.szefapp.persistence.task.TaskDao
import com.example.szefapp.persistence.task.TaskEntity
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class TaskListViewModel(private val dataSource: TaskDao) : ViewModel() {

    private val job = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + job)

fun getTasks(): Observable<Array<TaskEntity>> = dataSource.getTasks()

    fun updateTask(task: TaskEntity){
        uiScope.launch { try {
            dataSource.insertTask(task).subscribeOn(Schedulers.io()).subscribe()
        } catch (e:Exception){
            Log.e("e: ", e.message!!)
        }}
    }
    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun deleteTask(id: String) {
        uiScope.launch { try {
            dataSource.deleteTask(id).subscribeOn(Schedulers.io()).subscribe()
        } catch (e:Exception){
            Log.e("e: ", e.message!!)
        }}

    }


}
