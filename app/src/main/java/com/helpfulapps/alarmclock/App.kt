package com.helpfulapps.alarmclock

import android.app.Application
import android.nfc.NfcAdapter
import android.util.Log
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
    private val TAG = this.javaClass.simpleName

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
        Log.d(TAG, "send background")
        RxBus.publish(AppState.IsBackground)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForeground() {
        Log.d(TAG, "send foreground")
        RxBus.publish(AppState.IsForeground)
    }

    sealed class AppState {
        object IsForeground : AppState()
        object IsBackground : AppState()
    }

}