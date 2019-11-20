package com.helpfulapps.device.alarms.helpers

import android.os.Build

inline fun fromBuildVersion(version: Int, matching: () -> Unit, otherwise: () -> Unit = {}) {
    when {
        Build.VERSION.SDK_INT >= version -> matching()
        else -> otherwise()
    }
}

inline fun <T> fromBuildVersion(version: Int, matching: () -> T, otherwise: () -> T): T {
    return when {
        Build.VERSION.SDK_INT >= version -> matching()
        else -> otherwise()
    }
}

fun matchesVersionsFrom(version: Int): Boolean = Build.VERSION.SDK_INT >= version