package com.helpfulapps.domain.use_cases.weather

import com.helpfulapps.domain.models.weather.Weather
import com.helpfulapps.domain.repository.WeatherRepository
import com.helpfulapps.domain.use_cases.weather.definition.GetForecastForAlarmUseCase
import io.reactivex.Single

class GetForecastForAlarmUseCaseImpl(val repository: WeatherRepository) :
    GetForecastForAlarmUseCase {

    override fun invoke(parameter: GetForecastForAlarmUseCase.Params): Single<Weather> =
        repository.getForecastForAlarm(parameter.timestamp)
}