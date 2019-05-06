package com.helpfulapps.domain.use_cases.type

import io.reactivex.Observable

interface UseCaseWithParameter<P, R> {

    fun execute(parameter: P): Observable<R>
}