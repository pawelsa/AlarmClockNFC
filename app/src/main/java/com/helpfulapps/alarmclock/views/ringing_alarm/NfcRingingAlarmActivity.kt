package com.helpfulapps.alarmclock.views.ringing_alarm

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.provider.Settings
import com.google.android.material.snackbar.Snackbar
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.ActivityAlarmNfcBinding
import com.helpfulapps.alarmclock.helpers.layout_helpers.buildEnableNfcAlarmDialog
import kotlinx.android.synthetic.main.activity_alarm_nfc.*


class NfcRingingAlarmActivity : BaseRingingAlarmActivity<ActivityAlarmNfcBinding>() {

    override val layoutId: Int = R.layout.activity_alarm_nfc

    private val nfcAdapter: NfcAdapter by lazy {
        NfcAdapter.getDefaultAdapter(this)
    }

    private val nfcStopIntent: PendingIntent by lazy {
        Intent(STOP_ALARM_INTENT).let {
            PendingIntent.getBroadcast(this, 0, it, 0)
        }
    }

    private val stopAlarmReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            stopAlarm()
        }
    }

    override fun init() {
        super.init()
        binding.model = viewModel
        registerReceiver(
            stopAlarmReceiver,
            IntentFilter(STOP_ALARM_INTENT)
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
        checkIfNfcIsTurnedOn()
    }

    private fun checkIfNfcIsTurnedOn() {
        if (!nfcAdapter.isEnabled) {
            buildEnableNfcAlarmDialog(this) {
                startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
            }.show()
        }
    }

    private fun enableForegroundMode() {
        val tagDetected = IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
        val nfcTagFilters: Array<IntentFilter> = arrayOf(tagDetected)
        nfcAdapter.enableForegroundDispatch(this, nfcStopIntent, nfcTagFilters, null)
    }

    override fun onPause() {
        disableForegroundMode()
        super.onPause()
    }

    private fun disableForegroundMode() {
        nfcAdapter.disableForegroundDispatch(this)
    }

    override fun showMessage(text: String) {
        Snackbar.make(cl_ring_nfc_base, text, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(stopAlarmReceiver)
    }

}