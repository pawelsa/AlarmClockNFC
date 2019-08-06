package com.helpfulapps.domain.use_cases.weather

import com.helpfulapps.domain.repository.WeatherRepository
import com.helpfulapps.domain.use_cases.weather.definition.DownloadForecastForLocalizationUseCase
import io.reactivex.Completable

class DownloadForecastForLocalizationUseCaseImpl(private val _repository: WeatherRepository) :
    DownloadForecastForLocalizationUseCase {

    override fun invoke(parameter: DownloadForecastForLocalizationUseCase.Params): Completable =
        _repository.downloadForecast(parameter.lat, parameter.lon)

}