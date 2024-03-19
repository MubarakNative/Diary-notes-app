package com.mubarak.madexample.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.mubarak.madexample.R

fun View.openNavDrawer(activity: Activity) {
    val drawerLayout = activity.findViewById<DrawerLayout>(R.id.main_drawer_layout)
    drawerLayout.openDrawer(GravityCompat.START)
}

fun View.showSoftKeyboard(viewToFocus: View) {
    if (viewToFocus.requestFocus()) {
        val context = context ?: return
        val imm = ContextCompat.getSystemService(context, InputMethodManager::class.java)
        imm?.showSoftInput(viewToFocus, InputMethodManager.SHOW_IMPLICIT)
    }
}
fun MaterialToolbar.onUpButtonClick() {
    setNavigationOnClickListener {
        findNavController().popBackStack()
    }
}

fun View.hideSoftKeyboard(){
    val context = this.context ?: return
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken,0)
}
