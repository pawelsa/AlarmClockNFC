package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.models.alarm.Alarm
import com.helpfulapps.domain.repository.AlarmRepository
import com.helpfulapps.domain.repository.WeatherRepository
import com.helpfulapps.domain.use_cases.alarm.definition.GetAlarmsUseCase
import io.reactivex.Single

//TODO write it complete - it should get the most important data
class GetAlarmsUseCaseImpl(
    private val alarmRepository: AlarmRepository,
    private val weatherRepository: WeatherRepository
) : GetAlarmsUseCase {

    override fun invoke(): Single<List<Alarm>> =
        alarmRepository.getAlarms()//.zipWith(weatherRepository.getForecastForAlarms())
}