package com.example.szefapp.ui.tasklist

import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.szefapp.R
import com.example.szefapp.databinding.TaskListFragmentBinding
import com.example.szefapp.persistence.task.TaskEntity
import com.example.szefapp.ui.core.BaseFragment
import com.example.szefapp.ui.general.NumberPickerDialogFragment
import java.text.SimpleDateFormat
import java.util.*


@ExperimentalStdlibApi
class TaskListFragment : BaseFragment<TaskListViewModel, TaskListFragmentBinding>(),
    TaskListAdapter.TaskListener,
    NumberPickerDialogFragment.NoticeDialogListener {

    override fun getViewModelClass(): Class<out TaskListViewModel> {
        return TaskListViewModel::class.java
    }

    private val doneTaskAnimator = ValueAnimator.ofInt()

    private lateinit var taskListAdapter: TaskListAdapter

    override fun getBindingClass(
        layoutInflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): TaskListFragmentBinding {
        return TaskListFragmentBinding.inflate(layoutInflater, container, attachToRoot)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        initTaskList()
        initAnimations()
        binding.rewardText.text =
            activity?.getPreferences(Context.MODE_PRIVATE)?.getInt("POINTS", 0).toString()
        binding.textDayOfTheWeek.text = getCurrentDay()
        return binding.root
    }


    private fun initTaskList() {
        taskListAdapter = TaskListAdapter(mutableListOf(), this)
        binding.openTaskList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = taskListAdapter
        }
        binding.openTaskList.scrollToPosition(taskListAdapter.itemCount - 1)
    }

    private fun initAnimations() {
        doneTaskAnimator.duration =
            resources.getInteger(R.integer.default_animation_duration).toLong()
        doneTaskAnimator.addUpdateListener { animation ->
            binding.rewardText.text = animation.animatedValue.toString()
        }
    }

    private fun startDoneTaskAnimation(startValue: Int, endValue: Int) {
        doneTaskAnimator.setIntValues(startValue, endValue)
        doneTaskAnimator.start()

    }

    private fun getCurrentDay(): String {
        val simpleDateFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        return simpleDateFormat.format(Date()).capitalize(Locale.getDefault())
    }

    override fun onStart() {
        super.onStart()
        val navController = findNavController()
        binding.fab.setOnClickListener {
            navController.navigate(R.id.action_taskListFragment_to_addTaskFragment)
        }
        subscribe(viewModel::getTasks, this::onGetTasks)
    }

    private fun onGetTasks(tasks: Array<TaskEntity>) {
        taskListAdapter.updateData(tasks)
    }

    override fun onDelete(id: String) {
        viewModel.deleteTask(id)
        binding.openTaskList.scrollToPosition(taskListAdapter.itemCount - 1)
    }

    override fun onUpdate(task: TaskEntity) {
        viewModel.updateTask(task)
    }

    override fun onDialogPositiveClick(selectedNumber: Int) {
        binding.newTaskDays = selectedNumber
    }

}
