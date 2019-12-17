package com.example.db.weather.models

import com.example.db.AlarmAppDatabase
import com.example.db.weather.models.HourWeatherEntity.Companion.TABLE_NAME
import com.helpfulapps.data.db.weather.model.HourWeatherData
import com.raizlabs.android.dbflow.annotation.ForeignKey
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.rx2.structure.BaseRXModel


@Table(database = AlarmAppDatabase::class, name = TABLE_NAME, allFields = true)
data class HourWeatherEntity(
    @PrimaryKey(autoincrement = true)
    var id: Int = 0,
    var dt: Long = 0,
    var clouds: Int = 0,
    var rain: Double = 0.0,
    var snow: Double = 0.0,
    var wind: Double = 0.0,
    var humidity: Int = 0,
    var pressure: Double = 0.0,
    var temp: Double = 0.0,
    var tempMax: Double = 0.0,
    var tempMin: Double = 0.0,
    @ForeignKey(stubbedRelationship = true)
    var dayWeatherEntity: DayWeatherEntity? = null,
    @ForeignKey(stubbedRelationship = true)
    var weatherInfoEntity: WeatherInfoEntity = WeatherInfoEntity()
) : BaseRXModel() {

    companion object {
        const val TABLE_NAME = "HourWeatherEntity"
    }

    fun toData(): HourWeatherData {
        return HourWeatherData(
            id = this.id,
            dt = this.dt,
            clouds = this.clouds,
            rain = this.rain,
            snow = this.snow,
            wind = this.wind,
            humidity = this.humidity,
            pressure = this.pressure,
            temp = this.temp,
            tempMax = this.tempMax,
            tempMin = this.tempMin,
            weatherInfo = weatherInfoEntity.toDomain()
        )
    }
}