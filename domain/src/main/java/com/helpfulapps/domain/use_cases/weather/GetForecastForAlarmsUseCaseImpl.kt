package com.helpfulapps.domain.use_cases.weather

import com.helpfulapps.domain.models.weather.DayWeather
import com.helpfulapps.domain.repository.WeatherRepository
import com.helpfulapps.domain.use_cases.type.SingleUseCase
import io.reactivex.Single

interface GetForecastForAlarmsUseCase : SingleUseCase<List<DayWeather>>

class GetForecastForAlarmsUseCaseImpl(private val repository: WeatherRepository) :
    GetForecastForAlarmsUseCase {

    override fun invoke(): Single<List<DayWeather>> = repository.getForecastForAlarms()
}