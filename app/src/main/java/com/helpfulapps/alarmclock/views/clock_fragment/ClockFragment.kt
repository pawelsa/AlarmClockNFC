package com.helpfulapps.alarmclock.views.clock_fragment

import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.FragmentClockBinding
import com.helpfulapps.alarmclock.views.clock_fragment.add_alarm_bs.AddAlarmBottomSheet
import com.helpfulapps.alarmclock.views.main_activity.MainActivity
import com.helpfulapps.base.base.BaseFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel


class ClockFragment : BaseFragment<ClockViewModel, FragmentClockBinding>() {

    override val layoutId: Int = R.layout.fragment_clock

    override val viewModel: ClockViewModel by viewModel()
    private lateinit var modalBottomSheet: AddAlarmBottomSheet

    override fun init() {
        setupViewModel()
        setupData()
    }

    private fun setupViewModel() {
        viewModel.adapter = NewClockListAdapter(
            changeRingtone = ::changeRingtone,
            changeTime = ::changeTime,
            changeTitle = ::changeTitle,
            switchAlarm = ::switchAlarm,
            updateAlarm = ::updateAlarm,
            removeAlarm = ::removeAlarm
        )
        binding.viewModel = viewModel
    }

    private fun setupData() {
        viewModel.getAlarms()
    }

    private fun changeRingtone(currentRingtoneTitle: String): Pair<String, String> {
        TODO("implement")
    }

    private fun changeTime(time: Pair<Int, Int>): Pair<Int, Int> {
        TODO("implement")
    }

    private fun changeTitle(currentTitle: String): String {
        TODO("implement")
    }

    private fun switchAlarm(alarm: AlarmData) {
//        viewModel.switchAlarm(alarm.toDomain())
        modalBottomSheet = AddAlarmBottomSheet(alarm)
        modalBottomSheet.show(fragmentManager!!, AddAlarmBottomSheet::class.java.simpleName)
    }

    private fun updateAlarm(alarmData: AlarmData) {
        viewModel.updateAlarm(alarmData.toDomain())
    }

    private fun removeAlarm(alarmData: AlarmData) {
        viewModel.removeAlarm(alarmData.toDomain())
    }


    override fun onResume() {
        super.onResume()
        (mainActivity as MainActivity).fab_main_fab.setOnClickListener {
            modalBottomSheet = AddAlarmBottomSheet()
            modalBottomSheet.show(fragmentManager!!, AddAlarmBottomSheet::class.java.simpleName)
        }
    }
}