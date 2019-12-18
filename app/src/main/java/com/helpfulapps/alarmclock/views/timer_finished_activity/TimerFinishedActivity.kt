package com.helpfulapps.alarmclock.views.timer_finished_activity

import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.ViewCompat
import androidx.core.view.marginBottom
import androidx.core.view.updatePadding
import com.google.android.material.snackbar.Snackbar
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.ActivityTimerFinishedBinding
import com.helpfulapps.alarmclock.service.TimerService
import com.helpfulapps.base.base.BaseActivity
import com.helpfulapps.domain.eventBus.ServiceBus
import kotlinx.android.synthetic.main.activity_timer_finished.*
import org.koin.android.viewmodel.ext.android.viewModel

class TimerFinishedActivity : BaseActivity<TimerFinishedViewModel, ActivityTimerFinishedBinding>() {

    override val layoutId: Int = R.layout.activity_timer_finished
    override val viewModel: TimerFinishedViewModel by viewModel()
    private val blinkingAnimation: Animation by lazy {
        AnimationUtils.loadAnimation(
            baseContext,
            R.anim.blink
        )
    }
    private val fabBottomPadding by lazy { binding.fabTimerFinishedTimer.marginBottom }

    override fun init() {

        tv_timer_finished_time.startAnimation(blinkingAnimation)

        setupFabListener()
        setupWindowInsets()
        ViewCompat.requestApplyInsets(binding.root)
    }

    private fun setupFabListener() {
        fab_timer_finished_timer.setOnClickListener {
            ServiceBus.publish(TimerService.TimerServiceEvent.FinishTimer)
            finish()
        }
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            view.updatePadding(top = insets.systemWindowInsetTop)
            (binding.fabTimerFinishedTimer.layoutParams as ViewGroup.MarginLayoutParams).bottomMargin =
                fabBottomPadding + insets.systemGestureInsets.bottom
            insets
        }
    }

    override fun showMessage(text: String) {
        Snackbar.make(cl_timer_finished_base, text, Snackbar.LENGTH_LONG).show()
    }
}
