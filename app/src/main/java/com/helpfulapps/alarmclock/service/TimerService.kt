package com.helpfulapps.alarmclock.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.helpfulapps.alarmclock.helpers.Timer
import com.helpfulapps.domain.eventBus.RxBus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy

class TimerService : Service() {

    private val timer: Timer = Timer()
    private val disposables = CompositeDisposable()

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        handleIntent(intent)

        return START_STICKY
    }

    private fun handleIntent(intent: Intent?) {
        when (intent?.action) {
            TIMER_START -> startTimer(intent.getLongExtra(TIMER_TIME, 0L))
            TIMER_STOP -> stopTimer()
            TIMER_RESET -> resetTimer()
        }
    }

    private fun startTimer(time: Long) {
        timer.setupTimer(time)
        disposables += timer.emitter
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    RxBus.publish(TimerUpdate(it))
                },
                onComplete = {
                    RxBus.publish(TimerUpdate(-1L))
                }
            )
        timer.startTimer()
    }

    private fun resetTimer() {
        timer.startTimer()
    }

    private fun stopTimer() {
        timer.stopTimer()
    }


    companion object {
        const val TIMER_START = "com.helpfulapps.alarmclock.timer_start"
        const val TIMER_TIME = "com.helpfulapps.alarmclock.timer_time"
        const val TIMER_STOP = "com.helpfulapps.alarmclock.timer_stop"
        const val TIMER_RESET = "com.helpfulapps.alarmclock.timer_reset"
    }

    data class TimerUpdate(val currentTime: Long)

}