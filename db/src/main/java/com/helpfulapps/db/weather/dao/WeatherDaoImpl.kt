package com.helpfulapps.db.weather.dao

import android.content.Context
import com.helpfulapps.data.db.weather.dao.WeatherDao
import com.helpfulapps.data.db.weather.model.DayWeatherData
import com.helpfulapps.db.weather.models.DayWeatherEntity
import com.helpfulapps.db.weather.models.DayWeatherEntity_Table
import com.helpfulapps.db.weather.models.HourWeatherEntity
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.kotlinextensions.*
import com.raizlabs.android.dbflow.rx2.kotlinextensions.rx
import com.raizlabs.android.dbflow.sql.language.Delete
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

class WeatherDaoImpl(
    context: Context
) : WeatherDao {

    init {
        FlowManager.init(context)
    }

    override fun insert(dayWeatherData: DayWeatherData): Single<Boolean> {
        val dayWeather = DayWeatherEntity(dayWeatherData)
        return dayWeather.weatherInfoEntity!!.save()
            .flatMap {
                dayWeather.save()
                    .flatMap {
                        Observable.fromIterable(dayWeather.hourWeatherEntityList)
                            .map {
                                it.dayWeatherEntity = dayWeather
                                it
                            }.flatMap { hourWeather ->
                                hourWeather.weatherInfoEntity!!.save()
                                    .flatMapObservable {
                                        hourWeather.save().toObservable()
                                    }
                            }
                            .toList()
                            .map { it.all { true } }
                    }
            }
    }

    override fun clearWeatherTables() {
        Delete.table(DayWeatherEntity::class.java)
        Delete.table(HourWeatherEntity::class.java)
    }

    override fun streamWeatherList(): Flowable<DayWeatherData> {
        return (select from DayWeatherEntity::class).rx().queryStreamResults().map { it.toData() }
    }

    override fun getWeatherForTime(time: Long): Single<DayWeatherData> {
        return (select from DayWeatherEntity::class where (DayWeatherEntity_Table.dt lessThanOrEq time + HALF_DAY) and (DayWeatherEntity_Table.dt greaterThan time - HALF_DAY))
            .rx().querySingle().toSingle().map { it.toData() }
    }
/*    return (select from DayWeatherEntity::class)
    .rx().queryList()
    .map {
        val currentTime = GregorianCalendar.getInstance().timeInMillis
        it.first {dayWeather ->
            return@first abs(currentTime - dayWeather.dt) <= HALF_DAY
        }.toData()
    }*/

    companion object {
        private const val HALF_DAY = 12 * 60 * 60 * 1000
    }
}
