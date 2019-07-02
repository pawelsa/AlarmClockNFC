package com.helpfulapps.data.api.weather.converter

import com.helpfulapps.data.api.weather.model.ForecastForCity
import com.helpfulapps.data.db.weather.model.DayWeather
import com.helpfulapps.data.db.weather.model.HourWeather
import io.reactivex.Single
import java.util.*

fun Single<ForecastForCity>.transformResponseIntoDays(): Single<List<DayWeather>> =
    this.map {
        it.list
            .map { forecast -> HourWeather(forecast).apply { dt *= 1000 } }
            .groupBy { hourWeather -> hourWeather.dt.dayOfMonth() }
            .asSequence()
            .map { hourWeather ->
                DayWeather(
                    dt = hourWeather.value.first().dt.timestampAtMidnight(),
                    hourWeatherList = hourWeather.value
                )
            }
            .toList()
    }


private fun Long.dayOfMonth(): Int =
    GregorianCalendar()
        .apply {
            timeInMillis = this@dayOfMonth
        }
        .get(Calendar.DAY_OF_MONTH)

private fun Long.timestampAtMidnight() =
    GregorianCalendar()
        .apply {
            timeInMillis = this@timestampAtMidnight
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        .timeInMillis

