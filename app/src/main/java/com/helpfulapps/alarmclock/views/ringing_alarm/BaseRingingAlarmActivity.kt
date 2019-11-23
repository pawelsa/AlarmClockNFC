package com.helpfulapps.alarmclock.views.ringing_alarm

import android.content.Intent
import android.os.Build
import android.view.WindowManager
import androidx.databinding.ViewDataBinding
import com.google.android.material.snackbar.Snackbar
import com.helpfulapps.alarmclock.helpers.NotificationBuilderImpl
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

    abstract fun listenToAlarmSnooze()
    abstract fun listenToAlarmStop()

    fun stopAlarm() {
        Intent(this, AlarmService::class.java).let {
            it.action = STOP_ACTION
            startVersionedService(it)
        }
        finish()
    }


    fun snoozeAlarm() {
        Intent(this, AlarmService::class.java).let {
            it.action = SNOOZE_ACTION
            startVersionedService(it)
        }
        finish()
    }

    override fun showMessage(text: String) {
        Snackbar.make(cl_ringing_base, text, Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        const val STOP_ACTION = "com.helpfulapps.alarmclock.views.ringing_alarm.stop"
        const val SNOOZE_ACTION = "com.helpfulapps.alarmclock.views.ringing_alarm.snooze"
        const val STOP_ALARM_INTENT = "com.helpfulapps.alarmclock.views.ringing_alarm.close"
    }

}