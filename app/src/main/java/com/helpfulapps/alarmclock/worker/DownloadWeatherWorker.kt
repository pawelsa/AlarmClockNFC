package com.helpfulapps.alarmclock.worker

import android.content.Context
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.helpfulapps.domain.helpers.Settings
import com.helpfulapps.domain.helpers.singleOf
import com.helpfulapps.domain.use_cases.weather.DownloadForecastForCityUseCase
import com.helpfulapps.domain.use_cases.weather.DownloadForecastForLocalizationUseCase
import io.reactivex.Single
import org.koin.core.KoinComponent
import org.koin.core.inject

class DownloadWeatherWorker(
    context: Context,
    workerParameters: WorkerParameters
) : RxWorker(context, workerParameters), KoinComponent {

    private val downloadForecastForCityUseCase: DownloadForecastForCityUseCase by inject()
    private val downloadForecastForLocalizationUseCase: DownloadForecastForLocalizationUseCase by inject()
    private val settings: Settings by inject()

    override fun createWork(): Single<Result> {
        var cityName = inputData.getString(KEY_CITY_NAME)
        if (cityName == null) cityName = settings.city
        settings.city = cityName

        val longitude = inputData.getDouble(KEY_LONGITUDE, 0.0)
        val latitude = inputData.getDouble(KEY_LATITUDE, 0.0)

        return when {
            longitude != 0.0 && latitude != 0.0 -> {
                downloadForecastForLocalizationUseCase(
                    DownloadForecastForLocalizationUseCase.Params(
                        lat = latitude,
                        lon = longitude
                    )
                )
                    .toSingle {
                        Result.success()
                    }

            }
            cityName != "-1" -> {
                downloadForecastForCityUseCase(DownloadForecastForCityUseCase.Params(settings.city))
                    .toSingle {
                        Result.success()
                    }
            }
            else -> {
                singleOf { Result.success() }
            }
        }
    }

    companion object {
        const val KEY_CITY_NAME = "com.helpfulapps.alarmclock.city_name"
        const val KEY_LATITUDE = "com.helpfulapps.alarmclock.latitude"
        const val KEY_LONGITUDE = "com.helpfulapps.alarmclock.longitude"
    }

}