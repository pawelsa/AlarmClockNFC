package com.helpfulapps.domain.eventBus

sealed class DatabaseNotifiers {
    object Saved : DatabaseNotifiers()
    object Updated : DatabaseNotifiers()
    object Removed : DatabaseNotifiers()
}