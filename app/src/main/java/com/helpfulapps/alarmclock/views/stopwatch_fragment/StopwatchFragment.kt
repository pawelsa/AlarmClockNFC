package com.helpfulapps.alarmclock.views.stopwatch_fragment

import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.FragmentHourwatchBinding
import com.helpfulapps.base.base.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel

class StopwatchFragment : BaseFragment<StopWatchViewModel, FragmentHourwatchBinding>() {

    override val layoutId: Int = R.layout.fragment_stopwatch
    override val viewModel: StopWatchViewModel by viewModel()

    override fun init() {

    }
}