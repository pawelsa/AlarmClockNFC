package com.helpfulapps.alarmclock.main_activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.helpfulapps.alarmclock.clock_fragment.ClockFragment
import com.helpfulapps.alarmclock.hourwatch_fragment.HourWatchFragment
import com.helpfulapps.alarmclock.stopwatch_fragment.StopwatchFragment

class TabAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment? {

        return when (position) {
            TAB_1 -> {
                ClockFragment()
            }

            TAB_2 -> {
                HourWatchFragment()
            }

            TAB_3 -> {
                StopwatchFragment()
            }
            else -> null
        }
    }

    override fun getCount(): Int {
        return NB_TABS
    }

    companion object {

        private val TAB_1 = 0
        private val TAB_2 = 1
        private val TAB_3 = 2

        private val NB_TABS = 3
    }
}