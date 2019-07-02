package com.helpfulapps.alarmclock.views.clock_fragment

import androidx.lifecycle.ViewModel

class ClockViewModel : ViewModel() {

    private lateinit var adapter: ClockListAdapter

    fun setAdapter(adapter: ClockListAdapter) {
        this.adapter = adapter
    }

    fun getAdapter() = adapter

}