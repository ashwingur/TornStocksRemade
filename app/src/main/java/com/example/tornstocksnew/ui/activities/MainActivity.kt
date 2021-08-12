package com.example.tornstocksnew.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.tornstocksnew.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController
    private lateinit var bottomNavView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        bottomNavView = findViewById(R.id.bottom_nav)
        bottomNavView.setupWithNavController(navController)

        setupBottomNav()
    }

    private fun setupBottomNav() {
        // Slide left or right depending on the current selected bottom nav menu item
        val navOptionsToLeft = NavOptions
            .Builder()
            .setEnterAnim(R.anim.left_to_right)
            .setExitAnim(R.anim.left_to_right_exit)
            .setPopEnterAnim(R.anim.nav_default_pop_enter_anim)
            .setPopExitAnim(R.anim.nav_default_pop_exit_anim)
            .build()
        val navOptionsToRight = NavOptions
            .Builder()
            .setEnterAnim(R.anim.right_to_left)
            .setExitAnim(R.anim.right_to_left_exit)
            .setPopEnterAnim(R.anim.nav_default_pop_enter_anim)
            .setPopExitAnim(R.anim.nav_default_pop_exit_anim)
            .build()
        bottomNavView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.stocks_menu -> {
                    if (navController.currentDestination?.id != R.id.stocksFragment) {
                        navController.navigate(R.id.stocksFragment, null, navOptionsToLeft)
                    }
                }
                R.id.triggers_menu -> {
                    if (navController.currentDestination?.id == R.id.stocksFragment) {
                        navController.navigate(R.id.triggersFragment, null, navOptionsToRight)
                    } else if (navController.currentDestination?.id != R.id.triggersFragment) {
                        navController.navigate(R.id.triggersFragment, null, navOptionsToLeft)
                    }
                }
                R.id.user_menu -> {
                    if (navController.currentDestination?.id == R.id.settingsFragment) {
                        navController.navigate(R.id.userFragment, null, navOptionsToLeft)
                    } else if (navController.currentDestination?.id != R.id.userFragment) {
                        navController.navigate(R.id.userFragment, null, navOptionsToRight)
                    }
                }
                R.id.settings_menu -> {
                    if (navController.currentDestination?.id != R.id.settingsFragment) {
                        navController.navigate(R.id.settingsFragment, null, navOptionsToRight)
                    }
                }
            }
            return@setOnItemSelectedListener true
        }
    }

    fun updateBottomNav() {
        when (navController.currentDestination?.id) {
            R.id.stocksFragment -> {
                bottomNavView.selectedItemId = R.id.stocks_menu
            }
            R.id.triggersFragment -> {
                bottomNavView.selectedItemId = R.id.triggers_menu
            }
            R.id.userFragment -> {
                bottomNavView.selectedItemId = R.id.user_menu
            }
            R.id.settingsFragment -> {
                bottomNavView.selectedItemId = R.id.settings_menu
            }
        }
    }
}