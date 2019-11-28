package com.helpfulapps.alarmclock.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

class StopwatchService : Service() {

    private val TAG = this.javaClass.simpleName

    private val disposables = CompositeDisposable()
    private var isRunning: Boolean = false
    private var currentTime: Long = 0L


    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        handleIntent(intent)

//        val notification = buildNotification()

        return START_STICKY
    }

    private fun handleIntent(intent: Intent?) {
        when (intent?.action) {
            STOPWATCH_START -> startStopwatch()

        }
    }

    private fun startStopwatch() {
        disposables += Observable.interval(1, TimeUnit.MILLISECONDS)
            .doOnSubscribe { isRunning = true }
            .subscribe {
                if (isRunning) {
                    currentTime++
                }
                val time = SimpleDateFormat("HH:mm:ss.SSS").format(currentTime)
                Log.d(TAG, time)
            }
    }


    companion object {
        const val STOPWATCH_START = "com.helpfulapps.alarmclock.stopwatch_start"
        const val STOPWATCH_RESET = "com.helpfulapps.alarmclock.stopwatch_reset"
        const val STOPWATCH_PAUSE = "com.helpfulapps.alarmclock.stopwatch_pause"
        const val STOPWATCH_LAP = "com.helpfulapps.alarmclock.stopwatch_lap"
    }

}