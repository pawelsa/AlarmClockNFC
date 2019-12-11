package com.helpfulapps.alarmclock.views.hourwatch_fragment

import android.animation.ObjectAnimator
import android.content.Intent
import android.util.Log
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.FragmentHourwatchBinding
import com.helpfulapps.alarmclock.helpers.extensions.observe
import com.helpfulapps.alarmclock.helpers.extensions.showFab
import com.helpfulapps.alarmclock.helpers.extensions.startBlinking
import com.helpfulapps.alarmclock.service.TimerService
import com.helpfulapps.alarmclock.views.main_activity.MainActivity
import com.helpfulapps.base.base.BaseFragment
import com.helpfulapps.domain.eventBus.ServiceBus
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

    private var animation: ObjectAnimator? = null
    private var wasStarted = false

    override fun init() {

        viewModel.listenToTimer()
    }

    override fun onResume() {
        super.onResume()

        fab.setOnClickListener {
            when (currentState) {
                is HourWatchViewModel.TimerState.Paused -> restartTimer()
                is HourWatchViewModel.TimerState.Update -> pauseTimer()
                is HourWatchViewModel.TimerState.TimeIsUp -> resetTimer()
                is HourWatchViewModel.TimerState.Finished -> startTimer()
            }
        }

        bt_reset.setOnClickListener {
            if (currentState is HourWatchViewModel.TimerState.Update
                || currentState is HourWatchViewModel.TimerState.TimeIsUp
                || currentState is HourWatchViewModel.TimerState.Paused
            ) {
                resetTimer()
            }
        }
        // handle when service should go foreground
        // create notification for service
        // notification should be updated with new time
        // in notification user should be able to increase time by 1 minute or pause the timer
        // taping on notification should open app with timer tab active

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

    private fun handleState(it: HourWatchViewModel.TimerState) {
        Log.d(TAG, "state: ${it.javaClass.simpleName}")
        when (it) {
            is HourWatchViewModel.TimerState.Start -> setupStartedTimer()
            is HourWatchViewModel.TimerState.Update -> updateTimer(it)
            is HourWatchViewModel.TimerState.TimeIsUp -> setupTimerIsUp()
            is HourWatchViewModel.TimerState.Finished -> setupFinishTimer(it)
            is HourWatchViewModel.TimerState.Paused -> setupPauseTimer()
        }
    }

    private fun setupPauseTimer() {
        changeFabIconToStart()
        animation = tv_time_left.startBlinking()
        animation?.start()
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
        animation?.end()
        animation?.cancel()
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
        stopBlinking()
        changeFabIconToStop()
        pk_timer_picker.visibility = View.GONE
        changeFabIconToStop()
        tv_time_left.text = "0"
        animation = tv_time_left.startBlinking()
        animation?.start()
    }

    private fun updateTimer(updateTimer: HourWatchViewModel.TimerState.Update) {
        if (!wasStarted) {
            setupStartedTimer()
        }
        stopBlinking()
        tv_time_left.text = updateTimer.time.toString()
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