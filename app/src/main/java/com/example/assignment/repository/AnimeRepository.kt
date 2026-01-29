package com.example.assignment.repository

import android.util.Log
import com.example.assignment.model.data.AnimeData
import com.example.assignment.model.data.AnimeDetailData
import com.example.assignment.model.data.CharacterData
import com.example.assignment.model.db.AnimeDao
import com.example.assignment.model.db.AnimeEntity
import com.example.assignment.model.db.CharacterDao
import com.example.assignment.model.db.CharacterEntity
import com.example.assignment.model.retrofit.AnimeApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AnimeRepository(
    private val apiService: AnimeApiService,
    private val animeDao: AnimeDao,
    private val characterDao: CharacterDao
) {

    // --- Flows for UI Observation ---
    val allAnime: Flow<List<AnimeEntity>> = animeDao.getAllAnime()

    fun getAnimeDetails(id: Int): Flow<AnimeEntity?> = animeDao.getAnimeById(id)

    fun getCharactersFromDb(id: Int): Flow<List<CharacterEntity>> =
        characterDao.getCharactersByAnime(id)

    // --- Sync Logic: Anime List ---
    suspend fun refreshAnimeList() {
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.getTopAnime()
                if (response.isSuccessful) {
                    val remoteData = response.body()?.data ?: emptyList()

                    val entities = remoteData.map { anime ->
                        AnimeEntity(
                            id = anime.id,
                            title = anime.title,
                            imageUrl = anime.posterImage.jpg.imageUrl,
                            score = anime.score,
                            episodes = anime.episodes
                        )
                    }

                    animeDao.clearAll()
                    animeDao.insertAnimeList(entities)

                    Log.d("REPO", "Successfully saved to DB")
                }
            } catch (e: Exception) {
                Log.e("REPO", "Sync failed: ${e.message}")
            }
        }
    }
    // --- Sync Logic: Anime Details ---
    suspend fun refreshAnimeDetails(id: Int) {
        try {
            val response = apiService.getAnimeDetails(id)
            if (response.isSuccessful) {
                val apiData = response.body()?.data ?: return
                val updatedEntity = AnimeEntity(
                    id = apiData.id,
                    title = apiData.title,
                    imageUrl = apiData.images?.jpg?.imageUrl,
                    score = apiData.score,
                    episodes = apiData.episodes,
                    synopsis = apiData.synopsis,
                    genres = apiData.genres?.joinToString(", ") { it.name },
                    youtubeId = apiData.trailer?.youtubeId
                )
                animeDao.insertAnimeList(listOf(updatedEntity))
            }
        } catch (e: Exception) {
            Log.e("Repo", "Failed to sync details: ${e.message}")
        }
    }

    // -- Sync Logic: Characters ---
    suspend fun refreshCharacters(animeId: Int) {
        try {
            val response = apiService.getAnimeCharacters(animeId)
            if (response.isSuccessful) {
                val networkCharacters = response.body()?.data ?: emptyList()

                // Only keep the "Main" cast
                val mainCastEntities = networkCharacters
                    .filter { it.role == "Main" } // Only allow "Main" roles
                    .map { cast ->
                        CharacterEntity(
                            animeId = animeId,
                            name = cast.characterInfo.name,
                            role = cast.role
                        )
                    }

                // Save only the filtered list to Room
                characterDao.deleteCharactersByAnime(animeId)
                characterDao.insertCharacters(mainCastEntities)
            }
        } catch (e: Exception) {
            Log.e("Repo", "Character sync failed: ${e.message}")
        }
    }
}