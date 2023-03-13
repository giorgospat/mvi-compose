package com.example.simplemvi.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

internal fun withFactory(
    create: () -> ViewModel
): ViewModelProvider.Factory {
    return object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return create.invoke() as T
        }
    }
}