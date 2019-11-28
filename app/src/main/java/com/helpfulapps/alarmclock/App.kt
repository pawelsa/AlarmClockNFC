package com.helpfulapps.alarmclock

import android.app.Application
import android.nfc.NfcAdapter
import android.os.Build
import androidx.work.*
import com.helpfulapps.alarmclock.di.Modules
import com.helpfulapps.alarmclock.worker.DownloadWeatherWorker
import com.helpfulapps.domain.helpers.Settings
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.time.Duration
import java.util.*
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

        val leftTimeToMidnight = getTimeToMidnight()

        val downloadConstraints = getWorkerConstraints()

        val downloadWeather =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                PeriodicWorkRequestBuilder<DownloadWeatherWorker>(24, TimeUnit.HOURS)
                    .setConstraints(downloadConstraints)
                    .setInitialDelay(Duration.ofMillis(leftTimeToMidnight))
                    .build()
            } else {
                TODO("VERSION.SDK_INT < O")
            }

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                FORECAST_DOWNLOAD_WORK,
                ExistingPeriodicWorkPolicy.KEEP,
                downloadWeather
            )
    }

    private fun getWorkerConstraints(): Constraints {
        return Constraints.Builder()
            .apply {
                if (!settings.useMobileData)
                    setRequiredNetworkType(NetworkType.UNMETERED)
                else
                    setRequiredNetworkType(NetworkType.CONNECTED)
            }
            .build()
    }

    private fun getTimeToMidnight(): Long {
        val calendar = GregorianCalendar.getInstance()
        val currentTime = calendar.timeInMillis
        val timeAtMidnight = calendar.apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
        }.timeInMillis
        return timeAtMidnight - currentTime
    }

    companion object {
        const val FORECAST_DOWNLOAD_WORK = "com.helpfulapps.alarmclock.download_forecast_work"
    }
}