package com.helpfulapps.domain.use_cases

import com.helpfulapps.domain.use_cases.type.BaseUseCase

abstract class BaseUseCaseTest<T : BaseUseCase> {

    abstract val useCase: T
}