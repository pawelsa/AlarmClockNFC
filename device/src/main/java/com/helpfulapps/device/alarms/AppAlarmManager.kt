package com.helpfulapps.device.alarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import com.helpfulapps.base.extensions.rx.completableOf
import com.helpfulapps.device.alarms.helpers.IntentCreator
import com.helpfulapps.device.alarms.helpers.matchesVersionsFrom
import com.helpfulapps.domain.helpers.TimeSetter
import com.helpfulapps.domain.repository.AlarmClockManager
import io.reactivex.Completable
import com.helpfulapps.domain.models.alarm.Alarm as DomainAlarm


class AlarmClockManagerImpl(private val context: Context, private val manager: AlarmManager) :
    AlarmClockManager {

    private val TAG = AlarmClockManagerImpl::class.java.simpleName

    override fun setAlarm(domainAlarm: DomainAlarm): Completable {
        return completableOf {

            val alarm = Alarm(domainAlarm)
            val timeSetter = TimeSetter()
            val alarmStart = timeSetter.getAlarmStartingPoint(domainAlarm)
//            val alarmStart = System.currentTimeMillis() + 10 * 1000

            val alarmIntent = IntentCreator.getAlarmIntent(context, alarm.id)
            val alarmInfoIntent =
                IntentCreator.createPendingIntentForAlarmIconPress(context, alarm.id)
//            val alarmWakeup = IntentCreator.getAlarmWakeupIntent(context, alarm.id)

            setAlarmInAlarmManager(alarmStart, alarmInfoIntent, alarmIntent/*, alarmWakeup*/)
        }
    }

    override fun stopAlarm(alarmId: Long): Completable {
        return completableOf {
            val alarmIntent = IntentCreator.getAlarmIntent(context, alarmId.toInt())

            manager.cancel(alarmIntent)
        }
    }

    private fun setAlarmInAlarmManager(
        alarmStart: Long,
        alarmInfoIntent: PendingIntent?,
        alarmIntent: PendingIntent?
//        alarmWakeup: PendingIntent?
    ) {
        when {
            matchesVersionsFrom(Build.VERSION_CODES.LOLLIPOP) -> {
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    manager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        alarmStart - 3 * 60 * 1000,
                        alarmWakeup
                    )
                }*/
                manager.setAlarmClock(
                    AlarmManager.AlarmClockInfo(
                        alarmStart,
                        alarmInfoIntent
                    ), alarmIntent
                )
            }
            matchesVersionsFrom(Build.VERSION_CODES.M) -> {
                manager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    alarmStart,
                    alarmIntent
                )
            }
            else -> manager.set(AlarmManager.RTC_WAKEUP, alarmStart, alarmIntent)
        }
    }

}