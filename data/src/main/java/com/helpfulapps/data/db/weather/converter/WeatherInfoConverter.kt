package com.helpfulapps.data.db.weather.converter

import com.helpfulapps.data.db.weather.model.RainData
import com.helpfulapps.data.db.weather.model.SnowData
import com.helpfulapps.data.db.weather.model.TemperatureData
import com.helpfulapps.data.db.weather.model.WindData
import com.helpfulapps.domain.models.weather.Rain
import com.helpfulapps.domain.models.weather.Snow
import com.helpfulapps.domain.models.weather.Temperature
import com.helpfulapps.domain.models.weather.Wind


object WeatherInfoConverter {


    fun getTemperature(temperature: TemperatureData): Temperature {
        return when (temperature) {
            TemperatureData.VERY_COLD -> Temperature.VERY_COLD
            TemperatureData.COLD -> Temperature.COLD
            TemperatureData.NORMAL -> Temperature.NORMAL
            TemperatureData.HOT -> Temperature.HOT
            TemperatureData.VERY_HOT -> Temperature.VERY_HOT
            else -> Temperature.NO_DATA
        }
    }

    fun getWind(wind: WindData): Wind {
        return when (wind) {
            WindData.NORMAL -> Wind.NORMAL
            WindData.WINDY -> Wind.WINDY
            WindData.VERY_WINDY -> Wind.VERY_WINDY
            else -> Wind.NO_DATA
        }
    }

    fun getRain(rain: RainData): Rain {
        return when (rain) {
            RainData.NO_RAIN -> Rain.NO_RAIN
            RainData.MAY_RAIN -> Rain.MAY_RAIN
            RainData.WILL_RAIN -> Rain.WILL_RAIN
            RainData.HEAVY_RAIN -> Rain.HEAVY_RAIN
            else -> Rain.NO_DATA
        }
    }

    fun getSnow(snow: SnowData): Snow {
        return when (snow) {
            SnowData.NORMAL -> Snow.NORMAL
            SnowData.SNOWY -> Snow.SNOWY
            SnowData.VERY_SNOWY -> Snow.VERY_SNOWY
            else -> Snow.NO_DATA
        }
    }

}