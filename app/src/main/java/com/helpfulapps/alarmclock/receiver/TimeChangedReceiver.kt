package com.helpfulapps.alarmclock.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.helpfulapps.alarmclock.helpers.extensions.startVersionedService
import com.helpfulapps.alarmclock.service.RebootService

class TimeChangedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.TIME_SET") {
            Intent(context, RebootService::class.java).let {
                context.startVersionedService(it)
            }
        }
    }
}