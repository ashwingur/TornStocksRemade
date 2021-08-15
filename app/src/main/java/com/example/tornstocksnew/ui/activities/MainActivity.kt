package com.example.tornstocksnew.ui.activities

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.tornstocksnew.R
import com.example.tornstocksnew.databinding.ActivityMainBinding
import com.example.tornstocksnew.databinding.FragmentStocksBinding
import com.example.tornstocksnew.models.Stock
import com.example.tornstocksnew.utils.Constants
import com.example.tornstocksnew.viewmodels.MainActivityViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController
    private lateinit var bottomNavView: BottomNavigationView
    var cachedStocks: MutableList<Stock> = mutableListOf()
    val mainViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.loadApiKey()

        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        bottomNavView = binding.bottomNav
        bottomNavView.setupWithNavController(navController)

        mainViewModel.testDb()
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

    fun hideBottomNav(bool: Boolean){
        if (bool){
            bottomNavView.visibility = View.GONE
        } else {
            bottomNavView.visibility = View.VISIBLE
        }
    }
}