package com.helpfulapps.data.db.weather.model

import com.helpfulapps.data.AlarmAppDatabase
import com.helpfulapps.data.db.weather.model.DayWeather.Companion.TABLE_NAME
import com.raizlabs.android.dbflow.annotation.*
import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.list
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.kotlinextensions.where
import com.raizlabs.android.dbflow.rx2.structure.BaseRXModel
import io.reactivex.Single
import com.helpfulapps.domain.models.weather.DayWeather as DomainDayWeather
import com.helpfulapps.domain.models.weather.WeatherInfo as DomainWeatherInfo

@Table(database = AlarmAppDatabase::class, name = TABLE_NAME, allFields = true)
data class DayWeather(
    @PrimaryKey(autoincrement = true)
    var id: Int = 0,
    var dt: Long = 0,
    var cityName: String = "",
    @ColumnIgnore
    var hourWeatherList: List<HourWeather> = listOf(),
    @ForeignKey(stubbedRelationship = true)
    var weatherInfo: WeatherInfo? = null
) : BaseRXModel() {

    companion object {
        const val TABLE_NAME = "DayWeather"
    }

    @OneToMany(methods = [OneToMany.Method.ALL], variableName = "hourWeatherList")
    fun oneToManyHourWeathers(): List<HourWeather> =
        (select from HourWeather::class where HourWeather_Table.dayWeather_id.eq(id)).list

    override fun save(): Single<Boolean> {
        val res = super.save()
        hourWeatherList.forEach { weather ->
            weather.dayWeather = this@DayWeather
        }
        return res
    }

    fun toDomain(): DomainDayWeather {
        if (this.hourWeatherList.isEmpty()) {
            this.hourWeatherList =
                (select from HourWeather::class where HourWeather_Table.dayWeather_id.eq(this.id)).list
        }
        this.weatherInfo =
            (select from WeatherInfo::class where WeatherInfo_Table.id.eq(weatherInfo?.id)).querySingle()
        return DomainDayWeather(
            id = this.id,
            dt = this.dt,
            cityName = this.cityName,
            hourWeatherList = this.hourWeatherList
                .map { hourWeather -> hourWeather.toDomain() },
            weatherInfo = weatherInfo?.toDomain() ?: DomainWeatherInfo()
        )
    }
}