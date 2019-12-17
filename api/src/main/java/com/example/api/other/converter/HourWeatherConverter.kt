package com.example.api.other.converter

import com.helpfulapps.data.db.weather.model.*
import com.helpfulapps.domain.helpers.Settings

object HourWeatherConverter {

    fun analyzeWeather(
        hourWeatherEntity: HourWeatherData,
        units: Settings.Units
    ): WeatherInfoData {
        with(hourWeatherEntity) {
            weatherInfo = WeatherInfoData(
                temperature = getTemperature(
                    temp,
                    units
                ),
                snow = getSnow(snow),
                wind = getWind(wind, units),
                rain = getRain(rain)
            )
        }
        return hourWeatherEntity.weatherInfo
    }

    private fun getTemperature(temperature: Double, units: Settings.Units): TemperatureData {
        return if (units == Settings.Units.METRIC) {
            when (temperature) {
                in Temperature.VERY_HOT.celsiusRange -> TemperatureData.VERY_HOT
                in Temperature.HOT.celsiusRange -> TemperatureData.HOT
                in Temperature.NORMAL.celsiusRange -> TemperatureData.NORMAL
                in Temperature.COLD.celsiusRange -> TemperatureData.COLD
                in Temperature.VERY_COLD.celsiusRange -> TemperatureData.VERY_COLD
                else -> TemperatureData.NO_DATA
            }
        } else {
            when (temperature) {
                in Temperature.VERY_HOT.fahrenheitRange -> TemperatureData.VERY_HOT
                in Temperature.HOT.fahrenheitRange -> TemperatureData.HOT
                in Temperature.NORMAL.fahrenheitRange -> TemperatureData.NORMAL
                in Temperature.COLD.fahrenheitRange -> TemperatureData.COLD
                in Temperature.VERY_COLD.fahrenheitRange -> TemperatureData.VERY_COLD
                else -> TemperatureData.NO_DATA
            }
        }
    }

    private fun getSnow(snow: Double): SnowData {
        return when (snow) {
            in Snow.NORMAL.range -> SnowData.NORMAL
            in Snow.SNOWY.range -> SnowData.SNOWY
            in Snow.VERY_SNOWY.range -> SnowData.VERY_SNOWY
            else -> SnowData.NO_DATA
        }
    }

    private fun getWind(wind: Double, units: Settings.Units): WindData {
        return if (units == Settings.Units.METRIC) {
            when (wind) {
                in Wind.NORMAL.metricRange -> WindData.NORMAL
                in Wind.WINDY.metricRange -> WindData.WINDY
                in Wind.VERY_WINDY.metricRange -> WindData.VERY_WINDY
                else -> WindData.NO_DATA
            }
        } else {
            when (wind) {
                in Wind.NORMAL.imperialRange -> WindData.NORMAL
                in Wind.WINDY.imperialRange -> WindData.WINDY
                in Wind.VERY_WINDY.imperialRange -> WindData.VERY_WINDY
                else -> WindData.NO_DATA
            }
        }
    }

    private fun getRain(rain: Double): RainData {
        return when (rain) {
            in Rain.NO_RAIN.range -> RainData.NO_RAIN
            in Rain.MAY_RAIN.range -> RainData.MAY_RAIN
            in Rain.WILL_RAIN.range -> RainData.WILL_RAIN
            in Rain.HEAVY_RAIN.range -> RainData.HEAVY_RAIN
            else -> RainData.NO_DATA
        }
    }

}