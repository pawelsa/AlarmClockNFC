package com.helpfulapps.domain.use_cases.type

import io.reactivex.Completable

interface CompletableUseCaseWithParameter<P> {

    fun execute(parameter: P): Completable
}