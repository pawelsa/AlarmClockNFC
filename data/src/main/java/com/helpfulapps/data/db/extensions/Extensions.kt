package com.helpfulapps.data.db.extensions

import io.reactivex.Completable


fun Boolean.completed(throwable: Throwable) = Completable.create { subscriber ->
    when (this) {
        true -> subscriber.onComplete()
        else -> subscriber.onError(throwable)
    }
}
