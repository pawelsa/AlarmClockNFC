package com.helpfulapps.domain.models.weather

data class WeatherInfo(
    val temperature: Temperature = Temperature.NO_DATA,
    val rain: Rain = Rain.NO_DATA,
    val wind: Wind = Wind.NO_DATA,
    val snow: Snow = Snow.NO_DATA
)

enum class Temperature(val importance: Int) {
    VERY_COLD(-2),
    COLD(-1),
    NORMAL(0),
    HOT(1),
    VERY_HOT(2),
    NO_DATA(-10)
}

enum class Wind(val importance: Int) {
    NORMAL(0),
    WINDY(1),
    VERY_WINDY(2),
    NO_DATA(-10),
}

enum class Rain(val importance: Int) {
    NO_RAIN(0),
    MAY_RAIN(1),
    WILL_RAIN(2),
    HEAVY_RAIN(3),
    NO_DATA(-10)
}

enum class Snow(val importance: Int) {
    NORMAL(0),
    SNOWY(1),
    VERY_SNOWY(2),
    NO_DATA(-10)
}