package com.helpfulapps.alarmclock.views.hourwatch_fragment

import android.content.Intent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.FragmentHourwatchBinding
import com.helpfulapps.alarmclock.helpers.extensions.observe
import com.helpfulapps.alarmclock.helpers.extensions.showFab
import com.helpfulapps.alarmclock.helpers.secondsToString
import com.helpfulapps.alarmclock.service.TimerService
import com.helpfulapps.alarmclock.views.main_activity.MainActivity
import com.helpfulapps.base.base.BaseFragment
import com.helpfulapps.domain.eventBus.ServiceBus
import com.helpfulapps.domain.extensions.whenFalse
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_hourwatch.*
import org.koin.android.viewmodel.ext.android.viewModel
import xyz.aprildown.hmspickerview.HmsPickerView

class HourWatchFragment : BaseFragment<HourWatchViewModel, FragmentHourwatchBinding>() {

    override val layoutId: Int = R.layout.fragment_hourwatch
    override val viewModel: HourWatchViewModel by viewModel()

    private val fab: FloatingActionButton
        get() = (mainActivity as MainActivity).fab_main_fab

    private var currentState: HourWatchViewModel.TimerState =
        HourWatchViewModel.TimerState.Finished(-1)

    private val animation: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.blink) }
    private var wasStarted = false

    override fun init() {

        viewModel.listenToTimer()
    }

    override fun onResume() {
        super.onResume()

        fabListener()

        resetButtonListener()

        viewModel.timerStates.value.let {
            if (it == null) {
                setupFinishTimer(HourWatchViewModel.TimerState.Finished(-1))
            } else {
                handleState(it)
            }
        }

        viewModel.timerStates.observe(this) {
            handleState(it)
            currentState = it
        }


    }

    private fun resetButtonListener() {
        bt_reset.setOnClickListener {
            if (currentState is HourWatchViewModel.TimerState.Update
                || currentState is HourWatchViewModel.TimerState.TimeIsUp
                || currentState is HourWatchViewModel.TimerState.Paused
            ) {
                resetTimer()
            }
        }
    }

    private fun fabListener() {
        fab.setOnClickListener {
            when (currentState) {
                is HourWatchViewModel.TimerState.Paused -> restartTimer()
                is HourWatchViewModel.TimerState.Update -> pauseTimer()
                is HourWatchViewModel.TimerState.TimeIsUp -> resetTimer()
                is HourWatchViewModel.TimerState.Finished -> startTimer()
            }
        }
    }

    private fun handleState(it: HourWatchViewModel.TimerState) {
        when (it) {
            is HourWatchViewModel.TimerState.Start -> setupStartedTimer()
            is HourWatchViewModel.TimerState.Update -> updateTimer(it)
            is HourWatchViewModel.TimerState.TimeIsUp -> setupTimerIsUp()
            is HourWatchViewModel.TimerState.Finished -> setupFinishTimer(it)
            is HourWatchViewModel.TimerState.Paused -> setupPauseTimer()
            is HourWatchViewModel.TimerState.Restart -> setupRestartTimer()
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
        tv_time_left.startAnimation(animation)
    }

    private fun setupFinishTimer(finishTimer: HourWatchViewModel.TimerState.Finished) {
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
        bt_reset.visibility = View.GONE
        tv_time_left.visibility = View.GONE
    }

    private fun stopBlinking() {
        tv_time_left.clearAnimation()
    }

    private fun getPickerListener(): HmsPickerView.Listener {
        return object : HmsPickerView.Listener {
            override fun onHmsPickerViewHasNoInput(hmsPickerView: HmsPickerView) {
                if (currentState is HourWatchViewModel.TimerState.Finished) {
                    fab.hide()
                }
            }

            override fun onHmsPickerViewHasValidInput(hmsPickerView: HmsPickerView) {
                if (currentState is HourWatchViewModel.TimerState.Finished) {
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
        tv_time_left.text = "0"
        tv_time_left.startAnimation(animation)
    }

    private fun updateTimer(updateTimer: HourWatchViewModel.TimerState.Update) {
        whenFalse(wasStarted) {
            setupStartedTimer()
        }
        stopBlinking()

        val toDisplay = updateTimer.time.secondsToString()
        tv_time_left.text = toDisplay
    }

    private fun setupStartedTimer() {
        wasStarted = true
        changeFabIconToPause()
        stopBlinking()
        pk_timer_picker.visibility = View.GONE
        bt_reset.visibility = View.VISIBLE
        tv_time_left.text = (pk_timer_picker.getTimeInMillis() / 1000).toString()
        tv_time_left.visibility = View.VISIBLE
    }

    private fun resetTimer() {
        stopBlinking()
        tv_time_left.visibility = View.GONE
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