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

    private val _timerStates: MutableLiveData<TimerService.TimerServiceEvent> = MutableLiveData()
    val timerStates: LiveData<TimerService.TimerServiceEvent>
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
                _timerStates.value = it
            }
    }

}