package com.helpfulapps.alarmclock.views.main_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.helpfulapps.base.base.BaseViewModel

class MainActivityViewModel : BaseViewModel() {

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