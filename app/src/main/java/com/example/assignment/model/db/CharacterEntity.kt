package com.example.assignment.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey(autoGenerate = true)
    val characterId: Int = 0,
    val animeId: Int,
    val name: String,
    val role: String
)
