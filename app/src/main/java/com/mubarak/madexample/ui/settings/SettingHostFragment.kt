package com.mubarak.madexample.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mubarak.madexample.R
import com.mubarak.madexample.databinding.FragmentSettingsHostBinding
import com.mubarak.madexample.utils.onUpButtonClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingHostFragment : Fragment() {

private lateinit var binding: FragmentSettingsHostBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsHostBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolBarSettings.onUpButtonClick()

        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.setting_container, SettingsFragment())
            .commit()
    }

}