package com.helpfulapps.alarmclock.views.ringing_alarm

import android.content.Intent
import android.os.Build
import android.view.WindowManager
import androidx.databinding.ViewDataBinding
import com.google.android.material.snackbar.Snackbar
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.helpers.NotificationBuilderImpl
import com.helpfulapps.alarmclock.helpers.extensions.observe
import com.helpfulapps.alarmclock.helpers.fromBuildVersion
import com.helpfulapps.alarmclock.helpers.startVersionedService
import com.helpfulapps.alarmclock.service.AlarmService
import com.helpfulapps.base.base.BaseActivity
import kotlinx.android.synthetic.main.activity_ringing_alarm.*
import org.koin.android.viewmodel.ext.android.viewModel

abstract class BaseRingingAlarmActivity<T : ViewDataBinding> :
    BaseActivity<RingingAlarmViewModel, T>() {

    override val viewModel: RingingAlarmViewModel by viewModel()

    override fun init() {
        setupWindowFlags()

        setupAlarmData()

        listenToAlarmStop()
        listenToAlarmSnooze()
        subscribeSnoozing()
    }

    private fun setupWindowFlags() {
        fromBuildVersion(
            Build.VERSION_CODES.O_MR1,
            matching = {
                setShowWhenLocked(true)
                setTurnScreenOn(true)
            }, otherwise = {
                window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
                window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
            })
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun setupAlarmData() {
        val alarmId = intent.getIntExtra(NotificationBuilderImpl.KEY_ALARM_ID, -1)
        if (alarmId != -1) {
            viewModel.getAlarm(alarmId.toLong())
        }
    }

    private fun subscribeSnoozing() {
        viewModel.alarmSnoozed.observe(this) {
            when {
                it -> showMessage(getString(R.string.ringing_alarm_snoozed_succesfully))
                else -> showMessage(getString(R.string.ringing_alarm_snoozed_unsuccesfully))
            }
        }
    }

    abstract fun listenToAlarmSnooze()
    abstract fun listenToAlarmStop()

    fun stopAlarm() {
        Intent(this, AlarmService::class.java).also {
            it.action = STOP_ACTION
            startVersionedService(it)
        }
        finish()
    }

    fun snoozeAlarm() {
        viewModel.snoozeAlarm()
        stopAlarm()
        finish()
    }

    override fun showMessage(text: String) {
        Snackbar.make(cl_ringing_base, text, Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        const val STOP_ACTION = "com.helpfulapps.alarmclock.views.ringing_alarm.stop"
        const val STOP_ALARM_INTENT = "com.helpfulapps.alarmclock.views.ringing_alarm.close"
    }

}