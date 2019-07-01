package com.helpfulapps.alarmclock.views.layout_helpers

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
import com.helpfulapps.alarmclock.R


fun buildDialog(context: Context, oldLabel: String, listener: OnLabelChangedListener) {

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
            listener.onLabelChanged(userInput.text.toString())
            dialog.dismiss()
        }
        .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }

    val alertDialog = alertDialogBuilder.create()

    alertDialog.show()
}

interface OnLabelChangedListener {
    fun onLabelChanged(newLabel: String)
}