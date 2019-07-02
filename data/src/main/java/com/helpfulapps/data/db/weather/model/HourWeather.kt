package com.helpfulapps.data.db.weather.model

import com.helpfulapps.data.AlarmAppDatabase
import com.helpfulapps.data.api.weather.model.Forecast
import com.helpfulapps.data.db.weather.model.HourWeather.Companion.TABLE_NAME
import com.helpfulapps.domain.models.weather.HourWeather
import com.raizlabs.android.dbflow.annotation.ForeignKey
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.rx2.structure.BaseRXModel

@Table(database = AlarmAppDatabase::class, name = TABLE_NAME, allFields = true)
class HourWeather(
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
    var dayWeather: DayWeather? = null
) : BaseRXModel() {

    companion object {
        const val TABLE_NAME = "HourWeather"
    }

    constructor(forecast: Forecast) : this(
        dt = forecast.dt,
        clouds = forecast.clouds?.all ?: 0,
        rain = forecast.rain?._3h ?: 0.0,
        snow = forecast.snow?._3h ?: 0.0,
        wind = forecast.wind?.speed ?: 0.0,
        humidity = forecast.main.humidity,
        pressure = forecast.main.pressure,
        temp = forecast.main.temp,
        tempMax = forecast.main.temp_max,
        tempMin = forecast.main.temp_min
    )

    fun toDomain() =
        HourWeather(
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
            tempMin = this.tempMin
        )
}