package com.mubarak.madexample.ui.reminders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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


    }

}