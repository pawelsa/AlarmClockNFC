package com.helpfulapps.alarmclock.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.google.android.gms.location.LocationRequest
import com.helpfulapps.alarmclock.helpers.NotificationBuilder
import com.helpfulapps.alarmclock.helpers.startVersionedForeground
import com.helpfulapps.base.extensions.rx.backgroundTask
import com.helpfulapps.domain.use_cases.weather.DownloadForecastForLocalizationUseCase
import com.patloew.rxlocation.RxLocation
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import org.koin.android.ext.android.inject

class ForecastForLocalizationService : Service() {

    private val TAG = this.javaClass.simpleName

    private val disposables: CompositeDisposable = CompositeDisposable()
    private val downloadForecastForLocalizationUseCase: DownloadForecastForLocalizationUseCase by inject()
    private val notificationBuilder: NotificationBuilder by inject()

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
                return@flatMapCompletable downloadForecastForLocalizationUseCase(
                    DownloadForecastForLocalizationUseCase.Params(
                        lat = location.latitude,
                        lon = location.longitude
                    )
                )
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

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}