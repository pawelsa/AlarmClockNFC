package com.helpfulapps.db.weather.models

import com.helpfulapps.data.db.weather.model.TemperatureData
import com.helpfulapps.data.db.weather.model.WeatherInfoData
import com.helpfulapps.db.AlarmAppDatabase
import com.helpfulapps.db.weather.converter.WeatherInfoConverter
import com.helpfulapps.db.weather.models.WeatherInfoEntity.Companion.TABLE_NAME
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.rx2.structure.BaseRXModel

// TODO check if it is saved to database, or it is only overwritten
@Table(database = AlarmAppDatabase::class, name = TABLE_NAME, allFields = true)
data class WeatherInfoEntity(
    @PrimaryKey(autoincrement = true)
    var id: Long = 0,
    var temperature: Int = TemperatureData.NO_DATA.importance,
    var rain: Int = TemperatureData.NO_DATA.importance,
    var wind: Int = TemperatureData.NO_DATA.importance,
    var snow: Int = TemperatureData.NO_DATA.importance
) : BaseRXModel() {

    constructor(weatherInfoData: WeatherInfoData) : this() {
        temperature = weatherInfoData.temperature.importance
        rain = weatherInfoData.rain.importance
        wind = weatherInfoData.wind.importance
        snow = weatherInfoData.snow.importance
    }

    fun toData(): WeatherInfoData {
        return WeatherInfoData(
            temperature = WeatherInfoConverter.getTemperature(temperature),
            rain = WeatherInfoConverter.getRain(rain),
            wind = WeatherInfoConverter.getWind(wind),
            snow = WeatherInfoConverter.getSnow(snow)
        )
    }

    companion object {
        const val TABLE_NAME = "WeatherInfoEntity"
    }

}
