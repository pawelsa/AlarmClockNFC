package com.helpfulapps.alarmclock.views.clock_fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.FragmentClockBinding
import com.helpfulapps.alarmclock.helpers.fromBuildVersion
import com.helpfulapps.alarmclock.helpers.layout_helpers.buildRemoveAlarmDialog
import com.helpfulapps.alarmclock.views.clock_fragment.add_alarm_bs.AddAlarmBottomSheet
import com.helpfulapps.alarmclock.views.main_activity.MainActivity
import com.helpfulapps.base.base.BaseFragment
import com.helpfulapps.domain.models.alarm.Alarm
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel


class ClockFragment : BaseFragment<ClockViewModel, FragmentClockBinding>() {

    override val layoutId: Int = R.layout.fragment_clock

    override val viewModel: ClockViewModel by viewModel()
    private lateinit var modalBottomSheet: AddAlarmBottomSheet

    override fun init() {
        setupViewModel()
        setupData()
        checkBatteryOptimization()
    }

    private fun checkBatteryOptimization() {
        fromBuildVersion(Build.VERSION_CODES.M) {
            if (viewModel.askForBatteryOptimization) {
                val intent = Intent()
                val packageName = context!!.packageName
                val pm: PowerManager =
                    context!!.getSystemService(Context.POWER_SERVICE) as PowerManager
                if (pm.isIgnoringBatteryOptimizations(packageName)) {
                    intent.action = Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
                } else {
                    intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                    intent.data = Uri.parse("package:$packageName")
                    viewModel.batteryOptimizationTurnedOff()
                }
                context!!.startActivity(intent)
            }
        }
    }

    private fun setupViewModel() {
        viewModel.adapter = ClockListAdapter(
            switchAlarm = ::switchAlarm,
            openEditMode = ::openEdit,
            removeAlarm = ::removeAlarm
        )
        binding.viewModel = viewModel
    }

    private fun setupData() {
        viewModel.getAlarms()
        viewModel.subscribeToDatabaseChanges()
    }

    private fun switchAlarm(alarm: Alarm) {
        viewModel.switchAlarm(alarm)
    }

    private fun openEdit(alarm: Alarm) {
        modalBottomSheet = AddAlarmBottomSheet(alarm)
        modalBottomSheet.show(fragmentManager!!, AddAlarmBottomSheet::class.java.simpleName)
    }

    private fun removeAlarm(alarm: Alarm) {
        buildRemoveAlarmDialog(this.context!!) { shouldRemove ->
            if (shouldRemove) viewModel.removeAlarm(alarm)
        }.show()
    }

    override fun onResume() {
        super.onResume()
        (mainActivity as MainActivity).fab_main_fab.setOnClickListener {
            modalBottomSheet = AddAlarmBottomSheet()
            modalBottomSheet.show(fragmentManager!!, AddAlarmBottomSheet::class.java.simpleName)
        }
    }
}