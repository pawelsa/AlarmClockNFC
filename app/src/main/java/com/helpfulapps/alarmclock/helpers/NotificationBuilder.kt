package com.helpfulapps.alarmclock.helpers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.helpers.extensions.secondsToString
import com.helpfulapps.alarmclock.helpers.extensions.timeToString
import com.helpfulapps.alarmclock.service.AlarmService
import com.helpfulapps.alarmclock.service.StopwatchService
import com.helpfulapps.alarmclock.service.StopwatchService.Companion.STOPWATCH_LAP
import com.helpfulapps.alarmclock.service.StopwatchService.Companion.STOPWATCH_PAUSE
import com.helpfulapps.alarmclock.service.StopwatchService.Companion.STOPWATCH_RESUME
import com.helpfulapps.alarmclock.service.StopwatchService.Companion.STOPWATCH_STOP
import com.helpfulapps.alarmclock.service.TimerService
import com.helpfulapps.alarmclock.views.main_activity.MainActivity
import com.helpfulapps.alarmclock.views.main_activity.MainActivity.Companion.ACTION_OPEN_STOPWATCH
import com.helpfulapps.alarmclock.views.main_activity.MainActivity.Companion.ACTION_OPEN_TIMER
import com.helpfulapps.alarmclock.views.ringing_alarm.BaseRingingAlarmActivity
import com.helpfulapps.alarmclock.views.ringing_alarm.NfcRingingAlarmActivity
import com.helpfulapps.alarmclock.views.ringing_alarm.RingingAlarmActivity
import com.helpfulapps.alarmclock.views.timer_finished_activity.TimerFinishedActivity
import com.helpfulapps.domain.models.alarm.Alarm

interface NotificationBuilder {

    fun setNotificationType(notificationType: NotificationType): NotificationBuilder

    fun build(): Notification

    sealed class NotificationType {
        object HighPriority : NotificationType()
        data class TypeAlarm(val alarm: Alarm, val usingNfc: Boolean = false) : NotificationType()
        data class TypeStopwatchRunning(val seconds: Long) : NotificationType()
        data class TypeStopwatchPaused(val seconds: Long) : NotificationType()
        data class TypeTimer(val timeLeft: Long) : NotificationType()
        data class TypeTimerPaused(val timeLeft: Long) : NotificationType()
        object TypeTimerFinished : NotificationType()
        object TypeLocalization : NotificationType()
        object Initialization : NotificationType()
    }
}

class NotificationBuilderImpl(private val context: Context) : NotificationBuilder {

    private lateinit var builder: NotificationCompat.Builder

    override fun build(): Notification = builder.build()

    override fun setNotificationType(notificationType: NotificationBuilder.NotificationType) =
        apply {
            when (notificationType) {
                is NotificationBuilder.NotificationType.TypeAlarm -> setupAlarmType(notificationType)
                is NotificationBuilder.NotificationType.TypeStopwatchRunning -> setupStopWatchType(
                    notificationType
                )
                is NotificationBuilder.NotificationType.TypeStopwatchPaused -> setupStopWatchPausedType(
                    notificationType
                )
                is NotificationBuilder.NotificationType.TypeTimer -> setupTimerType(notificationType)
                is NotificationBuilder.NotificationType.TypeTimerPaused -> setupTimerPause(
                    notificationType
                )
                is NotificationBuilder.NotificationType.TypeTimerFinished -> setupTimerFinished()
                is NotificationBuilder.NotificationType.TypeLocalization -> setupLocalizationType()
                is NotificationBuilder.NotificationType.Initialization -> setupInitialization()
            }
        }

