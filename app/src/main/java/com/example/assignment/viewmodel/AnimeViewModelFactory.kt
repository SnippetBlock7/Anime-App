package com.example.assignment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.assignment.repository.AnimeRepository

class AnimeViewModelFactory(private val repository: AnimeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            // Check for the List ViewModel
            modelClass.isAssignableFrom(AnimeViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                AnimeViewModel(repository) as T
            }
            // Check for the Detail ViewModel
            modelClass.isAssignableFrom(AnimeDetailViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                AnimeDetailViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}