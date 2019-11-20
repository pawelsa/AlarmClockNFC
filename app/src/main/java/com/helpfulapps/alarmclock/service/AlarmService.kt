package com.helpfulapps.alarmclock.service

import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import android.util.Log
import com.helpfulapps.alarmclock.helpers.AlarmPlayer
import com.helpfulapps.alarmclock.helpers.NotificationBuilder
import com.helpfulapps.alarmclock.helpers.NotificationBuilderImpl.Companion.KEY_ALARM_ID
import com.helpfulapps.base.extensions.rx.backgroundTask
import com.helpfulapps.domain.models.alarm.Alarm
import com.helpfulapps.domain.use_cases.alarm.GetAlarmUseCase
import com.helpfulapps.domain.use_cases.alarm.StopRingingAlarmUseCase
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import org.koin.android.ext.android.inject


class AlarmService : Service() {

    private val TAG = AlarmService::class.java.simpleName

    private var alarm: Alarm? = null

    private val getAlarmUseCase: GetAlarmUseCase by inject()
    private val stopRingingAlarmUseCase: StopRingingAlarmUseCase by inject()
    private val alarmPlayer: AlarmPlayer by inject()
    private val notificationBuilder: NotificationBuilder by inject()

    private val disposable = CompositeDisposable()

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handleIntent(intent)
        return START_STICKY
    }

    private fun handleIntent(intent: Intent?) {
        val alarmId = intent?.getIntExtra(KEY_ALARM_ID, -1) ?: -1
        when {
            intent?.action == "STOP" -> stopAlarm()
            alarmId != -1 -> startAlarm(alarmId)
            else -> stopSelf()
        }
    }

    private fun stopAlarm() {
        alarm?.let {
            alarmPlayer.stopPlaying()
            disposable += stopRingingAlarmUseCase(StopRingingAlarmUseCase.Param(it))
                .backgroundTask()
                .subscribe {
                    stopSelf()
                }
        }
    }

    private fun startAlarm(alarmId: Int) {
        subscribeToAlarm(alarmId) {
            val ringtoneUri = Uri.parse(it.ringtoneUrl)
            alarmPlayer.startPlaying(ringtoneUri)
            val notification = notificationBuilder.setNotificationType(
                NotificationBuilder.NotificationType.TypeAlarm(
                    it
                )
            ).build()
            startForeground(1, notification)
        }
    }

    private fun subscribeToAlarm(alarmId: Int, onSuccess: (alarm: Alarm) -> Unit) {
        if (alarmId == -1) return
        disposable += getAlarmUseCase(GetAlarmUseCase.Params(alarmId.toLong()))
            .backgroundTask()
            .subscribeBy(
                onSuccess = {
                    alarm = it.alarm
                    alarm?.let { alarmToProcess ->
                        onSuccess(alarmToProcess)
                    }
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
