package com.helpfulapps.data.db.weather.db_model

import com.helpfulapps.data.db.AlarmAppDatabase
import com.helpfulapps.data.db.weather.db_model.ForecastDbModel.Companion.NAME
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

// TODO mapping between api, db, and domain
// TODO think about what data class/classes of weather should be in domain to easily get them and assign them to recyclerView items and new item being added

@Table(name = NAME, database = AlarmAppDatabase::class, allFields = true)
data class ForecastDbModel(

    @PrimaryKey(autoincrement = true)
    var id: Long = 0L,
    var cityName: String = "",
    var timezone: Int = -1,
    @ColumnIgnore
    var weatherList: List<WeatherDbModel> = listOf()
) : BaseRXModel() {

    companion object {
        const val NAME = "ForecastTable"
    }

    @OneToMany(methods = arrayOf(OneToMany.Method.ALL), variableName = "weatherList")
    public fun oneToManyWeathers() : List<WeatherDbModel> =
        (select from WeatherDbModel::class where  WeatherDbModel_Table.forecast_id.eq(id)).list
    /*
    fun toDomain() : Forecast =
        Forecast(

        )*/

}