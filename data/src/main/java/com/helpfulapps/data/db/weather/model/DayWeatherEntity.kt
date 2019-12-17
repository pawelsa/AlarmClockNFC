package com.helpfulapps.data.db.weather.model

import com.helpfulapps.data.AlarmAppDatabase
import com.helpfulapps.data.db.weather.model.DayWeatherEntity.Companion.TABLE_NAME
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
data class DayWeatherEntity(
    @PrimaryKey(autoincrement = true)
    var id: Int = 0,
    var dt: Long = 0,
    var cityName: String = "",
    @ColumnIgnore
    var hourWeatherEntityList: List<HourWeatherEntity> = listOf(),
    @ForeignKey(stubbedRelationship = true)
    var weatherInfoEntity: WeatherInfoEntity? = null
) : BaseRXModel() {

    companion object {
        const val TABLE_NAME = "DayWeather"
    }

    @OneToMany(methods = [OneToMany.Method.ALL], variableName = "hourWeatherEntityList")
    fun oneToManyHourWeathers(): List<HourWeatherEntity> =
        (select from HourWeatherEntity::class where HourWeatherEntity_Table.dayWeatherEntity_id.eq(
            id
        )).list

    override fun save(): Single<Boolean> {
        val res = super.save()
        hourWeatherEntityList.forEach { weather ->
            weather.dayWeatherEntity = this@DayWeatherEntity
        }
        return res
    }

    fun toDomain(): DomainDayWeather {
        if (this.hourWeatherEntityList.isEmpty()) {
            this.hourWeatherEntityList =
                (select from HourWeatherEntity::class where HourWeatherEntity_Table.dayWeatherEntity_id.eq(
                    this.id
                )).list
        }
        this.weatherInfoEntity =
            (select from WeatherInfoEntity::class where WeatherInfoEntity_Table.id.eq(
                weatherInfoEntity?.id
            )).querySingle()
        return DomainDayWeather(
            id = this.id,
            dt = this.dt,
            cityName = this.cityName,
            hourWeatherList = this.hourWeatherEntityList
                .map { hourWeather -> hourWeather.toDomain() },
            weatherInfo = weatherInfoEntity?.toDomain() ?: DomainWeatherInfo()
        )
    }
}

data class DayWeatherData(
    var id: Int = 0,
    var dt: Long = 0,
    var cityName: String = "",
    var hourWeatherEntityList: List<HourWeatherEntity> = listOf(),
    var weatherInfoEntity: WeatherInfoEntity? = null
)