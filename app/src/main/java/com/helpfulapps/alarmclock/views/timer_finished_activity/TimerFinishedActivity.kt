package com.helpfulapps.alarmclock.views.timer_finished_activity

import android.view.animation.Animation
import android.view.animation.AnimationUtils
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

    override fun init() {

        tv_timer_finished_time.startAnimation(blinkingAnimation)

        fab_timer_finished_timer.setOnClickListener {
            ServiceBus.publish(TimerService.TimerServiceEvent.FinishTimer)
            finish()
        }
    }

    override fun showMessage(text: String) {
        Snackbar.make(cl_timer_finished_base, text, Snackbar.LENGTH_LONG).show()
    }
}
