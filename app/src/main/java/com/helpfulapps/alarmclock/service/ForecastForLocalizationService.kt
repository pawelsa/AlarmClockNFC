package com.helpfulapps.alarmclock.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.google.android.gms.location.LocationRequest
import com.helpfulapps.alarmclock.helpers.NotificationBuilder
import com.helpfulapps.alarmclock.helpers.startVersionedForeground
import com.helpfulapps.alarmclock.worker.CreateWork
import com.helpfulapps.base.extensions.rx.backgroundTask
import com.helpfulapps.domain.helpers.Settings
import com.patloew.rxlocation.RxLocation
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import org.koin.android.ext.android.inject

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
                    CreateWork.oneTimeWeatherDownload(baseContext, settings.useMobileData, location)
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




    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}