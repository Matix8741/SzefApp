package com.example.szefapp.ui.addtask

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.navigation.fragment.findNavController
import com.example.szefapp.R
import com.example.szefapp.databinding.FragmentAddTaskBinding
import com.example.szefapp.persistence.task.TaskEntity
import com.example.szefapp.ui.core.BaseFragment
import java.text.SimpleDateFormat
import java.util.*

class AddTaskFragment : BaseFragment<AddTaskViewModel, FragmentAddTaskBinding>(),
    DatePickerDialog.OnDateSetListener {
    override fun getBindingClass(
        layoutInflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): FragmentAddTaskBinding {
        return FragmentAddTaskBinding.inflate(layoutInflater, container, attachToRoot)
    }

    override fun getViewModelClass(): Class<out AddTaskViewModel> {
        return AddTaskViewModel::class.java
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addNewTaskConfirmButton.setOnClickListener {
            viewModel.updateTask(
                TaskEntity(
                    isDone = false,
                    text = binding.addNewTaskDescription.editText?.text.toString()
                )
            )
            findNavController().popBackStack()
        }
        binding.addNewTaskDateText.text = getCurrentDate();
        binding.addNewTaskDateText.setOnClickListener {
            val datePickerFragment = DatePickerDialog(requireContext())
            datePickerFragment.setOnDateSetListener(this)
            datePickerFragment.show()
        }
        binding.addNewTaskRepeatCheckBox.setOnCheckedChangeListener { compoundButton, b ->
            binding.repeated = b
        }
    }

    private fun getCurrentDate(): String {
        val simpleDateFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
        return simpleDateFormat.format(Date())
    }

    override fun onDateSet(picker: DatePicker?, year: Int, monthMinusOne: Int, day: Int) {
        binding.addNewTaskDateText.text =
            getString(R.string.date_format, day, monthMinusOne + 1, year)
    }
}