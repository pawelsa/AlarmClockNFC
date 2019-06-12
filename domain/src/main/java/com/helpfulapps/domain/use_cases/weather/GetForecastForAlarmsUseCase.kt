package com.helpfulapps.domain.use_cases.weather

import com.helpfulapps.domain.models.weather.Forecast
import com.helpfulapps.domain.repository.WeatherRepository
import com.helpfulapps.domain.use_cases.type.SingleUseCase
import io.reactivex.Single

class GetForecastForAlarmsUseCase(val repository: WeatherRepository) : SingleUseCase<List<Forecast>> {

    override fun execute(): Single<List<Forecast>> = repository.getForecastForAlarms()
}