package com.mubarak.madexample.ui.settings

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.DropDownPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreferenceCompat
import com.mubarak.madexample.MyApplication
import com.mubarak.madexample.R
import com.mubarak.madexample.data.sources.local.model.TodoTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {


     private val application: MyApplication = MyApplication()
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.prefsetting, rootKey)

        val themePreference: DropDownPreference? = findPreference("theme")

        themePreference?.setOnPreferenceChangeListener { _, theme -> /**we get the [String] of which theme we select ex: light*/
            application.updateTheme(TodoTheme.fromValue(theme as  String)) /** This is for changing theme in runtime this is also update the default value of shared Pref*/
            true
        }


    }
}