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
    private val modalBottomSheet: AddAlarmBottomSheet by lazy {
        AddAlarmBottomSheet()
    }

    override fun init() {
        setupViewModel()
        setupData()
    }

    private fun setupViewModel() {
        viewModel.adapter = ClockListAdapter()
        binding.viewModel = viewModel
    }

    private fun setupData() {
        viewModel.getAlarms()
    }

    override fun onResume() {
        super.onResume()
        (mainActivity as MainActivity).fab_main_fab.setOnClickListener {
            modalBottomSheet.show(fragmentManager!!, AddAlarmBottomSheet::class.java.simpleName)
        }
    }
}