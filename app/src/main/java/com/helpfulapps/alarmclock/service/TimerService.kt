package com.helpfulapps.alarmclock.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.helpfulapps.alarmclock.helpers.Timer
import com.helpfulapps.domain.eventBus.RxBus
import org.koin.core.KoinComponent
import org.koin.core.inject

class TimerService : Service(), KoinComponent {

    private val timer: Timer by inject()

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
        timer.setupTimer(time) {
            RxBus.publish(TimerUpdate(it))
        }
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