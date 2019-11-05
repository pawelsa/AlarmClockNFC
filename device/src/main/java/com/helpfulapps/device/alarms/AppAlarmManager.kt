package com.helpfulapps.device.alarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import com.helpfulapps.base.extensions.rx.completableOf
import com.helpfulapps.domain.repository.AlarmClockManager
import io.reactivex.Completable
import java.util.*
import com.helpfulapps.domain.models.alarm.Alarm as DomainAlarm


class AlarmClockManagerImpl(private val context: Context, private val manager: AlarmManager) :
    AlarmClockManager {

    private val TAG = AlarmClockManagerImpl::class.java.simpleName

    override fun setAlarm(domainAlarm: DomainAlarm): Completable {
        return completableOf {

            val alarm = Alarm(domainAlarm)
            val alarmStart = getAlarmStartingPoint(alarm)
//            val alarmStart = System.currentTimeMillis() + 10 * 1000

            val alarmIntent = getAlarmIntent(alarm.id)
            val alarmInfoIntent = createPendingIntentForAlarmIconPress(alarm.id)

            setAlarmInAlarmManager(alarmStart, alarmInfoIntent, alarmIntent)

        }
    }

    private fun getAlarmIntent(alarmId: Int): PendingIntent {
        return Intent().let {
            it.component = ComponentName(
                BASE_PACKAGE,
                PACKAGE_ALARM_SERVICE
            )
            PendingIntent.getBroadcast(
                context,
                alarmId,
                it,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }

    private fun createPendingIntentForAlarmIconPress(alarmId: Int): PendingIntent {
        return Intent().let {
            it.component = ComponentName(
                BASE_PACKAGE,
                PACKAGE_MAIN_ACTIVITY
            )
            PendingIntent.getBroadcast(
                context,
                alarmId,
                it,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }

    private fun setAlarmInAlarmManager(
        alarmStart: Long,
        alarmInfoIntent: PendingIntent?,
        alarmIntent: PendingIntent?
    ) {
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

    override fun stopAlarm(alarmId: Long): Completable {
        return completableOf {
            val alarmIntent = getAlarmIntent(alarmId.toInt())

            manager.cancel(alarmIntent)
        }
    }

    fun getAlarmStartingPoint(alarm: Alarm): Long {
        var calendar = GregorianCalendar.getInstance()

        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        calendar = setupCalendar(calendar, alarm)

        if (alarm.isRepeating) {

            // my mapping is always 2 less than in Calendar, so I am substracting 2,
            // but sunday is only exception because in my array its index is 6, but in Calendar is 1
            val currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK).let {
                if (it - 2 < 0) it + 5 else it - 2
            }

            var index: Int
            for (x in alarm.repetitionDays.indices) {
                index = (x + currentDayOfWeek) % alarm.repetitionDays.size
                if (alarm.repetitionDays[index] && (currentDayOfWeek != index || currentHour > alarm.hour || currentMinute >= alarm.minute)) {
                    calendar.set(Calendar.DAY_OF_WEEK, index)
                }
            }
        } else {
            if (calendar.timeInMillis <= System.currentTimeMillis()) {
                calendar.add(Calendar.HOUR, HOURS_IN_DAY)
            }
        }
        return calendar.timeInMillis
    }

    private fun setupCalendar(calendar: Calendar, alarm: Alarm): Calendar {
        with(calendar) {
            set(Calendar.HOUR_OF_DAY, alarm.hour)
            set(Calendar.MINUTE, alarm.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar
    }

    companion object {
        const val BASE_PACKAGE = "com.helpfulapps.alarmclock"
        const val PACKAGE_MAIN_ACTIVITY = "$BASE_PACKAGE.views.main_activity.MainActivity"
        const val PACKAGE_ALARM_SERVICE = "$BASE_PACKAGE.receivers.AlarmService"
        const val HOURS_IN_DAY = 24
    }

}