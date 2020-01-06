package com.helpfulapps.alarmclock.views.ringing_alarm

import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.ActivityRingingAlarmBinding
import com.helpfulapps.alarmclock.helpers.extensions.observe
import com.helpfulapps.alarmclock.helpers.extensions.round
import com.helpfulapps.base.helpers.whenNotNull
import com.helpfulapps.domain.helpers.Settings
import kotlinx.android.synthetic.main.activity_ringing_alarm.*
import org.koin.android.ext.android.inject

class RingingAlarmActivity : BaseRingingAlarmActivity<ActivityRingingAlarmBinding>() {

    override val layoutId: Int = R.layout.activity_ringing_alarm
    private val settings: Settings by inject()
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
                    weatherInfoList.add(
                        getString(R.string.ringing_current_temperature) to "$temperature ${if (settings.units == Settings.Units.METRIC) getString(
                            R.string.weather_celcius
                        ) else getString(R.string.weather_fahrenheit)}"
                    )
                }
                whenNotNull(laterTemperature) { temperature ->
                    weatherInfoList.add(
                        getString(R.string.ringing_later_temperature) to "${temperature.round(
                            2
                        )} ${if (settings.units == Settings.Units.METRIC) getString(R.string.weather_celcius) else getString(
                            R.string.weather_fahrenheit
                        )}"
                    )
                }
                whenNotNull(averageRain) { rain ->
                    weatherInfoList.add(
                        getString(R.string.ringing_rain) to "${rain.round(2)} ${getString(
                            R.string.weather_rain_metric
                        )}"
                    )
                }
                whenNotNull(averageSnow) { snow ->
                    weatherInfoList.add(
                        getString(R.string.ringing_snow) to "${snow.round(2)} ${getString(
                            R.string.weather_rain_metric
                        )}"
                    )
                }
                whenNotNull(averageWind) { wind ->
                    weatherInfoList.add(
                        getString(R.string.ringing_wind) to "${wind.round(2)} ${if (settings.units == Settings.Units.METRIC) getString(
                            R.string.weather_wind_metric
                        ) else getString(R.string.weather_wind_imperial)}"
                    )
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