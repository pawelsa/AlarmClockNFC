package com.helpfulapps.alarmclock.views.hourwatch_fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.helpfulapps.alarmclock.service.TimerService
import com.helpfulapps.base.base.BaseViewModel
import com.helpfulapps.domain.eventBus.ServiceBus
import com.helpfulapps.domain.helpers.Settings
import io.reactivex.rxkotlin.plusAssign

class HourWatchViewModel(
    private val settings: Settings
) : BaseViewModel() {

    private val TAG = this.javaClass.simpleName

    private val _timerStates: MutableLiveData<TimerState> = MutableLiveData()
    val timerStates: LiveData<TimerState>
        get() = _timerStates

    fun listenToTimer() {
        Log.d(TAG, "listenToTimer")
        disposables += ServiceBus.listen(TimerService.TimerServiceEvent::class.java)
            .subscribe {
                /*Log.d(TAG, "timerServiceEvent : ${it.javaClass.simpleName}")
                when (it) {
                    is TimerService.TimerServiceEvent.UpdateTimer -> updateTimer(it)
                    is TimerService.TimerServiceEvent.TimeIsUpTimer -> {
                        Log.d(TAG, "timerIsUp")
                        _timer.value = 5000L.toString()
                    }
                    is TimerService.TimerServiceEvent.FinishTimer -> clearTimer()
//                    is TimerService.TimerServiceEvent.RestartTimer -> clearTimer()
//                    is TimerService.TimerServiceEvent.PauseTimer -> pauseTimer()
                }*/
                Log.d(TAG, "timerServiceEvent : ${it.javaClass.simpleName}")
                when (it) {
                    is TimerService.TimerServiceEvent.StartTimer -> _timerStates.value =
                        TimerState.Start(-1L)
                    is TimerService.TimerServiceEvent.UpdateTimer -> _timerStates.value =
                        TimerState.Update(it.timeLeft)
                    is TimerService.TimerServiceEvent.TimeIsUpTimer -> _timerStates.value =
                        TimerState.TimeIsUp
                    is TimerService.TimerServiceEvent.FinishTimer -> _timerStates.value =
                        TimerState.Finished(settings.timeLeft)
                    /*is TimerService.TimerServiceEvent.RestartTimer -> {
                        settings.timeLeft = -1L
                        _timerStates.value = TimerState.Start(settings.timeLeft)
                    }*/
                    is TimerService.TimerServiceEvent.PauseTimer -> _timerStates.value =
                        TimerState.Paused
                }
            }
    }

    sealed class TimerState {
        object Paused : TimerState()
        object Running : TimerState()
        data class Update(val time: Long) : TimerState()
        object TimeIsUp : TimerState()
        data class Finished(val time: Long) : TimerState()
        data class Start(val time: Long) : TimerState()
    }

}