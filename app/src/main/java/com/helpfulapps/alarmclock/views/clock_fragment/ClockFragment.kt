package com.helpfulapps.alarmclock.views.clock_fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.FragmentClockBinding
import com.helpfulapps.alarmclock.helpers.*
import com.helpfulapps.alarmclock.helpers.extensions.millisToString
import com.helpfulapps.alarmclock.helpers.layout_helpers.DividerItemDecoration
import com.helpfulapps.alarmclock.helpers.layout_helpers.buildRemoveAlarmDialog
import com.helpfulapps.alarmclock.views.clock_fragment.add_alarm_bs.AddAlarmBottomSheet
import com.helpfulapps.alarmclock.views.main_activity.MainActivity
import com.helpfulapps.base.base.BaseFragment
import com.helpfulapps.base.helpers.Failure
import com.helpfulapps.base.helpers.observe
import com.helpfulapps.domain.models.alarm.Alarm
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_clock.*
import org.koin.android.viewmodel.ext.android.viewModel


class ClockFragment : BaseFragment<ClockViewModel, FragmentClockBinding>() {

    override val layoutId: Int = R.layout.fragment_clock

    override val viewModel: ClockViewModel by viewModel()
    private lateinit var modalBottomSheet: AddAlarmBottomSheet

    private val adapter: ClockListAdapter by lazy {
        ClockListAdapter(
            switchAlarm = ::switchAlarm,
            openEditMode = ::openEdit,
            removeAlarm = ::removeAlarm
        ).apply {
            setHasStableIds(true)
        }
    }

    override fun init() {
        setupRecyclerView()
        binding.viewModel = viewModel
        setupData()
        subscribeAlarms()
        subscribeMessages()
        checkBatteryOptimization()
    }

    private fun subscribeMessages() {
        viewModel.message.observe(this) {
            when (it) {
                is AlarmRemoved -> showMessage(getString(R.string.message_alarm_removed))
                is AlarmTurnedOn -> showMessage(
                    getString(
                        R.string.message_alarm_switched,
                        it.timeToAlarm.millisToString(withLabels = true, withMillis = false)
                    )
                )
            }
        }
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

    private fun setupRecyclerView() {
        with(rv_clock_list) {
            layoutManager = LinearLayoutManager(context)
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            adapter = this@ClockFragment.adapter
            addItemDecoration(
                DividerItemDecoration(context, true)
            )
        }
    }

    private fun setupData() {
        viewModel.getAlarms()
        viewModel.subscribeToDatabaseChanges()
        viewModel.subscribeToAlarmChange()
    }

    private fun subscribeAlarms() {
        viewModel.alarmList.observe(this) {
            adapter.submitList(it)
        }
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

    override fun handleFailure(failure: Failure) {
        val stringResource: Int = when (failure) {
            is CouldNotRemoveAlarm -> R.string.failure_could_not_remove_alarm
            is CouldNotObtainAlarms -> R.string.failure_could_not_obtain_alarms
            is CouldNotSwitchAlarm -> if (failure.isTurningOn) R.string.failure_could_not_switch_on else R.string.failure_could_not_switch_off
            else -> R.string.failure_generic
        }
        showMessage(getString(stringResource))
    }

    override fun onResume() {
        super.onResume()
        (mainActivity as MainActivity).fab_main_fab.setOnClickListener {
            modalBottomSheet = AddAlarmBottomSheet()
            modalBottomSheet.show(fragmentManager!!, AddAlarmBottomSheet::class.java.simpleName)
        }
    }
}