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
            TIMER_START -> setupTimer(intent.getLongExtra(TIMER_TIME, 0L))
        }
    }

    private fun setupTimer(timeLeft: Long) {
        if (timeLeft != 0L) {
            timer.setupTimer(timeLeft)


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
                    // todo check whyy not correctly working, maybe problem is with show notification
                    isForeground = when (it) {
                        is App.AppState.IsForeground -> true
                        is App.AppState.IsBackground -> false
                    }
                }

            disposables += RxBus.listen(TimerServiceEvent::class.java)
                .subscribe {
                    when (it) {
                        is TimerServiceEvent.StopTimer -> stopTimer()
                        is TimerServiceEvent.RestartTimer -> restartTimer()
                        is TimerServiceEvent.FinishTimer -> finishTimer()
                    }
                }
            timer.startTimer()
        } else {
            stopSelf()
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
        timer.startTimer()
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
    }

    data class TimerUpdate(val currentTime: Long)

    sealed class TimerServiceEvent {
        object StopTimer : TimerServiceEvent()
        object RestartTimer : TimerServiceEvent()
        object FinishTimer : TimerServiceEvent()
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}