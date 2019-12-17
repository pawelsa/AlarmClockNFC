package com.example.api.other.converter


object WeatherInfoConverter {

    fun getTemperature(temperature: Int): Temperature {
        return when (temperature) {
            -2 -> Temperature.VERY_COLD
            -1 -> Temperature.COLD
            0 -> Temperature.NORMAL
            1 -> Temperature.HOT
            2 -> Temperature.VERY_HOT
            else -> Temperature.NO_DATA
        }
    }

    fun getRain(rain: Int): Rain {
        return when (rain) {
            0 -> Rain.NO_RAIN
            1 -> Rain.MAY_RAIN
            2 -> Rain.WILL_RAIN
            3 -> Rain.HEAVY_RAIN
            else -> Rain.NO_DATA
        }
    }

    fun getWind(wind: Int): Wind {
        return when (wind) {
            0 -> Wind.NORMAL
            1 -> Wind.WINDY
            2 -> Wind.VERY_WINDY
            else -> Wind.NO_DATA
        }
    }

    fun getSnow(snow: Int): Snow {
        return when (snow) {
            0 -> Snow.NORMAL
            1 -> Snow.SNOWY
            2 -> Snow.VERY_SNOWY
            else -> Snow.NO_DATA
        }
    }

}