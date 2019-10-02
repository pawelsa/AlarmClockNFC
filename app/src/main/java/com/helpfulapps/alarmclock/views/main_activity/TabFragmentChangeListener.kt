package com.helpfulapps.alarmclock.views.main_activity

import android.util.Log
import android.view.animation.AnimationUtils
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.SCROLL_STATE_IDLE
import androidx.viewpager.widget.ViewPager.SCROLL_STATE_SETTLING
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.helpfulapps.alarmclock.R

class TabFragmentChangeListener(
    private val floatingActionButton: FloatingActionButton
) : ViewPager.OnPageChangeListener {


    private val TAG = this.javaClass.name

    private var state = SCROLL_STATE_IDLE
    private var isFloatButtonHidden = false

    private var position = 0

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

        if (!isFloatButtonHidden && state == 1 && positionOffset.toDouble() != 0.0) {
            isFloatButtonHidden = true

            //hide floating action button
            swappingAway()
        }
    }

    override fun onPageSelected(position: Int) {
        //reset floating
        this.position = position
        if (state == SCROLL_STATE_SETTLING) {
            //have end in selected tab
            isFloatButtonHidden = false
            selectedTabs(position)
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
        //state 0 = nothing happen, state 1 = beginning scrolling, state 2 = stop at selected tab.
        this.state = state
        if (state == SCROLL_STATE_IDLE) {
            isFloatButtonHidden = false
        } else if (state == 2 && isFloatButtonHidden) {
            //this only happen if user is swapping but swap back to current tab (cancel to change tab)
            selectedTabs(position)
        }

    }

    fun onPageStarting(position: Int) {
        selectedTabs(position)
    }

    private fun swappingAway() {
        floatingActionButton.clearAnimation()
        val animation = AnimationUtils.loadAnimation(floatingActionButton.context, R.anim.pop_down)
        floatingActionButton.startAnimation(animation)
    }

    private fun selectedTabs(tab: Int) {
        Log.d(TAG, "selectedTab : $tab")
        val icon = when (tab) {
            0 -> R.drawable.ic_add
            else -> R.drawable.ic_start
        }
        floatingActionButton.setImageResource(icon)
        floatingActionButton.show()

        floatingActionButton.clearAnimation()
        val animation = AnimationUtils.loadAnimation(floatingActionButton.context, R.anim.pop_up)
        floatingActionButton.startAnimation(animation)
    }
}