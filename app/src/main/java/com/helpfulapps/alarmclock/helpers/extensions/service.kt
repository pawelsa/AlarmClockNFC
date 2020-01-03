package com.helpfulapps.alarmclock.helpers.extensions

import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import com.helpfulapps.alarmclock.helpers.fromBuildVersion

fun Context.startVersionedService(intent: Intent) {
    fromBuildVersion(Build.VERSION_CODES.O, {
        this.startForegroundService(intent)
    }, {
        this.startService(intent)
    })
}

fun Service.startVersionedForeground(notification: Notification, id: Int = 1) {
    fromBuildVersion(
        Build.VERSION_CODES.Q,
        matching = {
            startForeground(id, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION)
        },
        otherwise = {
            startForeground(id, notification)
        })
}