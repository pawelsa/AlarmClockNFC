package com.helpfulapps.alarmclock.worker

import android.content.Context
import android.location.Location
import androidx.work.*
import com.helpfulapps.base.helpers.whenNotNull
import java.util.*
import java.util.concurrent.TimeUnit

object CreateWork {

    fun periodicWeatherDownload(context: Context, useMobileData: Boolean) {

        val leftTimeToMidnight = getTimeToMidnight()

        val downloadConstraints = getWorkerConstraints(useMobileData)

        val downloadWeather =
            PeriodicWorkRequestBuilder<DownloadWeatherWorker>(24, TimeUnit.HOURS)
                .setConstraints(downloadConstraints)
                .setInitialDelay(leftTimeToMidnight, TimeUnit.MILLISECONDS)
                .addTag(FORECAST_DOWNLOAD_WORK)
                .build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                FORECAST_DOWNLOAD_WORK,
                ExistingPeriodicWorkPolicy.KEEP,
                downloadWeather
            )
    }

    private fun getWorkerConstraints(useMobileData: Boolean): Constraints {
        return Constraints.Builder()
            .apply {
                if (!useMobileData)
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


    fun oneTimeWeatherDownload(
        context: Context,
        useMobileData: Boolean,
        location: Location? = null
    ) {
        val downloadConstraints = getWorkerConstraints(useMobileData)

        val locationData = whenNotNull(location) {
            workDataOf(
                DownloadWeatherWorker.KEY_LATITUDE to it.latitude,
                DownloadWeatherWorker.KEY_LONGITUDE to it.longitude
            )
        }

        val downloadWeather =
            OneTimeWorkRequest.Builder(DownloadWeatherWorker::class.java)
                .setConstraints(downloadConstraints).apply {
                    whenNotNull(locationData) {
                        setInputData(it)
                    }
                }
                .addTag(FORECAST_DOWNLOAD_WORK_ONE_TIME)
                .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            FORECAST_DOWNLOAD_WORK_ONE_TIME,
            ExistingWorkPolicy.REPLACE,
            downloadWeather
        )
    }


    const val FORECAST_DOWNLOAD_WORK_ONE_TIME =
        "com.helpfulapps.alarmclock.download_forecast_work_one_time"
    const val FORECAST_DOWNLOAD_WORK = "com.helpfulapps.alarmclock.download_forecast_work"

}