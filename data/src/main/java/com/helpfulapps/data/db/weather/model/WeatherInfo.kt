package com.helpfulapps.data.db.weather.model

import com.helpfulapps.data.AlarmAppDatabase
import com.helpfulapps.data.api.weather.converter.Rain
import com.helpfulapps.data.api.weather.converter.Snow
import com.helpfulapps.data.api.weather.converter.Temperature
import com.helpfulapps.data.api.weather.converter.Wind
import com.helpfulapps.data.db.converter.WeatherInfoConverter
import com.helpfulapps.data.db.weather.model.WeatherInfo.Companion.TABLE_NAME
import com.helpfulapps.domain.models.weather.WeatherInfo
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.rx2.structure.BaseRXModel

// TODO check if it is saved to database, or it is only overwritten
@Table(database = AlarmAppDatabase::class, name = TABLE_NAME)
data class WeatherInfo(
    @PrimaryKey(autoincrement = true)
    var id: Long = 0,
    var temperature: Int = Temperature.NO_DATA.importance,
    var rain: Int = Rain.NO_DATA.importance,
    var wind: Int = Wind.NO_DATA.importance,
    var snow: Int = Snow.NO_DATA.importance
) : BaseRXModel() {

    fun toDomain(): WeatherInfo {
        return WeatherInfo(
            temperature = WeatherInfoConverter.getTemperature(temperature),
            rain = WeatherInfoConverter.getRain(rain),
            wind = WeatherInfoConverter.getWind(wind),
            snow = WeatherInfoConverter.getSnow(snow)
        )
    }

    companion object {
        const val TABLE_NAME = "WeatherInfo"
    }

}