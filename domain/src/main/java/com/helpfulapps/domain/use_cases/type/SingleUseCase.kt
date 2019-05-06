package com.helpfulapps.domain.use_cases.type

import io.reactivex.Single

interface SingleUseCase<T> {

    fun execute(): Single<T>
}