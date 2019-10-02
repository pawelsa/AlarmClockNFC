package com.helpfulapps.domain.use_cases.weather

import com.helpfulapps.domain.models.weather.DayWeather
import com.helpfulapps.domain.repository.WeatherRepository
import com.helpfulapps.domain.use_cases.weather.definition.GetForecastForAlarmUseCase
import io.reactivex.Single

class GetForecastForAlarmUseCaseImpl(private val _repository: WeatherRepository) :
    GetForecastForAlarmUseCase {

    override fun invoke(parameter: GetForecastForAlarmUseCase.Params): Single<DayWeather> =
        _repository.getForecastForAlarm(parameter.timestamp)
}