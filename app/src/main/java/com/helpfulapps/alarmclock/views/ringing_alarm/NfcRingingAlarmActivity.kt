package com.helpfulapps.alarmclock.views.ringing_alarm

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import com.google.android.material.snackbar.Snackbar
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.ActivityAlarmNfcBinding
import com.helpfulapps.alarmclock.service.AlarmService
import kotlinx.android.synthetic.main.activity_alarm_nfc.*
import kotlinx.android.synthetic.main.activity_ringing_alarm.*


class NfcRingingAlarmActivity : BaseRingingAlarmActivity<ActivityAlarmNfcBinding>() {

    override val layoutId: Int = R.layout.activity_alarm_nfc

    private val nfcAdapter: NfcAdapter by lazy {
        NfcAdapter.getDefaultAdapter(this)
    }

    private val nfcPendingIntent: PendingIntent by lazy {
        Intent(this, AlarmService::class.java).let {
            it.action = "STOP"
            PendingIntent.getService(this, 0, it, 0)
        }
    }

    // TODO maybe use this receiver to call stopAlarm() ????
    private val closeReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            finish()
        }
    }

    override fun init() {
        super.init()
        binding.model = viewModel
        registerReceiver(
            closeReceiver,
            IntentFilter("com.helpfulapps.alarmclock.views.ringing_alarm.close")
        )
    }

    override fun listenToAlarmSnooze() {
        fab_ring_nfc_snooze.setOnClickListener {
            snoozeAlarm()
        }
    }

    override fun listenToAlarmStop() {}

    override fun onResume() {
        super.onResume()
        enableForegroundMode()
    }

    private fun enableForegroundMode() {
        val tagDetected =
            IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
        val writeTagFilters: Array<IntentFilter> = arrayOf(tagDetected)
        nfcAdapter.enableForegroundDispatch(this, nfcPendingIntent, writeTagFilters, null)
    }

    override fun onPause() {
        disableForegroundMode()
        super.onPause()
    }

    private fun disableForegroundMode() {
        nfcAdapter.disableForegroundDispatch(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(closeReceiver)
    }

    override fun showMessage(text: String) {
        Snackbar.make(cl_ringing_base, text, Snackbar.LENGTH_SHORT).show()
    }

}