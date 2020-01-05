package com.helpfulapps.domain.use_cases.type

import io.reactivex.Single

interface SingleUseCase<T> : BaseUseCase {

    operator fun invoke(): Single<T>
}