package com.helpfulapps.alarmclock.helpers

import android.os.Build

inline fun fromBuildVersion(version: Int, matching: () -> Unit, otherwise: () -> Unit = {}) {
    when {
        Build.VERSION.SDK_INT >= version -> matching()
        else -> otherwise()
    }
}

inline fun fromBuildVersion(version: Int, matching: () -> Unit) {
    if (Build.VERSION.SDK_INT >= version) matching()
}