package com.helpfulapps.data.db.weather.converter

import com.helpfulapps.data.db.weather.model.DayWeatherData
import com.helpfulapps.data.db.weather.model.WeatherInfoData
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
            .mapNotNull { it.weatherInfo?.temperature }
            .maxBy { abs(it) }

        val wind = dayWeatherEntity.hourWeatherList
            .mapNotNull { it.weatherInfo?.wind }
            .maxBy { it }

        val rain = dayWeatherEntity.hourWeatherList
            .mapNotNull { it.weatherInfo?.rain }
            .maxBy { it }

        val snow = dayWeatherEntity.hourWeatherList
            .mapNotNull { it.weatherInfo?.snow }
            .maxBy { it }

        dayWeatherEntity.weatherInfo = WeatherInfoData(
            temperature = temperature ?: Temperature.NO_DATA.importance,
            wind = wind ?: Wind.NO_DATA.importance,
            rain = rain ?: Rain.NO_DATA.importance,
            snow = snow ?: Snow.NO_DATA.importance
        )
        return dayWeatherEntity.weatherInfo
    }
}
