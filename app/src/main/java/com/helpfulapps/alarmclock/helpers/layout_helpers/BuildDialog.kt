package com.helpfulapps.alarmclock.helpers.layout_helpers

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import com.google.android.material.textfield.TextInputEditText
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.helpers.AlarmPlayer
import com.helpfulapps.alarmclock.helpers.AlarmPlayerImpl
import com.helpfulapps.alarmclock.helpers.getRingtones


fun buildEditTitleDialog(context: Context, oldLabel: String, listener: (String) -> Unit): Dialog {

    val alertDialogBuilder = AlertDialog.Builder(context).apply {

        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_text_label, null)

        val userInput: TextInputEditText = dialogView.findViewById(R.id.et_dialog_alarm_name)
        // set dialog message
        userInput.setText(oldLabel)

        // set alert_dialog.xml to alertdialog builder
        setView(dialogView)
        setCancelable(false)
        setTitle(context.getString(R.string.dialog_rename_title))
        setPositiveButton("OK") { dialog, _ ->
            listener(userInput.text.toString())
            dialog.dismiss()
        }
        setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
    }
    return alertDialogBuilder.create()
}

fun buildSelectRingtoneDialog(
    context: Context,
    currentRingtoneTitle: String?,
    selectedRingtone: (Pair<String, String>) -> Unit
): Dialog {

    val alarmPlayer: AlarmPlayer = AlarmPlayerImpl(context)

    val ringtones = getRingtones(context)
    var selectedRingtoneIndex = ringtones
        .indexOfFirst { it.first == currentRingtoneTitle }
        .let { indexOfCurrentRingtoneInList ->
            if (indexOfCurrentRingtoneInList > -1) indexOfCurrentRingtoneInList else 0
        }

    val ringtoneTitles = ringtones.map { it.first }.toTypedArray()

    return AlertDialog.Builder(context).apply {
        setTitle(context.getString(R.string.select_ringtone_dialog_title))

        setSingleChoiceItems(ringtoneTitles, selectedRingtoneIndex) { _, whichPressed ->
            selectedRingtoneIndex = whichPressed
            alarmPlayer.startPlaying(ringtones[selectedRingtoneIndex].second.toUri())
        }
        setPositiveButton(android.R.string.ok) { _, _ ->
            selectedRingtone(ringtones[selectedRingtoneIndex])
            alarmPlayer.stopPlaying()
        }
        setNegativeButton(android.R.string.cancel) { _, _ ->
            alarmPlayer.stopPlaying()
        }
    }.create()
}

fun buildRemoveAlarmDialog(context: Context, response: (Boolean) -> Unit): Dialog {
    return AlertDialog.Builder(context).apply {
        setTitle(context.getString(R.string.dialog_remove_title))
        setMessage(context.getString(R.string.dialog_remove_message))
        setPositiveButton(context.getString(android.R.string.yes)) { _, _ ->
            response(true)
        }
        setNegativeButton(context.getString(android.R.string.no)) { _, _ ->
            response(false)
        }
    }.create()
}

fun buildEnableNfcAlarmDialog(context: Context, action: () -> Unit): Dialog {
    return AlertDialog.Builder(context).apply {
        setTitle(R.string.nfc_enable_dialog_title)
        setMessage(R.string.nfc_enable_dialog_message)
        setPositiveButton(context.getString(R.string.nfc_enable_dialog_open_settings)) { dialog, _ ->
            action()
            dialog.dismiss()
        }
        setNegativeButton(context.getString(android.R.string.no)) { dialog, _ ->
            dialog.dismiss()
        }
    }.create()
}

fun buildGpsEnableAlarmDialog(context: Context, action: () -> Unit): Dialog {
    return AlertDialog.Builder(context).apply {
        setTitle(context.getString(R.string.dialog_gps_enable_title))
        setMessage(context.getString(R.string.dialog_gps_enable_message))
        setPositiveButton(context.getString(R.string.gps_dialog_open_settings)) { dialog, _ ->
            action()
            dialog.dismiss()
        }
        setNegativeButton(context.getString(android.R.string.no)) { dialog, _ ->
            dialog.dismiss()
        }
    }.create()
}