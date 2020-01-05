package com.helpfulapps.device.alarms.helpers

import com.helpfulapps.device.alarms.Alarm
import com.helpfulapps.domain.helpers.TimeSetter
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*

class TimeSetterTest {

    private lateinit var timeSetter: TimeSetter
    // this is Mon 24.06.2019 12:20
    private val timestamp: Long = 1561378800000


    @Before
    fun setUp() {
        timeSetter =
            TimeSetter(GregorianCalendar.getInstance().apply {
                timeInMillis = timestamp
                timeZone = TimeZone.getTimeZone("GMT+0:00")
            },
                currentTime = { timestamp })
    }

    @Test
    fun obtainCorrectStartingTimeOfAlarm() {

        val alarm = createAlarm(isRepeating = false)
        val expect = 1561451400000L


        val result = timeSetter.getAlarmStartingTime(alarm)

        assertEquals(expect, result)

    }

    @Test
    fun obtainCorrectStartingTimeOfAlarmTheSameDay() {

        val alarm = createAlarm(
            repetitionDays = arrayOf(true, false, false, false, false, false, false),
            hour = 14
        )
        val expect = 1561386600000L


        val result = timeSetter.getAlarmStartingTime(alarm)

        assertEquals(expect, result)
    }

    @Test
    fun obtainCorrectStartingTimeOfAlarmTheNextDay() {

        val alarm = createAlarm(
            repetitionDays = arrayOf(false, true, false, false, false, false, false),
            hour = 6
        )
        val expect = 1561444200000L


        val result = timeSetter.getAlarmStartingTime(alarm)

        assertEquals(expect, result)
    }

    @Test
    fun obtainCorrectStartingTimeOfAlarmOnSunday() {

        val alarm = createAlarm(
            repetitionDays = arrayOf(false, false, false, false, false, false, true)
        )
        val expect = 1561883400000L


        val result = timeSetter.getAlarmStartingTime(alarm)

        assertEquals(expect, result)
    }


    private fun createAlarm(
        id: Int = 0,
        isRepeating: Boolean = true,
        hour: Int = 8,
        minute: Int = 30,
        repetitionDays: Array<Boolean> = Array(7) { false }
    ) = Alarm(
        id, isRepeating, hour, minute, repetitionDays
    )

}