package com.zarj.ahClickAgain.data.configs

import kotlinx.serialization.Serializable

@Serializable
data class LevelConfig(
    val id: String,           // "first_1"
    val needExp: Int,
    val mobIds: List<String>,
    val bossId: String,
    val backgroundId: String
)