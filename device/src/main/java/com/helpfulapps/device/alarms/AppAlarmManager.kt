package com.helpfulapps.device.alarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import com.helpfulapps.device.alarms.helpers.IntentCreator
import com.helpfulapps.device.alarms.helpers.matchesVersionsFrom
import com.helpfulapps.domain.extensions.completableOf
import com.helpfulapps.domain.helpers.Settings
import com.helpfulapps.domain.helpers.TimeSetter
import com.helpfulapps.domain.repository.AlarmClockManager
import io.reactivex.Completable
import com.helpfulapps.domain.models.alarm.Alarm as DomainAlarm


class AlarmClockManagerImpl(
    private val context: Context,
    private val manager: AlarmManager,
    private val settings: Settings
) : AlarmClockManager {

    private val TAG = AlarmClockManagerImpl::class.java.simpleName

    override fun setAlarm(domainAlarm: DomainAlarm): Completable {
        return completableOf {

            val alarmSetupData = getAlarmSetupData(domainAlarm)
            setAlarmInAlarmManager(alarmSetupData)
        }
    }

    override fun stopAlarm(alarmId: Long): Completable {
        return completableOf {
            val alarmIntent = IntentCreator.getAlarmIntent(context, alarmId.toInt())

            manager.cancel(alarmIntent)
        }
    }

    override fun snoozeAlarm(alarm: DomainAlarm): Completable {
        return completableOf {
            val timeSetter = TimeSetter()
            val snoozeTime =
                timeSetter.getAlarmSnoozeTime(alarm, settings.snoozeAlarmTime, settings.noSnoozes)
            if (snoozeTime != -1L) {
                val alarmIntent = IntentCreator.getAlarmIntent(context, alarm.id.toInt())
                val alarmInfoIntent =
                    IntentCreator.createPendingIntentForAlarmIconPress(context, alarm.id.toInt())
                val alarmSetupData = AlarmSetupData(snoozeTime, alarmInfoIntent, alarmIntent)
                setAlarmInAlarmManager(alarmSetupData)
            } else if (alarm.isRepeating) {
                val alarmSetupData = getAlarmSetupData(alarm)
                setAlarmInAlarmManager(alarmSetupData)
            }
        }
    }

    private fun getAlarmSetupData(domainAlarm: DomainAlarm): AlarmSetupData {
        val secondAlarm = Alarm(domainAlarm)
        val timeSetter = TimeSetter()
        val alarmStart = timeSetter.getAlarmStartingTime(domainAlarm)
//            val alarmStart = System.currentTimeMillis() + 10 * 1000

        val alarmIntent = IntentCreator.getAlarmIntent(context, secondAlarm.id)
        val alarmInfoIntent =
            IntentCreator.createPendingIntentForAlarmIconPress(context, secondAlarm.id)

        return AlarmSetupData(alarmStart, alarmInfoIntent, alarmIntent)
    }

    private fun setAlarmInAlarmManager(alarmSetupData: AlarmSetupData) {
        with(alarmSetupData) {
            when {
                matchesVersionsFrom(Build.VERSION_CODES.LOLLIPOP) -> {
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

    data class AlarmSetupData(
        val alarmStart: Long,
        val alarmInfoIntent: PendingIntent,
        val alarmIntent: PendingIntent
    )

}