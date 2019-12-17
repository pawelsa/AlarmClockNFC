package com.helpfulapps.data.helpers

import io.reactivex.Completable
import io.reactivex.exceptions.CompositeException


inline fun Completable.dropBreadcrumb(): Completable {
    val breadcrumb = BreadcrumbException()
    return this.onErrorResumeNext { error: Throwable ->
        throw CompositeException(error, breadcrumb)
    }
}

class BreadcrumbException : Exception()