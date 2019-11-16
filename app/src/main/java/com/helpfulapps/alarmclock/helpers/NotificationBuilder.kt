package com.helpfulapps.alarmclock.helpers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.views.ringing_alarm.RingingAlarmActivity

interface NotificationBuilder {
    fun createNotification(alarmId: Int): Notification
}

class NotificationBuilderImpl(private val context: Context) : NotificationBuilder {

    override fun createNotification(alarmId: Int): Notification {
        val fullScreenIntent = Intent(context, RingingAlarmActivity::class.java).also {
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            it.putExtra("EXIT", true)
            it.putExtra("ALARM_ID", alarmId)
        }
        val fullScreenPendingIntent = PendingIntent.getActivity(
            context, 0,
            fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder =
            NotificationCompat.Builder(context, CHANNEL_ALARM_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Incoming call $alarmId")
                .setContentText("(919) 555-1234")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setFullScreenIntent(fullScreenPendingIntent, true)
                .setVibrate(LongArray(3) { 500L })


        buildNotificationChannel()

        notificationBuilder.setChannelId(CHANNEL_ALARM_ID)

        return notificationBuilder.build()
    }

    private fun buildNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            // Create the NotificationChannel
            val name = CHANNEL_ALARM_NAME
            val descriptionText = CHANNEL_ALARM_DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(CHANNEL_ALARM_ID, name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    companion object {
        private const val CHANNEL_ALARM_NAME = ""
        private const val CHANNEL_ALARM_DESCRIPTION = ""
        private const val CHANNEL_ALARM_ID = "com.helpfulapps.alarmclock.ALARM_CHANNEL_ID"
    }

}