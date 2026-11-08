package com.zarj.ahClickAgain.data.configs

import kotlinx.serialization.Serializable

@Serializable
data class DungeonConfig(
    val id: String,           // "first"
    val name: String,
    val levelIds: List<String> // ["first_1", "first_2"]
)