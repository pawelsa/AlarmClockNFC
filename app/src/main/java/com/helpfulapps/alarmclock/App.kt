package com.helpfulapps.alarmclock

import android.app.Application
import com.helpfulapps.alarmclock.di.Modules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App).modules(Modules.modules)
        }

    }
}