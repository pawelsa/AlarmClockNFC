package com.helpfulapps.domain.use_cases.weather

import com.helpfulapps.domain.models.weather.Forecast
import com.helpfulapps.domain.repository.WeatherRepository
import com.helpfulapps.domain.use_cases.type.SingleUseCaseWithParameter
import io.reactivex.Single

class GetForecastForCityUseCase(val repository: WeatherRepository) : SingleUseCaseWithParameter<GetForecastForCityUseCase.Param, Forecast> {


    override fun execute(parameter: Param): Single<Forecast> =
        repository.getForecast(parameter.cityName, parameter.time)


    data class Param(
        val cityName : String,
        val time : Long
    )
}