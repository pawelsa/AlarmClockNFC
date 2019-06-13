package com.helpfulapps.domain.use_cases.weather

import com.helpfulapps.domain.models.weather.Weather
import com.helpfulapps.domain.repository.WeatherRepository
import com.helpfulapps.domain.use_cases.type.SingleUseCaseWithParameter
import io.reactivex.Single

class GetForecastForAlarmUseCase(val repository: WeatherRepository) :
    SingleUseCaseWithParameter<Long, Weather> {

    override fun execute(parameter: Long): Single<Weather> =
        repository.getForecastForAlarm(parameter)
}