package com.helpfulapps.alarmclock.test_extensions

import com.jraska.livedata.TestObserver


fun <O, T : List<O>> TestObserver<T>.contentEqual(
    comparisonList: List<O>,
    equals: (O, O) -> Boolean = { first, second -> first == second }
): TestObserver<Boolean> {
    return this.map {
        if (it.size != comparisonList.size) return@map false

        val pairList = it.zip(comparisonList)

        return@map pairList.all { (first, second) ->
            equals(first, second)
        }
    }
}