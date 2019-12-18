package com.helpfulapps.alarmclock.helpers.extensions

import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.helpfulapps.alarmclock.R

fun FloatingActionButton.showFab() {
    show()

    clearAnimation()
    val animation =
        AnimationUtils.loadAnimation(context, R.anim.pop_up)
    startAnimation(animation)
}


fun View.marginParams(): ViewGroup.MarginLayoutParams {
    return this.layoutParams as ViewGroup.MarginLayoutParams
}