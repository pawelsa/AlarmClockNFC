package com.helpfulapps.alarmclock.di

import com.helpfulapps.alarmclock.views.main_activity.MainActivityViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object Modules {

    val modules
        get() = listOf(
            appModules, repository, data, useCase
        )

    private val appModules = module {
        viewModel { MainActivityViewModel() }
    }

    private val repository = module {

    }

    private val data = module {

    }

    private val useCase = module {

    }

}