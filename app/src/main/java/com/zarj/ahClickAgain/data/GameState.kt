package com.zarj.ahClickAgain.data

import com.zarj.ahClickAgain.Dungeon.LevelProgress
import com.zarj.ahClickAgain.Sword
import com.zarj.ahClickAgain.Weapon
import kotlinx.serialization.Serializable

@Serializable
data class GameState(
    val rebirths: Int = 0,
    val coins: Long = 0,
    val diamond: Long = 0,
    val rebCoins: Long = 0,
    val weaponId: String = "sword_1",
    val weaponsHas: List<String> = emptyList(),
    val pets: List<Pet> = emptyList(),
    val levelsProgress: Map<String, LevelProgress> = emptyMap()
)

@Serializable
data class Pet(val name: String, val damage: Long)
