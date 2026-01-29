package com.example.assignment.model.data

import com.google.gson.annotations.SerializedName

data class AnimeDetailResponse(
    @SerializedName("data")
    val data: AnimeDetailData
)

data class AnimeDetailData(
    @SerializedName("mal_id")
    val id: Int,
    val title: String,
    val synopsis: String?,
    val episodes: Int?,
    val score: Double?,
    val trailer: TrailerInfo?,
    val images: ImageTypes?,
    val genres: List<Genre>?
)

data class TrailerInfo(
    @SerializedName("youtube_id")
    val youtubeId: String?,
    val url: String?,
    @SerializedName("embed_url")
    val embedUrl: String?
)

data class ImageTypes(
    val jpg: ImageUrls?
)

data class ImageUrls(
    @SerializedName("large_image_url")
    val imageUrl: String?
)

data class Genre(
    val name: String
)