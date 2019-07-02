package com.helpfulapps.data.db.weather.model

import com.helpfulapps.data.AlarmAppDatabase
import com.helpfulapps.domain.models.weather.Weather
import com.raizlabs.android.dbflow.annotation.ForeignKey
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.rx2.structure.BaseRXModel


@Table(name = WeatherDbModel.NAME, database = AlarmAppDatabase::class, allFields = true)
data class WeatherDbModel(

    @PrimaryKey(autoincrement = true)
    var id: Long = 0L,
    var timestamp: Long = -1,
    var cloudPercent: Int = -1,
    var windSpeed: Double = -1.0,
    var snowIn3h: Double = -1.0,
    var rainIn3h: Double = -1.0,
    var temp: Double = -1.0,
    var tempMax: Double = -1.0,
    var tempMin: Double = -1.0,
    var description: String = "",
    var icon: String = "",
    @ForeignKey(stubbedRelationship = true)
    var forecast: ForecastDbModel? = null
) : BaseRXModel() {

    fun toDomain(): Weather =
        Weather(
            timestamp = timestamp,
            cloudPercent = cloudPercent,
            windSpeed = windSpeed,
            snowIn3h = snowIn3h,
            rainIn3h = rainIn3h,
            temp = temp,
            tempMax = tempMax,
            tempMin = tempMin,
            description = description,
            icon = icon
        )

    companion object {
        const val NAME = "WeatherTable"
    }
}