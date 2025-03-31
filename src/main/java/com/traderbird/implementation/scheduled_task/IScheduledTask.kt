package com.traderbird.implementation.scheduled_task

interface IScheduledTask {
    // must be @synchronized
    fun shouldRemove(): Boolean

    // must be @synchronized
    fun onRemove(): Boolean

    // must be @synchronized
    fun lock(): Boolean

    // must be @synchronized
    fun unlock(): Boolean

    // must be @synchronized
    fun isLocked(): Boolean
}