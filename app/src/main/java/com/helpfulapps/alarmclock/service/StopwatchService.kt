package com.helpfulapps.alarmclock.service

import android.content.Intent
import com.helpfulapps.alarmclock.helpers.NotificationBuilder
import com.helpfulapps.alarmclock.helpers.Stopwatch
import com.helpfulapps.base.base.BaseService
import com.helpfulapps.domain.eventBus.ServiceBus
import io.reactivex.rxkotlin.plusAssign
import org.koin.core.inject

class StopwatchService : BaseService() {

    private val TAG = this.javaClass.simpleName

    private val stopwatch = Stopwatch()
    private val notificationBuilder: NotificationBuilder by inject()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        handleIntent(intent)

        return START_STICKY
    }

    private fun handleIntent(intent: Intent?) {
        when (intent?.action) {
            STOPWATCH_START -> startStopwatch()
            STOPWATCH_PAUSE -> ServiceBus.publish(StopWatchEvent.Pause)
            STOPWATCH_RESUME -> ServiceBus.publish(StopWatchEvent.Resume)
            STOPWATCH_LAP -> ServiceBus.publish(StopWatchEvent.TakeLap)
            STOPWATCH_STOP -> ServiceBus.publish(StopWatchEvent.Stop)
        }
    }

    private fun startStopwatch() {
        disposables += ServiceBus.listen(StopWatchEvent::class.java)
            .subscribe {
                when (it) {
                    is StopWatchEvent.Pause -> pauseStopwatch()
                    is StopWatchEvent.Resume -> resumeStopwatch()
                    is StopWatchEvent.TakeLap -> takeLap()
                    is StopWatchEvent.Stop -> stopStopwatch()
                }
            }

        //todo handle app visibility
        disposables += stopwatch.emitter.subscribe {
            ServiceBus.publish(StopWatchEvent.Update(it))
        }

        ServiceBus.publish(StopWatchEvent.Start)
        stopwatch.startStopwatch()

        val notification =
            notificationBuilder.setNotificationType(NotificationBuilder.NotificationType.TypeTimerFinished)
                .build()
        startForeground(STOPWATCH_SERVICE_ID, notification)
    }

    private fun stopStopwatch() {
        stopwatch.pauseStopwatch()
        stopForeground(true)
        stopSelf()
    }

    private fun takeLap() {
        stopwatch.takeLap()
        ServiceBus.publish(StopWatchEvent.Lap(stopwatch.laps))
    }

    private fun resumeStopwatch() {
        stopwatch.startStopwatch()
    }

    private fun pauseStopwatch() {
        stopwatch.pauseStopwatch()
        ServiceBus.publish(StopWatchEvent.Paused)
    }

    sealed class StopWatchEvent {
        object Start : StopWatchEvent()
        data class Update(val timeInMillis: Long) : StopWatchEvent()
        object Pause : StopWatchEvent()
        object Paused : StopWatchEvent()
        object Resume : StopWatchEvent()
        object TakeLap : StopWatchEvent()
        data class Lap(val laps: List<Long>) : StopWatchEvent()
        object Stop : StopWatchEvent()
    }

    companion object {
        const val STOPWATCH_SERVICE_ID = 6
        const val STOPWATCH_START = "com.helpfulapps.alarmclock.stopwatch_start"
        const val STOPWATCH_STOP = "com.helpfulapps.alarmclock.stopwatch_stop"
        const val STOPWATCH_RESUME = "com.helpfulapps.alarmclock.stopwatch_resume"
        const val STOPWATCH_PAUSE = "com.helpfulapps.alarmclock.stopwatch_pause"
        const val STOPWATCH_LAP = "com.helpfulapps.alarmclock.stopwatch_lap"
    }

}