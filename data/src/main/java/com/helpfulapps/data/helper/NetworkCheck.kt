package com.helpfulapps.data.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.helpfulapps.domain.exceptions.WeatherException
import io.reactivex.Maybe

class NetworkCheck(context: Context) {

    val cm: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val isConnectedToNetwork: Maybe<Boolean>
        get() = Maybe.create { e ->
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
            if (activeNetwork != null && activeNetwork.isConnected) {
                e.onSuccess(true)
            } else {
                e.onError(WeatherException("No internet connection"))
            }
        }
}