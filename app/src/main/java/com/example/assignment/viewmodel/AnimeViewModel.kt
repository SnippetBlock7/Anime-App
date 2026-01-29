package com.example.assignment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.assignment.model.db.AnimeEntity
import com.example.assignment.repository.AnimeRepository
import kotlinx.coroutines.launch

class AnimeViewModel(private val repository: AnimeRepository) : ViewModel() {

    // We convert the Flow from the Repository/Room into LiveData.
    // This will emit the cached data Instantly when the Fragment opens.
    val animeList: LiveData<List<AnimeEntity>> = repository.allAnime.asLiveData()

    // States for the UI
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    init {
        // Automatically try to sync with the API on startup
        refreshData()
    }

    // The Sync Logic
    fun refreshData() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                // This fetches from Network and saves to Room
                // animeList is observing the Database, which updates automatically.
                repository.refreshAnimeList()
            } catch (e: Exception) {
                _error.value = "Unable to refresh: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}