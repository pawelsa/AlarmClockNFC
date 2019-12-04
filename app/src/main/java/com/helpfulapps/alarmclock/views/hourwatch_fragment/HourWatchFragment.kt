package com.helpfulapps.alarmclock.views.hourwatch_fragment

import android.content.Intent
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.FragmentHourwatchBinding
import com.helpfulapps.alarmclock.helpers.extensions.observe
import com.helpfulapps.alarmclock.service.TimerService
import com.helpfulapps.alarmclock.views.main_activity.MainActivity
import com.helpfulapps.base.base.BaseFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_hourwatch.*
import org.koin.android.viewmodel.ext.android.viewModel

class HourWatchFragment : BaseFragment<HourWatchViewModel, FragmentHourwatchBinding>() {

    override val layoutId: Int = R.layout.fragment_hourwatch
    override val viewModel: HourWatchViewModel by viewModel()

    private val fab: FloatingActionButton
        get() = (mainActivity as MainActivity).fab_main_fab

    override fun init() {
        binding.viewModel = viewModel
    }

    private fun setupPreviousScreen() {
        fab.show()
    }

    private fun setupInitialScreen() {
        fab.hide()
    }

    override fun onResume() {
        super.onResume()

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


        fab.setOnClickListener {
            viewModel.fabPressed()
//            startTimer()
        }

        viewModel.isRunning.observe(this) {
            if (it) {
                startTimer()
            } else {
                stopTimer()
            }
        }

        viewModel.isPaused.observe(this) {
            if (it) {
                fab.setImageResource(R.drawable.ic_pause)
            } else {
                fab.setImageResource(R.drawable.ic_start)
            }
        }

        viewModel.timeLeft.observe(this) {
            Log.d(TAG, "timeLeft $it")
            if (it == 0L) {
                setupInitialScreen()
            } else {
                setupPreviousScreen()
            }
        }

        /* bt_reset.setOnClickListener {
             Intent(context, TimerService::class.java).let {
                 it.action = TimerService.TIMER_RESET
                 context?.startService(it)
             }
         }*/

        viewModel.listenToTimer()
    }

    private fun stopTimer() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun startTimer() {
        val timeLeft = pk_timer_picker.getTimeInMillis()
        Intent(context, TimerService::class.java).let {
            it.action = TimerService.TIMER_START
            it.putExtra(TimerService.TIMER_TIME, timeLeft)
            context?.startService(it)
        }
        changeFabIconToPause()
    }

    private fun changeFabIconToPause() {
        fab.setImageResource(R.drawable.ic_pause)
    }
}