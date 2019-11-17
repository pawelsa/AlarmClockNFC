package com.helpfulapps.alarmclock.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import android.util.Log
import com.helpfulapps.alarmclock.helpers.AlarmPlayer
import com.helpfulapps.alarmclock.helpers.NotificationBuilder
import com.helpfulapps.alarmclock.helpers.NotificationBuilderImpl.Companion.KEY_ALARM_ID
import com.helpfulapps.device.alarms.Alarm
import com.helpfulapps.domain.use_cases.alarm.GetAlarmUseCase
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import org.koin.android.ext.android.inject


class AlarmService : Service() {

    private val TAG = AlarmService::class.java.simpleName

    private val getAlarmUseCase: GetAlarmUseCase by inject()
    private val alarmPlayer: AlarmPlayer by inject()
    private val notificationBuilder: NotificationBuilder by inject()

    private val disposable = CompositeDisposable()

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handleIntent(intent)
        return START_STICKY
    }

    private fun handleIntent(intent: Intent?) {
        handleIntentAction(intent,
            start = { alarm ->
                val ringtoneUri = Uri.parse(alarm.ringtoneUrl)
                alarmPlayer.startPlaying(ringtoneUri)
                notificationBuilder.setNotificationType(
                    NotificationBuilder.NotificationType.TypeAlarm(
                        alarm
                    )
                ).build()
            },
            stop = {
                alarmPlayer.stopPlaying()
            })
    }

    private fun handleIntentAction(
        intent: Intent?,
        stop: () -> Unit,
        start: (alarm: Alarm) -> Notification
    ) {
        if (intent?.action == "STOP") {
            stop()
            stopSelf()
        } else {
            val alarmId = intent?.getIntExtra(KEY_ALARM_ID, -1) ?: -1
            if (alarmId != -1) {
                subscribeToAlarm(alarmId) {
                    val notification = start(it)
                    startForeground(1, notification)
                }
            } else {
                stopSelf()
            }
        }
    }

    private fun subscribeToAlarm(alarmId: Int, onSuccess: (alarm: Alarm) -> Unit) {
        disposable += getAlarmUseCase(GetAlarmUseCase.Params(alarmId.toLong())).subscribeBy(
            onSuccess = {
                onSuccess(Alarm(it.alarm))
            },
            onError = {
                it.printStackTrace()
                Log.e(TAG, it.message ?: "")
            }
        )
    }

    override fun onDestroy() {
        alarmPlayer.destroyPlayer()
        disposable.clear()
        super.onDestroy()
    }
}
