package com.helpfulapps.domain.use_cases.weather

import com.helpfulapps.domain.models.weather.DayWeather
import com.helpfulapps.domain.repository.WeatherRepository
import com.helpfulapps.domain.use_cases.weather.definition.GetForecastForAlarmsUseCase
import io.reactivex.Single

class GetForecastForAlarmsUseCaseImpl(private val _repository: WeatherRepository) :
    GetForecastForAlarmsUseCase {

    override fun invoke(): Single<List<DayWeather>> = _repository.getForecastForAlarms()
}