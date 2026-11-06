package com.zarj.ahClickAgain.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import kotlinx.serialization.Serializable
import java.util.concurrent.atomic.AtomicBoolean

object DataEngine {

    val inTransaction = AtomicBoolean(false)

    private val _state = MutableStateFlow(GameState())

    // Наружу отдаём только для чтения (чтобы никто не мог сделать _state.value = ... в обход логики)
    val state: StateFlow<GameState> = _state

    // Простое изменение одной переменной
    fun addCoins(amount: Long) {
        _state.update { current ->
            current.copy(coins = current.coins + amount)
        }
    }

    // Установка урона
    fun setDamage(newDamage: Long) {
        _state.update { it.copy(damage = newDamage) }
    }

    fun setWholeState(newState: GameState) {
        _state.value = newState
    }

    internal fun restore(loaded: GameState) {
        _state.value = loaded
    }



}

