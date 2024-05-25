package com.dicoding.asclepius

import android.app.Application

class MyApplication : Application() {
    lateinit var appContainer: AppContainer

    companion object {
        lateinit var instance: MyApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        appContainer = AppContainer()
    }
}