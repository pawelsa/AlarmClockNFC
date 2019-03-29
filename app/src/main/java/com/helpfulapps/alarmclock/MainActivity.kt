package com.helpfulapps.alarmclock

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pagerAdapter = MyFragmentAdapter(supportFragmentManager)
        viewpager.adapter = pagerAdapter
        easyTabs.viewPager = viewpager


    }
}
