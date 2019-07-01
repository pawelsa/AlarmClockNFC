package com.helpfulapps.domain.use_cases.weather.definition

import com.helpfulapps.domain.use_cases.type.CompletableUseCaseWithParameter

interface DownloadForecastForCityUseCase :
    CompletableUseCaseWithParameter<DownloadForecastForCityUseCase.Params> {

    data class Params(
        val cityName: String
    )
}