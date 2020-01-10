package com.helpfulapps.domain.helpers

import com.helpfulapps.domain.use_cases.mockData.MockData
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*

class TimeSetterTest {

    private lateinit var timeSetter: TimeSetter
    //todo change snoozing time, currently 5

    @Nested
    @DisplayName("Snooze for ")
    inner class Snoozes {

        @Test
        fun `first time should schedule`() {
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
        fun `second time should schedule`() {
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
        fun `third time should schedule`() {
            //06/20/2019 @ 2:10am
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
        fun `forth time should not schedule any more`() {
            //06/20/2019 @ 2:15am
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

    @Nested
    @DisplayName("Repeating alarm ")
    inner class RepeatingAlarms {

        @Nested
        @DisplayName("set in the morning ")
        inner class SetInTheMorning {

            @Test
            fun `for one day should be scheduled`() {
//                01/09/2020 @ 8:00am
                val currentTime = 1578556800005L
                val calendar = mock<Calendar>()
                mockkStatic(Calendar::class)
                every { Calendar.getInstance() } returns calendar
                whenever(calendar.timeInMillis).thenReturn(currentTime)

                timeSetter = TimeSetter(calendar, currentTime = { currentTime })

//                01/10/2020 @ 8:00am
                val expected =
                    GregorianCalendar.getInstance().apply { timeInMillis = 1578643200000L }
                val alarm = MockData.createAlarm(
                    hour = 8,
                    minute = 0,
                    isRepeating = true,
                    repetitionDays = arrayOf(false, false, false, false, true, false, false)
                )
                assertEquals(expected, timeSetter.getAlarmStartingCalendar(alarm))
            }

            @Test
            fun `for multiple days should be scheduled`() {
//                01/09/2020 @ 8:00am
                val currentTime = 1578556800005L
                val calendar = mock<Calendar>()
                mockkStatic(Calendar::class)
                every { Calendar.getInstance() } returns calendar
                whenever(calendar.timeInMillis).thenReturn(currentTime)

                timeSetter = TimeSetter(calendar, currentTime = { currentTime })

//                01/10/2020 @ 8:00am
                val expected =
                    GregorianCalendar.getInstance().apply { timeInMillis = 1578643200000L }
                val alarm = MockData.createAlarm(
                    hour = 8,
                    minute = 0,
                    isRepeating = true,
                    repetitionDays = arrayOf(true, true, true, true, true, false, false)
                )
                assertEquals(expected, timeSetter.getAlarmStartingCalendar(alarm))
            }

        }

        @Nested
        @DisplayName("set in the afternoon ")
        inner class SetInTheAfternoon {

            @Test
            fun `for one day should be scheduled`() {
//                01/09/2020 @ 5:00pm
                val currentTime = 1578589200000L
                val calendar = mock<Calendar>()
                mockkStatic(Calendar::class)
                every { Calendar.getInstance() } returns calendar
                whenever(calendar.timeInMillis).thenReturn(currentTime)

                timeSetter = TimeSetter(calendar, currentTime = { currentTime })

//                01/10/2020 @ 8:00am
                val expected =
                    GregorianCalendar.getInstance().apply { timeInMillis = 1578643200000L }
                val alarm = MockData.createAlarm(
                    hour = 8,
                    minute = 0,
                    isRepeating = true,
                    repetitionDays = arrayOf(false, false, false, false, true, false, false)
                )
                assertEquals(expected, timeSetter.getAlarmStartingCalendar(alarm))
            }

            @Test
            fun `for multiple days should be scheduled`() {
//                01/09/2020 @ 5:00pm
                val currentTime = 1578589200000L
                val calendar = mock<Calendar>()
                mockkStatic(Calendar::class)
                every { Calendar.getInstance() } returns calendar
                whenever(calendar.timeInMillis).thenReturn(currentTime)

                timeSetter = TimeSetter(calendar, currentTime = { currentTime })

//                01/10/2020 @ 8:00am
                val expected =
                    GregorianCalendar.getInstance().apply { timeInMillis = 1578643200000L }
                val alarm = MockData.createAlarm(
                    hour = 8,
                    minute = 0,
                    isRepeating = true,
                    repetitionDays = arrayOf(true, true, true, true, true, false, false)
                )
                assertEquals(expected, timeSetter.getAlarmStartingCalendar(alarm))
            }
        }

        @Nested
        @DisplayName("set at night ")
        inner class SetAtNight {

            @Test
            fun `for one day should be scheduled`() {
//                01/09/2020 @ 11:35pm
                val currentTime = 1578612900000L
                val calendar = mock<Calendar>()
                mockkStatic(Calendar::class)
                every { Calendar.getInstance() } returns calendar
                whenever(calendar.timeInMillis).thenReturn(currentTime)

                timeSetter = TimeSetter(calendar, currentTime = { currentTime })

//                01/10/2020 @ 8:00am
                val expected =
                    GregorianCalendar.getInstance().apply { timeInMillis = 1578643200000L }
                val alarm = MockData.createAlarm(
                    hour = 8,
                    minute = 0,
                    isRepeating = true,
                    repetitionDays = arrayOf(false, false, false, false, true, false, false)
                )
                assertEquals(expected, timeSetter.getAlarmStartingCalendar(alarm))
            }

            @Test
            fun `for multiple days should be scheduled`() {
//                01/09/2020 @ 11:35pm
                val currentTime = 1578612900000L
                val calendar = mock<Calendar>()
                mockkStatic(Calendar::class)
                every { Calendar.getInstance() } returns calendar
                whenever(calendar.timeInMillis).thenReturn(currentTime)

                timeSetter = TimeSetter(calendar, currentTime = { currentTime })

//                01/10/2020 @ 8:00am
                val expected =
                    GregorianCalendar.getInstance().apply { timeInMillis = 1578643200000L }
                val alarm = MockData.createAlarm(
                    hour = 8,
                    minute = 0,
                    isRepeating = true,
                    repetitionDays = arrayOf(true, true, true, true, true, false, false)
                )
                assertEquals(expected, timeSetter.getAlarmStartingCalendar(alarm))
            }
        }

        @Nested
        @DisplayName("set after midnight ")
        inner class AfterMidnight {

            @Test
            fun `for one day should be scheduled`() {
//                01/10/2020 @ 12:35am
                val currentTime = 1578616500000L
                val calendar = mock<Calendar>()
                mockkStatic(Calendar::class)
                every { Calendar.getInstance() } returns calendar
                whenever(calendar.timeInMillis).thenReturn(currentTime)

                timeSetter = TimeSetter(calendar, currentTime = { currentTime })

//                01/10/2020 @ 8:00am
                val expected =
                    GregorianCalendar.getInstance().apply { timeInMillis = 1578643200000L }
                val alarm = MockData.createAlarm(
                    hour = 8,
                    minute = 0,
                    isRepeating = true,
                    repetitionDays = arrayOf(false, false, false, false, true, false, false)
                )
                assertEquals(expected, timeSetter.getAlarmStartingCalendar(alarm))
            }

            @Test
            fun `for multiple days should be scheduled`() {
//                01/10/2020 @ 12:35am
                val currentTime = 1578616500000L
                val calendar = mock<Calendar>()
                mockkStatic(Calendar::class)
                every { Calendar.getInstance() } returns calendar
                whenever(calendar.timeInMillis).thenReturn(currentTime)

                timeSetter = TimeSetter(calendar, currentTime = { currentTime })

//                01/10/2020 @ 8:00am
                val expected =
                    GregorianCalendar.getInstance().apply { timeInMillis = 1578643200000L }
                val alarm = MockData.createAlarm(
                    hour = 8,
                    minute = 0,
                    isRepeating = true,
                    repetitionDays = arrayOf(true, true, true, true, true, false, false)
                )
                assertEquals(expected, timeSetter.getAlarmStartingCalendar(alarm))
            }
        }

    }
}