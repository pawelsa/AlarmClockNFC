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


fun buildEditTitleDialog(context: Context, oldLabel: String, listener: (String) -> Unit) {

    val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_text_label, null)

    val alertDialogBuilder = AlertDialog.Builder(context)

    // set alert_dialog.xml to alertdialog builder
    alertDialogBuilder.setView(dialogView)

    val userInput = dialogView.findViewById(R.id.ed_dialog_alarm_name) as TextInputEditText
    userInput.setText(oldLabel)

    // set dialog message
    alertDialogBuilder
        .setCancelable(false)
        .setPositiveButton("OK") { dialog, _ ->
            listener(userInput.text.toString())
            dialog.dismiss()
        }
        .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }

    val alertDialog = alertDialogBuilder.create()

    alertDialog.show()
}

fun buildSelectRingtoneDialog(
    context: Context,
    selectedRingtone: (Pair<String, String>) -> Unit
): Dialog {
    val builder = AlertDialog.Builder(context)
    builder.setTitle(context.getString(R.string.select_ringtone_dialog_title))

    val ringtones = getRingtones(context)
    val ringtoneTitles = ringtones.map { it.first }.toTypedArray()
    val checkedItem = 1 // cow
    var selectedItem = checkedItem
    var mp = MediaPlayer.create(context, ringtones[selectedItem].second.toUri())
    builder.setSingleChoiceItems(ringtoneTitles, checkedItem) { _, which ->
        selectedItem = which
        mp.stop()
        mp = MediaPlayer.create(context, ringtones[selectedItem].second.toUri())
        mp.isLooping = false
        mp.start()
    }

    builder.setPositiveButton("OK") { dialog, which ->
        selectedRingtone(ringtones[selectedItem])
        mp.stop()
        dialog.dismiss()
    }
    builder.setNegativeButton("Cancel") { dialog, which ->
        mp.stop()
    }

    return builder.create()
}