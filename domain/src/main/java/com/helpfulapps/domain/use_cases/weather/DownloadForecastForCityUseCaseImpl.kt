package com.helpfulapps.domain.use_cases.weather

import com.helpfulapps.domain.repository.WeatherRepository
import com.helpfulapps.domain.use_cases.weather.definition.DownloadForecastForCityUseCase
import io.reactivex.Completable

class DownloadForecastForCityUseCaseImpl(val repository: WeatherRepository) :
    DownloadForecastForCityUseCase {

    override fun invoke(parameter: DownloadForecastForCityUseCase.Params): Completable =
        repository.downloadForecast(parameter.cityName)

}