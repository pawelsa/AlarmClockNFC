package com.helpfulapps.alarmclock

import com.helpfulapps.base.base.BaseViewModel
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(value = [InstantExecutorExtension::class, RxSchedulerExtensionForJunit5::class])
abstract class BaseViewModelTest<T : BaseViewModel> {

    abstract val viewModel: T
}