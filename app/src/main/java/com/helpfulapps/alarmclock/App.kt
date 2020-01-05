package com.helpfulapps.alarmclock

import android.app.Application
import android.content.ComponentName
import android.content.pm.PackageManager
import android.nfc.NfcAdapter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.helpfulapps.alarmclock.di.Modules
import com.helpfulapps.alarmclock.receiver.RebootReceiver
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

        setupRebootReceiver()

    }

    private fun setupRebootReceiver() {
        val receiver = ComponentName(applicationContext, RebootReceiver::class.java)

        applicationContext.packageManager?.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        RxBus.publish(AppState.IsBackground)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForeground() {
        RxBus.publish(AppState.IsForeground)
    }

    sealed class AppState {
        object IsForeground : AppState()
        object IsBackground : AppState()
    }

}