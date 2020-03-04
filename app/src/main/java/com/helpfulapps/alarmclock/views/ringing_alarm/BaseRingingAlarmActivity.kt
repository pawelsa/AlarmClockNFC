package com.helpfulapps.alarmclock.views.ringing_alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.view.WindowManager
import androidx.databinding.ViewDataBinding
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.helpfulapps.alarmclock.helpers.NotificationBuilderImpl
import com.helpfulapps.alarmclock.helpers.extensions.startVersionedService
import com.helpfulapps.alarmclock.helpers.fromBuildVersion
import com.helpfulapps.alarmclock.service.AlarmService
import com.helpfulapps.base.base.BaseActivity
import org.koin.android.viewmodel.ext.android.viewModel

abstract class BaseRingingAlarmActivity<T : ViewDataBinding> :
    BaseActivity<RingingAlarmViewModel, T>() {

    override val viewModel: RingingAlarmViewModel by viewModel()

    private val autoSnoozeBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            finish()
        }
    }

    override fun init() {
        setupWindowFlags()

        setupAlarmData()

        listenToAlarmStop()
        listenToAlarmSnooze()

        setupReceiver()
    }

    private fun setupReceiver() {
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(autoSnoozeBroadcastReceiver, IntentFilter(AUTO_SNOOZE_ALARM))
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

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(autoSnoozeBroadcastReceiver)
        super.onDestroy()
    }

    companion object {
        const val STOP_ACTION = "com.helpfulapps.alarmclock.views.ringing_alarm.stop"
        const val SNOOZE_ACTION = "com.helpfulapps.alarmclock.views.ringing_alarm.snooze"
        const val AUTO_SNOOZE_ALARM = "com.helpfulapps.alarmclock.views.ringing_alarm.auto_snooze"
        const val STOP_ALARM_INTENT = "com.helpfulapps.alarmclock.views.ringing_alarm.close"
    }

}