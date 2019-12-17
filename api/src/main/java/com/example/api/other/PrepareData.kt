package com.example.api.other

import android.content.Context
import com.example.api.api.ApiCalls
import com.example.api.api.Downloader
import com.example.api.models.ForecastForCity
import com.example.api.other.converter.analyzeWeather
import com.helpfulapps.data.api.PrepareData
import com.helpfulapps.data.db.weather.model.DayWeatherData
import com.helpfulapps.data.extensions.dayOfMonth
import com.helpfulapps.data.extensions.timestampAtMidnight
import com.helpfulapps.data.helper.NetworkCheck
import com.helpfulapps.domain.exceptions.CouldNotObtainForecast
import com.helpfulapps.domain.helpers.Settings
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.SingleEmitter
import retrofit2.Response

class PrepareDataImpl(
    context: Context,
    private val networkCheck: NetworkCheck = NetworkCheck(context),
    private val apiCalls: ApiCalls = Downloader.create(),
    private val settings: Settings
) : PrepareData {

    override fun downloadForecast(cityName: String): Single<List<DayWeatherData>> {
        return networkCheck.isConnectedToNetwork
            .flatMap { apiCalls.downloadForecast(cityName, settings.units.unit) }
            .convertModelsAndSaveInDb()
    }

    override fun downloadForecastForCoordinates(
        lat: Double,
        lon: Double
    ): Single<List<DayWeatherData>> {
        return networkCheck.isConnectedToNetwork
            .flatMap {
                apiCalls.downloadForecastForCoordinates(
                    lat.toString(),
                    lon.toString(),
                    settings.units.unit
                )
            }
            .convertModelsAndSaveInDb()
    }

    private fun Maybe<Response<ForecastForCity>>.convertModelsAndSaveInDb() =
        this.getResponseBody()
            .transformResponseIntoDays()
            .analyzeWeather(settings.units)

    private fun Maybe<Response<ForecastForCity>>.getResponseBody() =
        this.flatMapSingle { response ->
            when (response.isSuccessful) {
                true -> Single.create { emitter: SingleEmitter<ForecastForCity> ->
                    println(response.raw().request())
                    emitter.onSuccess(response.body()!!)
                }
                else -> Single.error(CouldNotObtainForecast())
            }
        }


    private fun Single<ForecastForCity>.transformResponseIntoDays(): Single<List<DayWeatherData>> =
        this.map {
            settings.city = it.city.name
            it.list
                .map { forecast -> forecast.toDbModel().apply { dt *= 1000 } }
                .groupBy { hourWeather -> hourWeather.dt.dayOfMonth() }
                .map { hourWeather ->
                    DayWeatherData(
                        dt = hourWeather.value.first().dt.timestampAtMidnight(),
                        cityName = it.city.name,
                        hourWeatherList = hourWeather.value
                    )
                }
        }
}