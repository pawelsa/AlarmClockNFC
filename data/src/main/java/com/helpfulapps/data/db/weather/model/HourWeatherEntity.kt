package com.helpfulapps.data.db.weather.model

import com.helpfulapps.data.AlarmAppDatabase
import com.helpfulapps.data.db.weather.model.HourWeatherEntity.Companion.TABLE_NAME
import com.raizlabs.android.dbflow.annotation.ForeignKey
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.rx2.structure.BaseRXModel
import com.helpfulapps.domain.models.weather.HourWeather as DomainHourWeather
import com.helpfulapps.domain.models.weather.WeatherInfo as DomainWeatherInfo

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
        const val TABLE_NAME = "HourWeather"
    }

    fun toDomain(): DomainHourWeather {
        return DomainHourWeather(
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
            weatherInfo = weatherInfoEntity?.toDomain() ?: DomainWeatherInfo()
        )
    }
}

data class HourWeatherData(
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
    var dayWeatherEntity: DayWeatherEntity? = null,
    var weatherInfoEntity: WeatherInfoEntity? = null
)