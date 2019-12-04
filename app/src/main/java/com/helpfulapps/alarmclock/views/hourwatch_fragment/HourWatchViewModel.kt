package com.helpfulapps.alarmclock.views.hourwatch_fragment

import androidx.lifecycle.MutableLiveData
import com.helpfulapps.alarmclock.service.TimerService
import com.helpfulapps.base.base.BaseViewModel
import com.helpfulapps.domain.eventBus.RxBus
import com.helpfulapps.domain.helpers.Settings
import io.reactivex.rxkotlin.plusAssign

class HourWatchViewModel(
    private val settings: Settings
) : BaseViewModel() {

    val isRunning = MutableLiveData<Boolean>(false)
    val isPaused = MutableLiveData<Boolean>(false)

    val timeLeft: MutableLiveData<Long> = MutableLiveData<Long>().apply {
        value = if (settings.timeLeft == -1L) 0 else settings.timeLeft
    }

    fun listenToTimer() {
        disposables += RxBus.listen(TimerService.TimerUpdate::class.java).subscribe {
            timeLeft.value = it.currentTime
        }
    }

    fun fabPressed() {
        if (isRunning.value == false) {
            isRunning.value = true
            isPaused.value = false
            settings.timeLeft = timeLeft.value ?: -1L
        }
        if (isRunning.value == true) {
            isPaused.value = !(isPaused.value ?: false)
        }
    }

    fun resetTimer() {
        isRunning.value = false
        isPaused.value = false
        settings.timeLeft = -1L
        timeLeft.value = 0L
    }

}