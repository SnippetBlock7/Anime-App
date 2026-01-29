package com.example.assignment.model.data

import com.google.gson.annotations.SerializedName

data class CharacterResponse(
    @SerializedName("data")
    val data: List<CharacterData>
)

data class CharacterData(
    @SerializedName("character")
    val characterInfo: CharacterInfo,
    val role: String
)

data class CharacterInfo(
    val name: String
)