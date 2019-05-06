package com.helpfulapps.domain.use_cases.type

import io.reactivex.Single

interface SingleUseCaseWithParameter<P, R> {

    fun execute(parameter: P): Single<R>
}