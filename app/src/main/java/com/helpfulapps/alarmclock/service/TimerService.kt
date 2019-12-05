package com.helpfulapps.alarmclock.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.helpfulapps.alarmclock.App
import com.helpfulapps.alarmclock.helpers.NotificationBuilder
import com.helpfulapps.alarmclock.helpers.Timer
import com.helpfulapps.domain.eventBus.RxBus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import org.koin.core.KoinComponent
import org.koin.core.inject

class TimerService : Service(), KoinComponent {

    private val TAG = this.javaClass.simpleName
    private val timer: Timer = Timer()
    private val disposables = CompositeDisposable()
    private val notificationBuilder: NotificationBuilder by inject()
    private var isForeground: Boolean = true

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        handleIntent(intent)

        return START_STICKY
    }

    private fun handleIntent(intent: Intent?) {
        when (intent?.action) {
            TIMER_START -> startTimer(intent.getLongExtra(TIMER_TIME, 0L))
            TIMER_STOP -> stopTimer()
            TIMER_RESTART -> restartTimer()
            TIMER_FINISH -> finishTimer()
        }
    }

    private fun startTimer(time: Long? = null) {
        time?.let {
            if (it != 0L) {
                timer.setupTimer(it)
            }
        }

        timer.startTimer()
        disposables += timer.emitter
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    Log.d(TAG, "onNext")
                    RxBus.publish(TimerUpdate(it))
                    if (isForeground) {
                        stopForeground(true)
                    } else {
                        showNotification(it)
                    }
                },
                onComplete = {
                    Log.d(TAG, "onComplete")
                    RxBus.publish(TimerUpdate(-1L))
                    stopForeground(true)
                    stopSelf()
                }
            )

        disposables += RxBus.listen(App.AppState::class.java)
            .subscribe {
                isForeground = it.isForeground
            }
    }

    private fun showNotification(timeLeft: Long) {
        startForeground(TIMER_SERVICE_ID, getNotification(timeLeft))
    }

    private fun getNotification(timeLeft: Long): Notification =
        notificationBuilder.setNotificationType(
            NotificationBuilder.NotificationType.TypeTimer(
                timeLeft
            )
        ).build()

    private fun restartTimer() {
        startTimer()
    }

    private fun stopTimer() {
        timer.pauseTimer()
    }

    private fun finishTimer() {
        timer.pauseTimer()
        stopSelf()
    }


    companion object {
        const val TIMER_SERVICE_ID = 5
        const val TIMER_START = "com.helpfulapps.alarmclock.timer_start"
        const val TIMER_TIME = "com.helpfulapps.alarmclock.timer_time"
        const val TIMER_STOP = "com.helpfulapps.alarmclock.timer_stop"
        const val TIMER_RESTART = "com.helpfulapps.alarmclock.timer_restart"
        const val TIMER_FINISH = "com.helpfulapps.alarmclock.timer_finish"
    }

    data class TimerUpdate(val currentTime: Long)

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}