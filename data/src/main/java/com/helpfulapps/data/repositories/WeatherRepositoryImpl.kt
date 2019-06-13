package com.helpfulapps.data.repositories

import android.content.Context
import com.helpfulapps.data.api.weather.api.Downloader
import com.helpfulapps.data.api.weather.exceptions.CouldNotObtainForecast
import com.helpfulapps.data.api.weather.exceptions.WeatherException
import com.helpfulapps.data.api.weather.model.ForecastForCity
import com.helpfulapps.data.common.NetworkCheck
import com.helpfulapps.data.common.Settings
import com.helpfulapps.data.db.extensions.backgroundTask
import com.helpfulapps.data.db.extensions.between
import com.helpfulapps.data.db.extensions.completed
import com.helpfulapps.data.db.extensions.rxQueryListSingle
import com.helpfulapps.data.db.weather.model.ForecastDbModel
import com.helpfulapps.data.db.weather.model.WeatherDbModel
import com.helpfulapps.data.db.weather.model.WeatherDbModel_Table
import com.helpfulapps.domain.models.weather.Forecast
import com.helpfulapps.domain.models.weather.Weather
import com.helpfulapps.domain.repository.WeatherRepository
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.kotlinextensions.where
import com.raizlabs.android.dbflow.sql.language.Delete
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import retrofit2.Response

class WeatherRepositoryImpl(private val settings: Settings, private val context: Context) :
    WeatherRepository {

    init {
        FlowManager.init(context)
    }

    override fun downloadForecast(city: String): Completable =
        NetworkCheck(context).isConnectedToNetwork
            .flatMap {
                Downloader.create()
                    .downloadForecast(city, settings.units)
            }
            .saveInDb()

    override fun downloadForecast(lat: Long, lon: Long): Completable =
        NetworkCheck(context).isConnectedToNetwork
            .flatMap {
                Downloader.create()
                    .downloadForecastForCoordinates(lat.toString(), lon.toString(), settings.units)
            }
            .saveInDb()

    override fun getForecastForAlarms(): Single<Forecast> =
        (select from ForecastDbModel::class)
            .rxQueryListSingle()
            .map(ForecastDbModel::toDomain)
            .backgroundTask()

    override fun getForecastForAlarm(time: Long): Single<Weather> =
        (select from WeatherDbModel::class where (WeatherDbModel_Table.timestamp between 90 * 3600 * 1000))
            .rxQueryListSingle()
            .map(WeatherDbModel::toDomain)
            .backgroundTask()


    private fun Maybe<Response<ForecastForCity>>.saveInDb() =
        this.flatMapSingle { response ->
            when (response.isSuccessful) {
                true -> Single.create { emitter: SingleEmitter<ForecastForCity> ->
                    println(response.raw().request())
                    Delete.table(WeatherDbModel::class.java)
                    emitter.onSuccess(response.body()!!)
                }
                else -> Single.error(CouldNotObtainForecast())
            }
        }
            .map(ForecastForCity::toDbModel)
            .flatMap(ForecastDbModel::save)
            .flatMapCompletable { saved ->
                saved.completed(WeatherException("Couldn't save forecast in database"))
            }
            .observeOn(AndroidSchedulers.mainThread())

}