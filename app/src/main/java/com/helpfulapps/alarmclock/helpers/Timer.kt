package com.helpfulapps.alarmclock.helpers

import com.helpfulapps.domain.extensions.disposeCheck
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import java.util.concurrent.TimeUnit

class Timer {

    var timeLeft: Long = 0
        private set
    val emitter: Subject<Long> = BehaviorSubject.create()
    private var disposable: Disposable? = null

    val isRunning: Boolean
        get() = !(disposable?.isDisposed ?: true)
    val isPaused: Boolean
        get() = !isRunning

    fun setupTimer(time: Long) {
        timeLeft = time
    }

    fun startTimer() {
        if (timeLeft != 0L && (disposable == null || disposable?.isDisposed != false)) {
            disposable = Observable.interval(1, TimeUnit.SECONDS)
                .doOnSubscribe { emitter.onNext(timeLeft) }
                .subscribe {
                    emitter.onNext(--timeLeft)
                    if (timeLeft == 0L) {
                        emitter.onComplete()
                        pauseTimer()
                    }
                }
        }
    }

    fun pauseTimer() {
        disposable.disposeCheck()
    }

    fun addMinute() {
        timeLeft += 60
    }
}