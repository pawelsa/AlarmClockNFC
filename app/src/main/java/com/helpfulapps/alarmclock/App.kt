package com.helpfulapps.alarmclock

import android.app.Application
import android.nfc.NfcAdapter
import com.helpfulapps.alarmclock.di.Modules
import com.helpfulapps.alarmclock.worker.CreateWork
import com.helpfulapps.domain.helpers.Settings
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    private val settings: Settings by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App).modules(Modules.modules)
        }

        if (settings.city != "-1") {
            CreateWork.periodicWeatherDownload(this, settings.useMobileData)
        }

        if (settings.firstRun) {
            val nfcAdapter = NfcAdapter.getDefaultAdapter(this)
            settings.hasNfc = nfcAdapter != null
            settings.firstRun = false
        }

    }


}