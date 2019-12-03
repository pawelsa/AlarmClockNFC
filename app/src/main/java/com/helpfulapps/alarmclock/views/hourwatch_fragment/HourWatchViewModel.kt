package com.helpfulapps.alarmclock.views.hourwatch_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.helpfulapps.alarmclock.service.TimerService
import com.helpfulapps.base.base.BaseViewModel
import com.helpfulapps.domain.eventBus.RxBus
import io.reactivex.rxkotlin.plusAssign

class HourWatchViewModel : BaseViewModel() {

    val isRunning = MutableLiveData<Boolean>(false)

    private val _timeLeft: MutableLiveData<Long> = MutableLiveData()
    val timeLeft: LiveData<Long>
        get() = _timeLeft

    fun listenToTimer() {
        disposables += RxBus.listen(TimerService.TimerUpdate::class.java).subscribe {
            _timeLeft.value = it.currentTime
        }
    }

    fun fabPressed() {
        isRunning.value = isRunning.value != true
    }

}