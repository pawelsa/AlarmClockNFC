package com.helpfulapps.alarmclock.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.helpfulapps.alarmclock.service.AlarmService

//TODO remove
class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        Log.d("AlarmReceiver", "Opened alarm receiver")


        Intent(context, AlarmService::class.java).let {
            it.putExtra("ALARM_ID", intent?.getIntExtra("ALARM_ID", -1))
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> context?.startForegroundService(it)
                else -> context?.startService(it)
            }
        }
    }
}
