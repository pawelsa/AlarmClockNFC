package com.helpfulapps.alarmclock.views.clock_fragment

import android.widget.Toast
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.FragmentClockBinding
import com.helpfulapps.alarmclock.views.clock_fragment.add_alarm_bs.AddAlarmBottomSheet
import com.helpfulapps.alarmclock.views.main_activity.MainActivity
import com.helpfulapps.base.base.BaseFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_clock.*
import org.koin.android.viewmodel.ext.android.viewModel


class ClockFragment : BaseFragment<ClockViewModel, FragmentClockBinding>() {


    override val layoutId: Int = R.layout.fragment_clock

    override val viewModel: ClockViewModel by viewModel()

    override fun init() {
        viewModel.setAdapter(ClockListAdapter(rv_clock_list))

        testButton.setOnClickListener {
            viewModel.startAlarmMng()
        }

        stopButton.setOnClickListener {
            viewModel.stopAlarm()
        }

    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(this.context, "ClockFragment : $TAG", Toast.LENGTH_SHORT).show()
        (mainActivity as MainActivity).fab_main_fab.setOnClickListener {
            val modalBottomSheet = AddAlarmBottomSheet()
            modalBottomSheet.show(fragmentManager!!, AddAlarmBottomSheet::class.java.simpleName)
        }
    }
}