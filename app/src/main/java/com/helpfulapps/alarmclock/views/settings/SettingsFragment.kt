package com.helpfulapps.alarmclock.views.settings


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceFragmentCompat
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.di.Modules

/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = Modules.SHARED_PREFERENCES_KEY
        addPreferencesFromResource(R.xml.preferences)
    }

}
