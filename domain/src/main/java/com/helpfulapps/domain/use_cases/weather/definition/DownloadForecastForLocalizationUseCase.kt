package com.helpfulapps.domain.use_cases.weather.definition

import com.helpfulapps.domain.use_cases.type.CompletableUseCaseWithParameter

interface DownloadForecastForLocalizationUseCase :
    CompletableUseCaseWithParameter<DownloadForecastForLocalizationUseCase.Params> {

    data class Params(
        val lat: Long,
        val lon: Long
    )
}