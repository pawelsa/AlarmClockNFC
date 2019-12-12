package com.helpfulapps.domain.extensions

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.Disposable

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

fun Disposable?.disposeCheck() {
    this?.let {
        if (!it.isDisposed) {
            it.dispose()
        }
    }
}