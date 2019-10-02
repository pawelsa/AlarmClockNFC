package com.helpfulapps.domain.use_cases.type

import io.reactivex.Completable

interface CompletableUseCaseWithParameter<P> {

    operator fun invoke(parameter: P): Completable
}