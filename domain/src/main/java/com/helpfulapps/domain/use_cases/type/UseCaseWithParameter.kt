package com.helpfulapps.domain.use_cases.type

import io.reactivex.Observable

interface UseCaseWithParameter<P, R> : BaseUseCase {

    operator fun invoke(parameter: P): Observable<R>
}