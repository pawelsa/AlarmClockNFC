package com.helpfulapps.data.db.weather.model

import com.helpfulapps.data.AlarmAppDatabase
import com.helpfulapps.data.db.weather.model.DayWeather.Companion.TABLE_NAME
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

@Table(database = AlarmAppDatabase::class, name = TABLE_NAME)
class DayWeather(
    @PrimaryKey(autoincrement = true)
    var id: Int = 0,
    var dt: Long = 0,
    @ColumnIgnore
    var hourWeatherList: List<HourWeather> = listOf()
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
            weather.save()
        }
        return res
    }

}