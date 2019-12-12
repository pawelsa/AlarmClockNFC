package com.helpfulapps.alarmclock.views.stopwatch_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.helpfulapps.alarmclock.service.StopwatchService
import com.helpfulapps.base.base.BaseViewModel
import com.helpfulapps.domain.eventBus.ServiceBus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign

class StopWatchViewModel : BaseViewModel() {

    private val _stopwatchState: MutableLiveData<StopWatchState> = MutableLiveData()
    val stopwatchState: LiveData<StopWatchState>
        get() = _stopwatchState

    private val _lapTimes: MutableLiveData<List<Long>> = MutableLiveData()
    val lapTimes: LiveData<List<Long>>
        get() = _lapTimes

    fun observeStopwatch() {

        disposables += ServiceBus.listen(StopwatchService.StopWatchEvent::class.java)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                when (it) {
                    is StopwatchService.StopWatchEvent.Start -> _stopwatchState.value =
                        StopWatchState.Started
                    is StopwatchService.StopWatchEvent.Update -> _stopwatchState.value =
                        StopWatchState.Update(it.timeInMillis)
                    is StopwatchService.StopWatchEvent.Pause -> _stopwatchState.value =
                        StopWatchState.Paused
                    is StopwatchService.StopWatchEvent.Resume -> _stopwatchState.value =
                        StopWatchState.Resumed
                    is StopwatchService.StopWatchEvent.Lap -> _lapTimes.value = it.laps
                    is StopwatchService.StopWatchEvent.Stop -> _stopwatchState.value =
                        StopWatchState.Stopped
                }
            }

    }

    sealed class StopWatchState {
        object Stopped : StopWatchState()
        object Started : StopWatchState()
        data class Update(val time: Long) : StopWatchState()
        object Paused : StopWatchState()
        object Resumed : StopWatchState()
    }

}