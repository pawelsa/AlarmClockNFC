package com.helpfulapps.alarmclock.views.main_activity

import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.ViewCompat
import androidx.core.view.marginBottom
import androidx.core.view.updatePadding
import com.google.android.material.snackbar.Snackbar
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.ActivityMainBinding
import com.helpfulapps.base.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity<MainActivityViewModel, ActivityMainBinding>() {

    override val viewModel: MainActivityViewModel by viewModel()
    override val layoutId: Int = R.layout.activity_main

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


    override fun init() {
        initTabs()
        manageFragmentLaunching(intent)

        viewModel.downloadForeacast()

        binding.clMainRoot.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            view.updatePadding(top = insets.systemWindowInsetTop)
            (binding.fabMainFab.layoutParams as ViewGroup.MarginLayoutParams).bottomMargin =
                binding.fabMainFab.marginBottom + insets.systemGestureInsets.bottom
            insets.consumeSystemWindowInsets()
        }

        ib_main_menu.setOnClickListener {
            popupMenu.show()
        }

    }

    private fun initTabs() {
        setupFragmentChangeListener()

        val pagerAdapter = TabAdapter(supportFragmentManager)
        vp_main_tab_pager.offscreenPageLimit = 2
        vp_main_tab_pager.adapter = pagerAdapter
        vp_main_tab_pager.currentItem = 0
        et_main_tab_layout.viewPager = vp_main_tab_pager
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

    override fun showMessage(text: String) {
        Snackbar.make(l_main_first_layer, text, Snackbar.LENGTH_SHORT).show()
    }

}
