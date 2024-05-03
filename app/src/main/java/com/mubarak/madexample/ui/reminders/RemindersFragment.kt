package com.mubarak.madexample.ui.reminders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mubarak.madexample.R
import com.mubarak.madexample.databinding.FragmentRemindersBinding
import com.mubarak.madexample.utils.openNavDrawer

class RemindersFragment : Fragment() {

    private var _binding: FragmentRemindersBinding? = null
    private val binding: FragmentRemindersBinding get() = _binding!!
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
        val builder = MaterialAlertDialogBuilder(requireContext())
        val view = layoutInflater.inflate(R.layout.reminder_dialog, null)

        builder.setTitle(R.string.set_reminder)
        builder.setView(view).setPositiveButton(
            R.string.ok
        ) { dialog, _ ->
            // TODO: Create a reminder
        }.setNegativeButton(
            R.string.cancel
        ) { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()

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