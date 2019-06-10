package com.helpfulapps.data.db.weather.api

import com.helpfulapps.data.db.weather.model.ForecastForCity

import io.reactivex.Maybe
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiCalls {

    @GET("forecast")
    fun getForecast(
        @Query("q") cityName: String, @Query("units") units: String,
        @Query("APPID") APIID: String
    ): Maybe<Response<ForecastForCity>>

    @GET("forecast")
    fun getForecastForCoordinates(
        @Query("lat") lat: String, @Query("lon") lon: String,
        @Query("units") units: String, @Query("APPID") APIID: String
    ): Maybe<Response<ForecastForCity>>
}
