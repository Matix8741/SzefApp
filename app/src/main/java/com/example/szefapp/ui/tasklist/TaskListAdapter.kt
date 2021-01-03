package com.example.szefapp.ui.tasklist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.szefapp.databinding.TaskLayoutBinding
import com.example.szefapp.persistence.task.TaskEntity


class TaskListAdapter(
    private var taskList: MutableList<TaskEntity>,
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
        return TaskListViewHolder(binding)
    }


    fun updateData(taskList: Array<TaskEntity>) {
        this.taskList = taskList.toMutableList()
        notifyDataSetChanged()
    }

    fun addDataItem(task: TaskEntity) {
        this.taskList.add(taskList.size, task)
        notifyItemInserted(taskList.size - 1)
    }

    override fun onBindViewHolder(holder: TaskListViewHolder, position: Int) {
        val task = taskList[position]
        holder.taskBinding.taskText = task.text
        holder.taskBinding.isDone = task.isDone
        holder.taskBinding.deleteButton.setOnClickListener {
            listener.onDelete(task.id)
            val deletePosition = taskList.indexOf(task);
            if (deletePosition > -1) {
                taskList.removeAt(deletePosition)
                notifyItemRemoved(deletePosition)
            }
        }
        holder.taskBinding.checkbox.setOnClickListener { view ->

            if ((view as CheckBox).isChecked) {
                holder.taskBinding.isDone = true
                task.lastDoneDate = System.currentTimeMillis()
                task.isDone = true
            } else {
                val fiveMinutesInMilliseconds = 5 * 60 * 1000
                if (taskList[position].lastDoneDate + fiveMinutesInMilliseconds > System.currentTimeMillis()) {
                    holder.taskBinding.isDone = false
                    task.lastDoneDate = System.currentTimeMillis()
                    task.isDone = false
                } else {
                    view.isChecked = true
                    view.isClickable = false
                }
            }
            listener.onUpdate(task)
            notifyItemChanged(taskList.indexOf(task), task)
        }
    }

    override fun getItemCount() = taskList.size

    interface TaskListener {
        fun onDelete(id: String)
        fun onUpdate(task: TaskEntity)
    }
}