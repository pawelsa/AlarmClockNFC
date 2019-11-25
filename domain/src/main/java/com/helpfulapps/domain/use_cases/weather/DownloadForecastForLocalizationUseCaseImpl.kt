package com.helpfulapps.domain.use_cases.weather

import com.helpfulapps.domain.repository.WeatherRepository
import com.helpfulapps.domain.use_cases.type.CompletableUseCaseWithParameter
import io.reactivex.Completable

interface DownloadForecastForLocalizationUseCase :
    CompletableUseCaseWithParameter<DownloadForecastForLocalizationUseCase.Params> {

    data class Params(
        val lat: Double,
        val lon: Double
    )
}

class DownloadForecastForLocalizationUseCaseImpl(private val _repository: WeatherRepository) :
    DownloadForecastForLocalizationUseCase {

    override fun invoke(parameter: DownloadForecastForLocalizationUseCase.Params): Completable =
        _repository.downloadForecast(parameter.lat, parameter.lon)

}