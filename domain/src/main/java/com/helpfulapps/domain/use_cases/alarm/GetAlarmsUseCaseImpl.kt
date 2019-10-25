package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.models.alarm.WeatherAlarm
import com.helpfulapps.domain.models.weather.DayWeather
import com.helpfulapps.domain.repository.AlarmRepository
import com.helpfulapps.domain.repository.WeatherRepository
import com.helpfulapps.domain.use_cases.type.SingleUseCase
import io.reactivex.Single
import io.reactivex.functions.BiFunction


interface GetAlarmsUseCase : SingleUseCase<List<WeatherAlarm>>

class GetAlarmsUseCaseImpl(
    private val _alarmRepository: AlarmRepository,
    private val _weatherRepository: WeatherRepository
) : GetAlarmsUseCase {

    override fun invoke(): Single<List<WeatherAlarm>> =
        Single.zip(
            _alarmRepository.getAlarms(),
            _weatherRepository.getForecastForAlarms(),
            BiFunction { alarmList, dayWeatherList ->
                val weatherAlarmList = arrayListOf<WeatherAlarm>()

                alarmList.forEach { alarm ->
                    weatherAlarmList.add(
                        WeatherAlarm(
                            alarm,
                            dayWeatherList.find { alarm.equals(it) } ?: DayWeather()
                        )
                    )
                }
                weatherAlarmList
            })
}