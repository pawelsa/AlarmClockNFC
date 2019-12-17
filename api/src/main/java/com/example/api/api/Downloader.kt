package com.example.api.api

import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object Downloader {
    fun create(): com.example.api.api.ApiCalls {

        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(
                RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())
            )
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .baseUrl("http://api.openweathermap.org/data/2.5/")
            .build()

        return retrofit.create(com.example.api.api.ApiCalls::class.java)
    }
}