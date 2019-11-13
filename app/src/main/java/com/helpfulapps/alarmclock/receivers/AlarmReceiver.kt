package com.helpfulapps.alarmclock.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.helpfulapps.alarmclock.service.AlarmService

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        Log.d("AlarmReceiver", "Opened alarm receiver")


        Intent(context, AlarmService::class.java).let { intent ->
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> context?.startForegroundService(
                    intent
                )
                else -> context?.startService(intent)
            }
        }
    }
}
