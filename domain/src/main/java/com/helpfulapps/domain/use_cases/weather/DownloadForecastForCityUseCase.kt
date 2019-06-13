package com.helpfulapps.domain.use_cases.weather

import com.helpfulapps.domain.repository.WeatherRepository
import com.helpfulapps.domain.use_cases.type.CompletableUseCaseWithParameter
import io.reactivex.Completable

class DownloadForecastForCityUseCase(val repository: WeatherRepository) :
    CompletableUseCaseWithParameter<DownloadForecastForCityUseCase.Param> {

    override fun execute(parameter: Param): Completable =
        repository.downloadForecast(parameter.cityName)


    data class Param(
        val cityName: String
    )
}