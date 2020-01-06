package com.helpfulapps.alarmclock.helpers.layout_helpers

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.helpfulapps.alarmclock.R
import com.helpfulapps.device.alarms.other.AlarmPlayerImpl
import com.helpfulapps.domain.other.AlarmPlayer
import com.helpfulapps.domain.repository.RingtoneRepository

interface RingtoneDialogBuilder {
    fun buildSelectRingtoneDialog(
        context: Context,
        currentRingtoneTitle: String?,
        selectedRingtone: (Pair<String, String>) -> Unit
    ): Dialog
}

class RingtoneDialogBuilderImpl(private val ringtoneRepository: RingtoneRepository) :
    RingtoneDialogBuilder {

    override fun buildSelectRingtoneDialog(
        context: Context,
        currentRingtoneTitle: String?,
        selectedRingtone: (Pair<String, String>) -> Unit
    ): Dialog {

        val alarmPlayer: AlarmPlayer =
            AlarmPlayerImpl(context)

        val ringtones = ringtoneRepository.getRingtones()
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
                alarmPlayer.startPlaying(ringtones[selectedRingtoneIndex].second)
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
}