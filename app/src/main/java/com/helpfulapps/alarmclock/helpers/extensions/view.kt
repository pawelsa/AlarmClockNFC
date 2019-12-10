package com.helpfulapps.alarmclock.helpers.extensions

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.Animation
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

fun View.startBlinking() {
    ObjectAnimator.ofInt(this, "visibility", View.VISIBLE, View.INVISIBLE, View.VISIBLE)
        .apply {
            duration = 1000
            repeatMode = ValueAnimator.RESTART
            repeatCount = Animation.INFINITE
            start()
        }
}