package com.helpfulapps.data.api.weather.api

import com.helpfulapps.data.api.weather.model.ForecastForCity

import io.reactivex.Maybe
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiCalls {

    companion object {
        const val API_KEY = "b5510c64f6f33e494c575acf9befcc8f"
    }

    @GET("forecast")
    fun downloadForecast(
        @Query("q") cityName: String, @Query("units") units: String,
        @Query("APPID") APIID: String = API_KEY
    ): Maybe<Response<ForecastForCity>>

    @GET("forecast")
    fun downloadForecastForCoordinates(
        @Query("lat") lat: String, @Query("lon") lon: String,
        @Query("units") units: String, @Query("APPID") APIID: String = API_KEY
    ): Maybe<Response<ForecastForCity>>
}
