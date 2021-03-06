package com.helpfulapps.device.alarms.helpers

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.helpfulapps.domain.exceptions.WeatherException
import com.helpfulapps.domain.helpers.NetworkCheck
import io.reactivex.Maybe


class NetworkCheckImpl(context: Context) : NetworkCheck {

    val cm: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override val isConnectedToNetwork: Maybe<Boolean>
        get() = Maybe.create { e ->
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
            if (activeNetwork != null && activeNetwork.isConnected) {
                e.onSuccess(true)
            } else {
                e.onError(WeatherException("No internet connection"))
            }
        }
}