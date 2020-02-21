package com.example.szefapp.ui.task

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.szefapp.AppProvider
import com.example.szefapp.R
import com.example.szefapp.ViewModelFactory
import com.example.szefapp.persistence.task.TaskEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TaskListFragment : Fragment(), TaskListAdapter.TaskListener {

    private lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: TaskListViewModel by viewModels { viewModelFactory }

    private val taskDisposable = CompositeDisposable()
    private lateinit var taskListAdapter: TaskListAdapter

    companion object {
        fun newInstance() = TaskListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.task_list_fragment, container, false)
        viewModelFactory = AppProvider.provideViewModelFactory(requireContext())
        val viewManager = LinearLayoutManager(requireContext())
        taskListAdapter = TaskListAdapter(arrayOf(), this)
        view.findViewById<RecyclerView>(R.id.task_list).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = taskListAdapter
        }
        val editText = view.findViewById<EditText>(R.id.new_task_description)
//        val numberPicker = view.findViewById<NumberPicker>(R.id.number_picker)
//        numberPicker.minValue = 0
//        numberPicker.maxValue = 30
//        numberPicker.wrapSelectorWheel = false
        view.findViewById<Button>(R.id.add_new_task_button).setOnClickListener {
            val newTask =
                TaskEntity(text = editText.text.toString(), isDone = false, refreshTimer = 0)
            editText.text.clear()
            viewModel.updateTask(newTask)
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        taskDisposable.add(
            viewModel.getTasks().subscribeOn(Schedulers.io()).observeOn(
                AndroidSchedulers.mainThread()
            ).subscribe({ taskListAdapter.updateData(it) }, { error ->
                Log.e("error", error.message)
            })
        )
    }

    override fun onStop() {
        super.onStop()
        taskDisposable.clear()
    }

    override fun onDelete(id: String) {
        viewModel.deleteTask(id)
    }

    override fun onUpdate(task: TaskEntity) {
        viewModel.updateTask(task)
    }

}
