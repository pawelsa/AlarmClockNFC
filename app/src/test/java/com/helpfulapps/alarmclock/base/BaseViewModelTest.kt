package com.helpfulapps.alarmclock.base

import com.helpfulapps.base.base.BaseViewModel
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(value = [InstantExecutorExtension::class, RxSchedulerExtension::class])
abstract class BaseViewModelTest<T : BaseViewModel> {

    abstract val viewModel: T

}