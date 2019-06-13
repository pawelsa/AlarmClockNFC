package com.helpfulapps.data.db.weather.model

import com.helpfulapps.data.AlarmAppDatabase
import com.helpfulapps.data.db.weather.model.ForecastDbModel.Companion.NAME
import com.helpfulapps.domain.models.weather.Forecast
import com.raizlabs.android.dbflow.annotation.ColumnIgnore
import com.raizlabs.android.dbflow.annotation.OneToMany
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.list
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.kotlinextensions.where
import com.raizlabs.android.dbflow.rx2.structure.BaseRXModel
import io.reactivex.Single

@Table(name = NAME, database = AlarmAppDatabase::class, allFields = true)
data class ForecastDbModel(

    @PrimaryKey
    var id: Long = 0L,
    var cityName: String = "",
    var timezone: Int = -1,
    @ColumnIgnore
    var weatherList: List<WeatherDbModel> = listOf()
) : BaseRXModel() {

    companion object {
        const val NAME = "ForecastTable"
    }

    @OneToMany(methods = [OneToMany.Method.ALL], variableName = "weatherList")
    fun oneToManyWeathers(): List<WeatherDbModel> =
        (select from WeatherDbModel::class where WeatherDbModel_Table.forecast_id.eq(id)).list

    override fun save(): Single<Boolean> {
        val res = super.save()
        weatherList.forEach { weather ->
            weather.forecast = this
            weather.save()
        }
        return res
    }

    fun toDomain(): Forecast =
        Forecast(
            cityName = cityName,
            timezone = timezone,
            weatherList = weatherList.map { weather -> weather.toDomain() }
        )

}