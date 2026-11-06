package com.zarj.ahClickAgain.data

import android.content.Context
import android.content.SharedPreferences
import android.provider.ContactsContract.Data
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.edit
import com.zarj.ahClickAgain.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.util.concurrent.CancellationException
import java.util.concurrent.atomic.AtomicBoolean

object SaveManager {
    private const val SAVE_INTERVAL_MS = 3000L

    private lateinit var dataStore: DataStore<Preferences>
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val saveMutex = Mutex()

    private val dirty = AtomicBoolean(false)
    private var saveJob: Job? = null

    private val STATE_KEY = byteArrayPreferencesKey("game_state")

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = false
    }

    /** Какие поля считаем HIGH-приоритетом. Всё остальное — LOW. */


    /** Инициализация и загрузка с диска. Вызывается из App.onCreate. */
    fun init(context: Context) {
        dataStore = PreferenceDataStoreFactory.create(
            produceFile = { File(context.filesDir, "datastore/game_state.preferences_pb") }
        )
        scope.launch {
            val loaded = loadFromDisk()
            DataEngine.restore(loaded)
            startObserving()
        }
    }

    private suspend fun loadFromDisk(): GameState {
        val prefs = dataStore.data.first()
        val bytes = prefs[STATE_KEY] ?: return GameState()
        return try {
            json.decodeFromString<GameState>(bytes.decodeToString())
        } catch (e: Exception) {
            println("Failed to parse state, using default: ${e.message}")
            GameState()
        }
    }

    private fun startObserving() {
        scope.launch {
            DataEngine.state
                .map { it.toHighPriority() }
                .distinctUntilChanged()   // ← отсекает всё, где HIGH-поля не менялись
                .drop(1)                  // ← пропускаем текущее значение при подписке
                .collect {
                    saveNow(DataEngine.state.value)
                }
        }

        // LOW: любой изменение → dirty
        scope.launch {
            DataEngine.state
                .drop(1)
                .collect {
                    markDirty()
                }
        }
    }

    private fun markDirty() {
        dirty.set(true)
        if (saveJob?.isActive == true) return
        saveJob = scope.launch {
            delay(SAVE_INTERVAL_MS)
            if (dirty.getAndSet(false)) {
                saveNow(DataEngine.state.value)
            }
        }
    }

    private suspend fun saveNow(state: GameState) {
        saveMutex.withLock {
            dirty.set(false)   // ← сбрасываем, потому что только что сохранили
            val bytes = json.encodeToString(state).encodeToByteArray()
            dataStore.edit { prefs -> prefs[STATE_KEY] = bytes }
        }
    }

    /** Для onStop() — блокирующее сохранение */
    fun saveBlocking() = runBlocking {
        if (dirty.getAndSet(false)) {
            saveNow(DataEngine.state.value)
        }
    }
    private data class HighPrioritySnapshot(
        val pets: List<Pet>,
        val rebirths: Int,
        val damage: Long
    )

    private fun GameState.toHighPriority() = HighPrioritySnapshot(
        pets = pets,
        rebirths = rebirths,
        damage = damage
    )










}