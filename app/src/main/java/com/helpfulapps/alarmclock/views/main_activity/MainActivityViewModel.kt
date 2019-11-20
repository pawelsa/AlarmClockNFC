package com.helpfulapps.alarmclock.views.main_activity

import android.util.Log
import com.helpfulapps.base.base.BaseViewModel
import com.helpfulapps.base.extensions.rx.backgroundTask
import com.helpfulapps.domain.use_cases.weather.DownloadForecastForCityUseCase
import io.reactivex.rxkotlin.plusAssign

class MainActivityViewModel(
    private val downloadForecastForCityUseCase: DownloadForecastForCityUseCase
) : BaseViewModel() {

    private val TAG = this.javaClass.simpleName

    fun downloadForeacast() {
        disposables += downloadForecastForCityUseCase(DownloadForecastForCityUseCase.Params("Pszczyna"))
            .backgroundTask()
            .subscribe {
                Log.d(TAG, "Completed downloading forecast")
            }
    }

}