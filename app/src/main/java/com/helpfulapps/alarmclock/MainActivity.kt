package com.helpfulapps.alarmclock

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val TAG = this::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pagerAdapter = TabAdapter(supportFragmentManager)
        viewpager.adapter = pagerAdapter
        viewpager.currentItem = 0
        easyTabs.viewPager = viewpager

        val pager2 = Pager(floatingActionButton)
        viewpager.addOnPageChangeListener(pager2)

        when (intent.action) {
            "com.helpfulapps.alarmclock.addAlarm" -> addAlarm()
            "com.helpfulapps.alarmclock.setHourglass" -> setHourglass()
            "com.helpfulapps.alarmclock.startStopwatch" -> startStopwatch()
        }

    }

    fun addAlarm() {
        Log.d(TAG, "addAlarm: ")
        viewpager.setCurrentItem(0, false)
    }

    fun setHourglass() {
        Log.d(TAG, "setHourglass: ")
        viewpager.setCurrentItem(1, false)
    }

    fun startStopwatch() {
        Log.d(TAG, "startStopwatch: ")
        viewpager.setCurrentItem(2, false)
    }

}

class Pager2(private val floatingActionButton: FloatingActionButton) : ViewPager.OnPageChangeListener {
    private var state = 0
    private var isFloatButtonHidden = false
    private var position = 0

    private val TAG = this.javaClass.name

    private var last = false

    var first: Boolean = true


    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
/*

        if (first && positionOffset == 0f && positionOffsetPixels == 0){
            onPageSelected(0)
            first = false
        }
*/

        if (!last) {
            Log.d(TAG, "onPageScrolled: position : $position")
            last = true
        }
    }

    override fun onPageSelected(position: Int) {
        this.position = position
        Log.d(TAG, "onPageSelected: $position")
        last = false
    }

    override fun onPageScrollStateChanged(state: Int) {
        //state 0 = nothing happen, state 1 = begining scrolling, state 2 = stop at selected tab.
        this.state = state
        Log.d(TAG, "onPageScrollStateChanged: state : $state")
        last = false
    }
}
