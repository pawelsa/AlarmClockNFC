package com.helpfulapps.data.api.weather.converter

import com.helpfulapps.data.api.weather.model.ForecastForCity
import com.helpfulapps.data.db.weather.model.DayWeather
import com.helpfulapps.data.db.weather.model.HourWeather
import io.reactivex.Single
import java.util.*

class TransformResponseIntoDays {

    //najpierw stworzyć listę hourWeather,
// potem dopiero tworzyć dayWeather dzieląc po godzinach,
// sprawdzajać czy to nadal ten sam dzień


/*
    val dayOfMonthMidnight = forecast.dt.toDate().apply {
        set(Calendar.HOUR, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }*/

    fun Single<ForecastForCity>.transform(): Single<List<DayWeather>> =
        this.map {
            val hourWeatherList = arrayListOf<HourWeather>()
            it.list.forEach { forecast ->
                hourWeatherList.add(
                    HourWeather(
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
                )
            }

            listOf()
        }

    fun Long.toDate() = GregorianCalendar().apply { timeInMillis = this@toDate }
}