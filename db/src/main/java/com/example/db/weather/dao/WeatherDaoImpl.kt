package com.example.db.weather.dao

import android.content.Context
import com.example.db.weather.models.DayWeatherEntity
import com.example.db.weather.models.HourWeatherEntity
import com.helpfulapps.data.db.weather.dao.WeatherDao
import com.helpfulapps.data.db.weather.model.DayWeatherData
import com.helpfulapps.data.extensions.rxQueryListSingle
import com.helpfulapps.data.repositories.WeatherRepositoryImpl
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.kotlinextensions.and
import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.kotlinextensions.where
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
        return dayWeather.weatherInfoEntity.save()
            .flatMap {
                dayWeather.save()
                    .flatMap {
                        Observable.fromIterable(dayWeather.hourWeatherEntityList)
                            .map {
                                it.dayWeatherEntity = dayWeather
                                it
                            }.flatMap { hourWeather ->
                                hourWeather.weatherInfoEntity.save()
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
        (select from DayWeatherEntity::class where (DayWeatherEntity_Table.dt lessThanOrEq time + WeatherRepositoryImpl.ONE_AND_HALF_AN_HOUR) and (DayWeatherEntity_Table.dt greaterThan time - WeatherRepositoryImpl.ONE_AND_HALF_AN_HOUR))
            .rxQueryListSingle()
    }
}