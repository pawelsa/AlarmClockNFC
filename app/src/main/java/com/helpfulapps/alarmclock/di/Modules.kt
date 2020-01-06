package com.helpfulapps.alarmclock.di

import android.app.AlarmManager
import android.content.Context
import com.example.api.other.PrepareDataImpl
import com.helpfulapps.alarmclock.helpers.NotificationBuilder
import com.helpfulapps.alarmclock.helpers.NotificationBuilderImpl
import com.helpfulapps.alarmclock.helpers.layout_helpers.RingtoneDialogBuilder
import com.helpfulapps.alarmclock.helpers.layout_helpers.RingtoneDialogBuilderImpl
import com.helpfulapps.alarmclock.views.clock_fragment.ClockViewModel
import com.helpfulapps.alarmclock.views.clock_fragment.add_alarm_bs.AddAlarmBottomSheetViewModel
import com.helpfulapps.alarmclock.views.main_activity.MainActivityViewModel
import com.helpfulapps.alarmclock.views.ringing_alarm.RingingAlarmViewModel
import com.helpfulapps.alarmclock.views.settings.SettingsViewModel
import com.helpfulapps.alarmclock.views.statistics.StatisticsViewModel
import com.helpfulapps.alarmclock.views.stopwatch_fragment.StopWatchViewModel
import com.helpfulapps.alarmclock.views.timer_finished_activity.TimerFinishedViewModel
import com.helpfulapps.alarmclock.views.timer_fragment.TimerViewModel
import com.helpfulapps.data.api.PrepareData
import com.helpfulapps.data.db.alarm.dao.AlarmDao
import com.helpfulapps.data.db.stats.dao.StatsDao
import com.helpfulapps.data.db.weather.dao.WeatherDao
import com.helpfulapps.data.repositories.AlarmRepositoryImpl
import com.helpfulapps.data.repositories.StatsRepositoryImpl
import com.helpfulapps.data.repositories.WeatherRepositoryImpl
import com.helpfulapps.db.alarm.AlarmDaoImpl
import com.helpfulapps.db.stats.dao.StatsDaoImpl
import com.helpfulapps.db.weather.dao.WeatherDaoImpl
import com.helpfulapps.device.alarms.AlarmClockManagerImpl
import com.helpfulapps.device.alarms.helpers.NetworkCheckImpl
import com.helpfulapps.device.alarms.helpers.SettingsData
import com.helpfulapps.device.alarms.other.AlarmPlayerImpl
import com.helpfulapps.device.alarms.other.RingtoneRepositoryImpl
import com.helpfulapps.device.alarms.other.VibrationControllerImpl
import com.helpfulapps.domain.helpers.NetworkCheck
import com.helpfulapps.domain.helpers.Settings
import com.helpfulapps.domain.other.AlarmPlayer
import com.helpfulapps.domain.other.VibrationController
import com.helpfulapps.domain.repository.*
import com.helpfulapps.domain.use_cases.alarm.*
import com.helpfulapps.domain.use_cases.stats.GetAllStatsUseCase
import com.helpfulapps.domain.use_cases.stats.GetAllStatsUseCaseImpl
import com.helpfulapps.domain.use_cases.weather.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object Modules {

    const val SHARED_PREFERENCES_KEY = "AlarmClockSP"

    val modules
        get() = listOf(
            appModules, repository, data, useCase, other
        )

    private val appModules = module {
        viewModel { MainActivityViewModel() }
        viewModel { ClockViewModel(get(), get(), get(), get()) }
        viewModel { TimerViewModel(get()) }
        viewModel { StopWatchViewModel() }
        viewModel { AddAlarmBottomSheetViewModel(get(), get(), get(), get()) }
        viewModel { RingingAlarmViewModel(get()) }
        viewModel { SettingsViewModel(get()) }
        viewModel { StatisticsViewModel(get()) }
        viewModel { TimerFinishedViewModel() }
    }

    private val repository = module {
        single<AlarmRepository> { AlarmRepositoryImpl(get()) }
        single<WeatherRepository> { WeatherRepositoryImpl(get(), get()) }
        single<StatsRepository> { StatsRepositoryImpl(get()) }
        single<RingtoneRepository> { RingtoneRepositoryImpl(androidContext()) }
    }

    private val other = module {
        single<RingtoneDialogBuilder> { RingtoneDialogBuilderImpl(get()) }
        single<AlarmClockManager> {
            AlarmClockManagerImpl(
                androidContext(),
                androidContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager,
                get()
            )
        }
        single<AlarmPlayer> {
            AlarmPlayerImpl(
                androidContext()
            )
        }
        single<NotificationBuilder> { NotificationBuilderImpl(androidContext()) }
        single<Settings> {
            SettingsData(
                androidContext().getSharedPreferences(
                    SHARED_PREFERENCES_KEY,
                    Context.MODE_PRIVATE
                )
            )
        }
        single<VibrationController> {
            VibrationControllerImpl(
                androidContext()
            )
        }
    }

    private val data = module {
        // todo move DAOs from db to domain
        single<AlarmDao> { AlarmDaoImpl(androidContext()) }
        single<WeatherDao> { WeatherDaoImpl(androidContext()) }
        single<StatsDao> { StatsDaoImpl(androidContext()) }
        single<PrepareData> { PrepareDataImpl(get(), get()) }
        single<NetworkCheck> { NetworkCheckImpl(androidContext()) }
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
        single<StopRingingAlarmUseCase> { StopRingingAlarmUseCaseImpl(get(), get(), get()) }
        single<SnoozeAlarmUseCase> { SnoozeAlarmUseCaseImpl(get(), get(), get()) }
        single<GetAllStatsUseCase> { GetAllStatsUseCaseImpl(get()) }
        single<SetupAllAlarmsUseCase> { SetupAllAlarmsUseCaseImpl(get(), get()) }
    }

}