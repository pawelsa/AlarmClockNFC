package com.helpfulapps.alarmclock.di

import android.app.AlarmManager
import android.content.Context
import com.helpfulapps.alarmclock.helpers.*
import com.helpfulapps.alarmclock.views.clock_fragment.ClockViewModel
import com.helpfulapps.alarmclock.views.clock_fragment.add_alarm_bs.AddAlarmBottomSheetViewModel
import com.helpfulapps.alarmclock.views.hourwatch_fragment.HourWatchViewModel
import com.helpfulapps.alarmclock.views.main_activity.MainActivityViewModel
import com.helpfulapps.alarmclock.views.ringing_alarm.RingingAlarmViewModel
import com.helpfulapps.alarmclock.views.settings.SettingsViewModel
import com.helpfulapps.alarmclock.views.stopwatch_fragment.StopWatchViewModel
import com.helpfulapps.data.helper.SettingsData
import com.helpfulapps.data.repositories.AlarmRepositoryImpl
import com.helpfulapps.data.repositories.WeatherRepositoryImpl
import com.helpfulapps.device.alarms.AlarmClockManagerImpl
import com.helpfulapps.domain.helpers.Settings
import com.helpfulapps.domain.repository.AlarmClockManager
import com.helpfulapps.domain.repository.AlarmRepository
import com.helpfulapps.domain.repository.WeatherRepository
import com.helpfulapps.domain.use_cases.alarm.*
import com.helpfulapps.domain.use_cases.weather.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object Modules {

    const val SHARED_PREFERENCES_KEY = "AlarmClockSP"

    val modules
        get() = listOf(
            appModules, repository, data, useCase
        )

    private val appModules = module {
        viewModel { MainActivityViewModel() }
        viewModel { ClockViewModel(get(), get(), get(), get()) }
        viewModel { HourWatchViewModel() }
        viewModel { StopWatchViewModel() }
        viewModel { AddAlarmBottomSheetViewModel(get(), get(), get()) }
        viewModel { RingingAlarmViewModel(get()) }
        viewModel { SettingsViewModel(get()) }
    }

    private val repository = module {
        single<AlarmRepository> { AlarmRepositoryImpl(androidContext()) }
        single<AlarmClockManager> {
            AlarmClockManagerImpl(
                androidContext(),
                androidContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager,
                get()
            )
        }
        single<AlarmPlayer> { AlarmPlayerImpl(androidContext()) }
        single<NotificationBuilder> { NotificationBuilderImpl(androidContext()) }
        single<Settings> {
            SettingsData(
                androidContext().getSharedPreferences(
                    SHARED_PREFERENCES_KEY,
                    Context.MODE_PRIVATE
                )
            )
        }
        single<VibrationController> { VibrationControllerImpl(androidContext()) }
        single<WeatherRepository> { WeatherRepositoryImpl(androidContext(), settings = get()) }
        single<SnoozeAlarmUseCase> { SnoozeAlarmUseCaseImpl(get(), get()) }
    }

    private val data = module {

    }

    private val useCase = module {
        single<AddAlarmUseCase> { AddAlarmUseCaseImpl(get(), get()) }
        single<SwitchAlarmUseCase> { SwitchAlarmUseCaseImpl(get(), get()) }
        single<GetAlarmsUseCase> { GetAlarmsUseCaseImpl(get(), get()) }
        single<RemoveAlarmUseCase> { RemoveAlarmUseCaseImpl(get(), get()) }
        single<UpdateAlarmUseCase> { UpdateAlarmUseCaseImpl(get(), get()) }
        single<DownloadForecastForCityUseCase> { DownloadForecastForCityUseCaseImpl(get()) }
        single<DownloadForecastForLocalizationUseCase> {
            DownloadForecastForLocalizationUseCaseImpl(
                get()
            )
        }
        single<GetAlarmUseCase> { GetAlarmUseCaseImpl(get(), get()) }
        single<GetForecastForAlarmUseCase> { GetForecastForAlarmUseCaseImpl(get()) }
        single<GetForecastForAlarmsUseCase> { GetForecastForAlarmsUseCaseImpl(get()) }
        single<StopRingingAlarmUseCase> { StopRingingAlarmUseCaseImpl(get(), get()) }
    }

}