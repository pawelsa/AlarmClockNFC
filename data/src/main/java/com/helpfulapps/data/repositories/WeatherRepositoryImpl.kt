package com.helpfulapps.data.repositories

import android.content.Context
import com.helpfulapps.data.api.weather.api.ApiCalls
import com.helpfulapps.data.api.weather.converter.analyzeWeather
import com.helpfulapps.data.api.weather.exceptions.CouldNotObtainForecast
import com.helpfulapps.data.api.weather.exceptions.WeatherException
import com.helpfulapps.data.api.weather.model.ForecastForCity
import com.helpfulapps.data.helper.NetworkCheck
import com.helpfulapps.data.helper.Settings
import com.helpfulapps.data.db.weather.model.*
import com.helpfulapps.data.extensions.checkCompleted
import com.helpfulapps.data.extensions.dayOfMonth
import com.helpfulapps.data.extensions.rxQueryListSingle
import com.helpfulapps.data.extensions.timestampAtMidnight
import com.helpfulapps.domain.repository.WeatherRepository
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.kotlinextensions.*
import com.raizlabs.android.dbflow.rx2.kotlinextensions.rx
import com.raizlabs.android.dbflow.sql.language.Delete
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import retrofit2.Response
import java.util.concurrent.TimeUnit
import com.helpfulapps.domain.models.weather.DayWeather as DomainDayWeather

class WeatherRepositoryImpl(
    private val settings: Settings,
    private val networkCheck: NetworkCheck,
    private val apiCalls: ApiCalls,
    context: Context
) : WeatherRepository {

    init {
        FlowManager.init(context)
    }

    companion object {
        const val ONE_AND_HALF_AN_HOUR: Long = 15 * 3600 * 100
    }

    override fun downloadForecast(city: String): Completable =
        networkCheck.isConnectedToNetwork
            .flatMap { apiCalls.downloadForecast(city, settings.units.unit) }
            .convertModelsAndSaveInDb()

    override fun downloadForecast(lat: Long, lon: Long): Completable =
        networkCheck.isConnectedToNetwork
            .flatMap {
                apiCalls.downloadForecastForCoordinates(
                    lat.toString(),
                    lon.toString(),
                    settings.units.unit
                )
            }
            .convertModelsAndSaveInDb()

    override fun getForecastForAlarms(): Single<List<DomainDayWeather>> =
        getDayWeatherList()
            .map(DayWeather::toDomain)
            .toList()
            .timeout(2L, TimeUnit.SECONDS) { observer -> observer.onSuccess(emptyList()) }

    override fun getForecastForAlarm(time: Long): Single<DomainDayWeather> =
        getDayWeatherForTime(time)
            .timeout(2L, TimeUnit.SECONDS) { observer -> observer.onSuccess(DayWeather(id = -1)) }
            .map(DayWeather::toDomain)

    fun getDayWeatherList() = (select from DayWeather::class).rx().queryStreamResults()

    fun getDayWeatherForTime(time: Long) =
        (select from DayWeather::class where (DayWeather_Table.dt lessThanOrEq time + ONE_AND_HALF_AN_HOUR) and (DayWeather_Table.dt greaterThan time - ONE_AND_HALF_AN_HOUR))
            .rxQueryListSingle()

    private fun Maybe<Response<ForecastForCity>>.convertModelsAndSaveInDb() =
        this.clearTables()
            .getResponseBody()
            .transformResponseIntoDays()
            .analyzeWeather(settings.units)
            .saveInDatabase()
            .flatMapCompletable { savedInDb ->
                savedInDb.checkCompleted(WeatherException("Couldn't save forecast in database"))
            }
            .observeOn(AndroidSchedulers.mainThread())

    private fun Maybe<Response<ForecastForCity>>.clearTables() =
        this.doOnSuccess {
            Delete.table(DayWeather::class.java)
            Delete.table(HourWeather::class.java)
        }

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

    private fun Single<List<DayWeather>>.saveInDatabase() =
        this.flatMapObservable { list -> Observable.fromIterable(list) }
            .flatMap { dayWeatherList -> dayWeatherList.save().toObservable() }


    private fun Single<ForecastForCity>.transformResponseIntoDays(): Single<List<DayWeather>> =
        this.map {
            it.list
                .map { forecast -> HourWeather(forecast).apply { dt *= 1000 } }
                .groupBy { hourWeather -> hourWeather.dt.dayOfMonth() }
                .map { hourWeather ->
                    DayWeather(
                        dt = hourWeather.value.first().dt.timestampAtMidnight(),
                        hourWeatherList = hourWeather.value
                    )
                }
        }

}