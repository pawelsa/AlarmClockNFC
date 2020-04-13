package com.helpfulapps.domain.helpers

import com.helpfulapps.domain.use_cases.mockData.MockData
import com.helpfulapps.domain.use_cases.mockData.MockData.createAlarm
import com.soywiz.klock.DateTime
import com.soywiz.klock.Month
import com.soywiz.klock.Year
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


class NewTimeSetterTest {

    private lateinit var timeSetter: ITimeSetter

    @Nested
    @DisplayName("Get start time for ")
    inner class StartTime {

        @Nested
        @DisplayName("repeating alarm")
        inner class RepeatingAlarm {

            @Test
            fun `which time is set for earlier that day`() {
                //jest wtorek 12:30, a powtarza się we wtorki o 12:00
                val alarm = createAlarm(
                    hour = 12,
                    isRepeating = true,
                    repetitionDays = arrayOf(false, true, false, false, false, false, false)
                )
                // Tuesday, 3 March 2020 12:30:00
                val currentTime = 1583238600000L
                // Tuesday, 10 March 2020 12:00:00
                val expectedAlarmTime = 1583841600000L


                val obtainedTime = timeSetter.getAlarmStartingTime(alarm)
                assertEquals(expectedAlarmTime, obtainedTime)
            }

            @Test
            fun `which time is set in the next 10 minutes`() {
                val alarm = createAlarm(
                    hour = 12,
                    minute = 40,
                    isRepeating = true,
                    repetitionDays = arrayOf(false, true, false, false, false, false, false)
                )
                // Tuesday, 3 March 2020 12:30:00
                val currentTime = 1583238600000L
                // Tuesday, 3 March 2020 12:40:00
                val expectedAlarmTime = 1583239200000L


                val obtainedTime = timeSetter.getAlarmStartingTime(alarm)
                assertEquals(expectedAlarmTime, obtainedTime)
            }

            @Test
            fun `which should ring the next day`() {
                val alarm = createAlarm(
                    hour = 12,
                    isRepeating = true,
                    repetitionDays = arrayOf(false, false, true, false, false, false, false)
                )
                // Tuesday, 3 March 2020 12:30:00
                val currentTime = 1583238600000L
                // Wednesday, 4 March 2020 12:00:00
                val expectedAlarmTime = 1583323200000L


                val obtainedTime = timeSetter.getAlarmStartingTime(alarm)
                assertEquals(expectedAlarmTime, obtainedTime)
            }

            @Test
            fun `which should ring at midnight`() {
                val alarm = createAlarm(
                    hour = 0,
                    isRepeating = true,
                    repetitionDays = arrayOf(false, false, true, false, false, false, false)
                )
                // Tuesday, 3 March 2020 12:30:00
                val currentTime = 1583238600000L
                // Wednesday, 4 March 2020 00:00:00
                val expectedAlarmTime = 1583280000000L


                val obtainedTime = timeSetter.getAlarmStartingTime(alarm)
                assertEquals(expectedAlarmTime, obtainedTime)
            }

            @Test
            fun `which is set at sunday and should ring on monday`() {
                val alarm = createAlarm(
                    hour = 12,
                    isRepeating = true,
                    repetitionDays = arrayOf(true, true, false, false, false, false, false)
                )
                // Sunday, 8 March 2020 20:00:00
                val currentTime = 1583697600000L
                // Monday, 9 March 2020 12:00:00
                val expectedAlarmTime = 1583755200000L


                val obtainedTime = timeSetter.getAlarmStartingTime(alarm)
                assertEquals(expectedAlarmTime, obtainedTime)
            }

            @Test
            fun `which is set at saturday and should ring on sunday`() {
                val alarm = createAlarm(
                    hour = 12,
                    isRepeating = true,
                    repetitionDays = arrayOf(true, true, false, false, false, false, true)
                )
                // Saturday, 7 March 2020 12:00:00
                val currentTime = 1583582400000L
                // Sunday, 8 March 2020 12:00:00
                val expectedAlarmTime = 1583668800000L


                val obtainedTime = timeSetter.getAlarmStartingTime(alarm)
                assertEquals(expectedAlarmTime, obtainedTime)
            }

            @Test
            fun `which is set at saturday and should ring at monday`() {
                val alarm = createAlarm(
                    hour = 12,
                    isRepeating = true,
                    repetitionDays = arrayOf(true, true, false, false, false, false, false)
                )
                // Saturday, 7 March 2020 12:00:00
                val currentTime = 1583582400000L
                // Monday, 9 March 2020 12:00:00
                val expectedAlarmTime = 1583755200000L


                val obtainedTime = timeSetter.getAlarmStartingTime(alarm)
                assertEquals(expectedAlarmTime, obtainedTime)
            }

            @Test
            fun `which is set at sunday and should ring at tuesday`() {
                val alarm = createAlarm(
                    hour = 12,
                    isRepeating = true,
                    repetitionDays = arrayOf(false, true, false, false, false, false, false)
                )
                // Sunday, 8 March 2020 20:00:00
                val currentTime = 1583697600000L
                // Tuesday, 10 March 2020 12:00:00
                val expectedAlarmTime = 1583841600000L


                val obtainedTime = timeSetter.getAlarmStartingTime(alarm)
                assertEquals(expectedAlarmTime, obtainedTime)
            }

            @Test
            fun `which is set for all weekdays at saturday`() {
                val alarm = createAlarm(
                    hour = 12,
                    isRepeating = true,
                    repetitionDays = arrayOf(true, true, true, true, true, false, false)
                )
                // Saturday, 7 March 2020 12:00:00
                val currentTime = 1583582400000L
                // Monday, 9 March 2020 12:00:00
                val expectedAlarmTime = 1583755200000L


                val obtainedTime = timeSetter.getAlarmStartingTime(alarm)
                assertEquals(expectedAlarmTime, obtainedTime)
            }

            @Test
            fun `which is set for all weekdays as sunday`() {
                val alarm = createAlarm(
                    hour = 12,
                    isRepeating = true,
                    repetitionDays = arrayOf(true, true, true, true, true, false, false)
                )
                // Sunday, 8 March 2020 20:00:00
                val currentTime = 1583697600000L
                // Monday, 9 March 2020 12:00:00
                val expectedAlarmTime = 1583755200000L


                val obtainedTime = timeSetter.getAlarmStartingTime(alarm)
                assertEquals(expectedAlarmTime, obtainedTime)
            }

            @Test
            fun `which is set for all weekdays as monday before alarm`() {
                val alarm = createAlarm(
                    hour = 12,
                    isRepeating = true,
                    repetitionDays = arrayOf(true, true, true, true, true, false, false)
                )
                // Monday, 9 March 2020 11:00:00
                val currentTime = 1583751600000L
                // Monday, 9 March 2020 12:00:00
                val expectedAlarmTime = 1583755200000L


                val obtainedTime = timeSetter.getAlarmStartingTime(alarm)
                assertEquals(expectedAlarmTime, obtainedTime)
            }

            @Test
            fun `which is set for all weekdays as monday after alarm`() {
                val alarm = createAlarm(
                    hour = 12,
                    isRepeating = true,
                    repetitionDays = arrayOf(true, true, true, true, true, false, false)
                )
                // Monday, 9 March 2020 12:01:00
                val currentTime = 1583755260000L
                // Tuesday, 10 March 2020 12:00:00
                val expectedAlarmTime = 1583841600000L


                val obtainedTime = timeSetter.getAlarmStartingTime(alarm)
                assertEquals(expectedAlarmTime, obtainedTime)
            }

            @Test
            fun `which is set for all weekdays as wednesday`() {
                val alarm = createAlarm(
                    hour = 12,
                    isRepeating = true,
                    repetitionDays = arrayOf(true, true, true, true, true, false, false)
                )
                // Thursday, 12 March 2020 13:00:00
                val currentTime = 1584018000000L
                // Friday, 13 March 2020 12:00:00
                val expectedAlarmTime = 1584100800000L


                val obtainedTime = timeSetter.getAlarmStartingTime(alarm)
                assertEquals(expectedAlarmTime, obtainedTime)
            }

            @Test
            fun `which is set for all weekend as sunday after alarm time`() {
                val alarm = createAlarm(
                    hour = 12,
                    isRepeating = true,
                    repetitionDays = arrayOf(false, false, false, false, false, true, true)
                )
                // Sunday, 8 March 2020 13:00:00
                val currentTime = 1583672400000L
                // Sunday, 15 March 2020 12:00:00
                val expectedAlarmTime = 1584273600000L


                val obtainedTime = timeSetter.getAlarmStartingTime(alarm)
                assertEquals(expectedAlarmTime, obtainedTime)
            }

            @Test
            fun `which is set for all weekend as sunday before alarm time`() {
                val alarm = createAlarm(
                    hour = 12,
                    isRepeating = true,
                    repetitionDays = arrayOf(false, false, false, false, false, true, true)
                )
                // Sunday, 8 March 2020 11:00:00
                val currentTime = 1583665200000L
                // Sunday, 8 March 2020 12:00:00
                val expectedAlarmTime = 1583668800000L


                val obtainedTime = timeSetter.getAlarmStartingTime(alarm)
                assertEquals(expectedAlarmTime, obtainedTime)
            }

            @Test
            fun `which is set for all weekend as saturday before alarm time`() {
                val alarm = createAlarm(
                    hour = 12,
                    isRepeating = true,
                    repetitionDays = arrayOf(false, false, false, false, false, true, true)
                )
                // Saturday, 7 March 2020 11:00:00
                val currentTime = 1583578800000L
                // Saturday, 7 March 2020 12:00:00
                val expectedAlarmTime = 1583582400000L


                val obtainedTime = timeSetter.getAlarmStartingTime(alarm)
                assertEquals(expectedAlarmTime, obtainedTime)
            }

            @Test
            fun `which is set for all weekend as saturday after alarm time`() {
                val alarm = createAlarm(
                    hour = 12,
                    isRepeating = true,
                    repetitionDays = arrayOf(false, false, false, false, false, true, true)
                )
                // Saturday, 7 March 2020 13:00:00
                val currentTime = 1583586000000L
                // Sunday, 8 March 2020 12:00:00
                val expectedAlarmTime = 1583668800000L


                val obtainedTime = timeSetter.getAlarmStartingTime(alarm)
                assertEquals(expectedAlarmTime, obtainedTime)
            }

            @Test
            fun `which is set for all weekend as monday`() {
                val alarm = createAlarm(
                    hour = 12,
                    isRepeating = true,
                    repetitionDays = arrayOf(false, false, false, false, false, true, true)
                )
                // Monday, 9 March 2020 12:00:00
                val currentTime = 1583755200000L
                // Saturday, 14 March 2020 12:00:00
                val expectedAlarmTime = 1584187200000L


                val obtainedTime = timeSetter.getAlarmStartingTime(alarm)
                assertEquals(expectedAlarmTime, obtainedTime)
            }

            @Test
            fun `which is set for all weekend as friday`() {
                val alarm = createAlarm(
                    hour = 12,
                    isRepeating = true,
                    repetitionDays = arrayOf(false, false, false, false, false, true, true)
                )
                // Friday, 13 March 2020 12:00:00
                val currentTime = 1584100800000L
                // Saturday, 14 March 2020 12:00:00
                val expectedAlarmTime = 1584187200000L


                val obtainedTime = timeSetter.getAlarmStartingTime(alarm)
                assertEquals(expectedAlarmTime, obtainedTime)
            }


        }

        @Nested
        @DisplayName("non repeating alarm")
        inner class NonRepeatingAlarm

    }

}


