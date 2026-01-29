package com.example.assignment.model.retrofit

import com.example.assignment.model.data.AnimeDetailResponse
import com.example.assignment.model.data.AnimeResponse
import com.example.assignment.model.data.CharacterResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AnimeApiService {
    @GET("top/anime")
    suspend fun getTopAnime() : Response<AnimeResponse>

    @GET("anime/{anime_id}")
    suspend fun getAnimeDetails(@Path("anime_id") id: Int): Response<AnimeDetailResponse>

    @GET("anime/{anime_id}/characters")
    suspend fun getAnimeCharacters(@Path("anime_id") id: Int): Response<CharacterResponse>
}