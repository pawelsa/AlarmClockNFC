package com.helpfulapps.alarmclock.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.util.Log

class AlarmWakeupReceiver : BroadcastReceiver() {

    private val TAG = this.javaClass.simpleName

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "alarmWakeupReceiver ${System.currentTimeMillis()}")
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock =
            powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "alarmClock:PrepareForAlarm")
        wakeLock.acquire(10)
        wakeLock.release()
    }
}
