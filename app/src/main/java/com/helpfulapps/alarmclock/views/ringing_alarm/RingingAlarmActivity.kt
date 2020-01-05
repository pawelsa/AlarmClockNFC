package com.helpfulapps.alarmclock.views.ringing_alarm

import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.ActivityRingingAlarmBinding
import com.helpfulapps.alarmclock.helpers.extensions.observe
import com.helpfulapps.base.helpers.whenNotNull
import kotlinx.android.synthetic.main.activity_ringing_alarm.*

class RingingAlarmActivity : BaseRingingAlarmActivity<ActivityRingingAlarmBinding>() {

    override val layoutId: Int = R.layout.activity_ringing_alarm
    private val adapter: WeatherInfoAdapter by lazy {
        WeatherInfoAdapter()
    }

    override fun init() {
        super.init()
        binding.model = viewModel
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