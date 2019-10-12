package com.helpfulapps.base.extensions.rx

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

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

fun <T> Observable<T>.backgroundTask(): Observable<T> =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.backgroundTask(): Single<T> =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())


fun Completable.backgroundTask(): Completable =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())