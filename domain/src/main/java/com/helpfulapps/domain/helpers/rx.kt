package com.helpfulapps.domain.helpers

import io.reactivex.Completable
import io.reactivex.Single

fun <T> completableOf(action: () -> T): Completable {
    return Completable.create {
        try {
            action()
            it.onComplete()
        } catch (ex: Exception) {
            if (!it.isDisposed) {
                it.onError(ex)
            }
        }
    }
}

fun <T> singleOf(action: () -> T): Single<T> {
    return Single.create {
        try {
            it.onSuccess(action())
        } catch (ex: Exception) {
            if (!it.isDisposed) {
                it.onError(ex)
            }
        }
    }
}