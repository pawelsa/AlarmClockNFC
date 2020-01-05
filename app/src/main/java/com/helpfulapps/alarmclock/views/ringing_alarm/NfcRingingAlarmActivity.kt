package com.helpfulapps.alarmclock.views.ringing_alarm

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.provider.Settings
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.ActivityRingingAlarmBinding
import com.helpfulapps.alarmclock.helpers.extensions.observe
import com.helpfulapps.alarmclock.helpers.layout_helpers.buildEnableNfcAlarmDialog
import com.helpfulapps.base.helpers.whenNotNull
import kotlinx.android.synthetic.main.activity_alarm_nfc.*


class NfcRingingAlarmActivity : BaseRingingAlarmActivity<ActivityRingingAlarmBinding>() {

    override val layoutId: Int = R.layout.activity_ringing_alarm
    private val adapter: WeatherInfoAdapter by lazy {
        WeatherInfoAdapter()
    }

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
        binding.fabRingEnd.hide()
        binding.model = viewModel
        registerReceiver(
            stopAlarmReceiver,
            IntentFilter(STOP_ALARM_INTENT)
        )
        setupAdapter()
        subscribeWeatherData()
    }

    private fun subscribeWeatherData() {
        viewModel.weatherInfoDatas.observe(this) {
            val weatherInfoList = arrayListOf<Pair<String, String>>()

            with(it) {
                whenNotNull(currentTemperature) { temperature ->
                    weatherInfoList.add(getString(R.string.ringing_current_temperature) to temperature.toString())
                }
                whenNotNull(laterTemperature) { temperature ->
                    weatherInfoList.add(getString(R.string.ringing_later_temperature) to temperature.toString())
                }
                whenNotNull(averageRain) { rain ->
                    weatherInfoList.add(getString(R.string.ringing_rain) to rain.toString())
                }
                whenNotNull(averageSnow) { snow ->
                    weatherInfoList.add(getString(R.string.ringing_snow) to snow.toString())
                }
                whenNotNull(averageWind) { wind ->
                    weatherInfoList.add(getString(R.string.ringing_wind) to wind.toString())
                }
            }
            adapter.submitList(weatherInfoList)
        }
    }

    private fun setupAdapter() {
        binding.rvRingingWeatherInfo.layoutManager = LinearLayoutManager(this)
        binding.rvRingingWeatherInfo.adapter = adapter
    }

    override fun listenToAlarmSnooze() {
        binding.fabRingSnooze.setOnClickListener {
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