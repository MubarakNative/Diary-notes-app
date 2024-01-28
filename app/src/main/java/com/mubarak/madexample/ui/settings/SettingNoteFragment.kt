package com.mubarak.madexample.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mubarak.madexample.databinding.FragmentSettingsNoteBinding
import com.mubarak.madexample.utils.onUpButtonClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingNoteFragment : Fragment() {

private lateinit var binding: FragmentSettingsNoteBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsNoteBinding.inflate(
            inflater,
            container,
            false
        )

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolBarSettings.onUpButtonClick()
    }

}