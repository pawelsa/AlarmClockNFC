package com.helpfulapps.alarmclock.views.hourwatch_fragment

import android.content.Intent
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.FragmentHourwatchBinding
import com.helpfulapps.alarmclock.helpers.extensions.observe
import com.helpfulapps.alarmclock.helpers.extensions.startBlinking
import com.helpfulapps.alarmclock.service.TimerService
import com.helpfulapps.alarmclock.views.main_activity.MainActivity
import com.helpfulapps.base.base.BaseFragment
import com.helpfulapps.domain.eventBus.ServiceBus
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_hourwatch.*
import org.koin.android.viewmodel.ext.android.viewModel

class HourWatchFragment : BaseFragment<HourWatchViewModel, FragmentHourwatchBinding>() {

    override val layoutId: Int = R.layout.fragment_hourwatch
    override val viewModel: HourWatchViewModel by viewModel()

    private val fab: FloatingActionButton
        get() = (mainActivity as MainActivity).fab_main_fab

    override fun init() {

        viewModel.listenToTimer()
    }

    override fun onResume() {
        super.onResume()

        fab.setOnClickListener {
            TODO("implement")
        }

        bt_reset.setOnClickListener {
            TODO("implement")
        }
        // handle when service should go foreground
        // create notification for service
        // notification should be updated with new time
        // in notification user should be able to increase time by 1 minute or pause the timer
        // taping on notification should open app with timer tab active

        // listen for current input in picker
        // when input is valid show FAB
        // when FAB is pressed count down timer is shown and picker is gone
        // fab changes icon to pause, and when clicked, pauses timer
        // reset button is shown, when pressed, view goes to initial point, timer is stopped and service too

        // current timer should be saved in database, so that next time timer is opened, last timer is prepared to run

        // STATES:
        // initial, no timer setup
        // timer is running
        // timer is paused
        // app opened after timer finished, so timer form DB should be shown

        //empty
        //with values
        //running
        //paused
        //time is up

        viewModel.timerStates.observe(this) {
            when (it) {
                is TimerService.TimerServiceEvent.StartTimer -> setupStartedTimer()
                is TimerService.TimerServiceEvent.UpdateTimer -> updateTimer(it)
                is TimerService.TimerServiceEvent.TimeIsUpTimer -> setupTimerIsUp()
                is TimerService.TimerServiceEvent.FinishTimer -> setupFinishTimer()
//                    is TimerService.TimerServiceEvent.RestartTimer -> clearTimer()
//                    is TimerService.TimerServiceEvent.PauseTimer -> pauseTimer()
            }
        }


    }

    private fun setupFinishTimer() {
        pk_timer_picker.visibility = View.VISIBLE
        bt_reset.visibility = View.GONE
        tv_time_left.clearAnimation()
        tv_time_left.visibility = View.GONE
    }

    private fun setupTimerIsUp() {
        changeFabIconToStop()
        tv_time_left.startBlinking()
    }

    private fun updateTimer(updateTimer: TimerService.TimerServiceEvent.UpdateTimer) {
        tv_time_left.text = updateTimer.timeLeft.toString()
    }

    private fun setupStartedTimer() {
        pk_timer_picker.visibility = View.GONE
        bt_reset.visibility = View.VISIBLE
        tv_time_left.text = (pk_timer_picker.getTimeInMillis() / 1000).toString()
        tv_time_left.visibility = View.VISIBLE
    }

    private fun resetTimer() {
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
    }

    private fun restartTimer() {
        ServiceBus.publish(TimerService.TimerServiceEvent.RestartTimer)
        changeFabIconToPause()
    }

    private fun stopTimer() {
        ServiceBus.publish(TimerService.TimerServiceEvent.PauseTimer)
        changeFabIconToStart()
    }

    private fun changeFabIconToStop() {
        fab.setImageResource(R.drawable.ic_stop)
    }

    private fun changeFabIconToPause() {
        fab.setImageResource(R.drawable.ic_pause)
    }

    private fun changeFabIconToStart() {
        fab.setImageResource(R.drawable.ic_start)
    }
}