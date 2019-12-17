package com.helpfulapps.data.repositories

import com.helpfulapps.data.api.PrepareData
import com.helpfulapps.data.db.weather.dao.WeatherDao
import com.helpfulapps.data.db.weather.model.DayWeatherData
import com.helpfulapps.data.extensions.checkCompleted
import com.helpfulapps.domain.eventBus.DatabaseNotifiers
import com.helpfulapps.domain.eventBus.RxBus
import com.helpfulapps.domain.exceptions.WeatherException
import com.helpfulapps.domain.repository.WeatherRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import com.helpfulapps.domain.models.weather.DayWeather as DomainDayWeather

class WeatherRepositoryImpl(
    private val prepareData: PrepareData,
    private val weatherDao: WeatherDao
) : WeatherRepository {

    companion object {
        const val ONE_AND_HALF_AN_HOUR: Long = 15 * 3600 * 100
    }

    override fun downloadForecast(city: String): Completable =
        prepareData.downloadForecast(city)
            .convertModelsAndSaveInDb()
            .doOnComplete { RxBus.publish(DatabaseNotifiers.Saved) }

    override fun downloadForecast(lat: Double, lon: Double): Completable =
        prepareData.downloadForecastForCoordinates(
            lat,
            lon
        )
            .convertModelsAndSaveInDb()
            .doOnComplete { RxBus.publish(DatabaseNotifiers.Saved) }

    override fun getForecastForAlarms(): Single<List<DomainDayWeather>> =
        getDayWeatherList()
            .map(DayWeatherData::toDomain)
            .toList()
            .timeout(2L, TimeUnit.SECONDS) { observer -> observer.onSuccess(emptyList()) }

    override fun getForecastForAlarm(time: Long): Single<DomainDayWeather> =
        getDayWeatherForTime(time)
            .timeout(
                2L,
                TimeUnit.SECONDS
            ) { observer -> observer.onSuccess(DayWeatherData(id = -1)) }
            .map(DayWeatherData::toDomain)
            .onErrorResumeNext(Single.just(com.helpfulapps.domain.models.weather.DayWeather()))

    private fun Single<List<DayWeatherData>>.convertModelsAndSaveInDb() =
        clearTables()
            .saveInDatabase()
            .flatMapCompletable { savedInDb ->
                savedInDb.checkCompleted(WeatherException("Couldn't save forecast in database"))
            }
            .observeOn(AndroidSchedulers.mainThread())

    private fun Single<List<DayWeatherData>>.clearTables() =
        this.doOnSuccess {
            weatherDao.clearWeatherTables()
        }

    private fun Single<List<DayWeatherData>>.saveInDatabase() =
        this.flatMapObservable { list -> Observable.fromIterable(list) }
            .flatMapSingle { dayWeather ->
                weatherDao.insert(dayWeather)
            }


    fun getDayWeatherList() = weatherDao.streamWeatherList()

    fun getDayWeatherForTime(time: Long) = weatherDao.getWeatherForTime(time)
}