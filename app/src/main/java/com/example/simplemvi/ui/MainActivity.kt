package com.example.simplemvi.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.example.simplemvi.data.FakeRepositoryImpl
import com.example.simplemvi.ui.features.HomeScreen
import com.example.simplemvi.ui.features.HomeViewModel
import com.example.simplemvi.ui.theme.SimpleMVITheme
import kotlinx.coroutines.Dispatchers

class MainActivity : ComponentActivity() {

    private val viewModel: HomeViewModel by viewModels {
        withFactory {
            HomeViewModel(repository = FakeRepositoryImpl(io = Dispatchers.IO))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SimpleMVITheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    HomeScreen(viewModel = viewModel)
                }
            }
        }
    }
}