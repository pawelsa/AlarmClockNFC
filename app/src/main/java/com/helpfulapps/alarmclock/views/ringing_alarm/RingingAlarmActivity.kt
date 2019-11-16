package com.helpfulapps.alarmclock.views.ringing_alarm

import android.content.Intent
import android.view.WindowManager
import com.google.android.material.snackbar.Snackbar
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.ActivityRingingAlarmBinding
import com.helpfulapps.alarmclock.service.AlarmService
import com.helpfulapps.base.base.BaseActivity
import kotlinx.android.synthetic.main.activity_ringing_alarm.*
import org.koin.android.viewmodel.ext.android.viewModel

class RingingAlarmActivity : BaseActivity<RingingAlarmViewModel, ActivityRingingAlarmBinding>() {

    override val layoutId: Int = R.layout.activity_ringing_alarm

    override val viewModel: RingingAlarmViewModel by viewModel()


    override fun init() {

        val alarmId = intent.getIntExtra("ALARM_ID", -1)
        if (alarmId != -1) {
            viewModel.getAlarm(alarmId.toLong())
        }

        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)

        binding.model = viewModel

        fab_ring_end.setOnClickListener {
            Intent(this, AlarmService::class.java).also {
                it.action = "STOP"
                stopService(it)
            }
            finish()
        }

        fab_ring_snooze.setOnClickListener {
            // TODO implement
        }
    }

    override fun showMessage(text: String) {
        Snackbar.make(cl_ringing_base, text, Snackbar.LENGTH_SHORT).show()
    }

}