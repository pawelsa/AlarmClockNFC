package com.helpfulapps.alarmclock.views.hourwatch_fragment

import androidx.lifecycle.LiveData
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

    private val _timer: MutableLiveData<String> = MutableLiveData()
    val timer: LiveData<String>
        get() = _timer

    val timeLeft: MutableLiveData<Long> = MutableLiveData<Long>().apply {
        value = if (settings.timeLeft == -1L) 0 else settings.timeLeft
    }

    var fabPressed: ((Boolean) -> Unit)? = null
    var resetPressed: (() -> Unit)? = null
    var startTimer: (() -> Unit)? = null

    fun listenToTimer() {
        disposables += RxBus.listen(TimerService.TimerUpdate::class.java)
            .subscribe {
                if (it.currentTime == -1L) {
                    isRunning.value = false
                    isPaused.value = false
                    _timer.value = "0"
                } else {
                    _timer.value = "${it.currentTime}"
                }
            }
    }

    fun fabPressed() {
        if (isRunning.value == true) {
            val isPaused = !(isPaused.value ?: false)
            this.isPaused.value = isPaused
            fabPressed?.let { it(isPaused) }
        }
        if (isRunning.value == false) {
            isRunning.value = true
            isPaused.value = false
            settings.timeLeft = timeLeft.value ?: -1L
            startTimer?.let { it() }
        }
    }

    fun resetTimer() {
        isRunning.value = false
        isPaused.value = false
        settings.timeLeft = -1L
        timeLeft.value = 0L
        resetPressed?.let { it() }
    }

}