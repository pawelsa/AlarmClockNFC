package com.helpfulapps.alarmclock.di

import com.helpfulapps.alarmclock.views.clock_fragment.ClockViewModel
import com.helpfulapps.alarmclock.views.hourwatch_fragment.HourWatchViewModel
import com.helpfulapps.alarmclock.views.main_activity.MainActivityViewModel
import com.helpfulapps.alarmclock.views.stopwatch_fragment.StopWatchViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object Modules {

    val modules
        get() = listOf(
            appModules, repository, data, useCase
        )

    private val appModules = module {
        viewModel { MainActivityViewModel() }
        viewModel { ClockViewModel() }
        viewModel { HourWatchViewModel() }
        viewModel { StopWatchViewModel() }
    }

    private val repository = module {

    }

    private val data = module {

    }

    private val useCase = module {

    }

}