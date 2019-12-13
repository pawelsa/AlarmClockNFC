package com.helpfulapps.alarmclock.helpers

import com.helpfulapps.domain.extensions.disposeCheck
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import java.util.concurrent.TimeUnit

class Stopwatch {

    private var currentTime = 0L
    private var disposable: Disposable? = null
    val emitter: Subject<Long> = BehaviorSubject.create()
    val laps = mutableListOf<Long>()

    fun startStopwatch() {
        if (disposable == null || disposable?.isDisposed != false) {
            disposable = Observable.interval(1, TimeUnit.MILLISECONDS)
                .doOnSubscribe { emitter.onNext(currentTime) }
                .subscribe {
                    emitter.onNext(++currentTime)
                }
        }
    }

    fun pauseStopwatch() {
        disposable.disposeCheck()
    }

    fun takeLap() {
        val lapTime = currentTime
        if (!laps.contains(lapTime)) {
            laps.add(lapTime)
        }
    }

}