class TimeSetterTest {

    private lateinit var timeSetter: TimeSetter
    //todo change snoozing time, currently 5

    @Nested
    @DisplayName("Snooze for ")
    inner class Snoozes {


        @Test
        fun `first time should schedule`() {
//            20 June 2019 02:00:00 - 1560996000000L
            val calendar = DateTime.now().copyDayOfMonth(
                dayOfMonth = 20,
                month = Month.June,
                year = Year(2019),
                hours = 2,
                minutes = 0,
                seconds = 0,
                milliseconds = 0
            )

            timeSetter = TimeSetter(calendar)

//            Thursday, 20 June 2019 02:05:00 - 1560996300000L
            val expected = DateTime.now().copyDayOfMonth(
                dayOfMonth = 20,
                month = Month.June,
                year = Year(2019),
                hours = 2,
                minutes = 5,
                seconds = 0,
                milliseconds = 0
            ).milliseconds.toLong()
            val alarm = MockData.createAlarm(hour = 2, minute = 0)
            assertEquals(expected, timeSetter.getAlarmSnoozeTime(alarm, 5))
        }


        @Test
        fun `second time should schedule`() {
//            Thursday, 20 June 2019 02:05:00 - 1560996300000L
            val calendar = DateTime.now().copyDayOfMonth(
                dayOfMonth = 20,
                month = Month.June,
                year = Year(2019),
                hours = 2,
                minutes = 5,
                seconds = 0,
                milliseconds = 0
            )

            timeSetter = TimeSetter(calendar)

//            Thursday, 20 June 2019 02:10:00 - 1560996600000L
            val expected = DateTime.now().copyDayOfMonth(
                dayOfMonth = 20,
                month = Month.June,
                year = Year(2019),
                hours = 2,
                minutes = 10,
                seconds = 0,
                milliseconds = 0
            ).milliseconds.toLong()
            val alarm = MockData.createAlarm(hour = 2, minute = 0)
            assertEquals(expected, timeSetter.getAlarmSnoozeTime(alarm, 5))
        }

        @Test
        fun `third time should schedule`() {
            //06/20/2019 @ 2:10am - 1560996600000L
            val calendar = DateTime.now().copyDayOfMonth(
                dayOfMonth = 20,
                month = Month.June,
                year = Year(2019),
                hours = 2,
                minutes = 10,
                seconds = 0,
                milliseconds = 0
            )

            timeSetter = TimeSetter(calendar)

//            Thursday, 20 June 2019 02:15:00 - 1560996900000L
            val expected = DateTime.now().copyDayOfMonth(
                dayOfMonth = 20,
                month = Month.June,
                year = Year(2019),
                hours = 2,
                minutes = 15,
                seconds = 0,
                milliseconds = 0
            ).milliseconds.toLong()
            val alarm = MockData.createAlarm(hour = 2, minute = 0)
            assertEquals(expected, timeSetter.getAlarmSnoozeTime(alarm, 5))
        }

        @Test
        fun `forth time should not schedule any more`() {
            //06/20/2019 @ 2:15am - 1560996900000L
            val calendar = DateTime.now().copyDayOfMonth(
                dayOfMonth = 20,
                month = Month.June,
                year = Year(2019),
                hours = 2,
                minutes = 15,
                seconds = 0,
                milliseconds = 0
            )

            timeSetter = TimeSetter(calendar)

            val expected = -1L
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
//                01/09/2020 @ 8:00am - 1578556800005L
                val calendar = DateTime.now().copyDayOfMonth(
                    dayOfMonth = 9,
                    month = Month.January,
                    year = Year(2020),
                    hours = 8,
                    minutes = 0,
                    seconds = 0,
                    milliseconds = 0
                )

                timeSetter = TimeSetter(calendar)

//                01/10/2020 @ 8:00am - 1578643200000L
                val expected = DateTime.now().copyDayOfMonth(
                    dayOfMonth = 10,
                    month = Month.January,
                    year = Year(2020),
                    hours = 8,
                    minutes = 0,
                    seconds = 0,
                    milliseconds = 0
                )
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
//                01/09/2020 @ 8:00am - 1578556800005L
                val calendar = DateTime.now().copyDayOfMonth(
                    dayOfMonth = 9,
                    month = Month.January,
                    year = Year(2020),
                    hours = 8,
                    minutes = 0,
                    seconds = 0,
                    milliseconds = 0
                )

                timeSetter = TimeSetter(calendar)

//                01/10/2020 @ 8:00am - 1578643200000L
                val expected = DateTime.now().copyDayOfMonth(
                    dayOfMonth = 10,
                    month = Month.January,
                    year = Year(2020),
                    hours = 8,
                    minutes = 0,
                    seconds = 0,
                    milliseconds = 0
                )
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
//                01/09/2020 @ 5:00pm - 1578589200000L
                val calendar = DateTime.now().copyDayOfMonth(
                    dayOfMonth = 9,
                    month = Month.January,
                    year = Year(2020),
                    hours = 17,
                    minutes = 0,
                    seconds = 0,
                    milliseconds = 0
                )

                timeSetter = TimeSetter(calendar)

//                01/10/2020 @ 8:00am - 1578643200000L
                val expected = DateTime.now().copyDayOfMonth(
                    dayOfMonth = 10,
                    month = Month.January,
                    year = Year(2020),
                    hours = 8,
                    minutes = 0,
                    seconds = 0,
                    milliseconds = 0
                )
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
//                01/09/2020 @ 5:00pm - 1578589200000L
                val calendar = DateTime.now().copyDayOfMonth(
                    dayOfMonth = 9,
                    month = Month.January,
                    year = Year(2020),
                    hours = 17,
                    minutes = 0,
                    seconds = 0,
                    milliseconds = 0
                )

                timeSetter = TimeSetter(calendar)

//                01/10/2020 @ 8:00am
                val expected = DateTime.now().copyDayOfMonth(
                    dayOfMonth = 10,
                    month = Month.January,
                    year = Year(2020),
                    hours = 8,
                    minutes = 0,
                    seconds = 0,
                    milliseconds = 0
                )
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
//                01/09/2020 @ 11:35pm - 1578612900000L
                val calendar = DateTime.now().copyDayOfMonth(
                    dayOfMonth = 9,
                    month = Month.January,
                    year = Year(2020),
                    hours = 23,
                    minutes = 35,
                    seconds = 0,
                    milliseconds = 0
                )

                timeSetter = TimeSetter(calendar)

//                01/10/2020 @ 8:00am - 1578643200000L
                val expected = DateTime.now().copyDayOfMonth(
                    dayOfMonth = 10,
                    month = Month.January,
                    year = Year(2020),
                    hours = 8,
                    minutes = 0,
                    seconds = 0,
                    milliseconds = 0
                )
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
//                01/09/2020 @ 11:35pm - 1578612900000L
                val calendar = DateTime.now().copyDayOfMonth(
                    dayOfMonth = 9,
                    month = Month.January,
                    year = Year(2020),
                    hours = 23,
                    minutes = 35,
                    seconds = 0,
                    milliseconds = 0
                )

                timeSetter = TimeSetter(calendar)

//                01/10/2020 @ 8:00am - 1578643200000L
                val expected = DateTime.now().copyDayOfMonth(
                    dayOfMonth = 10,
                    month = Month.January,
                    year = Year(2020),
                    hours = 8,
                    minutes = 0,
                    seconds = 0,
                    milliseconds = 0
                )
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
//                01/10/2020 @ 12:35am - 1578616500000L
                val calendar = DateTime.now().copyDayOfMonth(
                    dayOfMonth = 9,
                    month = Month.January,
                    year = Year(2020),
                    hours = 12,
                    minutes = 35,
                    seconds = 0,
                    milliseconds = 0
                )

                timeSetter = TimeSetter(calendar)

//                01/10/2020 @ 8:00am - 1578643200000L
                val expected = DateTime.now().copyDayOfMonth(
                    dayOfMonth = 10,
                    month = Month.January,
                    year = Year(2020),
                    hours = 8,
                    minutes = 0,
                    seconds = 0,
                    milliseconds = 0
                )
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
//                01/10/2020 @ 12:35am - 1578616500000L
                val calendar = DateTime.now().copyDayOfMonth(
                    dayOfMonth = 9,
                    month = Month.January,
                    year = Year(2020),
                    hours = 12,
                    minutes = 35,
                    seconds = 0,
                    milliseconds = 0
                )

                timeSetter = TimeSetter(calendar)

//                01/10/2020 @ 8:00am - 1578643200000L
                val expected = DateTime.now().copyDayOfMonth(
                    dayOfMonth = 10,
                    month = Month.January,
                    year = Year(2020),
                    hours = 8,
                    minutes = 0,
                    seconds = 0,
                    milliseconds = 0
                )
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
