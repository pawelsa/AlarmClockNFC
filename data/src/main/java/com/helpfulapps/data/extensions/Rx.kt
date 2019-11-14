package com.helpfulapps.data.extensions

import com.raizlabs.android.dbflow.rx2.kotlinextensions.rx
import com.raizlabs.android.dbflow.sql.queriable.ModelQueriable
import io.reactivex.Completable
import io.reactivex.MaybeSource
import io.reactivex.Single

fun <T : Any> ModelQueriable<T>.rxQueryListSingle(maybeSource: MaybeSource<T>? = null): Single<T> {
    return this.rx().querySingle().toSingle()
}

fun Boolean.checkCompleted(throwable: Throwable) = Completable.create { subscriber ->
    when (this) {
        true -> subscriber.onComplete()
        else -> subscriber.onError(throwable)
    }
}