package com.zarj.ahClickAgain.data

import android.content.Context
import com.zarj.ahClickAgain.R
import com.zarj.ahClickAgain.data.configs.DungeonConfig
import com.zarj.ahClickAgain.data.configs.LevelConfig
import kotlinx.serialization.json.Json

object JsonLoader {
    private val json = Json { ignoreUnknownKeys = true }

    // Читает res/raw файл и превращает в строку
    private fun readRawResource(context: Context, resId: Int): String {
        return context.resources.openRawResource(resId).bufferedReader().use { it.readText() }
    }

    // Загружает уровни
    fun loadLevels(context: Context): Map<String, LevelConfig> {
        val jsonString = readRawResource(context, R.raw.levels)
        return json.decodeFromString<Map<String, LevelConfig>>(jsonString)
    }

    // Загружает данжи
    fun loadDungeons(context: Context): Map<String, DungeonConfig> {
        val jsonString = readRawResource(context, R.raw.dungeons)
        return json.decodeFromString<Map<String, DungeonConfig>>(jsonString)
    }
}