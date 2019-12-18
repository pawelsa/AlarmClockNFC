package com.helpfulapps.alarmclock.views.timer_fragment

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.marginBottom
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.FragmentTimerBinding
import com.helpfulapps.alarmclock.helpers.extensions.observe
import com.helpfulapps.alarmclock.helpers.extensions.showFab
import com.helpfulapps.alarmclock.helpers.secondsToString
import com.helpfulapps.alarmclock.service.TimerService
import com.helpfulapps.alarmclock.views.main_activity.MainActivity
import com.helpfulapps.base.base.BaseFragment
import com.helpfulapps.domain.eventBus.ServiceBus
import com.helpfulapps.domain.extensions.whenFalse
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_timer.*
import org.koin.android.viewmodel.ext.android.viewModel
import xyz.aprildown.hmspickerview.HmsPickerView


class TimerFragment : BaseFragment<TimerViewModel, FragmentTimerBinding>() {

    override val layoutId: Int = R.layout.fragment_timer
    override val viewModel: TimerViewModel by viewModel()

    private val fab: FloatingActionButton
        get() = (mainActivity as MainActivity).fab_main_fab

    private var currentState: TimerViewModel.TimerState =
        TimerViewModel.TimerState.Finished(-1)

    private val animation: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.blink) }
    private var wasStarted = false
    private val resetBottomMargin by lazy { binding.btTimerReset.marginBottom }


    override fun init() {

        viewModel.listenToTimer()

    }

    override fun onResume() {
        super.onResume()

        setupInsets()
        fabListener()
        resetButtonListener()
        checkInitState()
        subscribeToStates()
    }

    private fun setupInsets() {
        val dimensionInDp = 4 * binding.root.context.resources.displayMetrics.density.toInt()
        (binding.btTimerReset.layoutParams as ViewGroup.MarginLayoutParams).bottomMargin =
            resetBottomMargin + (activity as MainActivity).systemBottomInsets + dimensionInDp

        (binding.tvTimerLeft.layoutParams as ViewGroup.MarginLayoutParams).bottomMargin =
            (activity as MainActivity).systemBottomInsets
    }

    private fun checkInitState() {
        viewModel.timerStates.value.let {
            if (it == null) {
                setupFinishTimer(TimerViewModel.TimerState.Finished(-1))
            } else {
                handleState(it)
            }
        }
    }

    private fun subscribeToStates() {
        viewModel.timerStates.observe(this) {
            handleState(it)
            currentState = it
        }
    }

    private fun resetButtonListener() {
        bt_timer_reset.setOnClickListener {
            if (currentState is TimerViewModel.TimerState.Update
                || currentState is TimerViewModel.TimerState.TimeIsUp
                || currentState is TimerViewModel.TimerState.Paused
            ) {
                resetTimer()
            }
        }
    }

    private fun fabListener() {
        fab.setOnClickListener {
            when (currentState) {
                is TimerViewModel.TimerState.Paused -> restartTimer()
                is TimerViewModel.TimerState.Update -> pauseTimer()
                is TimerViewModel.TimerState.TimeIsUp -> resetTimer()
                is TimerViewModel.TimerState.Finished -> startTimer()
            }
        }
    }

    private fun handleState(it: TimerViewModel.TimerState) {
        when (it) {
            is TimerViewModel.TimerState.Start -> setupStartedTimer()
            is TimerViewModel.TimerState.Update -> updateTimer(it)
            is TimerViewModel.TimerState.TimeIsUp -> setupTimerIsUp()
            is TimerViewModel.TimerState.Finished -> setupFinishTimer(it)
            is TimerViewModel.TimerState.Paused -> setupPauseTimer()
            is TimerViewModel.TimerState.Restart -> setupRestartTimer()
        }
    }

    private fun setupRestartTimer() {
        stopBlinking()
    }

    private fun setupPauseTimer() {
        whenFalse(wasStarted) {
            setupStartedTimer()
        }
        changeFabIconToStart()
        tv_timer_left.startAnimation(animation)
    }

    private fun setupFinishTimer(finishTimer: TimerViewModel.TimerState.Finished) {
        stopBlinking()
        val time = if (finishTimer.time == -1L) {
            fab.hide()
            0
        } else {
            fab.showFab()
            finishTimer.time
        }
        changeFabIconToStart()
        pk_timer_picker.setTimeInMillis(time)
        pk_timer_picker.visibility = View.VISIBLE
        pk_timer_picker.setListener(getPickerListener())
        bt_timer_reset.visibility = View.GONE
        tv_timer_left.visibility = View.GONE
    }

    private fun stopBlinking() {
        tv_timer_left.clearAnimation()
    }

    private fun getPickerListener(): HmsPickerView.Listener {
        return object : HmsPickerView.Listener {
            override fun onHmsPickerViewHasNoInput(hmsPickerView: HmsPickerView) {
                if (currentState is TimerViewModel.TimerState.Finished) {
                    fab.hide()
                }
            }

            override fun onHmsPickerViewHasValidInput(hmsPickerView: HmsPickerView) {
                if (currentState is TimerViewModel.TimerState.Finished) {
                    fab.showFab()
                }
            }

            override fun onHmsPickerViewInputChanged(hmsPickerView: HmsPickerView, input: Long) =
                Unit
        }
    }

    private fun setupTimerIsUp() {
        changeFabIconToStop()
        pk_timer_picker.visibility = View.GONE
        changeFabIconToStop()
        tv_timer_left.text = "0"
        tv_timer_left.startAnimation(animation)
    }

    private fun updateTimer(updateTimer: TimerViewModel.TimerState.Update) {
        whenFalse(wasStarted) {
            setupStartedTimer()
        }
        stopBlinking()

        val toDisplay = updateTimer.time.secondsToString()
        tv_timer_left.text = toDisplay
    }

    private fun setupStartedTimer() {
        wasStarted = true
        changeFabIconToPause()
        stopBlinking()
        pk_timer_picker.visibility = View.GONE
        bt_timer_reset.visibility = View.VISIBLE
        tv_timer_left.text = (pk_timer_picker.getTimeInMillis() / 1000).toString()
        tv_timer_left.visibility = View.VISIBLE
    }

    private fun resetTimer() {
        stopBlinking()
        tv_timer_left.visibility = View.GONE
        ServiceBus.publish(TimerService.TimerServiceEvent.FinishTimer)
    }

    private fun startTimer() {
        val timeLeft = pk_timer_picker.getTimeInMillis() / 1000
        Intent(context, TimerService::class.java).let {
            it.action = TimerService.TIMER_START
            it.putExtra(TimerService.TIMER_TIME, timeLeft)
            context?.startService(it)
        }
        changeFabIconToPause()
        viewModel.setNewDefaultTimerValue(pk_timer_picker.getTimeInMillis())
    }

    private fun restartTimer() {
        stopBlinking()
        ServiceBus.publish(TimerService.TimerServiceEvent.RestartTimer)
        changeFabIconToPause()
    }

    private fun pauseTimer() {
        ServiceBus.publish(TimerService.TimerServiceEvent.PauseTimer)
        changeFabIconToStart()
    }

    private fun changeFabIconToStop() {
        fab.hide()
        fab.setImageResource(R.drawable.ic_stop)
        fab.show()
    }

    private fun changeFabIconToPause() {
        fab.hide()
        fab.setImageResource(R.drawable.ic_pause)
        fab.show()
    }

    private fun changeFabIconToStart() {
        fab.hide()
        fab.setImageResource(R.drawable.ic_start)
        fab.show()
    }
}