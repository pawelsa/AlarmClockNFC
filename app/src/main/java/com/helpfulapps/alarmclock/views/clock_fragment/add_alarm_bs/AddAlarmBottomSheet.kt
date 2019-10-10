package com.helpfulapps.alarmclock.views.clock_fragment.add_alarm_bs

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import ca.antonious.materialdaypicker.MaterialDayPicker
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.DialogAddAlarmBinding
import kotlinx.android.synthetic.main.dialog_add_alarm.*
import org.koin.android.viewmodel.ext.android.viewModel

class AddAlarmBottomSheet : BottomSheetDialogFragment() {

    private val TAG = this::class.java.simpleName
    private lateinit var binding: DialogAddAlarmBinding
    private val viewModel: AddAlarmBottomSheetViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_add_alarm, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.model = viewModel.newAlarm
        listenToView()
    }

    private fun listenToView() {
        listenToCancelButton()
        listenToSaveButton()
        listenToTimePicker()
        listenToRepeatCheckBox()
        listenToVibrationCheckBox()
        listenToDayPicker()
        listenToChangeSoundButton()
    }

    private fun listenToChangeSoundButton() {
        bt_add_alarm_sound.setOnClickListener {
            // TODO implement
        }
    }

    private fun listenToCancelButton() {
        ib_add_alarm_cancel.setOnClickListener {
            dismiss()
        }
    }

    private fun listenToSaveButton() {
        bt_add_alarm_save.setOnClickListener {
            /*          val alarm = getAlarmData()
                        viewModel.saveAlarm(alarm)*/
            Log.d(TAG, viewModel.newAlarm.toString())
        }
    }

    private fun listenToTimePicker() {
        et_add_alarm_time.setOnClickListener {
            viewModel.newAlarm.let {
                TimePickerDialog(
                    this.context, R.style.TimePickerTheme,
                    { _, hour, minute ->
                        it.hour = hour
                        it.minute = minute
                    }, it.hour, it.minute, true
                ).show()
            }
        }
    }

    private fun listenToRepeatCheckBox() {
        cb_add_alarm_repeat.setOnCheckedChangeListener { _, repeating ->
            viewModel.newAlarm.repeatingDays = Array(7) { false }
            dp_add_alarm_item_picker.visibility = if (repeating) View.VISIBLE else View.GONE
            viewModel.newAlarm.repeating = repeating
        }
    }

    private fun listenToVibrationCheckBox() {
        cb_add_alarm_vibrations.setOnCheckedChangeListener { _, vibrations ->
            viewModel.newAlarm.vibrations = vibrations
        }
    }

    private fun listenToDayPicker() {
        dp_add_alarm_item_picker.setDaySelectionChangedListener { list ->
            viewModel.newAlarm.let { newAlarm ->
                newAlarm.repeatingDays[0] = list.any { it == MaterialDayPicker.Weekday.MONDAY }
                newAlarm.repeatingDays[1] = list.any { it == MaterialDayPicker.Weekday.TUESDAY }
                newAlarm.repeatingDays[2] = list.any { it == MaterialDayPicker.Weekday.WEDNESDAY }
                newAlarm.repeatingDays[3] = list.any { it == MaterialDayPicker.Weekday.THURSDAY }
                newAlarm.repeatingDays[4] = list.any { it == MaterialDayPicker.Weekday.FRIDAY }
                newAlarm.repeatingDays[5] = list.any { it == MaterialDayPicker.Weekday.SATURDAY }
                newAlarm.repeatingDays[6] = list.any { it == MaterialDayPicker.Weekday.SUNDAY }
            }
        }
    }

}