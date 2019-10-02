package com.helpfulapps.alarmclock.views.hourwatch_fragment

import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.FragmentHourwatchBinding
import com.helpfulapps.base.base.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel

class HourWatchFragment : BaseFragment<HourWatchViewModel, FragmentHourwatchBinding>() {

    override val viewModel: HourWatchViewModel by viewModel()
    override val layoutId: Int = R.layout.fragment_hourwatch

    override fun init() {

    }

}