    private fun setupInitialization() {
        builder =
            NotificationCompat.Builder(context, CHANNEL_INITIALIZATION_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(
                    context.getString(R.string.notification_initialization)
                )
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setAutoCancel(false)

        buildNotificationChannel(NotificationBuilder.NotificationType.Initialization)

        builder.setChannelId(CHANNEL_INITIALIZATION_ID)
    }

    private fun setupStopWatchPausedType(notificationType: NotificationBuilder.NotificationType.TypeStopwatchPaused) {

        val restartIntent = Intent(context, StopwatchService::class.java).let {
            it.action = STOPWATCH_RESUME
            getService(context, 0, it, FLAG_UPDATE_CURRENT)
        }

        val stopIntent = Intent(context, StopwatchService::class.java).let {
            it.action = STOPWATCH_STOP
            getService(context, 0, it, FLAG_UPDATE_CURRENT)
        }

        builder =
            NotificationCompat.Builder(context, CHANNEL_STOPWATCH_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(
                    notificationType.seconds.secondsToString()
                )
                .setContentIntent(buildIntentToStartFragment(ACTION_OPEN_STOPWATCH))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .addAction(
                    R.drawable.ic_start,
                    context.getString(R.string.notification_stopwatch_resume),
                    restartIntent
                )
                .addAction(
                    0,
                    context.getString(R.string.notification_stopwatch_reset),
                    stopIntent
                )
                .setAutoCancel(false)

        buildNotificationChannel(NotificationBuilder.NotificationType.TypeStopwatchRunning(0L))

        builder.setChannelId(CHANNEL_STOPWATCH_ID)
    }


    private fun setupAlarmType(notificationType: NotificationBuilder.NotificationType.TypeAlarm) {
        val alarm = notificationType.alarm

        val snoozeAlarmIntent = Intent(context, AlarmService::class.java).let {
            it.action = BaseRingingAlarmActivity.SNOOZE_ACTION
            getService(context, 0, it, FLAG_UPDATE_CURRENT)
        }

        val stopAlarmIntent = Intent(context, AlarmService::class.java).let {
            it.action = BaseRingingAlarmActivity.STOP_ACTION
            getService(context, 0, it, FLAG_UPDATE_CURRENT)
        }

        val ringingActivity =
            if (notificationType.usingNfc) NfcRingingAlarmActivity::class.java else RingingAlarmActivity::class.java

        val fullScreenIntent = Intent(context, ringingActivity).also {
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            it.putExtra(KEY_ALARM_EXIT, true)
            it.putExtra(KEY_ALARM_ID, alarm.id.toInt())
        }
        val fullScreenPendingIntent = getActivity(
            context, 0,
            fullScreenIntent, FLAG_UPDATE_CURRENT
        )
        builder =
            NotificationCompat.Builder(context, CHANNEL_PRIORITY_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(
                    context.getString(
                        R.string.notification_alarm_text,
                        timeToString(
                            alarm.hour,
                            alarm.minute
                        )
                    )
                )
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setFullScreenIntent(fullScreenPendingIntent, true)
                .setVibrate(LongArray(3) { 500L })
                .addAction(
                    R.drawable.ic_snooze,
                    context.getString(R.string.notification_alarm_snooze),
                    snoozeAlarmIntent
                )

        if (!notificationType.usingNfc) {
            builder
                .addAction(
                    R.drawable.ic_cancel,
                    context.getString(R.string.notification_alarm_stop),
                    stopAlarmIntent
                )
        }

        buildNotificationChannel(NotificationBuilder.NotificationType.HighPriority)

        builder.setChannelId(CHANNEL_PRIORITY_ID)
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

    private fun setupStopWatchType(notificationType: NotificationBuilder.NotificationType.TypeStopwatchRunning) {

        val pauseIntent = Intent(context, StopwatchService::class.java).let {
            it.action = STOPWATCH_PAUSE
            getService(context, 0, it, FLAG_UPDATE_CURRENT)
        }

        val takeLapIntent = Intent(context, StopwatchService::class.java).let {
            it.action = STOPWATCH_LAP
            getService(context, 0, it, FLAG_UPDATE_CURRENT)
        }

        builder =
            NotificationCompat.Builder(context, CHANNEL_STOPWATCH_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(
                    notificationType.seconds.secondsToString()
                )
                .setContentIntent(buildIntentToStartFragment(ACTION_OPEN_STOPWATCH))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .addAction(
                    R.drawable.ic_pause,
                    context.getString(R.string.notification_stopwatch_pause),
                    pauseIntent
                )
                .addAction(
                    0,
                    context.getString(R.string.notification_stopwatch_take_lap),
                    takeLapIntent
                )
                .setAutoCancel(true)

        buildNotificationChannel(notificationType)

        builder.setChannelId(CHANNEL_STOPWATCH_ID)
    }

    private fun setupTimerType(notificationType: NotificationBuilder.NotificationType.TypeTimer) {

        val pauseIntent = Intent(context, TimerService::class.java).let {
            it.action = TimerService.TIMER_PAUSE
            getService(context, 0, it, FLAG_UPDATE_CURRENT)
        }

        val addMinuteIntent = Intent(context, TimerService::class.java).let {
            it.action = TimerService.TIMER_ADD_MINUTE
            getService(context, 0, it, FLAG_UPDATE_CURRENT)
        }

        builder =
            NotificationCompat.Builder(context, CHANNEL_TIMER_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(
                    context.getString(
                        R.string.notification_timer_text,
                        notificationType.timeLeft.secondsToString()
                    )
                )
                .setContentIntent(buildIntentToStartFragment(ACTION_OPEN_TIMER))
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .addAction(
                    R.drawable.ic_pause,
                    context.getString(R.string.notification_timer_pause),
                    pauseIntent
                )
                .addAction(
                    0,
                    context.getString(R.string.notification_timer_add_minute),
                    addMinuteIntent
                )
                .setAutoCancel(true)

        buildNotificationChannel(notificationType)

        builder.setChannelId(CHANNEL_TIMER_ID)
    }

    private fun setupTimerPause(notificationType: NotificationBuilder.NotificationType.TypeTimerPaused) {

        val restartIntent = Intent(context, TimerService::class.java).let {
            it.action = TimerService.TIMER_RESTART
            getService(context, 0, it, FLAG_UPDATE_CURRENT)
        }

        val addMinuteIntent = Intent(context, TimerService::class.java).let {
            it.action = TimerService.TIMER_ADD_MINUTE
            getService(context, 0, it, FLAG_UPDATE_CURRENT)
        }

        val finishIntent = Intent(context, TimerService::class.java).let {
            it.action = TimerService.TIMER_FINISH
            getService(context, 0, it, FLAG_UPDATE_CURRENT)
        }

        builder =
            NotificationCompat.Builder(context, CHANNEL_TIMER_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(
                    context.getString(
                        R.string.notification_timer_paused_text,
                        notificationType.timeLeft.secondsToString()
                    )
                )
                .setContentIntent(buildIntentToStartFragment(ACTION_OPEN_TIMER))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .addAction(
                    R.drawable.ic_pause,
                    context.getString(R.string.notification_timer_restart),
                    restartIntent
                )
                .addAction(
                    0,
                    context.getString(R.string.notification_timer_add_minute),
                    addMinuteIntent
                )
                .addAction(0, context.getString(R.string.notification_timer_stop), finishIntent)
                .setAutoCancel(true)

        buildNotificationChannel(NotificationBuilder.NotificationType.TypeTimer(0L))

        builder.setChannelId(CHANNEL_TIMER_ID)
    }

    private fun setupTimerFinished() {
        val stopTimerPendingIntent = Intent(context, TimerService::class.java).let {
            it.action = TimerService.TIMER_FINISH
            getService(context, 0, it, FLAG_CANCEL_CURRENT)
        }

        val fullScreenIntent = Intent(context, TimerFinishedActivity::class.java).also {
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        val fullScreenPendingIntent = getActivity(
            context, 0,
            fullScreenIntent, FLAG_UPDATE_CURRENT
        )

        builder = NotificationCompat.Builder(context, CHANNEL_PRIORITY_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(context.getString(R.string.notification_timer_text_finished))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .addAction(
                R.drawable.ic_stop,
                context.getString(R.string.timer_stop),
                stopTimerPendingIntent
            )
            .setAutoCancel(false)

        buildNotificationChannel(NotificationBuilder.NotificationType.HighPriority)
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
                is NotificationBuilder.NotificationType.HighPriority -> {
                    name = context.getString(R.string.channel_priority_name)
                    descriptionText = context.getString(R.string.channel_priority_description)
                    importance = NotificationManager.IMPORTANCE_HIGH
                    channelId = CHANNEL_PRIORITY_ID
                }
                is NotificationBuilder.NotificationType.TypeStopwatchRunning -> {
                    name = context.getString(R.string.channel_stopwatch_name)
                    descriptionText = context.getString(R.string.channel_stopwatch_description)
                    importance = NotificationManager.IMPORTANCE_LOW
                    channelId = CHANNEL_STOPWATCH_ID
                }
                is NotificationBuilder.NotificationType.TypeTimer -> {
                    name = context.getString(R.string.channel_timer_name)
                    descriptionText = context.getString(R.string.channel_timer_description)
                    importance = NotificationManager.IMPORTANCE_LOW
                    channelId = CHANNEL_TIMER_ID
                }
                is NotificationBuilder.NotificationType.TypeLocalization -> {
                    name = context.getString(R.string.channel_location_name)
                    descriptionText = context.getString(R.string.channel_location_description)
                    importance = NotificationManager.IMPORTANCE_LOW
                    channelId = CHANNEL_LOCALIZATION_ID
                }
                is NotificationBuilder.NotificationType.Initialization -> {
                    name = context.getString(R.string.notification_channel_initialization_name)
                    descriptionText =
                        context.getString(R.string.notification_channel_initialization_description)
                    importance = NotificationManager.IMPORTANCE_LOW
                    channelId = CHANNEL_INITIALIZATION_ID
                }
            }
            val mChannel = NotificationChannel(channelId, name, importance)
            mChannel.description = descriptionText

            notificationManager.createNotificationChannel(mChannel)
        }
    }

    private fun buildIntentToStartFragment(intentAction: String): PendingIntent {
        return Intent(context, MainActivity::class.java).let {
            it.action = intentAction
            it.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP
                    or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            getActivity(context, 0, it, 0)
        }
    }

    companion object {
        const val KEY_ALARM_EXIT = "com.helpfulapps.alarmclock.ALARM_EXIT"
        // if changed here, must be changed in intentcreator
        const val KEY_ALARM_ID = "com.helpfulapps.alarmclock.ALARM_ID"
        private const val CHANNEL_PRIORITY_ID = "com.helpfulapps.alarmclock.ALARM_PRIORITY_ID"
        private const val CHANNEL_TIMER_ID = "com.helpfulapps.alarmclock.TIMER_CHANNEL_ID"
        private const val CHANNEL_STOPWATCH_ID = "com.helpfulapps.alarmclock.STOPWATCH_CHANNEL_ID"
        private const val CHANNEL_LOCALIZATION_ID =
            "com.helpfulapps.alarmclock.CHANNEL_LOCALIZATION_ID"
        private const val CHANNEL_INITIALIZATION_ID =
            "com.helpfulapps.alarmclock.CHANNEL_INITIALIZATION_ID"
    }

}