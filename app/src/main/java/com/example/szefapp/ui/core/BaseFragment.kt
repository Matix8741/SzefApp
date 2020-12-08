package com.example.szefapp.ui.core

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.szefapp.AppProvider
import com.example.szefapp.ViewModelFactory
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

abstract class BaseFragment<T : BaseViewModel, P : ViewDataBinding> : Fragment() {

    private val taskDisposable = CompositeDisposable()

    protected lateinit var binding: P

    private lateinit var viewModelFactory: ViewModelFactory

    protected lateinit var viewModel: T

    abstract fun getViewModelClass(): Class<out T>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getBindingClass(inflater, container, false)
        viewModelFactory = AppProvider.provideViewModelFactory(requireContext())
        viewModel = viewModelFactory.create(getViewModelClass())
        return binding.root
    }

    abstract fun getBindingClass(
        layoutInflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): P

    protected fun <T> subscribe(
        func: () -> Single<T>,
        onSuccess: (t: T) -> Unit,
        onError: (e: Throwable) -> Unit = { error ->
            Log.e(
                "error",
                error.message!!
            )
        }
    ) {
        taskDisposable.add(
            func().subscribeOn(Schedulers.io()).observeOn(
                AndroidSchedulers.mainThread()
            ).subscribe(onSuccess, onError)
        )
    }

    override fun onStop() {
        super.onStop()
        taskDisposable.clear()
    }
}