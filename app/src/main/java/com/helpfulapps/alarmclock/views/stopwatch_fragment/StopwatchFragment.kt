package com.helpfulapps.alarmclock.views.stopwatch_fragment

import android.widget.Toast
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.FragmentStopwatchBinding
import com.helpfulapps.alarmclock.views.main_activity.MainActivity
import com.helpfulapps.base.base.BaseFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class StopwatchFragment : BaseFragment<StopWatchViewModel, FragmentStopwatchBinding>() {

    override val layoutId: Int = R.layout.fragment_stopwatch
    override val viewModel: StopWatchViewModel by viewModel()

    override fun init() {
    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(this.context, "StopwatchFragment : $TAG", Toast.LENGTH_SHORT).show()
        (mainActivity as MainActivity).fab_main_fab.setOnClickListener {
            mainActivity?.showMessage("StopwatchFragment")
        }
    }
}