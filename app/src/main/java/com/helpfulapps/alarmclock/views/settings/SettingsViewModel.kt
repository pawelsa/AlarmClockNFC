package com.helpfulapps.alarmclock.views.settings

import com.helpfulapps.base.base.BaseViewModel
import com.helpfulapps.base.extensions.rx.backgroundTask
import com.helpfulapps.domain.use_cases.weather.DownloadForecastForCityUseCase
import io.reactivex.rxkotlin.plusAssign

class SettingsViewModel(
    private val downloadForecastForCityUseCase: DownloadForecastForCityUseCase
) : BaseViewModel() {

    fun downloadForecast(city: String) {
        disposables += downloadForecastForCityUseCase(DownloadForecastForCityUseCase.Params(city))
            .backgroundTask()
            .subscribe {

            }
    }

}