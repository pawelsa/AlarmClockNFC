package com.helpfulapps.alarmclock.helpers.layout_helpers

import android.app.Dialog
import android.content.Context
import android.media.MediaPlayer
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import com.google.android.material.textfield.TextInputEditText
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.helpers.getRingtones


fun buildEditTitleDialog(context: Context, oldLabel: String, listener: (String) -> Unit): Dialog {

    val alertDialogBuilder = AlertDialog.Builder(context).apply {

        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_text_label, null)

        val userInput: TextInputEditText = dialogView.findViewById(R.id.ed_dialog_alarm_name)
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
    val ringtones = getRingtones(context)
    var selectedRingtoneIndex = ringtones
        .indexOfFirst { it.first == currentRingtoneTitle }
        .let { indexOfCurrentRingtoneInList ->
            if (indexOfCurrentRingtoneInList > -1) indexOfCurrentRingtoneInList else 0
        }

    val ringtoneTitles = ringtones.map { it.first }.toTypedArray()

    var mp = MediaPlayer.create(context, ringtones[selectedRingtoneIndex].second.toUri())

    return AlertDialog.Builder(context).apply {
        setTitle(context.getString(R.string.select_ringtone_dialog_title))

        setSingleChoiceItems(ringtoneTitles, selectedRingtoneIndex) { _, whichPressed ->
            selectedRingtoneIndex = whichPressed
            mp.stop()
            mp = MediaPlayer.create(context, ringtones[selectedRingtoneIndex].second.toUri())
            mp.isLooping = false
            mp.start()
        }
        setPositiveButton(android.R.string.ok) { _, _ ->
            selectedRingtone(ringtones[selectedRingtoneIndex])
            mp.stop()
        }
        setNegativeButton(android.R.string.cancel) { _, _ ->
            mp.stop()
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