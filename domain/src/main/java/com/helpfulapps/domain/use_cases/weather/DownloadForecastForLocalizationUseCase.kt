package com.helpfulapps.domain.use_cases.weather

import com.helpfulapps.domain.repository.WeatherRepository
import com.helpfulapps.domain.use_cases.type.CompletableUseCaseWithParameter
import io.reactivex.Completable

class DownloadForecastForLocalizationUseCase(val repository: WeatherRepository) :
    CompletableUseCaseWithParameter<DownloadForecastForLocalizationUseCase.Param> {

    override fun execute(parameter: Param): Completable =
        repository.downloadForecast(parameter.lat, parameter.lon, parameter.time)

    data class Param(
        val lat: Long,
        val lon: Long,
        val time: Long
    )
}