package com.helpfulapps.alarmclock

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.helpfulapps.base.base.BaseViewModel
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
abstract class BaseViewModelTest<T : BaseViewModel> {
    @get:Rule
    val schedulersRule = RxOverrideScheduleRule()
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    abstract val viewModel: T
}