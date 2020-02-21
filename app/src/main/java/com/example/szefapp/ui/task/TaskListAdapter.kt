package com.example.szefapp.ui.task

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.szefapp.R
import com.example.szefapp.databinding.TaskLayoutBinding
import com.example.szefapp.persistence.task.TaskEntity


class TaskListAdapter(
    private var taskList: Array<TaskEntity>,
    private val listener: TaskListener
) :
    RecyclerView.Adapter<TaskListAdapter.TaskListViewHolder>() {

    class TaskListViewHolder(val taskBinding: TaskLayoutBinding) :
        RecyclerView.ViewHolder(taskBinding.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TaskLayoutBinding.inflate(layoutInflater, parent, false)
        val taskView = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_layout, parent, false) as ConstraintLayout
        return TaskListViewHolder(binding)
    }


    fun updateData(taskList: Array<TaskEntity>) {
        this.taskList = taskList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: TaskListViewHolder, position: Int) {
        holder.taskBinding.taskText = taskList[position].text
        holder.taskBinding.isDone = taskList[position].isDone
        holder.taskBinding.deleteButton.setOnClickListener {
            listener.onDelete(
                taskList[position].id
            )
        }
        holder.taskBinding.checkbox.setOnClickListener { view ->
            val modifiedTask = taskList[position]
            if ((view as CheckBox).isChecked) {
                holder.taskBinding.isDone = true
                modifiedTask.modificationTime = System.currentTimeMillis()
                modifiedTask.isDone = true
            } else {
                val fiveMinutesInMilliseconds = 5 * 60 * 1000
                if (taskList[position].modificationTime + fiveMinutesInMilliseconds > System.currentTimeMillis()) {
                    holder.taskBinding.isDone = false
                    modifiedTask.modificationTime = System.currentTimeMillis()
                    modifiedTask.isDone = false
                } else {
                    view.isChecked = true
                    view.isClickable = false
                }
            }
            listener.onUpdate(modifiedTask)
        }
    }

    override fun getItemCount() = taskList.size

    interface TaskListener {
        fun onDelete(id: String)
        fun onUpdate(task: TaskEntity)
    }
}