package com.helpfulapps.domain.use_cases.type

import io.reactivex.Single

interface SingleUseCaseWithParameter<P, R> {

    operator fun invoke(parameter: P): Single<R>
}