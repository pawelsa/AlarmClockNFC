package com.helpfulapps.alarmclock.helpers

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit


interface Timer {
    fun setupTimer(time: Long, listener: (Long) -> Unit)
    fun startTimer()
    fun stopTimer()
}


class TimerImpl : Timer {

    private var timeLeft: Long = 0
    private lateinit var emitTime: (Long) -> Unit
    private var disposable: Disposable? = null

    override fun setupTimer(time: Long, listener: (Long) -> Unit) {
        timeLeft = time
        emitTime = listener
    }

    override fun startTimer() {
        if (timeLeft != 0L && (disposable == null || disposable?.isDisposed != false)) {
            disposable = Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    timeLeft--
                    emitTime(timeLeft)
                    if (timeLeft == 0L) {
                        stopTimer()
                    }
                }
        }
    }

    override fun stopTimer() {
        disposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
    }
}