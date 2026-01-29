package com.example.assignment.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "top_anime")
data class AnimeEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val imageUrl: String?,
    val score: Double?,
    val episodes : Int?,
    val synopsis: String? = null,
    val genres: String? = null,
    val youtubeId: String? = null
)
