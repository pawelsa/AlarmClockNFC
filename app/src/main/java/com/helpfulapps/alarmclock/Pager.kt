package com.helpfulapps.alarmclock

import android.util.Log
import android.view.animation.AnimationUtils
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Pager(private val floatingActionButton: FloatingActionButton) : ViewPager.OnPageChangeListener {
    private var state = 0
    private var isFloatButtonHidden = false
    private var position = 0

    private val TAG = this.javaClass.name
    var first: Boolean = true

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

        if (first && positionOffset == 0f && positionOffsetPixels == 0){
            onPageSelected(0)
            first = false
        }
        Log.d(TAG, "onPageScrolled: $position")
        if (!isFloatButtonHidden && state == 1 && positionOffset.toDouble() != 0.0) {
            isFloatButtonHidden = true

            //hide floating action button
            swappingAway()
        }
    }

    override fun onPageSelected(position: Int) {
        //reset floating
        this.position = position
        Log.d(TAG, "onPageSelected: $position")
        if (state == 2) {
            //have end in selected tab
            isFloatButtonHidden = false
            selectedTabs(position)

        }
    }

    override fun onPageScrollStateChanged(state: Int) {
        //state 0 = nothing happen, state 1 = begining scrolling, state 2 = stop at selected tab.
        this.state = state
        if (state == 0) {
            isFloatButtonHidden = false
        } else if (state == 2 && isFloatButtonHidden) {
            //this only happen if user is swapping but swap back to current tab (cancel to change tab)
            selectedTabs(position)
        }

    }


    private fun swappingAway() {
        floatingActionButton.clearAnimation()
        val animation = AnimationUtils.loadAnimation(floatingActionButton.context, R.anim.pop_down)
        floatingActionButton.startAnimation(animation)
    }

    private fun selectedTabs(tab: Int) {
        Log.d(TAG, "selectedTabs: $tab")
        val icon = when (tab) {
            0 -> R.drawable.ic_add
            else -> R.drawable.ic_start
        }
        floatingActionButton.setImageResource(icon)
        floatingActionButton.show()

        floatingActionButton.clearAnimation()
        val animation = AnimationUtils.loadAnimation(floatingActionButton.context, R.anim.pop_up)
        floatingActionButton.startAnimation(animation)

        //you can do more task. for example, change color for each tabs, or custom action for each tabs.
    }
}