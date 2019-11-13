package com.helpfulapps.device.alarms.helpers

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent

object IntentCreator {


    fun getAlarmIntent(context: Context, alarmId: Int): PendingIntent {
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

    fun createPendingIntentForAlarmIconPress(context: Context, alarmId: Int): PendingIntent {
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

    private const val BASE_PACKAGE = "com.helpfulapps.alarmclock"
    private const val PACKAGE_MAIN_ACTIVITY = "$BASE_PACKAGE.views.main_activity.MainActivity"
    private const val PACKAGE_ALARM_SERVICE = "$BASE_PACKAGE.receivers.AlarmReceiver"

}