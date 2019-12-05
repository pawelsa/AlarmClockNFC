package com.helpfulapps.alarmclock

import android.app.Application
import android.nfc.NfcAdapter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.helpfulapps.alarmclock.di.Modules
import com.helpfulapps.alarmclock.worker.CreateWork
import com.helpfulapps.domain.eventBus.RxBus
import com.helpfulapps.domain.helpers.Settings
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application(), LifecycleObserver {

    private val settings: Settings by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App).modules(Modules.modules)
        }

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        if (settings.city != "-1") {
            CreateWork.periodicWeatherDownload(this, settings.useMobileData)
        }

        if (settings.firstRun) {
            val nfcAdapter = NfcAdapter.getDefaultAdapter(this)
            settings.hasNfc = nfcAdapter != null
            settings.firstRun = false
        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        RxBus.publish(AppState(false))
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForeground() {
        RxBus.publish(AppState(true))
    }

    data class AppState(val isForeground: Boolean)

}