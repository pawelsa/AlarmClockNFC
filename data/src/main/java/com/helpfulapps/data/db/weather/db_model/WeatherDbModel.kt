package com.helpfulapps.data.db.weather.db_model

import com.helpfulapps.data.db.AlarmAppDatabase
import com.raizlabs.android.dbflow.annotation.ForeignKey
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.rx2.structure.BaseRXModel


@Table(name = WeatherDbModel.NAME, database = AlarmAppDatabase::class, allFields = true)
data class WeatherDbModel(
    
    @PrimaryKey(autoincrement = true)
    var id :Long = 0L,
    var timestamp: Int = -1,
    var cloudPercent : Int = -1,
    var windSpeed: Double = -1.0,
    var snowIn3h:Int = -1,
    var rainIn3h:Int = -1,
    var temp:Double = -1.0,
    var tempMax:Double = -1.0,
    var tempMin:Double = -1.0,
    var description:String = "",
    var icon:String = "",
    @ForeignKey(stubbedRelationship = true)
    var forecast:ForecastDbModel? = null
) : BaseRXModel() {

    companion object{
        const val NAME = "WeatherTable"
    }
}