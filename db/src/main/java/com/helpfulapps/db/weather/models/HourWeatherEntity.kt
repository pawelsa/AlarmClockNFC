package com.helpfulapps.db.weather.models

import com.helpfulapps.data.db.weather.model.HourWeatherData
import com.helpfulapps.data.db.weather.model.WeatherInfoData
import com.helpfulapps.db.AlarmAppDatabase
import com.helpfulapps.db.weather.models.HourWeatherEntity.Companion.TABLE_NAME
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
    var weatherInfoEntity: WeatherInfoEntity? = null
) : BaseRXModel() {

    companion object {
        const val TABLE_NAME = "HourWeatherEntity"
    }

    constructor(hourWeatherData: HourWeatherData) : this() {
        id = hourWeatherData.id
        dt = hourWeatherData.dt
        clouds = hourWeatherData.clouds
        rain = hourWeatherData.rain
        wind = hourWeatherData.wind
        humidity = hourWeatherData.humidity
        pressure = hourWeatherData.pressure
        temp = hourWeatherData.temp
        tempMax = hourWeatherData.tempMax
        tempMin = hourWeatherData.tempMin
        weatherInfoEntity = WeatherInfoEntity(hourWeatherData.weatherInfo)
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
            weatherInfo = weatherInfoEntity?.toData() ?: WeatherInfoData()
        )
    }
}