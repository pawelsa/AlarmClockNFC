package com.helpfulapps.alarmclock.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import android.util.Log
import com.helpfulapps.alarmclock.helpers.AlarmPlayer
import com.helpfulapps.alarmclock.helpers.NotificationBuilder
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
            stop = {
                alarmPlayer.stopPlaying()
            },
            start = { alarmId ->
                subscribeToAlarm(alarmId) { ringtoneUrl ->
                    val ringtoneUri = Uri.parse(ringtoneUrl)
                    alarmPlayer.startPlaying(ringtoneUri)
                }
                notificationBuilder.createNotification(alarmId)
            })
    }

    private fun subscribeToAlarm(alarmId: Int, onSuccess: (ringtoneUrl: String) -> Unit) {
        disposable += getAlarmUseCase(GetAlarmUseCase.Params(alarmId.toLong())).subscribeBy(
            onSuccess = {
                onSuccess(it.alarm.ringtoneUrl)
            },
            onError = {
                it.printStackTrace()
                Log.e(TAG, it.message ?: "")
            }
        )
    }

    private fun handleIntentAction(
        intent: Intent?,
        stop: () -> Unit,
        start: (alarmId: Int) -> Notification
    ) {
        if (intent?.action == "STOP") {
            stop()
            stopSelf()
        } else {
            val alarmId = intent?.getIntExtra("ALARM_ID", -1) ?: -1
            if (alarmId != -1) {
                val notification = start(alarmId)
                startForeground(1, notification)
            } else {
                stopSelf()
            }
        }
    }

    override fun onDestroy() {
        alarmPlayer.destroyPlayer()
        disposable.clear()
        super.onDestroy()
    }
}
