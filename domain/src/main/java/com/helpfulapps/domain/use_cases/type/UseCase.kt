package com.helpfulapps.domain.use_cases.type

import io.reactivex.Observable

interface UseCase<T> : BaseUseCase {

    operator fun invoke(): Observable<T>
}