package com.helpfulapps.data.db.weather.model

import com.helpfulapps.data.AlarmAppDatabase
import com.helpfulapps.data.db.weather.model.HourWeather.Companion.TABLE_NAME
import com.raizlabs.android.dbflow.annotation.ForeignKey
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.rx2.structure.BaseRXModel

@Table(database = AlarmAppDatabase::class, name = TABLE_NAME, allFields = true)
class HourWeather(
    @PrimaryKey(autoincrement = true)
    var id: Int = 0,
    var dt: Long = 0,
    var clouds: Int = 0,
    var rain: Double = 0.0,
    var snow: Double = 0.0,
    var wind: Double = 0.0,
    var humidity: Int = 0,
    var pressure: Double = 0.0,
    var temp: Double = 0.0,
    var tempMax: Double = 0.0,
    var tempMin: Double = 0.0,
    @ForeignKey(stubbedRelationship = true)
    var dayWeather: DayWeather? = null
) : BaseRXModel() {

    companion object {
        const val TABLE_NAME = "HourWeather"
    }
}