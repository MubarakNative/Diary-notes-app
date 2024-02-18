package com.mubarak.madexample

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.preference.PreferenceManager
import com.mubarak.madexample.data.sources.local.model.TodoTheme
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val preference = PreferenceManager.getDefaultSharedPreferences(this)
            .getString("theme", "system")

        updateTheme(TodoTheme.fromValue(preference!!)) /** this would update for whole app when it re-start again*/

    }

    fun updateTheme(theme: TodoTheme){
        /** We update the TodoTheme for runtime on using setOnPreferenceChangeListener*/
        AppCompatDelegate.setDefaultNightMode(when(theme){

            TodoTheme.LIGHT -> MODE_NIGHT_NO
            TodoTheme.DARK -> MODE_NIGHT_YES
            TodoTheme.FOLLOW_SYSTEM -> MODE_NIGHT_FOLLOW_SYSTEM

        })


    }

}

