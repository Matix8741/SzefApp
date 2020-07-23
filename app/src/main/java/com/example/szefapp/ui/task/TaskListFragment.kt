package com.example.szefapp.ui.task

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.szefapp.AppProvider
import com.example.szefapp.ViewModelFactory
import com.example.szefapp.databinding.TaskListFragmentBinding
import com.example.szefapp.persistence.task.TaskEntity
import com.example.szefapp.ui.general.NumberPickerDialogFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TaskListFragment : Fragment(), TaskListAdapter.TaskListener,
    NumberPickerDialogFragment.NoticeDialogListener {

    private lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: TaskListViewModel by viewModels { viewModelFactory }

    private val taskDisposable = CompositeDisposable()
    private lateinit var taskListAdapter: TaskListAdapter
    private lateinit var binding: TaskListFragmentBinding

    companion object {
        fun newInstance() = TaskListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val layoutInflater = LayoutInflater.from(context)
        binding = TaskListFragmentBinding.inflate(layoutInflater, container, false)
        binding.newTaskDays = 0
        viewModelFactory = AppProvider.provideViewModelFactory(requireContext())
        val viewManager = LinearLayoutManager(requireContext())
        taskListAdapter = TaskListAdapter(mutableListOf(), this)
        binding.taskList.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = taskListAdapter
        }
        binding.addDaysButton.setOnClickListener {
            NumberPickerDialogFragment(this, binding.newTaskDays!!).show(
                activity!!.supportFragmentManager,
                "numberPicker"
            )
        }
        binding.addNewTaskButton.setOnClickListener {
            val newTask =
                TaskEntity(
                    text = binding.newTaskDescription.text.toString(),
                    isDone = false,
                    refreshTimer = binding.newTaskDays!!
                )
            binding.newTaskDescription.text.clear()
            viewModel.updateTask(newTask)
            taskListAdapter.addDataItem(newTask)
            binding.taskList.scrollToPosition(taskListAdapter.itemCount-1)
        }
        return binding.root
    }

    private fun refreshTasks() {
        val currentDateInMilliseconds = System.currentTimeMillis()
        for(task in viewModel.getTasks().blockingGet()){
            if(task.refreshTimer > 0){
                val refreshMilliseconds = task.refreshTimer *24*60 * 60 * 1000
                if(currentDateInMilliseconds >= task.modificationTime + refreshMilliseconds){
                    task.isDone = false
                    viewModel.updateTask(task)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        refreshTasks()
        taskDisposable.add(
            viewModel.getTasks().subscribeOn(Schedulers.io()).observeOn(
                AndroidSchedulers.mainThread()
            ).subscribe({ taskListAdapter.updateData(it) }, { error ->
                Log.e("error", error.message!!)
            })
        )
    }

    override fun onStop() {
        super.onStop()
        taskDisposable.clear()
    }

    override fun onDelete(id: String) {
        viewModel.deleteTask(id)
        binding.taskList.scrollToPosition(taskListAdapter.itemCount-1)
    }

    override fun onUpdate(task: TaskEntity) {
        viewModel.updateTask(task)
    }

    override fun onDialogPositiveClick(selectedNumber: Int) {
        binding.newTaskDays = selectedNumber
    }

}
