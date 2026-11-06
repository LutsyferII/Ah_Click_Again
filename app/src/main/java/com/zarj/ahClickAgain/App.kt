package com.zarj.ahClickAgain

import android.app.Application
import com.zarj.ahClickAgain.data.DataEngine
import com.zarj.ahClickAgain.data.SaveManager

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        SaveManager.init(applicationContext)
        DataEngine.init(applicationContext)
    }
}