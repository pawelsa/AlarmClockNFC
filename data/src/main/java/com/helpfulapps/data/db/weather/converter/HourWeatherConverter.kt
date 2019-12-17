package com.helpfulapps.data.db.weather.converter

import com.example.db.weather.models.HourWeatherEntity
import com.example.db.weather.models.WeatherInfoEntity
import com.helpfulapps.data.db.weather.model.HourWeatherEntity
import com.helpfulapps.data.db.weather.model.WeatherInfoEntity
import com.helpfulapps.domain.helpers.Settings

object HourWeatherConverter {

    fun analyzeWeather(
        hourWeatherEntity: HourWeatherEntity,
        units: Settings.Units
    ): WeatherInfoEntity {
        with(hourWeatherEntity) {
            weatherInfoEntity = WeatherInfoEntity(
                temperature = getTemperature(
                    temp,
                    units
                ),
                snow = getSnow(snow),
                wind = getWind(wind, units),
                rain = getRain(rain)
            )
        }
        return hourWeatherEntity.weatherInfoEntity!!
    }

    private fun getTemperature(temperature: Double, units: Settings.Units): Int {
        return if (units == Settings.Units.METRIC) {
            when (temperature) {
                in Temperature.VERY_HOT.celsiusRange -> Temperature.VERY_HOT.importance
                in Temperature.HOT.celsiusRange -> Temperature.HOT.importance
                in Temperature.NORMAL.celsiusRange -> Temperature.NORMAL.importance
                in Temperature.COLD.celsiusRange -> Temperature.COLD.importance
                in Temperature.VERY_COLD.celsiusRange -> Temperature.VERY_COLD.importance
                else -> Temperature.NO_DATA.importance
            }
        } else {
            when (temperature) {
                in Temperature.VERY_HOT.fahrenheitRange -> Temperature.VERY_HOT.importance
                in Temperature.HOT.fahrenheitRange -> Temperature.HOT.importance
                in Temperature.NORMAL.fahrenheitRange -> Temperature.NORMAL.importance
                in Temperature.COLD.fahrenheitRange -> Temperature.COLD.importance
                in Temperature.VERY_COLD.fahrenheitRange -> Temperature.VERY_COLD.importance
                else -> Temperature.NO_DATA.importance
            }
        }
    }

    private fun getSnow(snow : Double): Int {
        return when (snow) {
            in Snow.NORMAL.range -> Snow.NORMAL.importance
            in Snow.SNOWY.range -> Snow.SNOWY.importance
            in Snow.VERY_SNOWY.range -> Snow.VERY_SNOWY.importance
            else -> Snow.NO_DATA.importance
        }
    }

    private fun getWind(wind: Double, units: Settings.Units): Int {
        return if (units == Settings.Units.METRIC) {
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

    private fun getRain(rain: Double): Int {
        return when (rain) {
            in Rain.NO_RAIN.range -> Rain.NO_RAIN.importance
            in Rain.MAY_RAIN.range -> Rain.MAY_RAIN.importance
            in Rain.WILL_RAIN.range -> Rain.WILL_RAIN.importance
            in Rain.HEAVY_RAIN.range -> Rain.HEAVY_RAIN.importance
            else -> Rain.NO_DATA.importance
        }
    }

}