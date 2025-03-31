package com.traderbird.implementation.scheduled_task

import java.time.Duration
import java.time.LocalDateTime

abstract class AbstractTimedTask(duration: Duration) : AbstractScheduledTask() {
    protected var expireTime: LocalDateTime = LocalDateTime.now()

    init {
        expireTime = LocalDateTime.now().plus(duration)
    }

    @Synchronized
    protected fun checkExpired(): Boolean {
        return LocalDateTime.now().isAfter(expireTime)
    }

    @Synchronized
    override fun shouldRemove(): Boolean {
        return super.shouldRemove() || checkExpired()
    }

}