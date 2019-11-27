package com.helpfulapps.alarmclock.service

import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.IBinder
import android.util.Log
import androidx.work.*
import com.google.android.gms.location.LocationRequest
import com.helpfulapps.alarmclock.App
import com.helpfulapps.alarmclock.helpers.NotificationBuilder
import com.helpfulapps.alarmclock.helpers.startVersionedForeground
import com.helpfulapps.alarmclock.worker.DownloadWeatherWorker
import com.helpfulapps.alarmclock.worker.DownloadWeatherWorker.Companion.KEY_LATITUDE
import com.helpfulapps.alarmclock.worker.DownloadWeatherWorker.Companion.KEY_LONGITUDE
import com.helpfulapps.base.extensions.rx.backgroundTask
import com.helpfulapps.domain.helpers.Settings
import com.patloew.rxlocation.RxLocation
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

class ForecastForLocalizationService : Service() {

    private val TAG = this.javaClass.simpleName

    private val disposables: CompositeDisposable = CompositeDisposable()
    private val notificationBuilder: NotificationBuilder by inject()
    private val settings: Settings by inject()

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val notification =
            notificationBuilder.setNotificationType(NotificationBuilder.NotificationType.TypeLocalization)
                .build()

        startVersionedForeground(notification)


        val locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
            .setInterval(3000)

        disposables += RxLocation(baseContext)
            .location()
            .updates(locationRequest)
            .skip(4)
            .firstOrError()
            .flatMapCompletable { location ->
                return@flatMapCompletable Completable.create {
                    setupDownloadForecastForLocationWork(location)
                    it.onComplete()
                }
            }
            .backgroundTask()
            .subscribeBy(
                onComplete = {
                    Log.d(TAG, "Download forecast for localization completed")
                    stopSelf()
                },
                onError = {
                    Log.d(TAG, "Download forecast error")
                    it.printStackTrace()
                }
            )


        return START_STICKY
    }

    private fun setupDownloadForecastForLocationWork(location: Location) {
        val downloadConstraints = Constraints.Builder()
            .apply {
                if (settings.useMobileData)
                    setRequiredNetworkType(NetworkType.CONNECTED)
                else
                    setRequiredNetworkType(NetworkType.UNMETERED)
            }
            .build()

        val locationData = workDataOf(
            KEY_LATITUDE to location.latitude,
            KEY_LONGITUDE to location.longitude
        )

        val downloadWeather =
            PeriodicWorkRequestBuilder<DownloadWeatherWorker>(24, TimeUnit.HOURS)
                .setConstraints(downloadConstraints)
                .setInputData(locationData)
                .build()

        WorkManager.getInstance(baseContext).enqueueUniquePeriodicWork(
            App.FORECAST_DOWNLOAD_WORK,
            ExistingPeriodicWorkPolicy.REPLACE,
            downloadWeather
        )
    }


    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}