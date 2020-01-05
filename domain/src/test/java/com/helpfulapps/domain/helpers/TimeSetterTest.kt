package com.helpfulapps.domain.helpers

import com.helpfulapps.domain.use_cases.mockData.MockData
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

class TimeSetterTest {

    private lateinit var timeSetter: TimeSetter
    //todo change snoozing time, currently 5
    @Test
    fun `should schedule first alarm`() {
        val currentTime = 1560996000000L
        val calendar = mock<Calendar>()
        mockkStatic(Calendar::class)
        every { Calendar.getInstance() } returns calendar
        //06/20/2019 @ 2:00am
        whenever(calendar.timeInMillis).thenReturn(currentTime)

        timeSetter = TimeSetter(calendar, currentTime = { currentTime })

        val expected = 1560996300000L
        val alarm = MockData.createAlarm(hour = 2, minute = 0)
        assertEquals(expected, timeSetter.getAlarmSnoozeTime(alarm, 5))
    }

    @Test
    fun `should schedule second alarm`() {
        val currentTime = 1560996300000L
        val calendar = mock<Calendar>()
        mockkStatic(Calendar::class)
        every { Calendar.getInstance() } returns calendar
        //06/20/2019 @ 2:00am
        whenever(calendar.timeInMillis).thenReturn(currentTime)

        timeSetter = TimeSetter(calendar, currentTime = { currentTime })

        val expected = 1560996600000L
        val alarm = MockData.createAlarm(hour = 2, minute = 0)
        assertEquals(expected, timeSetter.getAlarmSnoozeTime(alarm, 5))
    }

    @Test
    fun `should schedule third alarm`() {
        val currentTime = 1560996600000L
        val calendar = mock<Calendar>()
        mockkStatic(Calendar::class)
        every { Calendar.getInstance() } returns calendar
        //06/20/2019 @ 2:00am
        whenever(calendar.timeInMillis).thenReturn(currentTime)

        timeSetter = TimeSetter(calendar, currentTime = { currentTime })

        val expected = 1560996900000L
        val alarm = MockData.createAlarm(hour = 2, minute = 0)
        assertEquals(expected, timeSetter.getAlarmSnoozeTime(alarm, 5))
    }

    @Test
    fun `should not schedule any more`() {
        val currentTime = 1560996900000L
        val calendar = mock<Calendar>()
        mockkStatic(Calendar::class)
        every { Calendar.getInstance() } returns calendar
        //06/20/2019 @ 2:00am
        whenever(calendar.timeInMillis).thenReturn(currentTime + 5)

        timeSetter = TimeSetter(calendar, currentTime = { currentTime })

        val expected = -1
        val alarm = MockData.createAlarm(hour = 2, minute = 0)
        assertEquals(expected, timeSetter.getAlarmSnoozeTime(alarm, 5))
    }

}