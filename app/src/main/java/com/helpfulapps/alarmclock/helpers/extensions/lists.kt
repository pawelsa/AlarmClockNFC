package com.helpfulapps.alarmclock.helpers.extensions

fun <T> List<T>.contentEqual(list: List<T>): Boolean {
    return this.size == list.size && this.containsAll(list) && list.containsAll(this)
}

fun <T> List<T>.contentNotEqual(list: List<T>): Boolean {
    return !contentEqual(list)
}

fun <T> List<T>.contentEqualInOrder(
    list: List<T>,
    comparator: (T, T) -> Boolean = { first, second -> first == second }
): Boolean {
    if (this.size != list.size) return false

    val pairList = this.zip(list)

    return pairList.all { (first, second) ->
        comparator(first, second)
    }
}

fun <T> List<T>.contentNotEqualInOrder(
    list: List<T>,
    comparator: (T, T) -> Boolean = { first, second -> first == second }
): Boolean {
    return !this.contentEqualInOrder(list, comparator)
}