package com.helpfulapps.alarmclock.views.hourwatch_fragment

import android.widget.Toast
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.FragmentHourwatchBinding
import com.helpfulapps.alarmclock.views.main_activity.MainActivity
import com.helpfulapps.base.base.BaseFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class HourWatchFragment : BaseFragment<HourWatchViewModel, FragmentHourwatchBinding>() {

    override val layoutId: Int = R.layout.fragment_hourwatch
    override val viewModel: HourWatchViewModel by viewModel()

    override fun init() {

    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(this.context, "HourWatchFragment : $TAG", Toast.LENGTH_SHORT).show()
        (mainActivity as MainActivity).fab_main_fab.setOnClickListener {
            mainActivity?.showMessage("HourWatchFragment")
        }
    }
}