package com.helpfulapps.data.db.weather.model

import com.helpfulapps.data.AlarmAppDatabase
import com.helpfulapps.data.api.weather.converter.Rain
import com.helpfulapps.data.api.weather.converter.Snow
import com.helpfulapps.data.api.weather.converter.Temperature
import com.helpfulapps.data.api.weather.converter.Wind
import com.helpfulapps.data.api.weather.model.Forecast
import com.helpfulapps.data.common.Units
import com.helpfulapps.data.db.weather.model.HourWeather.Companion.TABLE_NAME
import com.helpfulapps.domain.models.weather.HourWeather
import com.raizlabs.android.dbflow.annotation.ForeignKey
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.rx2.structure.BaseRXModel
import com.helpfulapps.domain.models.weather.WeatherInfo as DomainWeatherInfo

@Table(database = AlarmAppDatabase::class, name = TABLE_NAME, allFields = true)
data class HourWeather(
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
    var dayWeather: DayWeather? = null,
    @ForeignKey(stubbedRelationship = true)
    var weatherInfo: WeatherInfo? = null
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

    fun analyzeWeather(units: Units) {
        weatherInfo = WeatherInfo(
            temperature = getTemperature(units),
            snow = getSnow(),
            wind = getWind(units),
            rain = getRain()
        )
    }

    private fun getTemperature(units: Units): Int {
        return if (units == Units.METRIC) {
            when (temp) {
                in Temperature.VERY_HOT.celsiusRange -> Temperature.VERY_HOT.importance
                in Temperature.HOT.celsiusRange -> Temperature.HOT.importance
                in Temperature.NORMAL.celsiusRange -> Temperature.NORMAL.importance
                in Temperature.COLD.celsiusRange -> Temperature.COLD.importance
                in Temperature.VERY_COLD.celsiusRange -> Temperature.VERY_COLD.importance
                else -> Temperature.NO_DATA.importance
            }
        } else {
            when (temp) {
                in Temperature.VERY_HOT.fahrenheitRange -> Temperature.VERY_HOT.importance
                in Temperature.HOT.fahrenheitRange -> Temperature.HOT.importance
                in Temperature.NORMAL.fahrenheitRange -> Temperature.NORMAL.importance
                in Temperature.COLD.fahrenheitRange -> Temperature.COLD.importance
                in Temperature.VERY_COLD.fahrenheitRange -> Temperature.VERY_COLD.importance
                else -> Temperature.NO_DATA.importance
            }
        }
    }

    private fun getSnow(): Int {
        return when (snow) {
            in Snow.NORMAL.range -> Snow.NORMAL.importance
            in Snow.SNOWY.range -> Snow.SNOWY.importance
            in Snow.VERY_SNOWY.range -> Snow.VERY_SNOWY.importance
            else -> Snow.NO_DATA.importance
        }
    }

    private fun getWind(units: Units): Int {
        return if (units == Units.METRIC) {
            when (wind) {
                in Wind.NORMAL.metricRange -> Wind.NORMAL.importance
                in Wind.WINDY.metricRange -> Wind.WINDY.importance
                in Wind.VERY_WINDY.metricRange -> Wind.VERY_WINDY.importance
                else -> Wind.NO_DATA.importance
            }
        } else {
            when (wind) {
                in Wind.NORMAL.imperialRange -> Wind.NORMAL.importance
                in Wind.WINDY.imperialRange -> Wind.WINDY.importance
                in Wind.VERY_WINDY.imperialRange -> Wind.VERY_WINDY.importance
                else -> Wind.NO_DATA.importance
            }
        }
    }

    private fun getRain(): Int {
        return when (rain) {
            in Rain.NO_RAIN.range -> Rain.NO_RAIN.importance
            in Rain.MAY_RAIN.range -> Rain.MAY_RAIN.importance
            in Rain.WILL_RAIN.range -> Rain.WILL_RAIN.importance
            in Rain.HEAVY_RAIN.range -> Rain.HEAVY_RAIN.importance
            else -> Rain.NO_DATA.importance
        }
    }

    fun toDomain(): HourWeather {
        return HourWeather(
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
            weatherInfo = weatherInfo?.toDomain() ?: DomainWeatherInfo()
        )
    }
}