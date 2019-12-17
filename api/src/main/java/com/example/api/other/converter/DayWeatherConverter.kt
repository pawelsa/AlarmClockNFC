package com.example.api.other.converter

import com.helpfulapps.data.db.weather.model.*
import com.helpfulapps.domain.helpers.Settings
import kotlin.math.abs


object DayWeatherConverter {

    fun analyzeWeather(
        dayWeatherEntity: DayWeatherData,
        units: Settings.Units
    ): WeatherInfoData {
        dayWeatherEntity.hourWeatherList.forEach {
            HourWeatherConverter.analyzeWeather(
                it,
                units
            )
        }
        val temperature = dayWeatherEntity.hourWeatherList
            .mapNotNull { it.weatherInfo.temperature }
            .maxBy { abs(it.importance) }

        val wind = dayWeatherEntity.hourWeatherList
            .mapNotNull { it.weatherInfo.wind }
            .maxBy { it }

        val rain = dayWeatherEntity.hourWeatherList
            .mapNotNull { it.weatherInfo.rain }
            .maxBy { it }

        val snow = dayWeatherEntity.hourWeatherList
            .mapNotNull { it.weatherInfo.snow }
            .maxBy { it }

        dayWeatherEntity.weatherInfo = WeatherInfoData(
            temperature = temperature ?: TemperatureData.NO_DATA,
            wind = wind ?: WindData.NO_DATA,
            rain = rain ?: RainData.NO_DATA,
            snow = snow ?: SnowData.NO_DATA
        )
        return dayWeatherEntity.weatherInfo
    }
}