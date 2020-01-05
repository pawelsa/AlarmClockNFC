package com.helpfulapps.domain.use_cases.type

import io.reactivex.Completable

interface CompletableUseCaseWithParameter<P> : BaseUseCase {

    operator fun invoke(parameter: P): Completable
}