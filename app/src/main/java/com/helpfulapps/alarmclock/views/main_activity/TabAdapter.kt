package com.helpfulapps.alarmclock.views.main_activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.helpfulapps.alarmclock.views.clock_fragment.ClockFragment
import com.helpfulapps.alarmclock.views.hourwatch_fragment.HourWatchFragment
import com.helpfulapps.alarmclock.views.stopwatch_fragment.StopwatchFragment

class TabAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            TAB_2 -> HourWatchFragment()
            TAB_3 -> StopwatchFragment()
            else -> ClockFragment()
        }
    }

    override fun getCount(): Int = NB_TABS

    companion object {

        private val TAB_1 = 0
        private val TAB_2 = 1
        private val TAB_3 = 2

        private val NB_TABS = 3
    }
}