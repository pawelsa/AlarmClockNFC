package com.helpfulapps.domain.helpers

import com.helpfulapps.domain.models.alarm.Alarm
import java.util.*

class TimeSetter(
    private var calendar: Calendar = GregorianCalendar.getInstance(),
    private val currentTime: () -> Long = { System.currentTimeMillis() }
) {
    private val TAG = this.javaClass.simpleName

    fun getAlarmStartingCalendar(alarm: Alarm): Calendar = setStartingPoint(alarm)

    fun getAlarmStartingTime(alarm: Alarm): Long {
        val calendar = setStartingPoint(alarm)

        return calendar.timeInMillis
    }

    fun getAlarmSnoozeTime(alarm: Alarm, snoozingTime: Int, noSnoozes: Int): Long {
        val baseAlarmStarting =
            setHourAndMinute(alarm, GregorianCalendar.getInstance()).timeInMillis
        val currentT = currentTime()
        var snoozeTime = -1L

        for (timesSnoozed in 1..noSnoozes) {
            val nextTime = baseAlarmStarting + (timesSnoozed * snoozingTime * MINUTE_MILLIS)
            if (nextTime > currentT) {
                snoozeTime = nextTime
                break
            }
        }
        return snoozeTime
    }

    private fun setStartingPoint(alarm: Alarm): Calendar {
        return if (alarm.isRepeating) {
            analyzeRepeatingAlarms(alarm)
        } else {
            analyzeSingleAlarm(alarm)
        }
    }

    private fun analyzeRepeatingAlarms(alarm: Alarm): Calendar {


        val currentTime = calendar.get(Calendar.HOUR_OF_DAY) to calendar.get(Calendar.MINUTE)

        calendar = setHourAndMinute(alarm, calendar)

        // my mapping is always 2 less than in Calendar, so I am substracting 2,
        // but sunday is only exception because in my array its index is 6, but in Calendar is 1
        val currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK).let {
            if (it - 2 < 0) it + 5 else it - 2
        }

        //index is set starting form the currentDayOfWeek,
        // so we can quicker find when the next alarm is starting
        var index: Int
        val alarmTime = alarm.hour to alarm.minute

        for (x in alarm.repetitionDays.indices) {
            index = (x + currentDayOfWeek) % alarm.repetitionDays.size
            if (alarm.repetitionDays[index]) {
                when {
                    startsToday(alarmTime, index, currentDayOfWeek, currentTime) -> return calendar
                    currentDayOfWeek != index -> {
                        val daysToAdd = (index - currentDayOfWeek).let {
                            if (it < 0) it + 7 else it
                        }
                        calendar.add(Calendar.HOUR_OF_DAY, daysToAdd * HOURS_IN_DAY)
                        return calendar
                    }
                }
            }
        }
        calendar.add(
            Calendar.HOUR_OF_DAY,
            HOURS_IN_WEEK
        )
        return calendar
    }

    private fun startsToday(
        alarmTime: Time,
        index: Int,
        currentDayOfWeek: Int,
        currentTime: Time
    ) =
        currentDayOfWeek == index && ((currentTime.first == alarmTime.first && currentTime.second < alarmTime.second) || (currentTime.first < alarmTime.first))

    private fun analyzeSingleAlarm(alarm: Alarm): Calendar {
        val calendar = setHourAndMinute(alarm, calendar)

        if (calendar.timeInMillis <= currentTime()) {
            calendar.add(
                Calendar.HOUR,
                HOURS_IN_DAY
            )
        }
        return calendar
    }

    fun setHourAndMinute(
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
        private const val HOURS_IN_DAY_MILLIS = HOURS_IN_DAY * 3600 * 1000
        private const val MINUTE_MILLIS = 60 * 1000
        private const val FIVE_MINUTES_MILLIS = 5 * MINUTE_MILLIS
        const val HOURS_IN_WEEK = HOURS_IN_DAY * 7
    }

}