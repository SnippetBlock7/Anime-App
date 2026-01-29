package com.example.assignment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.assignment.model.db.AnimeEntity
import com.example.assignment.model.db.CharacterEntity
import com.example.assignment.repository.AnimeRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class AnimeDetailViewModel(private val repository: AnimeRepository) : ViewModel() {
    private val _animeId = MutableLiveData<Int>()

    // Observe the single anime from Room.
    // switchMap automatically switches the observation to the new ID when changed.
    val animeDetail: LiveData<AnimeEntity?> = _animeId.switchMap { id ->
        repository.getAnimeDetails(id).asLiveData()
    }

    // Observe the characters from Room.
    val characters: LiveData<List<CharacterEntity>> = _animeId.switchMap { id ->
        repository.getCharactersFromDb(id).asLiveData()
    }

    // 3. UI States for Feedback (Network status)
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun fetchDetails(id: Int) {
        // Prevent re-fetching if we are already observing this ID
        if (_animeId.value == id) return
        _animeId.value = id

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                // Sync both from network to database
                // Use coroutineScope to run them in parallel
                coroutineScope {
                    val detailsJob = launch { repository.refreshAnimeDetails(id) }
                    val charactersJob = launch { repository.refreshCharacters(id) }

                    detailsJob.join()
                    charactersJob.join()
                }
            } catch (e: Exception) {
                _error.value = "Offline mode: showing last cached data"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
