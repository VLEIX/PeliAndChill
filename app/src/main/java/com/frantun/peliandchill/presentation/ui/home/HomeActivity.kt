package com.frantun.peliandchill.presentation.ui.home

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.frantun.peliandchill.R
import com.frantun.peliandchill.presentation.ui.home.model.HomeState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    onStateUpdated(state)
                }
            }
        }
    }

    private fun onStateUpdated(state: HomeState?) {
        when (state) {
            is HomeState.Loading -> {
                Log.d("HomeState", "Loading")
            }
            is HomeState.Init -> {
                Log.d("HomeState", "Init")
            }
            is HomeState.Initialized -> {
                Log.d("HomeState", "Initialized")
            }
            else -> Unit
        }
    }
}
