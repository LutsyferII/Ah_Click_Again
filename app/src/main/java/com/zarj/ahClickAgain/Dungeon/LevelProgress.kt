package com.zarj.ahClickAgain.Dungeon

import kotlinx.serialization.Serializable

@Serializable
data class LevelProgress(
    val exp: Int = 0,
)
