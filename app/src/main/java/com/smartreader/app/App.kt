package com.smartreader.app

import android.app.Application
import com.smartreader.app.data.local.AppPreferences

class App : Application() {

    lateinit var appPreferences: AppPreferences
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        appPreferences = AppPreferences(this)
    }

    companion object {
        lateinit var instance: App
            private set
    }
}
