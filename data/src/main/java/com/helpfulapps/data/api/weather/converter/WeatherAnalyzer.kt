package com.helpfulapps.data.api.weather.converter

import com.helpfulapps.data.db.converter.DayWeatherConverter
import com.helpfulapps.data.db.weather.model.DayWeather
import com.helpfulapps.domain.helpers.Settings
import io.reactivex.Single

//TODO maybe it should be wrapped in factory pattern ?
fun Single<List<DayWeather>>.analyzeWeather(units: Settings.Units) =
    this.map { dayWeatherList ->
        dayWeatherList.forEach { DayWeatherConverter.analyzeWeather(it, units) }
        dayWeatherList
    }

enum class Temperature(
    val importance: Int,
    val celsiusRange: ClosedFloatingPointRange<Double>,
    val fahrenheitRange: ClosedFloatingPointRange<Double>
) {
    VERY_COLD(-2, -1000.00..3.9999, -1000.0..39.1999),
    COLD(-1, 4.0..11.9999, 39.2..53.5999),
    NORMAL(0, 12.0..21.9999, 53.6..71.6),
    HOT(1, 22.0..29.9999, 71.6..84.1999),
    VERY_HOT(2, 30.0..1000.0, 84.2..1000.0),
    NO_DATA(-10, 0.0..0.0, 0.0..0.0)
}

// TODO check ranges
enum class Wind(
    val importance: Int,
    val metricRange: ClosedFloatingPointRange<Double>,
    val imperialRange: ClosedFloatingPointRange<Double>
) {
    NORMAL(0, 0.0..4.0, 0.0..8.9477),
    WINDY(1, 4.01..8.0, 8.94771..17.895),
    VERY_WINDY(2, 8.01..10000.0, 17.8951..10000.0),
    NO_DATA(-10, 0.0..0.0, 0.0..0.0)
}

enum class Rain(
    val importance: Int,
    val range: ClosedFloatingPointRange<Double>
) {
    NO_RAIN(0, 0.0..0.09),
    MAY_RAIN(1, 0.1..0.5),
    WILL_RAIN(2, 0.51..1.5),
    HEAVY_RAIN(3, 1.51..1000.0),
    NO_DATA(-10, 0.0..0.0)
}

enum class Snow(
    val importance: Int,
    val range: ClosedFloatingPointRange<Double>
) {
    NORMAL(0, 0.0..0.4999),
    SNOWY(1, 0.5..1.999),
    VERY_SNOWY(2, 2.0..100.0),
    NO_DATA(-10, 0.0..0.0)
}