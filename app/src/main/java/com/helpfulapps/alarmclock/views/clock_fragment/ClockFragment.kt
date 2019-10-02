package com.helpfulapps.alarmclock.views.clock_fragment

import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.FragmentClockBinding
import com.helpfulapps.base.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_clock.*
import org.koin.android.viewmodel.ext.android.viewModel


class ClockFragment : BaseFragment<ClockViewModel, FragmentClockBinding>() {


    override val layoutId: Int = R.layout.fragment_clock

    override val viewModel: ClockViewModel by viewModel()

    override fun init() {
        viewModel.setAdapter(ClockListAdapter(rv_clock_list))

        binding.viewModel
    }
}