package com.mubarak.madexample.utils

import android.app.Activity
import android.content.Context
import android.inputmethodservice.InputMethodService
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.appbar.MaterialToolbar
import com.mubarak.madexample.R

fun View.openNavDrawer(activity: Activity){
        val drawerLayout =activity.findViewById<DrawerLayout>(R.id.main_drawer_layout)
        drawerLayout.openDrawer(GravityCompat.START)
    }

fun View.showSoftKeyboard(view: View,context: Context){
    if (view.requestFocus()){
        val imm = ContextCompat.getSystemService(context,InputMethodManager::class.java)
        imm?.showSoftInput(view,InputMethodManager.SHOW_IMPLICIT)
    }
}
