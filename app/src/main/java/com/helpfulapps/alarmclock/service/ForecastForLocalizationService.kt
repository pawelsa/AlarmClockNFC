package com.helpfulapps.alarmclock.service

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.IBinder
import com.google.android.gms.location.LocationRequest
import com.helpfulapps.alarmclock.helpers.NotificationBuilder
import com.helpfulapps.alarmclock.helpers.extensions.startVersionedForeground
import com.helpfulapps.alarmclock.worker.CreateWork
import com.helpfulapps.base.extensions.rx.backgroundTask
import com.helpfulapps.domain.exceptions.NoPermissionException
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

        startVersionedForeground(
            notification,
            NOTIFICATION_ID,
            ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
        )


        val locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
            .setInterval(3000)

        val rxLocation = RxLocation(baseContext)

        disposables += rxLocation
            .settings()
            .checkAndHandleResolution(locationRequest)
            .flatMapCompletable {
                return@flatMapCompletable locationListener(it, locationRequest)
            }
            .backgroundTask()
            .subscribeBy(
                onComplete = {
                    stopSelf()
                },
                onError = {
                    it.printStackTrace()
                }
            )


        return START_STICKY
    }

    private fun locationListener(
        hasPermission: Boolean,
        locationRequest: LocationRequest
    ): Completable {
        return if (hasPermission) {
            RxLocation(baseContext)
                .location()
                .updates(locationRequest)
                .skip(4)
                .firstOrError()
                .flatMapCompletable { location ->
                    return@flatMapCompletable Completable.create {
                        CreateWork.oneTimeWeatherDownload(
                            baseContext,
                            settings.useMobileData,
                            location
                        )
                        it.onComplete()
                    }
                }
        } else {
            Completable.error(NoPermissionException())
        }
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    companion object {
        private const val NOTIFICATION_ID = 8
    }
}