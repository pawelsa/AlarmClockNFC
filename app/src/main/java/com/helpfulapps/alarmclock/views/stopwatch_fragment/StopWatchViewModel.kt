package com.helpfulapps.alarmclock.views.stopwatch_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.helpfulapps.alarmclock.service.StopwatchService
import com.helpfulapps.base.base.BaseViewModel
import com.helpfulapps.base.extensions.rx.backgroundTask
import com.helpfulapps.domain.eventBus.ServiceBus
import io.reactivex.rxkotlin.plusAssign

class StopWatchViewModel : BaseViewModel() {

    private val _stopwatchState: MutableLiveData<StopWatchState> =
        MutableLiveData()
    val stopwatchState: LiveData<StopWatchState>
        get() = _stopwatchState

    private val _lapTimes: MutableLiveData<List<Long>> = MutableLiveData()
    val lapTimes: LiveData<List<Long>>
        get() = _lapTimes

    private val _currentTime: MutableLiveData<Long> = MutableLiveData()
    val currentTime: LiveData<Long>
        get() = _currentTime

    fun observeStopwatch() {

        disposables += ServiceBus.listen(StopwatchService.StopWatchEvent::class.java)
            .backgroundTask()
            .subscribe {
                when (it) {
                    is StopwatchService.StopWatchEvent.Start -> _stopwatchState.value =
                        StopWatchState.Started
                    is StopwatchService.StopWatchEvent.Update -> _currentTime.value =
                        it.timeInMillis
                    is StopwatchService.StopWatchEvent.Paused -> _stopwatchState.value =
                        StopWatchState.Paused
                    is StopwatchService.StopWatchEvent.Resume -> _stopwatchState.value =
                        StopWatchState.Resumed
                    is StopwatchService.StopWatchEvent.Lap -> _lapTimes.value = it.laps
                    is StopwatchService.StopWatchEvent.Stop -> {
                        _lapTimes.value = emptyList()
                        _currentTime.value = 0
                        _stopwatchState.value =
                            StopWatchState.Stopped
                    }
                }
            }

    }

    sealed class StopWatchState {
        object Stopped : StopWatchState()
        object Started : StopWatchState()
        object Paused : StopWatchState()
        object Resumed : StopWatchState()
    }

}