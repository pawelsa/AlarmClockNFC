package com.helpfulapps.alarmclock

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class MyFragmentAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment? {

        when (position) {
            TAB_1 -> {
                return EasyTabTextFragmentTab1()
            }

            TAB_2 -> {
                return EasyTabTextFragmentTab2()
            }

            TAB_3 -> {
                return EasyTabTextFragmentTab3()
            }

            else -> return null
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