package com.example.szefapp.ui.general

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import com.example.szefapp.R

class NumberPickerDialogFragment(
    private val listener: NoticeDialogListener,
    private val initValue: Int
) : DialogFragment() {


    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    interface NoticeDialogListener {
        fun onDialogPositiveClick(selectedNumber: Int)
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.pick_days_to_reload)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.number_picker_dialog, null)
            val numberPicker = view.findViewById<NumberPicker>(R.id.numberPicker)
            numberPicker.maxValue = 30
            numberPicker.minValue = 0
            numberPicker.value = initValue
            numberPicker.setOnValueChangedListener { _, _, selectedNumber ->
                listener.onDialogPositiveClick(selectedNumber)
            }
            builder.setView(view)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be NULL")
    }
}