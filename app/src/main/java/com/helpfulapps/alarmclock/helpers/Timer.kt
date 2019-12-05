package com.helpfulapps.alarmclock.helpers

import android.util.Log
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import java.util.concurrent.TimeUnit

class Timer {

    private val TAG = this.javaClass.simpleName

    private var timeLeft: Long = 0
    val emitter: Subject<Long> = BehaviorSubject.create()
    private var disposable: Disposable? = null

    val isRunning: Boolean
        get() = disposable?.isDisposed ?: false

    fun setupTimer(time: Long) {
        timeLeft = time
    }

    fun startTimer() {
        if (timeLeft != 0L && (disposable == null || disposable?.isDisposed != false)) {
            disposable = Observable.interval(1, TimeUnit.SECONDS)
                .doOnSubscribe { emitter.onNext(timeLeft) }
                .subscribe {
                    Log.d(TAG, "timer: $it")
                    timeLeft--
                    emitter.onNext(timeLeft)
                    if (timeLeft == 0L) {
                        emitter.onComplete()
                        pauseTimer()
                    }
                }
        }
    }

    fun pauseTimer() {
        disposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
    }
}