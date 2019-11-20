package com.helpfulapps.alarmclock.helpers

import android.content.Context
import android.content.Intent
import android.os.Build

inline fun Context.startVersionedService(intent: Intent) {
    fromBuildVersion(Build.VERSION_CODES.O, {
        this.startForegroundService(intent)
    }, {
        this.startService(intent)
    })
}