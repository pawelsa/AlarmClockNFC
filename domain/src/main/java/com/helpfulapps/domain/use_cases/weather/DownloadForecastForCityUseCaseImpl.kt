package com.helpfulapps.domain.use_cases.weather

import com.helpfulapps.domain.repository.WeatherRepository
import com.helpfulapps.domain.use_cases.type.CompletableUseCaseWithParameter
import io.reactivex.Completable

interface DownloadForecastForCityUseCase :
    CompletableUseCaseWithParameter<DownloadForecastForCityUseCase.Params> {

    data class Params(
        val cityName: String
    )
}

class DownloadForecastForCityUseCaseImpl(private val _repository: WeatherRepository) :
    DownloadForecastForCityUseCase {

    override fun invoke(parameter: DownloadForecastForCityUseCase.Params): Completable =
        _repository.downloadForecast(parameter.cityName)

}