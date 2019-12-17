package com.helpfulapps.data.repositories

import android.content.Context
import com.helpfulapps.data.api.weather.api.ApiCalls
import com.helpfulapps.data.api.weather.api.Downloader
import com.helpfulapps.data.api.weather.converter.analyzeWeather
import com.helpfulapps.data.api.weather.model.ForecastForCity
import com.helpfulapps.data.db.weather.model.DayWeatherEntity
import com.helpfulapps.data.db.weather.model.DayWeatherEntity_Table
import com.helpfulapps.data.db.weather.model.HourWeatherEntity
import com.helpfulapps.data.extensions.checkCompleted
import com.helpfulapps.data.extensions.dayOfMonth
import com.helpfulapps.data.extensions.rxQueryListSingle
import com.helpfulapps.data.extensions.timestampAtMidnight
import com.helpfulapps.data.helper.NetworkCheck
import com.helpfulapps.domain.eventBus.DatabaseNotifiers
import com.helpfulapps.domain.eventBus.RxBus
import com.helpfulapps.domain.exceptions.CouldNotObtainForecast
import com.helpfulapps.domain.exceptions.WeatherException
import com.helpfulapps.domain.helpers.Settings
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
    context: Context,
    private val networkCheck: NetworkCheck = NetworkCheck(context),
    private val apiCalls: ApiCalls = Downloader.create(),
    private val settings: Settings
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
            .doOnComplete { RxBus.publish(DatabaseNotifiers.Saved) }

    override fun downloadForecast(lat: Double, lon: Double): Completable =
        networkCheck.isConnectedToNetwork
            .flatMap {
                apiCalls.downloadForecastForCoordinates(
                    lat.toString(),
                    lon.toString(),
                    settings.units.unit
                )
            }
            .convertModelsAndSaveInDb()
            .doOnComplete { RxBus.publish(DatabaseNotifiers.Saved) }

    override fun getForecastForAlarms(): Single<List<DomainDayWeather>> =
        getDayWeatherList()
            .map(DayWeatherEntity::toDomain)
            .toList()
            .timeout(2L, TimeUnit.SECONDS) { observer -> observer.onSuccess(emptyList()) }

    override fun getForecastForAlarm(time: Long): Single<DomainDayWeather> =
        getDayWeatherForTime(time)
            .timeout(
                2L,
                TimeUnit.SECONDS
            ) { observer -> observer.onSuccess(DayWeatherEntity(id = -1)) }
            .map(DayWeatherEntity::toDomain)
            .onErrorResumeNext(Single.just(com.helpfulapps.domain.models.weather.DayWeather()))

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
            Delete.table(DayWeatherEntity::class.java)
            Delete.table(HourWeatherEntity::class.java)
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

    private fun Single<List<DayWeatherEntity>>.saveInDatabase() =
        this.flatMapObservable { list -> Observable.fromIterable(list) }
            .flatMap { dayWeather ->
                dayWeather.weatherInfoEntity?.save()?.flatMapObservable {
                    dayWeather.save()
                        .flatMapObservable {
                            Observable.fromIterable(dayWeather.hourWeatherEntityList)
                                .flatMap { hourWeather ->
                                    hourWeather.dayWeatherEntity = dayWeather
                                    hourWeather.weatherInfoEntity?.save()?.toObservable()
                                        ?.flatMap { hourWeather.save().toObservable() }
                                }
                        }
                }
            }

    private fun Single<ForecastForCity>.transformResponseIntoDays(): Single<List<DayWeatherEntity>> =
        this.map {
            settings.city = it.city.name
            it.list
                .map { forecast -> forecast.toDbModel().apply { dt *= 1000 } }
                .groupBy { hourWeather -> hourWeather.dt.dayOfMonth() }
                .map { hourWeather ->
                    DayWeatherEntity(
                        dt = hourWeather.value.first().dt.timestampAtMidnight(),
                        cityName = it.city.name,
                        hourWeatherEntityList = hourWeather.value
                    )
                }
        }


    fun getDayWeatherList() = (select from DayWeatherEntity::class).rx().queryStreamResults()

    fun getDayWeatherForTime(time: Long) =
        (select from DayWeatherEntity::class where (DayWeatherEntity_Table.dt lessThanOrEq time + ONE_AND_HALF_AN_HOUR) and (DayWeatherEntity_Table.dt greaterThan time - ONE_AND_HALF_AN_HOUR))
            .rxQueryListSingle()
}