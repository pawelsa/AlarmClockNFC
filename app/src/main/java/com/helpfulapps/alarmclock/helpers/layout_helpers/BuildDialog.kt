package com.helpfulapps.alarmclock.helpers.layout_helpers

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
import com.helpfulapps.alarmclock.R


fun buildEditTitleDialog(context: Context, oldLabel: String, listener: (String) -> Unit): Dialog {

    val alertDialogBuilder = AlertDialog.Builder(context, R.style.AlertDialogTheme).apply {

        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_text_label, null)

        val userInput: TextInputEditText = dialogView.findViewById(R.id.et_dialog_alarm_name)
        // set dialog message
        userInput.setText(oldLabel)
//        userInput.setBackgroundColor(ContextCompat.getColor(context, R.color.background))

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


fun buildRemoveAlarmDialog(context: Context, response: (Boolean) -> Unit): Dialog {
    return AlertDialog.Builder(context, R.style.AlertDialogTheme).apply {
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
    return AlertDialog.Builder(context, R.style.AlertDialogTheme).apply {
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
    return AlertDialog.Builder(context, R.style.AlertDialogTheme).apply {
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