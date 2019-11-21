package com.helpfulapps.alarmclock.views.ringing_alarm

import com.google.android.material.snackbar.Snackbar
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.ActivityAlarmNfcBinding
import kotlinx.android.synthetic.main.activity_alarm_nfc.*
import kotlinx.android.synthetic.main.activity_ringing_alarm.*

class NfcRingingAlarmActivity : BaseRingingAlarmActivity<ActivityAlarmNfcBinding>() {

    override val layoutId: Int = R.layout.activity_alarm_nfc


    override fun init() {
        super.init()
        binding.model = viewModel
    }

    override fun listenToAlarmSnooze() {
        fab_ring_nfc_snooze.setOnClickListener {
            snoozeAlarm()
        }
    }

    override fun listenToAlarmStop() {
        imageView.setOnClickListener {
            stopAlarm()
        }
    }

    override fun showMessage(text: String) {
        Snackbar.make(cl_ringing_base, text, Snackbar.LENGTH_SHORT).show()
    }
}