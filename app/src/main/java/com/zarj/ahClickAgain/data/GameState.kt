package com.zarj.ahClickAgain.data

import kotlinx.serialization.Serializable

@Serializable
data class GameState(
    val coins: Long = 0,
    val damage: Long = 1,
    val pets: List<Pet> = emptyList(),
    val rebirths: Int = 0
)

@Serializable
data class Pet(val name: String, val damage: Long)
