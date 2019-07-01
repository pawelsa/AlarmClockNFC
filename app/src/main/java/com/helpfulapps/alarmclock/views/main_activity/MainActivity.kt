package com.helpfulapps.alarmclock.views.main_activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val TAG = this::class.java.name

    private val pager: TabFragmentChangeListener by lazy { TabFragmentChangeListener(fab_main_fab) }

    private val popupMenu by lazy {
        val popup = PopupMenu(applicationContext, ib_main_menu)
        popup.menuInflater.inflate(R.menu.clock_menu, popup.menu)
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_settings -> {
                    Log.d(TAG, "onMenuClicked: settings")
                    true
                }
                R.id.menu_help -> {
                    Log.d(TAG, "onMenuClicked: help")
                    true
                }
                else -> {
                    false
                }
            }
        }
        popup
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)


        viewModel.listenToMenuButtonClicks().observe(this, Observer {
            popupMenu.show()
        })

        initTabs()
    }

    private fun initTabs() {
        setupFragmentChangeListener()

        val pagerAdapter = TabAdapter(supportFragmentManager)
        vp_main_tab_pager.adapter = pagerAdapter
        vp_main_tab_pager.currentItem = 0
        et_main_tab_layout.viewPager = vp_main_tab_pager

        manageFragmentLaunching(intent)
    }

    private fun setupFragmentChangeListener() {

        vp_main_tab_pager.addOnPageChangeListener(pager)
    }

    private fun manageFragmentLaunching(intent: Intent) {
        when (intent.action) {
            "com.helpfulapps.alarmclock.addAlarm" -> addAlarm()
            "com.helpfulapps.alarmclock.setHourglass" -> setHourglass()
            "com.helpfulapps.alarmclock.startStopwatch" -> startStopwatch()
        }
    }

    private fun addAlarm() {
        Log.d(TAG, "addAlarm: ")
        vp_main_tab_pager.setCurrentItem(0, false)
        pager.onPageStarting(0)
    }

    private fun setHourglass() {
        Log.d(TAG, "setHourglass: ")
        vp_main_tab_pager.setCurrentItem(1, false)
        pager.onPageStarting(1)
    }

    private fun startStopwatch() {
        Log.d(TAG, "startStopwatch: ")
        vp_main_tab_pager.setCurrentItem(2, false)
        pager.onPageStarting(2)
    }

}
