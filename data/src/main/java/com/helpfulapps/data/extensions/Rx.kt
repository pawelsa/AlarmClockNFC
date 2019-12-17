package com.helpfulapps.data.extensions

import io.reactivex.Completable

fun Boolean.checkCompleted(throwable: Throwable) = Completable.create { subscriber ->
    when (this) {
        true -> subscriber.onComplete()
        else -> subscriber.onError(throwable)
    }
}