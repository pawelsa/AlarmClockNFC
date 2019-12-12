package com.helpfulapps.alarmclock.helpers

import android.util.Log
import com.helpfulapps.domain.extensions.disposeCheck
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import java.util.concurrent.TimeUnit

class Stopwatch {

    private val TAG = this.javaClass.simpleName
    private var currentTime = 0L
    val emitter: Subject<Long> = BehaviorSubject.create()
    private var disposable: Disposable? = null
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
        Log.d(TAG, "Is null? : ${disposable == null}")
    }

    fun takeLap() {
        laps.add(currentTime)
    }

}