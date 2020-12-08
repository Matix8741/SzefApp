package com.example.szefapp.ui.core

import android.util.Log
import androidx.lifecycle.ViewModel
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val job = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + job)


    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    protected fun load(loader: () -> Completable) {
        uiScope.launch {
            try {
                loader().subscribeOn(Schedulers.io()).subscribe()
            } catch (e: Exception) {
                Log.e("e: ", e.message!!)
            }
        }
    }
}