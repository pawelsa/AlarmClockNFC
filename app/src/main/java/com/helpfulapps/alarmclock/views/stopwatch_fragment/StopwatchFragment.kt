package com.helpfulapps.alarmclock.views.stopwatch_fragment

import android.content.Intent
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.databinding.FragmentStopwatchBinding
import com.helpfulapps.alarmclock.helpers.extensions.observe
import com.helpfulapps.alarmclock.helpers.startVersionedService
import com.helpfulapps.alarmclock.service.StopwatchService
import com.helpfulapps.alarmclock.service.StopwatchService.Companion.STOPWATCH_START
import com.helpfulapps.alarmclock.views.main_activity.MainActivity
import com.helpfulapps.base.base.BaseFragment
import com.helpfulapps.domain.eventBus.ServiceBus
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_stopwatch.*
import org.koin.android.viewmodel.ext.android.viewModel

class StopwatchFragment : BaseFragment<StopWatchViewModel, FragmentStopwatchBinding>() {

    override val layoutId: Int = R.layout.fragment_stopwatch
    override val viewModel: StopWatchViewModel by viewModel()

    private val fab: FloatingActionButton
        get() = (mainActivity as MainActivity).fab_main_fab

    private val animation: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.blink) }

    private var currentState: StopWatchViewModel.StopWatchState =
        StopWatchViewModel.StopWatchState.Stopped

    private val adapter: StopwatchTimesAdapter by lazy {
        StopwatchTimesAdapter().also {
            rv_stopwatch_times.layoutManager = LinearLayoutManager(context)
            rv_stopwatch_times.adapter = it
        }
    }

    override fun init() {
    }

    override fun onResume() {
        super.onResume()

        viewModel.observeStopwatch()

        checkInitialState()
        subscribeState()
        subscribeLapTimes()

        setupFabListener()
        setupResetListener()
        setupLapListener()
    }

    private fun setupLapListener() {
        bt_stopwatch_lap.setOnClickListener {
            ServiceBus.publish(StopwatchService.StopWatchEvent.TakeLap)
        }
    }

    private fun subscribeLapTimes() {
        viewModel.lapTimes.observe(this) {
            if (it.isEmpty()) {
                rv_stopwatch_times.visibility = View.GONE
            } else {
                rv_stopwatch_times.visibility = View.VISIBLE
                // todo convert it to string with time format, use for it resources
                adapter.submitList(it.mapIndexed { index, lapTime -> LapModel(index + 1, lapTime) })
            }
        }
    }

    private fun setupResetListener() {
        bt_stopwatch_reset.setOnClickListener {
            ServiceBus.publish(StopwatchService.StopWatchEvent.Stop)
        }
    }

    private fun setupFabListener() {
        fab.setOnClickListener {
            when (currentState) {
                is StopWatchViewModel.StopWatchState.Stopped -> {
                    Intent(context, StopwatchService::class.java).let {
                        it.action = STOPWATCH_START
                        context?.startVersionedService(it)
                    }
                }
                is StopWatchViewModel.StopWatchState.Paused -> ServiceBus.publish(StopwatchService.StopWatchEvent.Resume)
                else -> ServiceBus.publish(StopwatchService.StopWatchEvent.Pause)
            }
        }
    }

    private fun checkInitialState() {
        viewModel.stopwatchState.value.let {
            if (it == null) {
                setupStoppedState()
            } else {
                handleState(it)
            }
        }
    }

    private fun subscribeState() {
        viewModel.stopwatchState.observe(this) {
            handleState(it)
            currentState = it
        }
    }

    private fun handleState(state: StopWatchViewModel.StopWatchState) {
        Log.d(TAG, "state: ${state.javaClass.simpleName}")
        when (state) {
            is StopWatchViewModel.StopWatchState.Stopped -> setupStoppedState()
            is StopWatchViewModel.StopWatchState.Started -> setupStartedState()
            is StopWatchViewModel.StopWatchState.Update -> setupUpdateState(state)
            is StopWatchViewModel.StopWatchState.Paused -> setupPausedState()
            is StopWatchViewModel.StopWatchState.Resumed -> setupResumedState()
        }
    }

    private fun setupResumedState() {
        tv_stopwatch_time.clearAnimation()
        changeFabIconToPause()
    }

    private fun setupPausedState() {
        tv_stopwatch_time.startAnimation(animation)
        changeFabIconToStart()
    }

    private fun setupUpdateState(state: StopWatchViewModel.StopWatchState.Update) {
        tv_stopwatch_time.text = state.time.toString()
    }

    private fun setupStartedState() {
        bt_stopwatch_lap.visibility = View.VISIBLE
        bt_stopwatch_reset.visibility = View.VISIBLE
        changeFabIconToPause()
    }

    private fun setupStoppedState() {
        rv_stopwatch_times.visibility = View.GONE
        bt_stopwatch_reset.visibility = View.GONE
        bt_stopwatch_lap.visibility = View.GONE
        tv_stopwatch_time.clearAnimation()
        tv_stopwatch_time.text = "0"
        changeFabIconToStart()
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