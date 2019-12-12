package com.helpfulapps.domain.eventBus

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

object ServiceBus {

    private val publisher = BehaviorSubject.create<Any>()

    fun publish(event: Any) = publisher.onNext(event)

    fun <T> listen(eventType: Class<T>): Observable<T> = publisher.ofType(eventType)

}