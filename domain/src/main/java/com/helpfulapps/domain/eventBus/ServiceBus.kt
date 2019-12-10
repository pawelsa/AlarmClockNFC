package com.helpfulapps.domain.eventBus

import io.reactivex.subjects.BehaviorSubject

object ServiceBus {

    private val publisher = BehaviorSubject.create<Any>()
    private val criticalPublisher = BehaviorSubject.create<Any>()

    fun publish(event: Any) = publisher.onNext(event)

    fun <T> listen(eventType: Class<T>) = publisher.ofType(eventType)

    fun publishCritical(event: Any) = criticalPublisher.onNext(event)

    fun <T> listenCritical(eventType: Class<T>) = criticalPublisher.ofType(eventType)

}