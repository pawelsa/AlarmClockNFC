package com.helpfulapps.alarmclock.views.clock_fragment.add_alarm_bs

import android.Manifest
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.DialogAddAlarmBinding
import com.helpfulapps.alarmclock.helpers.ShortPermissionListener
import com.helpfulapps.alarmclock.helpers.Time
import com.helpfulapps.alarmclock.helpers.extensions.observe
import com.helpfulapps.alarmclock.helpers.layout_helpers.buildSelectRingtoneDialog
import com.helpfulapps.alarmclock.views.main_activity.MainActivity
import com.helpfulapps.domain.models.alarm.Alarm
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import kotlinx.android.synthetic.main.dialog_add_alarm.*
import org.koin.android.viewmodel.ext.android.viewModel

class AddAlarmBottomSheet(val alarm: Alarm? = null) : BottomSheetDialogFragment() {

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
        alarm?.let {
            viewModel.setAlarm(it)
        }
        binding.model = viewModel
        listenToView()
        subscribeData()
    }

    private fun subscribeData() {
        viewModel.getDefaultAlarmTitle(context!!)
        viewModel.setupData()
        subscribeSavingAlarm()
    }

    private fun subscribeSavingAlarm() {
        viewModel.alarmSaved.observe(this) { alarmSaved ->
            when {
                alarmSaved -> dismiss()
                else -> Snackbar.make(
                    binding.clAddAlarmBase,
                    getString(R.string.add_alarm_fail),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun listenToView() {
        listenToCancelButton()
        listenToSaveButton()
        listenToTimePicker()
        listenToChangeSoundButton()
    }

    private fun listenToChangeSoundButton() {
        bt_add_alarm_sound.setOnClickListener {
            Dexter.withActivity(activity)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : ShortPermissionListener {

                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                        buildSelectRingtoneDialog(
                            context!!,
                            viewModel.ringtoneTitle.value
                        ) {
                            viewModel.ringtone = it
                        }.show()
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                        (activity as MainActivity).showMessage(getString(R.string.permission_ringtone))
                    }
                }
                ).check()
        }
    }

    private fun listenToCancelButton() {
        ib_add_alarm_cancel.setOnClickListener {
            dismiss()
        }
    }

    private fun listenToSaveButton() {
        bt_add_alarm_save.setOnClickListener {
            viewModel.saveAlarm(alarm != null)
        }
    }

    private fun listenToTimePicker() {
        tv_add_alarm_time.setOnClickListener {
            viewModel.run {
                TimePickerDialog(
                    this@AddAlarmBottomSheet.context, R.style.TimePickerTheme,
                    { _, hour, minute ->
                        time = Time(hour, minute)
                    },
                    time.first, time.second, true
                ).show()
            }
        }
    }

}