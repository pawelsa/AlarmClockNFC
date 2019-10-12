package com.helpfulapps.alarmclock.helpers

import android.content.Context
import android.media.RingtoneManager

fun getDefaultRingtone(context: Context): Pair<String, String> {
    val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
    val ringtoneAlarm = RingtoneManager.getRingtone(context, uri)
    val title = ringtoneAlarm.getTitle(context).let {
        it.substring(it.indexOf("(") + 1, it.indexOf(")"))
    }
    return title to uri.toString()
}

fun getRingtones(context: Context): Array<Pair<String, String>> {
    val manager = RingtoneManager(context)
    manager.setType(RingtoneManager.TYPE_ALARM)

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