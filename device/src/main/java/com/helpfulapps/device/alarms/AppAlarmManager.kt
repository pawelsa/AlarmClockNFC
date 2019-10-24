package com.helpfulapps.device.alarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import com.helpfulapps.base.extensions.rx.completableOf
import com.helpfulapps.domain.repository.AppAlarmManager
import io.reactivex.Completable
import java.util.*
import com.helpfulapps.domain.models.alarm.Alarm as DomainAlarm


class AppAlarmManagerImpl(private val context: Context, private val manager: AlarmManager) :
    AppAlarmManager {

    private val TAG = AppAlarmManagerImpl::class.java.simpleName

    override fun setAlarm(domainAlarm: DomainAlarm): Completable {
        return completableOf {

            val alarm = Alarm(domainAlarm)
            //            val alarmStart = getAlarmStartingPoint(domainAlarm)
            val alarmStart = System.currentTimeMillis() + 10 * 1000

            val alarmIntent = getAlarmIntent(alarm)
            val alarmInfoIntent = createPendingIntentForAlarmIconPress(alarm)

            setAlarmInAlarmManager(alarmStart, alarmInfoIntent, alarmIntent)

        }
    }

    private fun getAlarmIntent(alarm: Alarm): PendingIntent {
        return Intent().let {
            it.component = ComponentName(
                BASE_PACKAGE,
                PACKAGE_ALARM_SERVICE
            )
            PendingIntent.getBroadcast(
                context,
                alarm.id,
                it,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }

    private fun createPendingIntentForAlarmIconPress(alarm: Alarm): PendingIntent {
        return Intent().let {
            it.component = ComponentName(
                BASE_PACKAGE,
                PACKAGE_MAIN_ACTIVITY
            )
            PendingIntent.getBroadcast(
                context,
                alarm.id,
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

    override fun stopAlarm(domainAlarm: DomainAlarm) {

        val alarm = Alarm(domainAlarm)
        val alarmIntent = getAlarmIntent(alarm)

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

    companion object {
        const val BASE_PACKAGE = "com.helpfulapps.alarmclock"
        const val PACKAGE_MAIN_ACTIVITY = "$BASE_PACKAGE.views.main_activity.MainActivity"
        const val PACKAGE_ALARM_SERVICE = "$BASE_PACKAGE.receivers.AlarmService"
    }

}