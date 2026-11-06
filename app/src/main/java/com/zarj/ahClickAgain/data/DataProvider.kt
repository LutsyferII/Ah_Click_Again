package com.zarj.ahClickAgain.data

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.StateFlow
import kotlin.reflect.KClass

class DataProvider<T: Any>(k:Preferences.Key<T>, v:StateFlow<T>, t: KClass<T>) {
    val key: Preferences.Key<T> = k
    val vallue: StateFlow<T> = v
    var simple_val: T? = null
    val type: KClass<T> = t
    init{
        simple_val = vallue.value
    }


}