package com.helpfulapps.alarmclock.di

import android.app.AlarmManager
import android.content.Context
import com.helpfulapps.alarmclock.views.clock_fragment.ClockViewModel
import com.helpfulapps.alarmclock.views.clock_fragment.add_alarm_bs.AddAlarmBottomSheetViewModel
import com.helpfulapps.alarmclock.views.hourwatch_fragment.HourWatchViewModel
import com.helpfulapps.alarmclock.views.main_activity.MainActivityViewModel
import com.helpfulapps.alarmclock.views.ringing_alarm.RingingAlarmViewModel
import com.helpfulapps.alarmclock.views.stopwatch_fragment.StopWatchViewModel
import com.helpfulapps.data.repositories.AlarmRepositoryImpl
import com.helpfulapps.data.repositories.WeatherRepositoryImpl
import com.helpfulapps.device.alarms.AppAlarmManagerImpl
import com.helpfulapps.domain.repository.AlarmRepository
import com.helpfulapps.domain.repository.AppAlarmManager
import com.helpfulapps.domain.repository.WeatherRepository
import com.helpfulapps.domain.use_cases.alarm.*
import com.helpfulapps.domain.use_cases.alarm.definition.*
import com.helpfulapps.domain.use_cases.weather.DownloadForecastForCityUseCaseImpl
import com.helpfulapps.domain.use_cases.weather.DownloadForecastForLocalizationUseCaseImpl
import com.helpfulapps.domain.use_cases.weather.GetForecastForAlarmUseCaseImpl
import com.helpfulapps.domain.use_cases.weather.GetForecastForAlarmsUseCaseImpl
import com.helpfulapps.domain.use_cases.weather.definition.DownloadForecastForCityUseCase
import com.helpfulapps.domain.use_cases.weather.definition.DownloadForecastForLocalizationUseCase
import com.helpfulapps.domain.use_cases.weather.definition.GetForecastForAlarmUseCase
import com.helpfulapps.domain.use_cases.weather.definition.GetForecastForAlarmsUseCase
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
        viewModel { ClockViewModel(get(), get()) }
        viewModel { HourWatchViewModel() }
        viewModel { StopWatchViewModel() }
        viewModel { AddAlarmBottomSheetViewModel(get()) }
        viewModel { RingingAlarmViewModel() }
    }

    private val repository = module {
        single<AlarmRepository> { AlarmRepositoryImpl(androidContext()) }
        single<AppAlarmManager> {
            AppAlarmManagerImpl(
                androidContext(),
                androidContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
            )
        }
        single<WeatherRepository> { WeatherRepositoryImpl(androidContext()) }
    }

    private val data = module {

    }

    private val useCase = module {
        single<AddAlarmUseCase> { AddAlarmUseCaseImpl(get()) }
        single<SwitchAlarmUseCase> { SwitchAlarmUseCaseImpl(get()) }
        single<GetAlarmsUseCase> { GetAlarmsUseCaseImpl(get(), get()) }
        single<RemoveAlarmUseCase> { RemoveAlarmUseCaseImpl(get()) }
        single<UpdateAlarmUseCase> { UpdateAlarmUseCaseImpl(get()) }
        single<DownloadForecastForCityUseCase> { DownloadForecastForCityUseCaseImpl(get()) }
        single<DownloadForecastForLocalizationUseCase> {
            DownloadForecastForLocalizationUseCaseImpl(
                get()
            )
        }
        single<GetForecastForAlarmUseCase> { GetForecastForAlarmUseCaseImpl(get()) }
        single<GetForecastForAlarmsUseCase> { GetForecastForAlarmsUseCaseImpl(get()) }
    }

}