package com.helpfulapps.alarmclock.helpers

import android.content.Context
import android.media.RingtoneManager


fun getRingtones(context: Context): Array<Pair<String, String>> {
    val manager = RingtoneManager(context)
    manager.setType(RingtoneManager.TYPE_RINGTONE)

    val list = arrayListOf<Pair<String, String>>()
    with(manager.cursor) {
        while (moveToNext()) {
            val notificationTitle = getString(RingtoneManager.TITLE_COLUMN_INDEX)
            val notificationUri =
                getString(RingtoneManager.URI_COLUMN_INDEX) + "/" + getString(
                    RingtoneManager.ID_COLUMN_INDEX
                )
            list.add(notificationTitle to notificationUri)
        }
    }
    return arrayOfNulls<Pair<String, String>>(list.size).let {
        list.toArray(it)
    }
}