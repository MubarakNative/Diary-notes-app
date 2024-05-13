package com.mubarak.madexample.ui.reminders

import android.app.Dialog
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.mubarak.madexample.R
import com.mubarak.madexample.databinding.FragmentReminderDialogBinding
import java.util.Calendar

class ReminderDialogFragment : DialogFragment() {

    private var _binding: FragmentReminderDialogBinding? = null
    private val binding: FragmentReminderDialogBinding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        _binding = FragmentReminderDialogBinding.inflate(layoutInflater, null, false)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(binding.root)
            .setPositiveButton(getString(R.string.ok)) { _, _ ->

            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        binding.tvPickDate.setOnClickListener {
            openDatePicker()
        }

        binding.tvPickTime.setOnClickListener {
            openTimePicker()
        }
        dialog.show()
        return dialog
    }

    private fun openDatePicker() {
        val picker = MaterialDatePicker.Builder.datePicker()
            .setCalendarConstraints(
                CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointForward.now()).build()
            )
            .setTitleText("Pick a date")
            .setPositiveButtonText(getString(R.string.ok))
            .setNegativeButtonText(getString(R.string.cancel))

            .build()

        picker.addOnPositiveButtonClickListener {
            // Respond to positive button click.
            Log.d("picker", "Date Picker: ${picker.selection}")
            Toast.makeText(requireContext(), picker.selection.toString(), Toast.LENGTH_SHORT).show()

        }

        picker.show(childFragmentManager, "ewd")

    }

    private fun openTimePicker() {
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(if (is24HourFormat(requireContext())) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H)
            .setTitleText("Pick a time")
            .setPositiveButtonText(getString(R.string.ok))
            .setNegativeButtonText(getString(R.string.cancel))

            .build()
        timePicker.show(childFragmentManager, "TimePicker")
        timePicker.addOnPositiveButtonClickListener {

        }
        timePicker.addOnNegativeButtonClickListener {

        }


    }
}