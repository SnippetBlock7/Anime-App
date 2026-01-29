package com.example.assignment.model.data

import com.google.gson.annotations.SerializedName

data class AnimeData(
    @SerializedName("mal_id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("episodes")
    val episodes: Int?,
    @SerializedName("score")
    val score: Double?,
    @SerializedName("images")
    val posterImage: ImageType
)

data class ImageType(
    @SerializedName("jpg")
    val jpg: ImageUrl
)

data class ImageUrl(
    @SerializedName("image_url")
    val imageUrl: String
)

data class AnimeResponse(
    @SerializedName("data")
    val data: List<AnimeData>)