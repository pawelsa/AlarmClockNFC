package com.helpfulapps.alarmclock.helpers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.service.TimerService
import com.helpfulapps.alarmclock.service.TimerService.Companion.TIMER_STOP
import com.helpfulapps.alarmclock.views.ringing_alarm.NfcRingingAlarmActivity
import com.helpfulapps.alarmclock.views.ringing_alarm.RingingAlarmActivity
import com.helpfulapps.domain.models.alarm.Alarm

interface NotificationBuilder {

    fun setNotificationType(notificationType: NotificationType): NotificationBuilder

    fun build(): Notification

    sealed class NotificationType {
        data class TypeAlarm(val alarm: Alarm, val usingNfc: Boolean = false) : NotificationType()
        object TypeStopwatch : NotificationType()
        data class TypeTimer(val timeLeft: Long) : NotificationType()
        object TypeTimerFinished : NotificationType()
        object TypeLocalization : NotificationType()
    }
}

class NotificationBuilderImpl(private val context: Context) : NotificationBuilder {

    private lateinit var builder: NotificationCompat.Builder

    override fun build(): Notification = builder.build()

    override fun setNotificationType(notificationType: NotificationBuilder.NotificationType) =
        apply {
            when (notificationType) {
                is NotificationBuilder.NotificationType.TypeAlarm -> setupAlarmType(notificationType)
                is NotificationBuilder.NotificationType.TypeStopwatch -> setupStopWatchType()
                is NotificationBuilder.NotificationType.TypeTimer -> setupTimerType(notificationType)
                is NotificationBuilder.NotificationType.TypeTimerFinished -> setupTimerFinished()
                is NotificationBuilder.NotificationType.TypeLocalization -> setupLocalizationType()
            }
        }


    private fun setupAlarmType(notificationType: NotificationBuilder.NotificationType) {
        notificationType as NotificationBuilder.NotificationType.TypeAlarm

        val alarm = notificationType.alarm

        val ringingActivity =
            if (notificationType.usingNfc) NfcRingingAlarmActivity::class.java else RingingAlarmActivity::class.java

        val fullScreenIntent = Intent(context, ringingActivity).also {
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            it.putExtra(KEY_ALARM_EXIT, true)
            it.putExtra(KEY_ALARM_ID, alarm.id.toInt())
        }
        val fullScreenPendingIntent = PendingIntent.getActivity(
            context, 0,
            fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        builder =
            NotificationCompat.Builder(context, CHANNEL_ALARM_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(
                    context.getString(
                        R.string.notification_alarm_text,
                        timeToString(alarm.hour, alarm.minute)
                    )
                )
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setFullScreenIntent(fullScreenPendingIntent, true)
                .setVibrate(LongArray(3) { 500L })


        buildNotificationChannel(notificationType)

        builder.setChannelId(CHANNEL_ALARM_ID)
    }

    private fun setupLocalizationType() {
        builder =
            NotificationCompat.Builder(context, CHANNEL_LOCALIZATION_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(
                    context.getString(
                        R.string.notification_location_text
                    )
                )
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setAutoCancel(false)

        buildNotificationChannel(NotificationBuilder.NotificationType.TypeLocalization)

        builder.setChannelId(CHANNEL_LOCALIZATION_ID)

    }

    private fun setupStopWatchType() {
        // set builder
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun setupTimerType(notificationType: NotificationBuilder.NotificationType.TypeTimer) {
        builder =
            NotificationCompat.Builder(context, CHANNEL_TIMER_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(
                    context.getString(
                        R.string.notification_timer_text, notificationType.timeLeft.toString()
                    )
                )
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setAutoCancel(false)

        buildNotificationChannel(notificationType)

        builder.setChannelId(CHANNEL_TIMER_ID)
    }

    private fun setupTimerFinished() {
        val stopTimerPendingIntent = Intent(context, TimerService::class.java).let {
            it.action = TIMER_STOP
            PendingIntent.getService(context, 0, it, FLAG_UPDATE_CURRENT)
        }

        builder = NotificationCompat.Builder(context, CHANNEL_TIMER_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(context.getString(R.string.app_name))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .addAction(
                R.drawable.ic_stop,
                context.getString(R.string.timer_stop),
                stopTimerPendingIntent
            )
            .setAutoCancel(false)

        buildNotificationChannel(NotificationBuilder.NotificationType.TypeTimer(0L))
    }

    private fun buildNotificationChannel(notificationType: NotificationBuilder.NotificationType) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            // Create the NotificationChannel
            var name = ""
            var descriptionText = ""
            var importance = NotificationManager.IMPORTANCE_DEFAULT
            var channelId = ""
            when (notificationType) {
                is NotificationBuilder.NotificationType.TypeAlarm -> {
                    name = context.getString(R.string.channel_alarm_name)
                    descriptionText = context.getString(R.string.channel_alarm_description)
                    importance = NotificationManager.IMPORTANCE_HIGH
                    channelId = CHANNEL_ALARM_ID
                }
                is NotificationBuilder.NotificationType.TypeStopwatch -> {
                    TODO("NOT IMPLEMENTED, CHANNEL STOPWATCH")
                }
                is NotificationBuilder.NotificationType.TypeTimer -> {
                    name = context.getString(R.string.channel_timer_name)
                    descriptionText = context.getString(R.string.channel_timer_description)
                    importance = NotificationManager.IMPORTANCE_HIGH
                    channelId = CHANNEL_TIMER_ID
                }
                is NotificationBuilder.NotificationType.TypeLocalization -> {
                    name = context.getString(R.string.channel_location_name)
                    descriptionText = context.getString(R.string.channel_location_description)
                    importance = NotificationManager.IMPORTANCE_LOW
                    channelId = CHANNEL_LOCALIZATION_ID
                }
            }
            val mChannel = NotificationChannel(channelId, name, importance)
            mChannel.description = descriptionText

            notificationManager.createNotificationChannel(mChannel)
        }
    }

    companion object {
        const val KEY_ALARM_EXIT = "com.helpfulapps.alarmclock.ALARM_EXIT"
        // if changed here, must be changed in intentcreator
        const val KEY_ALARM_ID = "com.helpfulapps.alarmclock.ALARM_ID"
        private const val CHANNEL_ALARM_ID = "com.helpfulapps.alarmclock.ALARM_CHANNEL_ID"
        private const val CHANNEL_TIMER_ID = "com.helpfulapps.alarmclock.TIMER_CHANNEL_ID"
        private const val CHANNEL_LOCALIZATION_ID =
            "com.helpfulapps.alarmclock.CHANNEL_LOCALIZATION_ID"
    }

}