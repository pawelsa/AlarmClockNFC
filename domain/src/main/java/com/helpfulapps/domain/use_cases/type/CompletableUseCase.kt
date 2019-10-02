package com.helpfulapps.domain.use_cases.type

import io.reactivex.Completable

interface CompletableUseCase {

    operator fun invoke(): Completable
}