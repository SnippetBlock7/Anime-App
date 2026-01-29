package com.example.assignment.model.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeDao {
    @Query("SELECT * FROM top_anime")
    fun getAllAnime(): Flow<List<AnimeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnimeList(animeList: List<AnimeEntity>)

    @Query("SELECT * FROM top_anime WHERE id = :animeId")
    fun getAnimeById(animeId: Int): Flow<AnimeEntity?>

    @Query("DELETE FROM top_anime")
    suspend fun clearAll()
}