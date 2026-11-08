package com.zarj.ahClickAgain.data


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
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


    fun setWholeState(newState: GameState) {
        _state.value = newState
    }

    internal fun restore(loaded: GameState) {
        _state.value = loaded
    }



}

