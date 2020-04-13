package com.helpfulapps.domain.helpers

import com.helpfulapps.domain.models.alarm.Alarm
import com.soywiz.klock.DateTime
import com.soywiz.klock.days
import com.soywiz.klock.minutes
import com.soywiz.klock.weeks
import com.soywiz.klock.Time as time


interface ITimeSetter {
    fun getAlarmStartingCalendar(alarm: Alarm): DateTime
    fun getAlarmStartingTime(alarm: Alarm): Long
    fun getAlarmSnoozeTime(alarm: Alarm, snoozingTime: Int): Long
    fun setHourAndMinute(alarm: Alarm): DateTime
}


class TimeSetter(
    private val currentTime: DateTime = DateTime.now()
) : ITimeSetter {
    private val TAG = this.javaClass.simpleName

    override fun getAlarmStartingCalendar(alarm: Alarm): DateTime = setStartingPoint(alarm)

    override fun getAlarmStartingTime(alarm: Alarm): Long {
        val calendar = setStartingPoint(alarm)

        return calendar.milliseconds.toLong()
    }

    override fun getAlarmSnoozeTime(alarm: Alarm, snoozingTime: Int): Long {
        val baseAlarmStarting = setHourAndMinute(alarm)

        var snoozeTime: DateTime? = null

        for (timesSnoozed in 1..3) {
            val nextTime = baseAlarmStarting + (timesSnoozed * snoozingTime).minutes
            if (nextTime > currentTime) {
                snoozeTime = nextTime
                break
            }
        }
        return snoozeTime?.milliseconds?.toLong() ?: -1
    }

    private fun setStartingPoint(alarm: Alarm): DateTime {
        return if (alarm.isRepeating) {
            analyzeRepeatingAlarms(alarm)
        } else {
            analyzeSingleAlarm(alarm)
        }
    }

    private fun analyzeRepeatingAlarms(alarm: Alarm): DateTime {
        var alarmStartTime = setHourAndMinute(alarm)

        // my mapping is always 1 less than in DayOfWeek, so I am substracting 1,
        // but sunday is only exception because in my array its index is 6, but in DayOfWeek is 0
        val currentDayOfWeek = alarmStartTime.dayOfWeekInt.let {
            if (it - 1 < 0) 6 else it - 1
        }

        //index is set starting form the currentDayOfWeek,
        // so we can quicker find when the next alarm is starting
        var index: Int
        val alarmTime = time(hour = alarm.hour, minute = alarm.minute)

        for (x in alarm.repetitionDays.indices) {
            index = (x + currentDayOfWeek) % alarm.repetitionDays.size
            if (alarm.repetitionDays[index]) {
                when {
                    startsToday(
                        alarmTime,
                        index,
                        currentDayOfWeek,
                        currentTime.time
                    ) -> return alarmStartTime
                    currentDayOfWeek != index -> {
                        val daysToAdd = (index - currentDayOfWeek).let {
                            if (it < 0) it + 7 else it
                        }
                        alarmStartTime += daysToAdd.days
                        return alarmStartTime
                    }
                }
            }
        }
        alarmStartTime += 1.weeks
        return alarmStartTime
    }

    private fun startsToday(
        alarmTime: time,
        index: Int,
        currentDayOfWeek: Int,
        currentTime: time
    ) =
        currentDayOfWeek == index && ((currentTime.hour == alarmTime.hour && currentTime.minute < alarmTime.minute) || (currentTime.hour < alarmTime.hour))

    private fun analyzeSingleAlarm(alarm: Alarm): DateTime {
        val dateTime = setHourAndMinute(alarm)

        return when {
            dateTime <= DateTime.now() -> dateTime + 1.days
            else -> dateTime
        }
    }

    override fun setHourAndMinute(alarm: Alarm): DateTime {
        return currentTime.copyDayOfMonth(
            hours = alarm.hour,
            minutes = alarm.minute,
            seconds = 0,
            milliseconds = 0
        )
    }

}