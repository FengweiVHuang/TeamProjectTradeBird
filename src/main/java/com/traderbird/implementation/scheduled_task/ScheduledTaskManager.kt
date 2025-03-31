package com.traderbird.implementation.scheduled_task

import org.springframework.scheduling.annotation.Scheduled
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.HashMap

object ScheduledTaskManager {
    @JvmStatic
    val scheduledTasks : ConcurrentHashMap<String, IScheduledTask> = ConcurrentHashMap()

    operator fun get(key: String): IScheduledTask? {
        return scheduledTasks[key]
    }

    operator fun set(key: String, value: IScheduledTask) {
        scheduledTasks[key] = value
    }

    fun removeTask(key : String) : Boolean {
        scheduledTasks.remove(key)?.let {
            return true
        }
        return false
    }

    @Scheduled(fixedRate = 60000)
    @Synchronized
    fun removeExpiredTasks() {
        val iterator = scheduledTasks.iterator()
        while (iterator.hasNext()) {
            val (_, value) = iterator.next()

            if (value.isLocked())
                continue

            if (value.shouldRemove()) {

                if (value.lock()) {

                    if (value.onRemove())
                        iterator.remove()

                    value.unlock()
                }
            }
        }
    }
}