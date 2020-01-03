package com.helpfulapps.alarmclock.views.timer_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.helpfulapps.alarmclock.service.TimerService
import com.helpfulapps.base.base.BaseViewModel
import com.helpfulapps.base.extensions.rx.backgroundTask
import com.helpfulapps.domain.eventBus.ServiceBus
import com.helpfulapps.domain.helpers.Settings
import io.reactivex.rxkotlin.plusAssign

class TimerViewModel(
    private val settings: Settings
) : BaseViewModel() {

    private val _timerStates: MutableLiveData<TimerState> = MutableLiveData()
    val timerStates: LiveData<TimerState>
        get() = _timerStates

    fun listenToTimer() {
        disposables += ServiceBus.listen(TimerService.TimerServiceEvent::class.java)
            .backgroundTask()
            .subscribe {
                _timerStates.value = when (it) {
                    is TimerService.TimerServiceEvent.StartTimer -> TimerState.Start(-1L)
                    is TimerService.TimerServiceEvent.UpdateTimer -> TimerState.Update(it.timeLeft)
                    is TimerService.TimerServiceEvent.TimeIsUpTimer -> TimerState.TimeIsUp
                    is TimerService.TimerServiceEvent.FinishTimer -> TimerState.Finished(settings.timeLeft)
                    is TimerService.TimerServiceEvent.PauseTimer -> TimerState.Paused
                    is TimerService.TimerServiceEvent.RestartTimer -> TimerState.Restart
                }
            }
    }

    fun setNewDefaultTimerValue(time: Long) {
        settings.timeLeft = time
    }

    sealed class TimerState {
        data class Start(val time: Long) : TimerState()
        object Restart : TimerState()
        object Paused : TimerState()
        data class Update(val time: Long) : TimerState()
        object TimeIsUp : TimerState()
        data class Finished(val time: Long) : TimerState()
    }

}