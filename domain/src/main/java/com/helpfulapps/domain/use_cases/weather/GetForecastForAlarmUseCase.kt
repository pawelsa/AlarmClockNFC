package com.helpfulapps.domain.use_cases.weather

import com.helpfulapps.domain.models.alarm.Alarm
import com.helpfulapps.domain.models.weather.Forecast
import com.helpfulapps.domain.repository.WeatherRepository
import com.helpfulapps.domain.use_cases.type.SingleUseCaseWithParameter
import io.reactivex.Single

class GetForecastForAlarmUseCase(val repository: WeatherRepository) :
    SingleUseCaseWithParameter<Alarm, Forecast> {

    override fun execute(parameter: Alarm): Single<Forecast> =
        repository.getForecastForAlarm(parameter)
}