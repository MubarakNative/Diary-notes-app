package com.mubarak.madexample.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mubarak.madexample.R
import com.mubarak.madexample.databinding.FragmentSettingsHostBinding

/**A Host Fragment for SettingPreference Screen*/
class SettingHostFragment : Fragment() {

    private var _binding: FragmentSettingsHostBinding? = null
    private val binding: FragmentSettingsHostBinding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsHostBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolBarSettings.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        childFragmentManager
            .beginTransaction()
            .replace(R.id.setting_container, SettingsFragment())
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}