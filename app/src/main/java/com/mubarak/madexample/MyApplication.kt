package com.mubarak.madexample

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.preference.PreferenceManager
import com.google.android.material.color.DynamicColors
import com.mubarak.madexample.utils.TodoTheme
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        DynamicColors.applyToActivitiesIfAvailable(this)

        // default theme is follow system
        val preference = PreferenceManager.getDefaultSharedPreferences(this)
            .getString("theme", "FOLLOW_SYSTEM")

        updateTheme(enumValueOf<TodoTheme>(preference!!))
    }

    fun updateTheme(theme: TodoTheme) { //  change the theme from Whole Application level because to avoid theming problem
        AppCompatDelegate.setDefaultNightMode(
            when (theme) {
                TodoTheme.LIGHT -> MODE_NIGHT_NO
                TodoTheme.DARK -> MODE_NIGHT_YES
                TodoTheme.FOLLOW_SYSTEM -> MODE_NIGHT_FOLLOW_SYSTEM

            }
        )


    }

}

