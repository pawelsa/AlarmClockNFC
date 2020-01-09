package com.helpfulapps.alarmclock.views.ringing_alarm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.helpfulapps.base.base.BaseViewModel
import com.helpfulapps.base.extensions.rx.backgroundTask
import com.helpfulapps.domain.models.alarm.WeatherAlarm
import com.helpfulapps.domain.models.weather.DayWeather
import com.helpfulapps.domain.models.weather.Rain
import com.helpfulapps.domain.models.weather.Snow
import com.helpfulapps.domain.models.weather.Wind
import com.helpfulapps.domain.use_cases.alarm.GetAlarmUseCase
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import java.util.*
import kotlin.math.abs

class RingingAlarmViewModel(
    private val getAlarmUseCase: GetAlarmUseCase
) : BaseViewModel() {

    private val _weatherAlarm: MutableLiveData<WeatherAlarm> = MutableLiveData()
    val weatherAlarm: LiveData<WeatherAlarm>
        get() = _weatherAlarm

    private val _weatherInfoDatas: MutableLiveData<WeatherData> = MutableLiveData()
    val weatherInfoDatas: LiveData<WeatherData>
        get() = _weatherInfoDatas

    private var alarmId: Long = -1

    fun getAlarm(alarmId: Long) {
        this.alarmId = alarmId
        disposables += getAlarmUseCase(GetAlarmUseCase.Params(alarmId))
            .map {
                val weatherData = extractWeatherData(it.dayWeather)
                return@map it to weatherData
            }
            .backgroundTask()
            .subscribeBy(
                onSuccess = {
                    Log.d(TAG, "${it.first.alarm.hour}:${it.first.alarm.minute}")
                    _weatherAlarm.value = it.first
                    _weatherInfoDatas.value = it.second
                },
                onError = {
                    it.printStackTrace()
                }
            )
    }

    private fun extractWeatherData(dayWeather: DayWeather): WeatherData {
        val currentTime = GregorianCalendar.getInstance().timeInMillis
        val afternoonTime =
            GregorianCalendar.getInstance().apply { set(Calendar.HOUR_OF_DAY, 15) }
                .timeInMillis
        val withinOneAndHalfHour: (Long) -> Boolean =
            { time ->
                abs(time - currentTime) <= ONE_AND_HALF_HOUR_MILLIS
            }

        val weatherData = WeatherData()
        val weatherInfo = dayWeather.weatherInfo
        val hourWeatherList = dayWeather.hourWeatherList

        if (hourWeatherList.isEmpty()) return weatherData

        weatherData.currentTemperature =
            hourWeatherList.find { hourWeather -> withinOneAndHalfHour(hourWeather.dt) }
                ?.temp

        if (weatherData.currentTemperature == null) {
            weatherData.currentTemperature = hourWeatherList[0].temp
        }

        if (withinOneAndHalfHour(afternoonTime)) {
            weatherData.laterTemperature =
                hourWeatherList.find { withinOneAndHalfHour(afternoonTime) }?.temp
        }

        if (weatherInfo.rain != Rain.NO_RAIN && weatherInfo.rain != Rain.NO_DATA) {
            weatherData.averageRain =
                (hourWeatherList.sumByDouble { hourWeather -> hourWeather.rain }) / hourWeatherList.size
        }

        if (weatherInfo.snow != Snow.NORMAL && weatherInfo.snow != Snow.NO_DATA) {
            weatherData.averageSnow =
                (hourWeatherList.sumByDouble { hourWeather -> hourWeather.snow }) / hourWeatherList.size
        }

        if (weatherInfo.wind != Wind.NO_DATA && weatherInfo.wind != Wind.NORMAL) {
            weatherData.averageWind =
                (hourWeatherList.sumByDouble { hourWeather -> hourWeather.wind }) / hourWeatherList.size
        }
        return weatherData
    }


    companion object {
        private const val ONE_AND_HALF_HOUR_MILLIS = 90 * 60 * 1000
    }
}