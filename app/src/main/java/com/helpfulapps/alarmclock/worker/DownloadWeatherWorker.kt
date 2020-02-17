package com.helpfulapps.alarmclock.worker

import android.content.Context
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.helpfulapps.domain.exceptions.WorkException
import com.helpfulapps.domain.helpers.Settings
import com.helpfulapps.domain.use_cases.weather.DownloadForecastForCityUseCase
import com.helpfulapps.domain.use_cases.weather.DownloadForecastForLocalizationUseCase
import io.reactivex.Completable
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

        val location = getLocation()
        saveLocation(location)

        return when {
            location.isLocalizationValid -> prepareDownloaderForLocation(location)
            location.isCityNameValid -> prepareDownloaderForCityName(location)
            else -> Completable.error(WorkException())
        }
            .handleResultAndException()
    }

    private fun getLocation(): Location {
        val location = Location(
            getCityName(),
            inputData.getDouble(KEY_LONGITUDE, 0.0),
            inputData.getDouble(KEY_LATITUDE, 0.0)
        )
        if (location.isNotValid) {
            if (settings.city != "-1") {
                return location.copy(cityName = settings.city)
            } else if (settings.longitude != 0.0f && settings.latitude != 0.0f) {
                return location.copy(
                    longitude = settings.longitude.toDouble(),
                    latitude = settings.latitude.toDouble()
                )
            }
        }
        return location
    }

    private fun saveLocation(location: Location) {
        if (location.isCityNameValid) {
            settings.city = location.cityName
        }
        if (location.isLocalizationValid) {
            settings.latitude = location.latitude.toFloat()
            settings.longitude = location.longitude.toFloat()
        }
    }

    private fun prepareDownloaderForCityName(location: Location) =
        downloadForecastForCityUseCase(DownloadForecastForCityUseCase.Params(location.cityName))

    private fun prepareDownloaderForLocation(location: Location): Completable {
        return downloadForecastForLocalizationUseCase(
            DownloadForecastForLocalizationUseCase.Params(
                lat = location.latitude,
                lon = location.longitude
            )
        )
    }

    private fun Completable.handleResultAndException(): Single<Result> {
        return this
            .toSingleDefault(Result.success())
            .onErrorReturn {
                return@onErrorReturn Result.retry()
            }
    }

    private fun getCityName(): String {
        val cityName = inputData.getString(KEY_CITY_NAME) ?: settings.city
        settings.city = cityName
        return cityName
    }

    private data class Location(
        val cityName: String = "-1",
        val longitude: Double = 0.0,
        val latitude: Double = 0.0
    ) {
        val isLocalizationValid: Boolean = longitude != 0.0 && latitude != 0.0
        val isCityNameValid: Boolean = cityName != "-1"
        val isNotValid: Boolean = !isLocalizationValid && !isCityNameValid
    }

    companion object {
        const val KEY_CITY_NAME = "com.helpfulapps.alarmclock.city_name"
        const val KEY_LATITUDE = "com.helpfulapps.alarmclock.latitude"
        const val KEY_LONGITUDE = "com.helpfulapps.alarmclock.longitude"
    }

}