package com.helpfulapps.domain.use_cases.weather

import com.helpfulapps.domain.repository.WeatherRepository
import com.helpfulapps.domain.use_cases.weather.definition.DownloadForecastForCityUseCase
import io.reactivex.Completable

class DownloadForecastForCityUseCaseImpl(private val _repository: WeatherRepository) :
    DownloadForecastForCityUseCase {

    override fun invoke(parameter: DownloadForecastForCityUseCase.Params): Completable =
        _repository.downloadForecast(parameter.cityName)

}