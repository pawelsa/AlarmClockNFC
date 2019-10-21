package com.helpfulapps.alarmclock.receivers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.views.ringing_alarm.RingingAlarmActivity

class AlarmService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        Log.d("AlarmService", "Opened alarm receiver")

        val fullScreenIntent = Intent(baseContext, RingingAlarmActivity::class.java)
        val fullScreenPendingIntent = PendingIntent.getActivity(
            baseContext, 0,
            fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationManager =
            baseContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilder =
            NotificationCompat.Builder(baseContext, "One")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Incoming call")
                .setContentText("(919) 555-1234")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)

                // Use a full-screen intent only for the highest-priority alerts where you
                // have an associated activity that you would like to launch after the user
                // interacts with the notification. Also, if your app targets Android 10
                // or higher, you need to request the USE_FULL_SCREEN_INTENT permission in
                // order for the platform to invoke this notification.
                .setFullScreenIntent(fullScreenPendingIntent, true)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = "Test channel"
            val descriptionText = "Test channel description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel("One", name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(mChannel)
        }

        notificationBuilder.setChannelId("One")

        val incomingCallNotification = notificationBuilder.build()
        notificationManager.notify(1, incomingCallNotification)


        return START_STICKY
    }
}
