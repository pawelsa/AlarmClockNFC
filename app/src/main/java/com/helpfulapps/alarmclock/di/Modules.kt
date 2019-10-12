package com.helpfulapps.alarmclock.di

import com.helpfulapps.alarmclock.views.clock_fragment.ClockViewModel
import com.helpfulapps.alarmclock.views.clock_fragment.add_alarm_bs.AddAlarmBottomSheetViewModel
import com.helpfulapps.alarmclock.views.hourwatch_fragment.HourWatchViewModel
import com.helpfulapps.alarmclock.views.main_activity.MainActivityViewModel
import com.helpfulapps.alarmclock.views.ringing_alarm.RingingAlarmViewModel
import com.helpfulapps.alarmclock.views.stopwatch_fragment.StopWatchViewModel
import com.helpfulapps.data.repositories.AlarmRepositoryImpl
import com.helpfulapps.domain.repository.AlarmRepository
import com.helpfulapps.domain.use_cases.alarm.AddAlarmUseCaseImpl
import com.helpfulapps.domain.use_cases.alarm.definition.AddAlarmUseCase
import org.koin.android.ext.koin.androidContext
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
        viewModel { AddAlarmBottomSheetViewModel(get()) }
        viewModel { RingingAlarmViewModel() }
    }

    private val repository = module {
        single<AlarmRepository> { AlarmRepositoryImpl(androidContext()) }
    }

    private val data = module {

    }

    private val useCase = module {
        single<AddAlarmUseCase> { AddAlarmUseCaseImpl(get()) }
    }

}