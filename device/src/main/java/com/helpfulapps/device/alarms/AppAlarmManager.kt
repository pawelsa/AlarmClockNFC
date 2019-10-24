package com.helpfulapps.device.alarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.helpfulapps.base.extensions.rx.completableOf
import com.helpfulapps.domain.repository.AppAlarmManager
import io.reactivex.Completable
import java.util.*
import com.helpfulapps.domain.models.alarm.Alarm as DomainAlarm


class AppAlarmManagerImpl(private val context: Context, private val manager: AlarmManager) :
    AppAlarmManager {

    private val TAG = AppAlarmManagerImpl::class.java.simpleName

    override fun setAlarm(
        alarm: DomainAlarm,
        classIntent: Class<*>,
        classInfoIntent: Class<*>
    ): Completable {
        return completableOf {
            //            val alarmStart = getAlarmStartingPoint(alarm)
            val alarmStart = System.currentTimeMillis() + 10 * 1000

            Log.d(TAG, "SettingAlarm at : $alarmStart")
            /*val alarmInfoIntent = Intent(context, classInfoIntent).let { intent ->
                PendingIntent.getActivity(context, 0, intent, 0)
            }
            val alarmIntent = Intent(context, classIntent).let { intent ->
                PendingIntent.getActivity(context, 1, intent, 0)
            }*/

            val alarmIntent = Intent().let {
                it.component = ComponentName(
                    "com.helpfulapps.alarmclock",
                    "com.helpfulapps.alarmclock.receivers.AlarmService"
                )
                PendingIntent.getBroadcast(context, 0, it, 0)
            }
            val alarmInfoIntent = alarmIntent

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
    }

    override fun stopAlarm(classIntent: Class<*>) {

        val alarmIntent = Intent(context, classIntent).let {
            PendingIntent.getActivity(context, 0, it, 0)
        }

        manager.cancel(alarmIntent)
    }

    private fun getAlarmStartingPoint(alarm: DomainAlarm): Long {
        val calendar = GregorianCalendar.getInstance()
        with(calendar) {
            set(Calendar.HOUR_OF_DAY, alarm.hours)
            set(Calendar.MINUTE, alarm.minutes)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            if (this.timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.HOUR, 24)
            }
        }
        return calendar.timeInMillis
    }

}