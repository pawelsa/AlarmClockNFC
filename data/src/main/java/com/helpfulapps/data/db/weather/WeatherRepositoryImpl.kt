package com.helpfulapps.data.db.weather

import com.helpfulapps.data.db.common.Settings
import com.helpfulapps.domain.repository.WeatherRepository
import io.reactivex.Single
import com.helpfulapps.domain.models.weather.Forecast as ForecastModel

class WeatherRepositoryImpl(private val settings: Settings) : WeatherRepository {
    override fun getForecast(
        city: String,
        time: Long
    ): Single<com.helpfulapps.domain.models.weather.Forecast> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getForecast(
        lat: Long,
        lon: Long,
        time: Long
    ): Single<com.helpfulapps.domain.models.weather.Forecast> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /*
    // todo implement using forecastFromDatabase
    override fun getForecast(city: String, time: Long): Single<ForecastModel> =
        Downloader.create()
            .getForecast(city, settings.units, API_KEY)
            .toForecastAtTime(time)


    override fun getForecast(lat: Long, lon: Long, time: Long): Single<ForecastModel> =
        Downloader.create()
            .getForecastForCoordinates(lat.toString(),lon.toString(), settings.units, API_KEY)
            .toForecastAtTime(time)



    private fun Maybe<Response<ForecastForCity>>.toForecastAtTime(time : Long) =
        this.flatMapSingle { apiResponse ->
            when (apiResponse.isSuccessful) {
                true -> Single.just(apiResponse.body()!!)
                else -> throw Throwable("Not succesfully downloaded")
            }
        }
            .map { forecastForCity ->
                val currentTime = System.currentTimeMillis()
                val lowerBorderForTime = currentTime - 90*3600*1000
                val higherBorderForTime = currentTime + 90*3600*1000
                var choosenForecast: Forecast? = null
                forecastForCity.list.forEach {
                    if (it.dt in lowerBorderForTime..higherBorderForTime) {
                        choosenForecast = it
                    }
                }
                choosenForecast ?: Throwable("some problem")
            }*/

}