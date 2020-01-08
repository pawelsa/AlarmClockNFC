package com.helpfulapps.alarmclock.views.settings

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val views = findViewById<View>(android.R.id.content)
        ViewCompat.setOnApplyWindowInsetsListener(views) { view, insets ->
            view.updatePadding(top = insets.systemWindowInsetTop)
            insets
        }

        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, SettingsFragment())
            .commit()
    }
}
