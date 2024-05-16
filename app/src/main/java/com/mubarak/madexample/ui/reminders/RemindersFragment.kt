package com.mubarak.madexample.ui.reminders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.timepicker.MaterialTimePicker
import com.mubarak.madexample.databinding.FragmentRemindersBinding
import com.mubarak.madexample.utils.openNavDrawer

class RemindersFragment : Fragment() {

    private var _binding: FragmentRemindersBinding? = null
    private val binding: FragmentRemindersBinding get() = _binding!!
    private val reminderViewModel: ReminderViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRemindersBinding.inflate(
            layoutInflater,
            container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolBarReminders.setNavigationOnClickListener {
            requireView().openNavDrawer(requireActivity())
        }

        binding.fabCreateReminderNote.setOnClickListener {
            navigateToAddEditFragment()
            openDialog()
        }


    }


    private fun openDialog() {
        val timePicker = MaterialTimePicker.Builder()
            .setTitleText("Pick a time to remind")
            .setPositiveButtonText(getString(com.mubarak.madexample.R.string.ok))
            .setNegativeButtonText(getString(com.mubarak.madexample.R.string.cancel))

            .build()
        timePicker.addOnPositiveButtonClickListener {

        }
        timePicker.addOnNegativeButtonClickListener {

        }
        timePicker.show(childFragmentManager, "TimePicker")
    }


    private fun navigateToAddEditFragment() {
        val action = RemindersFragmentDirections.actionRemindersFragmentToActionNoteFragment(
            -1
        )
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}