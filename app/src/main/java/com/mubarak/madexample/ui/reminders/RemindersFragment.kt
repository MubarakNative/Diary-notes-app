package com.mubarak.madexample.ui.reminders

import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.mubarak.madexample.databinding.FragmentRemindersBinding
import com.mubarak.madexample.ui.SharedViewModel
import com.mubarak.madexample.utils.openNavDrawer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RemindersFragment : Fragment() {

    private var _binding: FragmentRemindersBinding? = null
    private val binding: FragmentRemindersBinding get() = _binding!!

    private val sharedViewModel: SharedViewModel by viewModels()
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

        sharedViewModel.noteIdEvent.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
            }
        }


    }


    private fun openDialog() {
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(if (is24HourFormat(requireContext())) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H)
            .setTitleText("Pick a time to remind")
            .setPositiveButtonText(getString(com.mubarak.madexample.R.string.ok))
            .setNegativeButtonText(getString(com.mubarak.madexample.R.string.cancel))
            .build()
        timePicker.addOnPositiveButtonClickListener {
            Toast.makeText(requireContext(), "Ok clicked", Toast.LENGTH_SHORT).show()
            sharedViewModel.noteIdEvent.observe(viewLifecycleOwner) {
                it.getContentIfNotHandled()?.let {
                    Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
                }
            }

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