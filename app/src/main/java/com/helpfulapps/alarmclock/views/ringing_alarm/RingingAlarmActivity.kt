package com.helpfulapps.alarmclock.views.ringing_alarm

import com.google.android.material.snackbar.Snackbar
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.ActivityRingingAlarmBinding
import kotlinx.android.synthetic.main.activity_ringing_alarm.*

class RingingAlarmActivity : BaseRingingAlarmActivity<ActivityRingingAlarmBinding>() {

    override val layoutId: Int = R.layout.activity_ringing_alarm

    override fun init() {
        super.init()
        binding.model = viewModel
    }

    override fun listenToAlarmSnooze() {
        fab_ring_snooze.setOnClickListener {
            snoozeAlarm()
        }
    }

    override fun listenToAlarmStop() {
        fab_ring_end.setOnClickListener {
            stopAlarm()
        }
    }

    override fun showMessage(text: String) {
        Snackbar.make(cl_ring_base, text, Snackbar.LENGTH_SHORT).show()
    }

}