package com.helpfulapps.data.db.converter

import com.helpfulapps.data.api.weather.converter.Rain
import com.helpfulapps.data.api.weather.converter.Snow
import com.helpfulapps.data.api.weather.converter.Temperature
import com.helpfulapps.data.api.weather.converter.Wind
import com.helpfulapps.data.db.weather.model.DayWeatherEntity
import com.helpfulapps.data.db.weather.model.WeatherInfoEntity
import com.helpfulapps.domain.helpers.Settings
import kotlin.math.abs

object DayWeatherConverter {

    fun analyzeWeather(
        dayWeatherEntity: DayWeatherEntity,
        units: Settings.Units
    ): WeatherInfoEntity {
        dayWeatherEntity.hourWeatherEntityList.forEach {
            HourWeatherConverter.analyzeWeather(
                it,
                units
            )
        }
        val temperature = dayWeatherEntity.hourWeatherEntityList
            .mapNotNull { it.weatherInfoEntity?.temperature }
            .maxBy { abs(it) }

        val wind = dayWeatherEntity.hourWeatherEntityList
            .mapNotNull { it.weatherInfoEntity?.wind }
            .maxBy { it }

        val rain = dayWeatherEntity.hourWeatherEntityList
            .mapNotNull { it.weatherInfoEntity?.rain }
            .maxBy { it }

        val snow = dayWeatherEntity.hourWeatherEntityList
            .mapNotNull { it.weatherInfoEntity?.snow }
            .maxBy { it }

        dayWeatherEntity.weatherInfoEntity = WeatherInfoEntity(
            temperature = temperature ?: Temperature.NO_DATA.importance,
            wind = wind ?: Wind.NO_DATA.importance,
            rain = rain ?: Rain.NO_DATA.importance,
            snow = snow ?: Snow.NO_DATA.importance
        )
        return dayWeatherEntity.weatherInfoEntity!!
    }
}
