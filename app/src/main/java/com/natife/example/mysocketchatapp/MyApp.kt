package com.natife.example.mysocketchatapp

import android.app.Application
import com.natife.example.mysocketchatapp.di.dataModule
import com.natife.example.mysocketchatapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApp)
            modules(listOf(dataModule, viewModelModule))
        }
    }
}