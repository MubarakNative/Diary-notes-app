package com.mubarak.madexample.ui.main

import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
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
    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

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

        onBackPressedDispatcher.addCallback(this) {
            if (binding.mainDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
            } else {
                isEnabled = false
                onBackPressedDispatcher.onBackPressed()
                isEnabled = true
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.settingNoteFragment || destination.id == R.id.actionNoteFragment || destination.id == R.id.searchNoteFragment) {
                binding.mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            } else {
                binding.mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }
        }

        navigationView.setupWithNavController(navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}