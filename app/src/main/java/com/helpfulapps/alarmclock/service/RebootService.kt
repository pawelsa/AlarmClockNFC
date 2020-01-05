package com.helpfulapps.alarmclock.service

import android.content.Intent
import com.helpfulapps.alarmclock.helpers.NotificationBuilder
import com.helpfulapps.alarmclock.helpers.extensions.startVersionedForeground
import com.helpfulapps.base.base.BaseService
import com.helpfulapps.domain.helpers.Settings
import com.helpfulapps.domain.use_cases.alarm.SetupAllAlarmsUseCase
import com.helpfulapps.domain.use_cases.weather.DownloadForecastForCityUseCase
import io.reactivex.Completable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import org.koin.core.inject

class RebootService : BaseService() {

    private val setupAllAlarmsUseCase: SetupAllAlarmsUseCase by inject()
    private val downloadForecastForCityUseCase: DownloadForecastForCityUseCase by inject()
    private val notificationBuilder: NotificationBuilder by inject()
    private val settings: Settings by inject()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val useCases = arrayListOf(setupAllAlarmsUseCase())

        if (settings.city != "-1") {
            useCases.add(
                downloadForecastForCityUseCase(
                    DownloadForecastForCityUseCase.Params(
                        settings.city
                    )
                )
            )
        }

        disposables += Completable.merge(useCases)
            .doOnSubscribe {
                val notification =
                    notificationBuilder
                        .setNotificationType(NotificationBuilder.NotificationType.Initialization)
                        .build()
                startVersionedForeground(notification, NOTIFICATION_ID)
            }
            .subscribeBy(
                onComplete = {
                    stopForeground(true)
                },
                onError = {}
            )

        return START_STICKY
    }

    companion object {
        private const val NOTIFICATION_ID = 4
    }
}