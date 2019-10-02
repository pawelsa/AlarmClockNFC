package com.helpfulapps.alarmclock.views.clock_fragment

import com.helpfulapps.base.base.BaseViewModel


class ClockViewModel : BaseViewModel() {

    private lateinit var adapter: ClockListAdapter

    fun setAdapter(adapter: ClockListAdapter) {
        this.adapter = adapter
    }

    fun getAdapter() = adapter

}