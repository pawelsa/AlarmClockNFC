package com.helpfulapps.device.alarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.os.Build
import java.util.*

interface AppAlarmManager {
    fun setAlarm(alarm: Alarm, alarmIntent: PendingIntent, alarmInfoIntent: PendingIntent)
    fun stopAlarm(alarmIntent: PendingIntent)
}

class AppAlarmManagerImpl(private val manager: AlarmManager) : AppAlarmManager {

    override fun setAlarm(
        alarm: Alarm,
        alarmIntent: PendingIntent,
        alarmInfoIntent: PendingIntent
    ) {

        val alarmStart = getAlarmStartingPoint(alarm)

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> manager.setAlarmClock(
                AlarmManager.AlarmClockInfo(
                    alarmStart,
                    alarmInfoIntent
                ), alarmIntent
            )
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> manager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                alarmStart,
                alarmIntent
            )
            else -> manager.set(AlarmManager.RTC_WAKEUP, alarmStart, alarmIntent)
        }
    }

    override fun stopAlarm(alarmIntent: PendingIntent) {
        manager.cancel(alarmIntent)
    }

    private fun getAlarmStartingPoint(alarm: Alarm): Long {
        val calendar = GregorianCalendar.getInstance()
        with(calendar) {
            set(Calendar.HOUR_OF_DAY, alarm.hour)
            set(Calendar.MINUTE, alarm.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            if (this.timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.HOUR, 24)
            }
        }
        return calendar.timeInMillis
    }

}