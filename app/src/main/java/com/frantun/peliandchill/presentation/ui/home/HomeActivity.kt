package com.frantun.peliandchill.presentation.ui.home

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.frantun.peliandchill.databinding.ActivityHomeBinding
import com.frantun.peliandchill.presentation.common.BaseActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setBottomNavigation()
    }

    private fun setBottomNavigation() {
        val navView: BottomNavigationView = binding.homeNavView

        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        val navController = navHostFragment.findNavController()
        navView.setupWithNavController(navController)
    }
}
