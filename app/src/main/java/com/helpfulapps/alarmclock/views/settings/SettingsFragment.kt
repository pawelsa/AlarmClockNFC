package com.helpfulapps.alarmclock.views.settings


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.LocationManager
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.work.*
import com.helpfulapps.alarmclock.App
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.di.Modules
import com.helpfulapps.alarmclock.helpers.ShortPermissionListener
import com.helpfulapps.alarmclock.helpers.layout_helpers.buildGpsEnableAlarmDialog
import com.helpfulapps.alarmclock.helpers.startVersionedService
import com.helpfulapps.alarmclock.service.ForecastForLocalizationService
import com.helpfulapps.alarmclock.worker.DownloadWeatherWorker
import com.helpfulapps.domain.helpers.Settings
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionGrantedResponse
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit


class SettingsFragment : PreferenceFragmentCompat() {

    val viewModel: SettingsViewModel by viewModel()
    val settings: Settings by inject()

    private val sharedPreferenceListener: SharedPreferences.OnSharedPreferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == Settings.KEY_CITY) {
                setupWorkForDownloadingForecast()
            }
            if (key == Settings.KEY_USE_MOBILE_DATA) {
                setupWorkForDownloadingForecast()
            }
        }

    private fun setupWorkForDownloadingForecast() {
        val downloadConstraints = Constraints.Builder()
            .apply {
                if (settings.useMobileData)
                    setRequiredNetworkType(NetworkType.CONNECTED)
                else
                    setRequiredNetworkType(NetworkType.UNMETERED)
            }
            .build()

        val downloadWeather =
            PeriodicWorkRequestBuilder<DownloadWeatherWorker>(24, TimeUnit.HOURS)
                .setConstraints(downloadConstraints)
                .build()
        WorkManager.getInstance(context!!).enqueueUniquePeriodicWork(
            App.FORECAST_DOWNLOAD_WORK,
            ExistingPeriodicWorkPolicy.REPLACE,
            downloadWeather
        )
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = Modules.SHARED_PREFERENCES_KEY
        addPreferencesFromResource(R.xml.preferences)

        val forecastForLocalization =
            findPreference<Preference>("com.helpfulapps.alarmclock.city_gps")

        forecastForLocalization?.setOnPreferenceClickListener {

            Dexter.withActivity(activity as SettingsActivity)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(object : ShortPermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                        super.onPermissionGranted(response)

                        val lm: LocationManager =
                            context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                        var gpsEnabled = false
                        var networkEnabled = false
                        try {
                            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
                            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                        } catch (e: Exception) {
                        }
                        if (gpsEnabled && networkEnabled) {
                            context?.startVersionedService(
                                Intent(
                                    context,
                                    ForecastForLocalizationService::class.java
                                )
                            )
                        } else {
                            buildGpsEnableAlarmDialog(context!!) {
                                startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                            }.show()
                        }
                    }
                })
                .check()
            true
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(
            sharedPreferenceListener
        )
    }

    override fun onStop() {
        super.onStop()
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(
            sharedPreferenceListener
        )
    }

}
