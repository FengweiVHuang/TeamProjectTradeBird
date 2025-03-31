package com.traderbird.implementation.scheduled_task

abstract class AbstractScheduledTask : IScheduledTask {
    protected var locked: Boolean = false
    protected var handled: Boolean = false

    @Synchronized
    override fun lock(): Boolean {
        if (locked)
            return false

        locked = true
        return true
    }

    @Synchronized
    override fun unlock(): Boolean {
        if (!locked)
            return false

        locked = false
        return true
    }

    @Synchronized
    override fun isLocked(): Boolean {
        return locked
    }

    @Synchronized
    override fun shouldRemove(): Boolean {
        return handled
    }


}