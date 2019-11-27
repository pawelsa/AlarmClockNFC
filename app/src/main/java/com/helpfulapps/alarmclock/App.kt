package com.helpfulapps.alarmclock

import android.app.Application
import android.nfc.NfcAdapter
import androidx.work.*
import com.helpfulapps.alarmclock.di.Modules
import com.helpfulapps.alarmclock.worker.DownloadWeatherWorker
import com.helpfulapps.domain.helpers.Settings
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit

class App : Application() {

    private val settings: Settings by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App).modules(Modules.modules)
        }

        if (settings.city != "-1") {
            periodicWeatherDownload()
        }

        if (settings.firstRun) {
            val nfcAdapter = NfcAdapter.getDefaultAdapter(this)
            settings.hasNfc = nfcAdapter != null
            settings.firstRun = false
        }

    }

    private fun periodicWeatherDownload() {
        val downloadConstraints = Constraints.Builder()
            .apply {
                if (!settings.useMobileData)
                    this.setRequiredNetworkType(NetworkType.UNMETERED)
                else
                    this.setRequiredNetworkType(NetworkType.CONNECTED)
            }
            .build()

        val downloadWeather =
            PeriodicWorkRequestBuilder<DownloadWeatherWorker>(24, TimeUnit.HOURS)
                .setConstraints(downloadConstraints)
                .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            FORECAST_DOWNLOAD_WORK,
            ExistingPeriodicWorkPolicy.KEEP,
            downloadWeather
        )
    }

    companion object {
        const val FORECAST_DOWNLOAD_WORK = "com.helpfulapps.alarmclock.download_forecast_work"
    }
}