package com.helpfulapps.alarmclock.helpers

import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TimerTest {

    private lateinit var timer: Timer

    @Before
    fun setUp() {
        timer = Timer()
    }

    @Test
    fun `should start timer`() {
        timer.setupTimer(5)
        timer.startTimer()

        val iterable = timer.emitter.blockingIterable()

        assertEquals(6, iterable.toList().size)
    }

    @Test
    fun `should pause timer`() {

        timer.setupTimer(5)
        timer.startTimer()

        Thread.sleep(2008)

        timer.stopTimer()

        Thread.sleep(2000)

        timer.startTimer()

        val iterable = timer.emitter.blockingIterable()

        assertEquals(4, iterable.toList().size)
    }

    @Test
    fun `should finish`() {
        timer.setupTimer(3)
        timer.startTimer()

        val testObserver = timer.emitter.test()

        testObserver.awaitTerminalEvent()

        testObserver
            .assertComplete()
            .dispose()
    }

}