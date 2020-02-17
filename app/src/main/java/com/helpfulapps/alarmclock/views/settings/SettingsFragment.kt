package com.helpfulapps.alarmclock.views.settings


import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.di.Modules
import com.helpfulapps.alarmclock.helpers.ShortPermissionListener
import com.helpfulapps.alarmclock.helpers.extensions.startVersionedService
import com.helpfulapps.alarmclock.service.ForecastForLocalizationService
import com.helpfulapps.alarmclock.worker.CreateWork
import com.helpfulapps.domain.helpers.Settings
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionGrantedResponse
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel


class SettingsFragment : PreferenceFragmentCompat() {

    val viewModel: SettingsViewModel by viewModel()
    val settings: Settings by inject()

    private val sharedPreferenceListener: SharedPreferences.OnSharedPreferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == Settings.KEY_CITY) {
                CreateWork.oneTimeWeatherDownload(context!!, settings.useMobileData)
            }
            if (key == Settings.KEY_USE_MOBILE_DATA) {
                CreateWork.periodicWeatherDownload(context!!, settings.useMobileData)
            }
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
                        context?.startVersionedService(
                            Intent(
                                context,
                                ForecastForLocalizationService::class.java
                            )
                        )
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
