package com.helpfulapps.alarmclock.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.helpfulapps.alarmclock.helpers.Timer
import com.helpfulapps.domain.eventBus.RxBus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy

class TimerService : Service() {

    private val TAG = this.javaClass.simpleName
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
                },
                onComplete = {
                    Log.d(TAG, "onComplete")
                    RxBus.publish(TimerUpdate(-1L))
                    stopSelf()
                }
            )
    }

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