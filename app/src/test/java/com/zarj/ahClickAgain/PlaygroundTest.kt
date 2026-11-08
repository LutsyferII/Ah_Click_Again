package com.zarj.ahClickAgain

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test

class PlaygroundTest {
    @Test
    fun stateFlowBasics() {
        // Создаём: начальное значение 0L
        val coins = MutableStateFlow(0L)

        // Читаем
        println("start: ${coins.value}")   // ${...} = интерполяция строк (как String.format)

        // Пишем — как обычная переменная
        coins.value = 10L
        println("after set: ${coins.value}")

        // Атомарно меняем: update { old -> new }
        coins.update { it + 5 }            // it = текущее значение
        println("after update: ${coins.value}")  // 15

        // Дедупликация: то же значение — ничего не произойдёт
        coins.value = 15L                  // не ошибка, просто ничего
    }
    @Test
    fun collectBasics() = runBlocking {   // runBlocking — запустить корутины и подождать
        val coins = MutableStateFlow(0L)
        val exp = MutableStateFlow(0L)

        val listener = launch {
            coins.collect { value ->
                println("coins: $value")
                exp.value +=32
            }
        }
        val listener2 = launch {
            println("START")
            exp.collect {
                println("exp: "+exp.value)
                delay(1000L)
                if(coins.value<200){
                    println("Only "+coins.value+"? Lets change it!")
                    coins.value += 10L

                    println("We did it! Now "+coins.value+"!")
                }
            }
        }
        println("ACTIVE = "+listener2.isActive)



        //listener.cancel()
        //listener2.cancel() // отписались
    }

    @Test
    fun multipleCoroutines() = runBlocking {
        val jobs = (1..5).map { i ->
            launch {
                println("coroutine $i started")
                delay(1000)
                println("coroutine $i finished")
            }
        }
        jobs.forEach { it.join()}   // ждём все

        for (i in 1..10){
            println("it now = $i")
        }
        println("all done")
    }


}