package com.example.db.weather.models

import com.example.db.AlarmAppDatabase
import com.example.db.weather.models.DayWeatherEntity.Companion.TABLE_NAME
import com.helpfulapps.data.db.weather.model.DayWeatherData
import com.raizlabs.android.dbflow.annotation.*
import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.list
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.kotlinextensions.where
import com.raizlabs.android.dbflow.rx2.structure.BaseRXModel
import io.reactivex.Single

@Table(database = AlarmAppDatabase::class, name = TABLE_NAME, allFields = true)
data class DayWeatherEntity(
    @PrimaryKey(autoincrement = true)
    var id: Int = 0,
    var dt: Long = 0,
    var cityName: String = "",
    @ColumnIgnore
    var hourWeatherEntityList: List<HourWeatherEntity> = listOf(),
    @ForeignKey(stubbedRelationship = true)
    var weatherInfoEntity: WeatherInfoEntity = WeatherInfoEntity()
) : BaseRXModel() {

    companion object {
        const val TABLE_NAME = "DayWeatherEntity"
    }

    constructor(dayWeatherData: DayWeatherData) : this() {
        id = dayWeatherData.id
        dt = dayWeatherData.dt
        cityName = dayWeatherData.cityName
        hourWeatherEntityList = dayWeatherData.hourWeatherList.map { HourWeatherEntity(it) }
        weatherInfoEntity = WeatherInfoEntity(dayWeatherData.weatherInfo)
    }

    @OneToMany(methods = [OneToMany.Method.ALL], variableName = "hourWeatherEntityList")
    fun oneToManyHourWeathers(): List<HourWeatherEntity> =
        (select from HourWeatherEntity::class where HourWeatherEntity_Table.dayWeatherEntity_id.eq(
            id
        )).list

    override fun save(): Single<Boolean> {
        val res = super.save()
        hourWeatherEntityList.forEach { weather ->
            weather.dayWeatherEntity = this@DayWeatherEntity
        }
        return res
    }

    fun toData(): DayWeatherData {
        if (this.hourWeatherEntityList.isEmpty()) {
            this.hourWeatherEntityList =
                (select from HourWeatherEntity::class where HourWeatherEntity_Table.dayWeatherEntity_id.eq(
                    this.id
                )).list
        }
        this.weatherInfoEntity =
            (select from WeatherInfoEntity::class where WeatherInfoEntity_Table.id.eq(
                weatherInfoEntity.id
            )).querySingle()
        return DayWeatherData(
            id = this.id,
            dt = this.dt,
            cityName = this.cityName,
            hourWeatherList = this.hourWeatherEntityList
                .map { hourWeather -> hourWeather.toData() },
            weatherInfo = weatherInfoEntity.toDomain()
        )
    }
}