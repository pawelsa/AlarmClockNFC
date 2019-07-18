package com.helpfulapps.data.db.converter

import com.helpfulapps.data.api.weather.converter.Rain
import com.helpfulapps.data.api.weather.converter.Snow
import com.helpfulapps.data.api.weather.converter.Temperature
import com.helpfulapps.data.api.weather.converter.Wind
import com.helpfulapps.data.helper.Units
import com.helpfulapps.data.db.weather.model.DayWeather
import com.helpfulapps.data.db.weather.model.WeatherInfo
import kotlin.math.abs

object DayWeatherConverter {

    fun analyzeWeather(dayWeather: DayWeather, units: Units) : WeatherInfo {
        dayWeather.hourWeatherList.forEach {
            HourWeatherConverter.analyzeWeather(
                it,
                units
            )
        }
        val temperature = dayWeather.hourWeatherList
            .mapNotNull { it.weatherInfo?.temperature }
            .maxBy { abs(it) }

        val wind = dayWeather.hourWeatherList
            .mapNotNull { it.weatherInfo?.wind }
            .maxBy { it }

        val rain = dayWeather.hourWeatherList
            .mapNotNull { it.weatherInfo?.rain }
            .maxBy { it }

        val snow = dayWeather.hourWeatherList
            .mapNotNull { it.weatherInfo?.snow }
            .maxBy { it }

        return WeatherInfo(
            temperature = temperature ?: Temperature.NO_DATA.importance,
            wind = wind ?: Wind.NO_DATA.importance,
            rain = rain ?: Rain.NO_DATA.importance,
            snow = snow ?: Snow.NO_DATA.importance
        )
    }
}
