package com.helpfulapps.data.db.weather.model

import com.helpfulapps.data.AlarmAppDatabase
import com.helpfulapps.data.api.weather.converter.Rain
import com.helpfulapps.data.api.weather.converter.Snow
import com.helpfulapps.data.api.weather.converter.Temperature
import com.helpfulapps.data.api.weather.converter.Wind
import com.helpfulapps.data.db.weather.model.WeatherInfo.Companion.TABLE_NAME
import com.helpfulapps.domain.models.weather.WeatherInfo
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.rx2.structure.BaseRXModel
import com.helpfulapps.domain.models.weather.Rain as DomainRain
import com.helpfulapps.domain.models.weather.Snow as DomainSnow
import com.helpfulapps.domain.models.weather.Temperature as DomainTemperature
import com.helpfulapps.domain.models.weather.Wind as DomainWind

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
            temperature = getTemperature(),
            rain = getRain(),
            wind = getWind(),
            snow = getSnow()
        )
    }

    private fun getTemperature(): DomainTemperature {
        return when (temperature) {
            -2 -> DomainTemperature.VERY_COLD
            -1 -> DomainTemperature.COLD
            0 -> DomainTemperature.NORMAL
            1 -> DomainTemperature.HOT
            2 -> DomainTemperature.VERY_HOT
            else -> DomainTemperature.NO_DATA
        }
    }

    private fun getRain(): DomainRain {
        return when (rain) {
            0 -> DomainRain.NO_RAIN
            1 -> DomainRain.MAY_RAIN
            2 -> DomainRain.WILL_RAIN
            3 -> DomainRain.HEAVY_RAIN
            else -> DomainRain.NO_DATA
        }
    }

    private fun getWind(): DomainWind {
        return when (wind) {
            0 -> DomainWind.NORMAL
            1 -> DomainWind.WINDY
            2 -> DomainWind.VERY_WINDY
            else -> DomainWind.NO_DATA
        }
    }

    private fun getSnow(): DomainSnow {
        return when (snow) {
            0 -> DomainSnow.NORMAL
            1 -> DomainSnow.SNOWY
            2 -> DomainSnow.VERY_SNOWY
            else -> DomainSnow.NO_DATA
        }
    }

    companion object {
        const val TABLE_NAME = "WeatherInfo"
    }

}