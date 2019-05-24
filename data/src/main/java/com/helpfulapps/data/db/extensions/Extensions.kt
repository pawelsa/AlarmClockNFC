package com.helpfulapps.data.db.extensions

import io.reactivex.CompletableSource


fun Boolean.completed(throwableMessage: String) =
    CompletableSource {
        when (this) {
            true -> it.onComplete()
            else -> it.onError(Throwable(throwableMessage))
        }
    }