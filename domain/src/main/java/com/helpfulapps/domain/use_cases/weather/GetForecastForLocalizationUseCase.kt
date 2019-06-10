package com.helpfulapps.domain.use_cases.weather

import com.helpfulapps.domain.models.weather.Forecast
import com.helpfulapps.domain.repository.WeatherRepository
import com.helpfulapps.domain.use_cases.type.SingleUseCaseWithParameter
import io.reactivex.Single

class GetForecastForLocalizationUseCase(val repository: WeatherRepository) : SingleUseCaseWithParameter<GetForecastForLocalizationUseCase.Param, Forecast> {

    override fun execute(parameter: Param): Single<Forecast> = repository.getForecast(parameter.lat, parameter.lon, parameter.time)

    data class Param(
        val lat : Long,
        val lon : Long,
        val time : Long
    )
}