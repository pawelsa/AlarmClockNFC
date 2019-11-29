package com.helpfulapps.alarmclock.views.hourwatch_fragment

import android.content.Intent
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.FragmentHourwatchBinding
import com.helpfulapps.alarmclock.helpers.extensions.observe
import com.helpfulapps.alarmclock.service.TimerService
import com.helpfulapps.alarmclock.views.main_activity.MainActivity
import com.helpfulapps.base.base.BaseFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_hourwatch.*
import org.koin.android.viewmodel.ext.android.viewModel

class HourWatchFragment : BaseFragment<HourWatchViewModel, FragmentHourwatchBinding>() {

    override val layoutId: Int = R.layout.fragment_hourwatch
    override val viewModel: HourWatchViewModel by viewModel()

    override fun init() {

    }

    override fun onResume() {
        super.onResume()

        (mainActivity as MainActivity).fab_main_fab.setOnClickListener {
            Intent(context, TimerService::class.java).let {
                it.action = TimerService.TIMER_START
                it.putExtra(TimerService.TIMER_TIME, 30L)
                context?.startService(it)
            }
        }

        viewModel.listenToTimer()
        viewModel.timeLeft.observe(this) {
            tv_time_left.text = it.toString()
        }
    }
}