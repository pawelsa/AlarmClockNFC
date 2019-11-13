package com.helpfulapps.device.alarms.helpers

import com.helpfulapps.device.alarms.Alarm
import java.util.*

class TimeSetter(
    private var calendar: Calendar = GregorianCalendar.getInstance(),
    private val currentTime: () -> Long = { System.currentTimeMillis() }
) {

    fun getAlarmStartingPoint(alarm: Alarm): Long {
        val calendar = setStartingPoint(alarm)

        return calendar.timeInMillis
    }

    private fun setStartingPoint(alarm: Alarm): Calendar {
        return if (alarm.isRepeating) {
            analyzeRepeatingAlarms(alarm)
        } else {
            analyzeSingleAlarm(alarm)
        }
    }

    private fun analyzeRepeatingAlarms(alarm: Alarm): Calendar {


        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        calendar = setHourAndMinute(alarm, calendar)

        // my mapping is always 2 less than in Calendar, so I am substracting 2,
        // but sunday is only exception because in my array its index is 6, but in Calendar is 1
        val currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK).let {
            if (it - 2 < 0) it + 5 else it - 2
        }

        //index is set starting form the currentDayOfWeek,
        // so we can quicker find when the next alarm is starting
        var index: Int
        for (x in alarm.repetitionDays.indices) {
            index = (x + currentDayOfWeek) % alarm.repetitionDays.size
            if (currentDayOfWeek == index && currentHour < alarm.hour && currentMinute <= alarm.minute) {
                return calendar
            } else if (alarm.repetitionDays[index] && currentDayOfWeek != index) {
                val daysToAdd = (index - currentDayOfWeek).let {
                    if (it < 0) it + 7 else it
                }
                calendar.add(Calendar.HOUR_OF_DAY, daysToAdd * HOURS_IN_DAY)
                return calendar
            }
        }
        calendar.add(Calendar.HOUR_OF_DAY, HOURS_IN_WEEK)
        return calendar
    }

    private fun analyzeSingleAlarm(alarm: Alarm): Calendar {
        val calendar = setHourAndMinute(alarm, calendar)

        if (calendar.timeInMillis <= currentTime()) {
            calendar.add(Calendar.HOUR, HOURS_IN_DAY)
        }
        return calendar
    }

    private fun setHourAndMinute(
        alarm: Alarm,
        calendar: Calendar
    ): Calendar {
        with(calendar) {
            set(Calendar.HOUR_OF_DAY, alarm.hour)
            set(Calendar.MINUTE, alarm.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar
    }

    companion object {
        private const val HOURS_IN_DAY = 24
        const val HOURS_IN_WEEK = HOURS_IN_DAY * 7
    }

}