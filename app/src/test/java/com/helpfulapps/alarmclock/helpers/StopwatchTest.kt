package com.helpfulapps.alarmclock.helpers

import io.reactivex.Observable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit

class StopwatchTest {

    private var stopwatch: Stopwatch = Stopwatch()

    @BeforeEach
    fun setUp() {
        stopwatch = Stopwatch()
    }

    @Test
    fun `should start and stop successfully`() {
        Observable.interval(1L, TimeUnit.SECONDS)
            .doOnSubscribe { stopwatch.startStopwatch() }
            .take(1)
            .blockingSubscribe {
                stopwatch.pauseStopwatch()
            }

        assertTrue(stopwatch.seconds >= 1)
    }

    @Test
    fun `should take a lap successfully`() {
        Observable.interval(100L, TimeUnit.MILLISECONDS)
            .doOnSubscribe { stopwatch.startStopwatch() }
            .doOnDispose { stopwatch.pauseStopwatch() }
            .take(4)
            .blockingSubscribe {
                stopwatch.takeLap()
            }

        assertEquals(4, stopwatch.laps.size)
    }

    @Test
    fun `should restart successfully`() {
        Observable.interval(100L, TimeUnit.MILLISECONDS)
            .doOnSubscribe { stopwatch.startStopwatch() }
            .doOnDispose { stopwatch.pauseStopwatch() }
            .take(5)
            .blockingSubscribe {
                when (it) {
                    1L -> stopwatch.pauseStopwatch()
                    2L -> stopwatch.startStopwatch()
                }
            }

        val currentTime = stopwatch.emitter
            .blockingFirst()

        assertTrue(currentTime in 390L..410L)
    }

    @Test
    fun `should take multiple laps with pause successfully`() {
        Observable.interval(100L, TimeUnit.MILLISECONDS)
            .doOnSubscribe { stopwatch.startStopwatch() }
            .doOnDispose { stopwatch.pauseStopwatch() }
            .take(5)
            .blockingSubscribe {
                when (it) {
                    0L -> stopwatch.takeLap()
                    1L -> stopwatch.pauseStopwatch()
                    2L -> stopwatch.startStopwatch()
                    3L -> stopwatch.takeLap()
                    4L -> stopwatch.takeLap()
                }
            }

        assertTrue(stopwatch.laps.size == 3)
        assertTrue(stopwatch.laps[0] in 100L..110L)
        assertTrue(stopwatch.laps[1] in 300L..310L)
        assertTrue(stopwatch.laps[2] in 400L..410L)
    }

}