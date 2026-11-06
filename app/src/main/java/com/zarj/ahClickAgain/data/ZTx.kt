package com.zarj.ahClickAgain.data

import com.zarj.ahClickAgain.data.DataEngine.inTransaction

class ZTx(initialState: GameState) {
    // Черновик, с которым мы работаем внутри транзакции
    var draft: GameState = initialState
        private set

    // Изменить черновик
    fun update(block: (GameState) -> GameState) {
        draft = block(draft)
    }
}

// Вспомогательная функция для запуска транзакции
inline fun transaction(block: ZTx.() -> Unit) {
    check(inTransaction.compareAndSet(false, true)) { "Nested transaction!"

    }
    try {
        val tx = ZTx(DataEngine.state.value)
        tx.block()
        DataEngine.setWholeState(tx.draft)
    } finally {
        inTransaction.set(false)
    }
}