package com.mubarak.madexample.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.mubarak.madexample.R
import com.mubarak.madexample.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // lay our ui behind system bar and we also need to
        // manage views overlapping each other by using window inserts
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val navigationView = binding.navigationView

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
                as NavHostFragment

        navController = navHostFragment.navController

        // our top-level destinations
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeNoteFragment,
                R.id.settingNoteFragment
            ), binding.mainDrawerLayout
        )

        navigationView.setupWithNavController(navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}