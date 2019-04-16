package com.helpfulapps.alarmclock.main_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {

    private val TAG = this::class.java.name

    private val onMenuClick = MutableLiveData<Boolean>()

    fun onMenuClicked() {
        if (onMenuClick.value == null) {
            onMenuClick.value = true
        }
        onMenuClick.value = !onMenuClick.value!!
    }

    fun listenToMenuButtonClicks() = onMenuClick as LiveData<Boolean>
}